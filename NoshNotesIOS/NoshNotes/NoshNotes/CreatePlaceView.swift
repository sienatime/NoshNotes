// Created by andy_bartholomew on 1/31/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import GooglePlaces
import SwiftUI

struct CreatePlaceView: View {
  // This doesn't feel totally right.
  // Probably we should have some sort of bound variable representing the flow being complete.
  @Environment(\.presentationMode) var presentationMode

  @State private var searchText: String = ""
  @State private var searchResults: [GMSAutocompletePrediction] = []
  private let token = GMSAutocompleteSessionToken.init()

  var body: some View {
    NavigationStack {
      List(searchResults, id: \.placeID) { result in
        Text(result.attributedFullText.string)
      }
    }.searchable(text: $searchText, prompt: "Search Google Places")
      .onChange(of: searchText) { newValue in
        // TODO: figure out how to debounce without Combine or just use Combine
        search(text: newValue)
      }

  }

  // TODO: put this back in the UI
  var saveButton: some View {
    Button("Save") {
      presentationMode.wrappedValue.dismiss()
    }
    .buttonStyle(.borderedProminent)
  }

  @MainActor
  func search(text: String) {
    Task {
      do {
        let results = try await doSearch(searchText: searchText)
        self.searchResults = results
      } catch {
        print("Autocomplete error: \(error)")
        self.searchResults = []
      }
    }
  }

  @MainActor
  func doSearch(searchText: String) async throws -> [GMSAutocompletePrediction] {
    let filter = GMSAutocompleteFilter()
//    filter.types = ["restaurant"]
//    filter.locationBias = GMSPlaceRectangularLocationOption(
//      northEastBounds,
//      southWestBounds)

    return try await withCheckedThrowingContinuation { continuation in
      GMSPlacesClient.shared().findAutocompletePredictions(
        fromQuery: searchText,

        filter: filter,
        sessionToken: token,
        callback: { (results, error) in
          if let error {
            continuation.resume(throwing: error)
          }
          if let results {
            continuation.resume(returning: results)
          }
        })
    }
  }
}
