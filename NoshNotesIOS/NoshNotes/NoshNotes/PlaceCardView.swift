// Created by andy_bartholomew on 1/24/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlaceCardView: View {
  let place: Place
  let tags: [String]

  var body: some View {
    VStack(alignment: .leading, spacing: 8) {
      Text(place.name).fontWeight(.bold)
      Text(place.note ?? "")
      HStack {
        ForEach(tags, id: \.self) {
          Text("(\($0))")
            .fontWeight(.light)
        }
      }
    }
  }
}

struct PlaceCardView_Previews: PreviewProvider {
  static var previews: some View {
    PlaceCardView(
      place: Place(id: "1", name: "Cool Place", note: "it's cool", tagIDs: []),
      tags: ["cool", "place"])
  }
}
