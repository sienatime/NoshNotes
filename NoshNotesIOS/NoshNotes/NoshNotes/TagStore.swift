// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import FirebaseDatabase
import FirebaseDatabaseSwift

struct TagWithID: Codable, Identifiable {
  let id: String
  let tag: Tag

  var name: String { tag.name }
  var icon: String? { tag.icon }
}

struct Tag: Codable, Hashable {
  let name: String
  let icon: String?
  let backgroundColor: String?
  let textColor: String?

  static func makeForPreview(name: String) -> Tag {
    Tag(name: name, icon: "fork.knife", backgroundColor: "#EDEDED", textColor: "#444444")
  }
}

enum FirebaseError: Error {
  case childrenType
  case noKey
}

class TagStore: ObservableObject {

  @Published var tags: [TagWithID] = []

  // We have to put this on the @MainActor so the SwiftUI Views can observe the tags property.
  @MainActor
  public func reloadTags() async {
    // Some SwiftUI examples put the logic of taking the fetched data and setting it on the Published property in the view but I don't like that. I want all the state updates to be in one place
    do {
      self.tags = try await fetchTags()
    } catch {
      print(error)
      self.tags = []
    }
  }

  @MainActor
  public func createTag() async {
    do {
      try await Task.sleep(for: .seconds(2))
    } catch {
      print("error saving tag: \(error)")
    }
  }

  // Fetches the tags and returns them asynchronously.
  // We could try to separate the state changes from the API later on.
  private func fetchTags() async throws -> [TagWithID] {
    let data = try await ref.getData()
    guard let children = data.children.allObjects as? [DataSnapshot] else {
      print("snapshot.children.allObject was not of type [DataSnapshot] for some reason")
      throw FirebaseError.childrenType
    }
    let keyedTags = try children.map { child in
      // let's fail if any child is invalid
      let tag = try child.data(as: Tag.self)
      return TagWithID(id: child.key, tag: tag)
    }
    return keyedTags
  }

  private lazy var ref: DatabaseReference = Database.database().reference(withPath: "tags")
}
