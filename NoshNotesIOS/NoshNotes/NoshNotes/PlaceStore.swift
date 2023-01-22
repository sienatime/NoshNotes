// Created by andy_bartholomew on 1/21/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import FirebaseDatabase
import FirebaseDatabaseSwift

struct Place: Codable, Identifiable {
  var id: String { uid }

  let note: String?
  let remoteId: String
  let tags: [String: Bool]
  let uid: String
}

class PlaceStore: ObservableObject {
  @Published var allPlaces: [Place] = []

  // We have to put this on the @MainActor so the SwiftUI Views can observe the tags property.
  @MainActor
  public func reloadPlaces() async {
    do {
      self.allPlaces = try await fetchPlaces()
      print(self.allPlaces)
    } catch {
      print(error)
      self.allPlaces = []
    }
  }

  private func fetchPlaces() async throws -> [Place] {
    let data = try await ref.getData()
    guard let children = data.children.allObjects as? [DataSnapshot] else {
      print("snapshot.children.allObject was not of type [DataSnapshot] for some reason")
      throw FirebaseError.childrenType
    }
    let places = try children.map { child in
      // let's fail if any child is invalid
      try child.data(as: Place.self)
    }
    return places
  }

  private lazy var ref: DatabaseReference = Database.database().reference(withPath: "places")
}
