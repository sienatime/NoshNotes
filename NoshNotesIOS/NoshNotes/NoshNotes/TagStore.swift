// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import FirebaseDatabase
import FirebaseDatabaseSwift

struct TagWithID: Codable, Identifiable, Hashable {
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

  @MainActor
  public func observeChanges() async {
    do {
      for try await tags in streamTags() {
        self.tags = tags
      }
    } catch {
      print(error)
      self.tags = []
    }
  }

  @MainActor
  public func createTag(name: String) async throws {
    guard let newKey = ref.childByAutoId().key else {
      throw FirebaseError.noKey
    }
    try await ref.child(newKey).setValue(["name": name])
  }

  // Map from DataSnapshot to [Tag]
  // This AsyncThrowingMapSequence type is irritating.
  private func streamTags() -> AsyncThrowingMapSequence<AsyncStream<DataSnapshot>, [TagWithID]> {
    streamData().map { data in
      // This requires explicit self because we are wrapping the block in the returned sequence. Except if you in-line this method in observeChanges it still requires it :shrug:
      try self.makeTags(from: data)
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

  // Fetches the tags and returns them asynchronously.
  // We could try to separate the state changes from the API later on.
  private func makeTags(from data: DataSnapshot) throws -> [TagWithID] {
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
