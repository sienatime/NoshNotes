// Created by andy_bartholomew on 1/29/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI
import GooglePlaces

struct GooglePlaceImage: View {
  
  let imageMetadata: GMSPlacePhotoMetadata?
  @State private var image: UIImage?

  @Environment(\.imageLoader) private var imageLoader

  let height: CGFloat = 180
  let width: CGFloat = 360

  var body: some View {
    if let image {
      Image(uiImage: image)
        .resizable()
        .aspectRatio(contentMode: .fill)
        .frame(width: width, height: height)
        .clipped()
    } else {
      Color
        .blue
        .opacity(0.2)
        .frame(width: width, height: height)
        .task(id: imageMetadata) {
          await loadImage()
        }
    }
  }

  private func loadImage() async {
    guard let imageMetadata else {
      return
    }
    do {
      self.image = try await imageLoader.fetchImage(metadata: imageMetadata)
    } catch {
      print("something went wrong: \(error)")
      self.image = nil
    }
  }
}

struct GooglePlaceImage_Previews: PreviewProvider {
  static var previews: some View {
    GooglePlaceImage(imageMetadata: nil)
  }
}


struct PlaceImageStoreKey: EnvironmentKey {
  static let defaultValue = PlaceImageStore()
}

extension EnvironmentValues {
  var imageLoader: PlaceImageStore {
    get { self[PlaceImageStoreKey.self] }
    set { self[PlaceImageStoreKey.self ] = newValue}
  }
}
