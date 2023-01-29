// Created by andy_bartholomew on 1/29/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlaceDetailView: View {
  let place: Place
  let tagNames: [String]

  @State private var updatedNote: String
  // TODO: implement edit mode

  init(place: Place, tagNames: [String]) {
    self.place = place
    self.tagNames = tagNames
    self.updatedNote = place.note ?? ""
  }

  var body: some View {
    VStack {
      GooglePlaceImage(imageMetadata: place.imageMetadata)
      Form {
        TextField(text: $updatedNote, prompt: Text("What looks good about this place?")) {
          Text("Note")
        }
      }
      Text("Tags")
      Spacer()
    }.padding(.horizontal)
  }
}

struct PlaceDetailView_Previews: PreviewProvider {
  static var previews: some View {
    PlaceDetailView(
      place: Place(id: "123", name: "Cool Place", note: "it's cool"),
      tagNames: ["cool", "place"]
    )
  }
}
