// Created by andy_bartholomew on 1/29/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlaceDetailView: View {
  let place: Place
  let tags: [TagWithID]

  @State private var updatedNote: String
  @State private var selectedTagIDs: Set<String>

  @EnvironmentObject var placeStore: PlaceStore
  @Environment(\.dismiss) var dismiss: DismissAction

  init(place: Place, tags: [TagWithID]) {
    self.place = place
    self.tags = tags

    // When we added the DismissAction we had to start using the underscore versions of these State properties
    self._updatedNote = State(initialValue: place.note ?? "")
    self._selectedTagIDs = State(initialValue: place.tagIDs)
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
            save()
          }.buttonStyle(.borderedProminent)
        }
      }.padding(.horizontal)
    }
  }

  private func save() {
    let newPlace = Place(
      id: place.id,
      name: place.name,
      remoteId: place.remoteId,
      note: updatedNote,
      tagIDs: selectedTagIDs
    )

    Task {
      do {
        try await placeStore.update(place: newPlace)
        dismiss()
      } catch {
        print(error)
      }
    }
  }
}

struct PlaceDetailView_Previews: PreviewProvider {
  static var previews: some View {
    PlaceDetailView(
      place: Place.forPreview(name: "Cool Place", note: "it's cool", tagIDs: ["1", "4"]),
      tags: [
        TagWithID(id: "1", tag: Tag.makeForPreview(name: "Dinner")),
        TagWithID(id: "2", tag: Tag.makeForPreview(name: "Lunch")),
        TagWithID(id: "3", tag: Tag.makeForPreview(name: "Brunch")),
        TagWithID(id: "4", tag: Tag.makeForPreview(name: "Sushi")),
        TagWithID(id: "5", tag: Tag.makeForPreview(name: "Bar")),
        TagWithID(id: "6", tag: Tag.makeForPreview(name: "Mediterranean")),
        TagWithID(id: "7", tag: Tag.makeForPreview(name: "Japanese")),
      ]
    )
  }
}
