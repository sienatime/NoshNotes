// Created by andy_bartholomew on 1/29/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlaceDetailView: View {
  let place: Place
  let tags: [String]
  // TODO: implement edit mode

  var body: some View {
    PlaceCardView(place: place, tags: tags)
  }
}

struct PlaceDetailView_Previews: PreviewProvider {
  static var previews: some View {
    PlaceDetailView(
      place: Place(id: "123", name: "Cool Place", note: "it's cool"),
      tags: ["cool", "place"]
    )
  }
}
