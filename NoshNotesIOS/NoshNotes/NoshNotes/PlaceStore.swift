// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import FirebaseDatabase

struct Tag {

}

protocol PlaceStore {
  func getTags() async
}

class DefaultPlaceStore: PlaceStore {

  public func getTags() {
    ref.child("tags").getData { error, data in
      if let error {
        print(error)
      }
      if let data {
        print(data)
      }
    }
  }

  private lazy var ref: DatabaseReference = Database.database().reference()
}
