// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import FirebaseDatabase
import FirebaseDatabaseSwift

struct Tag: Codable {
  let name: String
}

protocol TagStore {
  func getTags() async-> [Tag]
}

class DefaultTagStore: TagStore {

  public func getTags() async -> [Tag] {
    do {
      let data = try await ref.getData()
      guard let children = data.children.allObjects as? [DataSnapshot] else {
        print("snapshot.children.allObject was not of type [DataSnapshot] for some reason")
        return []
      }
      let tags = try children.map { child in
        // let's throw if any child is invalid
        try child.data(as: Tag.self)
      }
      return tags
    } catch {
      print(error)
    }
    return []
  }

  private lazy var ref: DatabaseReference = Database.database().reference(withPath: "tags")
}
