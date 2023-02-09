// Created by andy_bartholomew on 1/29/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlaceDetailView: View {
  let place: Place
  let allTags: [TagWithID]

  @State private var updatedNote: String
  @State private var selectedTagIDs: Set<String>
  @State private var newTagModalIsPresented: Bool

  @EnvironmentObject var placeStore: PlaceStore
  @Environment(\.dismiss) var dismiss: DismissAction

  init(place: Place, allTags: [TagWithID]) {
    self.place = place
    self.allTags = allTags

    // When we added the DismissAction we had to start using the underscore versions of these State properties
    self._updatedNote = State(initialValue: place.note ?? "")
    self._selectedTagIDs = State(initialValue: place.tagIDs)
    self._newTagModalIsPresented = State(initialValue: false)
  }

  var body: some View {
    VStack {
      EditPlaceView(
        name: place.name,
        imageMetadata: place.imageMetadata,
        allTags: allTags,
        note: $updatedNote,
        selectedTagIDs: $selectedTagIDs)
      Spacer()
      HStack {
        addTagButton
        Spacer()
        Button("Save") {
          save()
        }.buttonStyle(.borderedProminent)
      }
    }.padding(.horizontal)
  }

  private var addTagButton: some View {
    Button("Add Tag") {
      newTagModalIsPresented = true
    }.sheet(isPresented: $newTagModalIsPresented) {
      NewTagView()
        .presentationDetents([.fraction(0.3)])
      // hard-coding this sheet to be 1/3 of the screen :P
    }.padding(.vertical, 10)
  }

  private func save() {
    let selectedTags = selectedTagIDs.compactMap { id in
      allTags.first { tag in
        tag.id == id
      }
    }

    let newPlace = Place(
      id: place.id,
      name: place.name,
      remoteId: place.remoteId,
      note: updatedNote,
      tags: selectedTags
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
      place: Place.forPreview(
        name: "Cool Place",
        note: "it's cool",
        tags: [
          TagWithID(id: "1", tag: Tag.makeForPreview(name: "Dinner")),
          TagWithID(id: "4", tag: Tag.makeForPreview(name: "Sushi")),
        ]),
      allTags: [
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
