// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import FirebaseDatabase
import FirebaseDatabaseSwift

struct Tag: Codable {
  let name: String
}

class TagStore: ObservableObject {

  @Published var tags: [Tag] = []

  @MainActor
  public func reloadTags() async {
    // Some SwiftUI examples put the logic of taking the fetched data and setting it on the Published property in the view but I don't like that. I want all the state updates to be in one place
    self.tags = await fetchTags()
  }

  // Fetches the tags and returns them asynchronously.
  // We could try to separate the state changes from the API later on.
  private func fetchTags() async -> [Tag] {
    do {
      let data = try await ref.getData()
      guard let children = data.children.allObjects as? [DataSnapshot] else {
        print("snapshot.children.allObject was not of type [DataSnapshot] for some reason")
        return []
      }
      let tags = try children.map { child in
        // let's fail if any child is invalid
        try child.data(as: Tag.self)
      }
      return tags
    } catch {
      print(error)
      return []
    }
  }

  private lazy var ref: DatabaseReference = Database.database().reference(withPath: "tags")
}
