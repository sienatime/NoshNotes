// Created by andy_bartholomew on 1/26/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import GooglePlaces

class PlaceImageStore {

  init() {
    placesClient = GMSPlacesClient.shared()
  }

  public func fetchImage(metadata: GMSPlacePhotoMetadata) async throws -> UIImage {
    try await withCheckedThrowingContinuation { continuation in
      placesClient.loadPlacePhoto(metadata) { image, error in
        if let error {
          continuation.resume(throwing: error)
        } else if let image {
          continuation.resume(returning: image)
        }
      }
    }

  }

  private let placesClient: GMSPlacesClient
}

