// Created by andy_bartholomew on 1/29/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlaceDetailView: View {
  let place: Place
  let tagNames: [String]

  @State private var updatedNote: String

  init(place: Place, tagNames: [String]) {
    self.place = place
    self.tagNames = tagNames
    self.updatedNote = place.note ?? ""
  }

  var body: some View {
    VStack(spacing: 16) {
      GooglePlaceImage(imageMetadata: place.imageMetadata)
      VStack(alignment: .leading, spacing: 16) {
        HStack {
          Text("Note")

          TextField("Note", text: $updatedNote, prompt: Text("What looks good about this place?"))
            .textFieldStyle(.roundedBorder)
        }
        Text("Tags")
        HStack {
          // TODO: show all available tags with the current tags selected
          ForEach(tagNames, id: \.self) {
            TagView(text: $0)
          }
        }
        Button("Add Tag") {
          // TODO: launch the tag creation dialog
        }
        Button("Save") {
          print("save!")
          // TODO: write to the Firebase backend
        }.buttonStyle(.borderedProminent)
      }
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
