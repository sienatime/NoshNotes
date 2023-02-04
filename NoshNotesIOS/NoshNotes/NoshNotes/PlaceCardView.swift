// Created by andy_bartholomew on 1/24/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlaceCardView: View {
  let place: Place
  // we ignore place.tagIds and instead display these tags. is that bad? I didn't want to talk to the tag store in this view.
  let tags: [Tag]

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
        ForEach(tags, id: \.self) { tag in
          TagView(
            name: tag.name,
            icon: tag.icon,
            textColor: tag.textColor,
            backgroundColor: tag.backgroundColor)
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
      tags: ["cool", "place"].map { Tag.makeForPreview(name: $0) })
    PlaceCardView(
      place: Place.forPreview(
        name: "Other Place"),
      tags: ["other", "place"].map { Tag.makeForPreview(name: $0) })
  }
}

