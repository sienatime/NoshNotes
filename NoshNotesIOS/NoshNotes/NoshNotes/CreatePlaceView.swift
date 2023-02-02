// Created by andy_bartholomew on 1/31/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import GooglePlaces
import SwiftUI

struct CreatePlaceView: View {
  @Binding var shown: Bool

  @State private var searchText: String = ""
  @State private var searchResults: [GMSAutocompletePrediction] = []
  private let autocompleteToken = GMSAutocompleteSessionToken.init()

  @EnvironmentObject var placeStore: PlaceStore

  var body: some View {
    NavigationStack {
      List(searchResults, id: \.placeID) { result in
        NavigationLink(result.attributedFullText.string)  {
          CreatePlaceFromGooglePlaceView(
            isPresented: $shown,
            googlePlaceID: result.placeID,
            autocompleteToken: autocompleteToken)
        }
      }
    }.searchable(text: $searchText, prompt: "Search Google Places")
      .onChange(of: searchText) { newValue in
        // TODO: figure out how to debounce without Combine or just use Combine
        search(text: newValue)
      }
  }

  private func search(text: String) {
    Task {
      do {
        let results = try await placeStore.search(text: text, token: autocompleteToken)
        self.searchResults = results
      } catch {
        print("Autocomplete error: \(error)")
        self.searchResults = []
      }
    }

  }
}
