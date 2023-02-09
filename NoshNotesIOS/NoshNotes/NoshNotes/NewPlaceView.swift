// Created by andy_bartholomew on 2/1/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import GooglePlaces
import SwiftUI

struct NewPlaceView: View {
  @Binding var isPresented: Bool

  let googlePlaceID: String
  let autocompleteToken: GMSAutocompleteSessionToken

  @State private var note: String = ""
  @State private var selectedTagIDs: Set<String> = []
  @State private var googlePlace: GooglePlace?
  @State private var newTagModalIsPresented: Bool = false

  @EnvironmentObject var placeStore: PlaceStore
  @EnvironmentObject var tagStore: TagStore

  var body: some View {
    ZStack {
      inputForm
      buttonFloater
    }
    .padding(.horizontal)
    .navigationTitle(googlePlace?.name ?? "")
    .task {
      do {
        googlePlace = try await placeStore.fetchPlaceData(id: googlePlaceID, sessionToken: autocompleteToken)
      } catch {
        print(error)
      }
    }
  }

  private var inputForm: some View {
    ScrollView {
      VStack(spacing: 16) {
        GooglePlaceImage(imageMetadata: googlePlace?.imageMetadata)
        VStack(alignment: .leading, spacing: 16) {
          noteField
          Text("Tags")
          TagSelectorView(tags: tagStore.tags, selectedTagIDs: $selectedTagIDs, numRows: 4)
        }
      }
    }
  }

  private var noteField: some View {
    HStack {
      Text("Note:")
      TextField("Note", text: $note, prompt: Text("What looks good about this place?"))
        .textFieldStyle(.roundedBorder)
    }
  }

  private var buttonFloater: some View {
    VStack {
      Spacer()
      buttonBar
    }
  }

  private var buttonBar: some View {
    HStack {
      addTagButton
      Spacer()
      Button("Save") {
        save()
      }.buttonStyle(.borderedProminent)
    }
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
    let newPlace = NewPlace(
      note: note,
      remoteId: googlePlaceID,
      tagIDs: selectedTagIDs
    )

    Task {
      do {
        try await placeStore.create(newPlace: newPlace)
        isPresented = false
      } catch {
        print(error)
      }
    }
  }
}

struct NewPlaceView_Previews: PreviewProvider {
  static let tagStore = TagStore()

  static var previews: some View {
    NewPlaceView(
      isPresented: .constant(true),
      googlePlaceID: "123",
      autocompleteToken: GMSAutocompleteSessionToken())
    .environmentObject(tagStore)
    .environmentObject(PlaceStore(tagStore: tagStore))
  }
}
