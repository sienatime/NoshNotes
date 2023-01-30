// Created by andy_bartholomew on 1/29/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlaceDetailView: View {
  let place: Place
  let tags: [TagWithID]

  @State private var updatedNote: String
  @State private var selectedTagIDs: Set<String>

  init(place: Place, tags: [TagWithID]) {
    self.place = place
    self.tags = tags
    self.updatedNote = place.note ?? ""
    self.selectedTagIDs = place.tagIDs
  }

  var body: some View {
    ScrollView {
      VStack(spacing: 16) {
        GooglePlaceImage(imageMetadata: place.imageMetadata)
        VStack(alignment: .leading, spacing: 16) {
          HStack {
            Text("Note")

            TextField("Note", text: $updatedNote, prompt: Text("What looks good about this place?"))
              .textFieldStyle(.roundedBorder)
          }
          Text("Tags")
          TagSelectorView(tags: tags, selectedTagIDs: $selectedTagIDs)          .frame(maxHeight: 120)
          Button("Add Tag") {
            // TODO: launch the tag creation dialog. Gonna need to have a way to reload all tags after creating a tag though.
          }
          Button("Save") {
            print("save!")
            // TODO: write to the Firebase backend
          }.buttonStyle(.borderedProminent)
        }
      }.padding(.horizontal)
    }
  }
}

struct PlaceDetailView_Previews: PreviewProvider {
  static var previews: some View {
    PlaceDetailView(
      place: Place(id: "123", name: "Cool Place", note: "it's cool"),
      tags: [
        TagWithID(id: "1", tag: Tag(name: "Dinner")),
        TagWithID(id: "2", tag: Tag(name: "Lunch")),
        TagWithID(id: "3", tag: Tag(name: "Brunch")),
        TagWithID(id: "4", tag: Tag(name: "Sushi")),
        TagWithID(id: "5", tag: Tag(name: "Bar")),
        TagWithID(id: "6", tag: Tag(name: "Mediterranean")),
        TagWithID(id: "7", tag: Tag(name: "Japanese")),
      ]
    )
  }
}
