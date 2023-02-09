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

  @EnvironmentObject var placeStore: PlaceStore
  @EnvironmentObject var tagStore: TagStore

  var body: some View {
    inputForm
    .navigationTitle(googlePlace?.name ?? "")
    .padding(.horizontal)
    .task {
      do {
        googlePlace = try await placeStore.fetchPlaceData(id: googlePlaceID, sessionToken: autocompleteToken)
      } catch {
        print(error)
      }
    }
  }

  private var inputForm: some View {
    EditPlaceView(
      name: nil, // we put the name in the navigation title instead
      imageMetadata: googlePlace?.imageMetadata,
      allTags: tagStore.tags,
      note: $note,
      selectedTagIDs: $selectedTagIDs,
      saveAction: save)
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
