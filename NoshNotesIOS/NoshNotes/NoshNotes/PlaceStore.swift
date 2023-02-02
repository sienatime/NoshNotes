// Created by andy_bartholomew on 1/21/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import FirebaseDatabase
import FirebaseDatabaseSwift
import GooglePlaces

struct Place: Identifiable, Hashable {
  init(
    id: String,
    name: String,
    remoteId: String,
    note: String? = nil,
    tagIDs: Set<String> = [],
    imageMetadata: GMSPlacePhotoMetadata? = nil)
  {
    self.id = id
    self.name = name
    self.remoteId = remoteId
    self.note = note
    self.tagIDs = tagIDs
    self.imageMetadata = imageMetadata
  }

  static func forPreview(
    id: String = UUID().uuidString,
    name: String,
    note: String? = nil,
    tagIDs: Set<String> = []
  ) -> Place
  {
    Place(id: id, name: name, remoteId: "", note: note, tagIDs: tagIDs)
  }

  let id: String // The Firebase ID
  let name: String
  let note: String?
  let tagIDs: Set<String>
  let imageMetadata: GMSPlacePhotoMetadata?
  let remoteId: String // The Google Place ID
}

struct FirebasePlace: Codable, Identifiable {
  var id: String { uid }

  let note: String?
  let remoteId: String // aka Google Places ID
  let tags: [String: Bool] // Key = Firebase Tag ID
  let uid: String // Firebase ID
}

// Our own custom struct representing validated Google Places data
struct GooglePlace {
  enum DataError: Error {
    case dataMissing(field: String)
  }

  let id: String // The Google Places SDK ID
  let name: String
  let imageMetadata: GMSPlacePhotoMetadata?

  init(id: String, name: String, imageMetadata: GMSPlacePhotoMetadata?) {
    self.id = id
    self.name = name
    self.imageMetadata = imageMetadata
  }

  init(gmsPlace: GMSPlace) throws {
    guard let name = gmsPlace.name else {
      throw DataError.dataMissing(field: "name")
    }
    guard let id = gmsPlace.placeID else {
      throw DataError.dataMissing(field: "placeID")
    }
    self.id = id
    self.name = name
    self.imageMetadata = gmsPlace.photos?.first
  }
}

// A Place that hasn't been created on the backend yet so it doesn't have an id
struct NewPlace {
  var note: String?
  var remoteId: String
  var tagIDs: Set<String>
}

extension Set {
  func toDictionaryKeysWithTrueValues() -> [Element: Bool] {
    let keyValuePairs = map { ($0, true) }
    return Dictionary(keyValuePairs, uniquingKeysWith: { first, _ in first })
  }
}

class PlaceStore: ObservableObject {
  init() {
    placesClient = GMSPlacesClient.shared()
  }

  @Published var allPlaces: [Place] = []

  public func create(newPlace: NewPlace) async throws {
    guard let newKey = ref.childByAutoId().key else {
      throw FirebaseError.noKey
    }
    let newFirebasePlace = FirebasePlace(
      note: newPlace.note,
      remoteId: newPlace.remoteId,
      tags: newPlace.tagIDs.toDictionaryKeysWithTrueValues(),
      uid: newKey)

    try await update(firebasePlace: newFirebasePlace)
  }

  private func createNewFirebasePlaceID() async throws {
  }

  public func update(place: Place) async throws {

    let firebasePlace = FirebasePlace(
      note: place.note,
      remoteId: place.remoteId,
      tags: place.tagIDs.toDictionaryKeysWithTrueValues(),
      uid: place.id)

    try await update(firebasePlace: firebasePlace)
  }

  private func update(firebasePlace: FirebasePlace) async throws {
    try await withCheckedThrowingContinuation { (continuation: CheckedContinuation<Void, Error>) -> Void in
      do {
        try ref.child(firebasePlace.id).setValue(from: firebasePlace) { error in
          if let error {
            continuation.resume(throwing: error)
          } else {
            continuation.resume(returning: ())
          }
        }
      } catch {
        continuation.resume(throwing: error)
      }
    }
  }

  // On the Main Actor because it updates a @Published property observed by SwiftUI
  @MainActor
  func observeChanges() async {
    do {
      for try await places in streamPlaces() {
        self.allPlaces = places
      }
    } catch {
      print(error)
      self.allPlaces = []
    }
  }

  // Map from DataSnapshot to [Place] asynchronously
  // This AsyncThrowingMapSequence type is irritating. 
  private func streamPlaces() -> AsyncThrowingMapSequence<AsyncStream<DataSnapshot>, [Place]> {
    streamData().map { data in
      // This requires explicit self because we are wrapping the block in the returned sequence. Except if you in-line this method in observeChanges it still requires it :shrug:
      try await self.makePlaces(from: data)
    }
  }

  // Convert from Firebase closure based API to AsyncStream
  private func streamData() -> AsyncStream<DataSnapshot> {
    AsyncStream { continuation in
      ref.observe(.value) { data in
        continuation.yield(data)
      }
    }
  }

  // Converts the Firebase DataSnapshot to a list of Places
  // But also fetches from Google Places first to populate the place names.
  private func makePlaces(from data: DataSnapshot) async throws -> [Place] {
    guard let children = data.children.allObjects as? [DataSnapshot] else {
      print("snapshot.children.allObject was not of type [DataSnapshot] for some reason")
      throw FirebaseError.childrenType
    }
    let firebasePlaces = try children.map { child in
      // let's fail if any child is invalid
      try child.data(as: FirebasePlace.self)
    }
    /// use this fake data if Firebase goes down
//    let firebasePlaces = [
//      FirebasePlace(note: "cool place", remoteId: "ChIJDwOJGqu5woAR3tTmF6s8bfE", tags: [:], uid: "123")
//    ]

    let placeData = try await fetchPlaceData(ids: firebasePlaces.map(\.remoteId))

    let places = firebasePlaces.compactMap { firebasePlace in
      buildPlace(from: firebasePlace, with: placeData)
    }
    return places
  }

  private func fetchPlaces() async throws -> [Place] {
    let data = try await ref.getData()
    return try await makePlaces(from: data)
  }

  private func buildPlace(from firebasePlace: FirebasePlace, with placeData: [String: GooglePlace]) -> Place? {
    guard let googlePlace = placeData[firebasePlace.remoteId] else {
      print("Google place ID \(firebasePlace.remoteId) not found")
      return nil
    }
    return Place(
      id: firebasePlace.id,
      name: googlePlace.name,
      remoteId: googlePlace.id,
      note: firebasePlace.note,
      tagIDs: Set(firebasePlace.tags.keys),
      imageMetadata: googlePlace.imageMetadata)
  }

  private func fetchPlaceData(ids: [String]) async throws -> [String: GooglePlace] {
    try await withThrowingTaskGroup(of: (String, GooglePlace?).self, body: { group in
      for id in ids {
        group.addTask {
          await (id, try self.fetchPlaceData(id: id))
        }
      }

      return try await group.reduce(into: [:], { partialResult, resultTuple in
        partialResult[resultTuple.0] = resultTuple.1
      })
    })
  }

  private func fetchPlaceData(id: String) async throws -> GooglePlace {
    let gmsPlace = try await fetchGMSPlaceDetails(id: id)
    return try GooglePlace(gmsPlace: gmsPlace)
  }

  private func fetchGMSPlaceDetails(id: String) async throws -> GMSPlace {
    try await withCheckedThrowingContinuation { continuation in
      fetchGMSPlaceDetails(id: id) { result in
        switch result {
        case .failure(let error):
          continuation.resume(throwing: error)
        case .success(let gmsPlace):
          continuation.resume(returning: gmsPlace)
        }
      }
    }
  }

  private func fetchGMSPlaceDetails(id: String, completion: @escaping (Result<GMSPlace, Error>) -> Void) {
    let fields: GMSPlaceField = [.name, .placeID, .photos]
    placesClient.fetchPlace(
      fromPlaceID: id,
      placeFields: fields,
      sessionToken: nil) { placeData, error in
        if let error {
          completion(.failure(error))
        } else if let placeData {
          completion(.success(placeData))
        }
      }
  }

  private lazy var ref: DatabaseReference = Database.database().reference(withPath: "places")
  private let placesClient: GMSPlacesClient
}

