// Created by andy_bartholomew on 1/24/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlaceCardView: View {
  let place: Place
  let tags: [String]
  @State private var image: UIImage?

  @Environment(\.imageLoader) private var imageLoader

  var body: some View {
    VStack(alignment: .leading, spacing: 8) {
      if let image {
        Image(uiImage: image)
          .resizable()
          .aspectRatio(contentMode: .fill)
          .frame(width: 330, height: 240)
          .clipped()
      } else {
        Color.blue.opacity(0.2).frame(width: 330, height: 240)
      }
      Text(place.name)
        .fontWeight(.bold)
        .font(.title)
      place.note.map { note in
        Text(note)
          .font(.body.italic())
      }
      HStack {
        ForEach(tags, id: \.self) {
          Text($0)
            .fontWeight(.light)
            .padding(.horizontal, 10)
            .padding(.vertical, 4)
            .background(Color(white: 0.8))
            .cornerRadius(20)
        }
      }
    }.task {
      await loadImage()
    }
  }

  private func loadImage() async {
    guard let imageMetadata = place.imageMetadata else {
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

struct PlaceCardView_Previews: PreviewProvider {
  static var previews: some View {
    PlaceCardView(
      place: Place(
        id: "1",
        name: "Cool Place",
        note: "it's cool"),
      tags: ["cool", "place"])
    PlaceCardView(
      place: Place(
        id: "2",
        name: "Other Place"),
      tags: ["other", "place"])
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
