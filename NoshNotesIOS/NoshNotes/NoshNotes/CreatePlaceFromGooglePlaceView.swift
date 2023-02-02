// Created by andy_bartholomew on 2/1/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import GooglePlaces
import SwiftUI

struct CreatePlaceFromGooglePlaceView: View {
  @Binding var isPresented: Bool

  let googlePlaceID: String
  let autocompleteToken: GMSAutocompleteSessionToken

  @State private var note: String = ""
  @State private var selectedTagIDs: Set<String> = []
  @State private var googlePlace: GooglePlace?

  @EnvironmentObject var placeStore: PlaceStore
  @EnvironmentObject var tagStore: TagStore

  var body: some View {
    ScrollView {
      // TODO: re-use place editing views from PlaceDetailView
      VStack(alignment: .leading, spacing: 16) {
        if let googlePlace {
          Text(googlePlace.name)
          GooglePlaceImage(imageMetadata: googlePlace.imageMetadata)
        }

        HStack {
          Text("Note")
          TextField("Note", text: $note, prompt: Text("What looks good about this place?"))
            .textFieldStyle(.roundedBorder)
        }
        Text("Tags")
        TagSelectorView(tags: tagStore.tags, selectedTagIDs: $selectedTagIDs)
          .frame(maxHeight: 120)
        Button("Save") {
          save()
        }.buttonStyle(.borderedProminent)
      }.padding(.horizontal)
    }.task {
      do {
        googlePlace = try await placeStore.fetchPlaceData(id: googlePlaceID, sessionToken: autocompleteToken)
      } catch {
        print(error)
      }
    }
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

struct CreatePlaceFromGooglePlaceView_Previews: PreviewProvider {
  static var previews: some View {
    CreatePlaceFromGooglePlaceView(
      isPresented: .constant(true),
      googlePlaceID: "123",
      autocompleteToken: GMSAutocompleteSessionToken())
  }
}
