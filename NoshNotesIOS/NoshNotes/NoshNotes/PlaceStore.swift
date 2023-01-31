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

class PlaceStore: ObservableObject {
  init() {
    placesClient = GMSPlacesClient.shared()
  }

  @Published var allPlaces: [Place] = []

  public func update(place: Place) async throws {
    var tagsDict: [String: Bool] = [:]
    for tagId in place.tagIDs {
      tagsDict[tagId] = true
    }

    let firebasePlace = FirebasePlace(
      note: place.note,
      remoteId: place.remoteId,
      tags: tagsDict,
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
  func observePlaces() {
    ref.observe(.value) { [weak self] data in self?.handle(data: data) }
  }

  @MainActor
  private func handle(data: DataSnapshot) {
    Task {
      do {
        let places = try await makePlaces(from: data)
        self.allPlaces = places
      } catch {
        print(error)
        self.allPlaces = []
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
