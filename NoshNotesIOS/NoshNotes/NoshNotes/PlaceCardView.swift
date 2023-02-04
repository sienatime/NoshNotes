// Created by andy_bartholomew on 1/24/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlaceCardView: View {
  let place: Place
  // we ignore place.tagIds and instead display these tag names. is that bad? I didn't want to talk to the tag store in this view.
  let tagNames: [String]

  @State private var image: UIImage?

  var body: some View {
    VStack(alignment: .leading, spacing: 8) {
      GooglePlaceImage(imageMetadata: place.imageMetadata)
      Text(place.name)
        .fontWeight(.bold)
        .font(.title)
      place.note.map { note in
        Text(note)
          .font(.body.italic())
      }
      HStack {
        ForEach(tagNames, id: \.self) {
          TagButton(name: $0)
        }
      }
    }
  }
}

struct PlaceCardView_Previews: PreviewProvider {
  static var previews: some View {
    PlaceCardView(
      place: Place.forPreview(
        name: "Cool Place",
        note: "it's cool"),
      tagNames: ["cool", "place"])
    PlaceCardView(
      place: Place.forPreview(
        name: "Other Place"),
      tagNames: ["other", "place"])
  }
}

