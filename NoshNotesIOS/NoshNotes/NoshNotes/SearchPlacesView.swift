// Created by andy_bartholomew on 1/31/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import GooglePlaces
import SwiftUI

struct SearchPlacesView: View {
  @Binding var shown: Bool

  @State private var searchText: String = ""
  @State private var searchResults: [GMSAutocompletePrediction] = []
  private let autocompleteToken = GMSAutocompleteSessionToken.init()

  @EnvironmentObject var placeStore: PlaceStore

  @State private var searchTask: Task<Void, Never>?

  var body: some View {
    NavigationStack {
      // TODO: add a dismiss button somewhere
      List(searchResults, id: \.placeID) { result in
        listItem(for: result)
      }.navigationDestination(for: GMSAutocompletePrediction.self) { result in
        NewPlaceView(
          isPresented: $shown,
          googlePlaceID: result.placeID,
          autocompleteToken: autocompleteToken)
      }
    }
    .searchable(text: $searchText, prompt: "Search Google Places")
    .onChange(of: searchText) { newValue in
      search(text: newValue)
    }
  }

  private func listItem(for result: GMSAutocompletePrediction) -> some View {
    NavigationLink(value: result) {
      VStack(alignment: .leading) {
        Text(result.attributedPrimaryText.string)
          .font(.system(.body))
        if let secondaryText = result.attributedSecondaryText?.string {
          Text(secondaryText)
            .font(.system(.caption))
        }
      }
    }
  }

  private func search(text: String) {
    // I'd rather debounce using some sort of functional paradigm but this is simple enough.
    searchTask?.cancel()
    searchTask = Task {
      do {
        try await Task.sleep(nanoseconds: 275 * 1_000_000) // 275 milliseconds

        let results = try await placeStore.search(text: text, token: autocompleteToken)
        self.searchResults = results
      } catch {
        print("Autocomplete error: \(error)")
        self.searchResults = []
      }
    }

  }
}
