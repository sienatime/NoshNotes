// Created by andy_bartholomew on 1/31/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import GooglePlaces
import SwiftUI

struct CreatePlaceView: View {
  @Binding var shown: Bool

  @State private var searchText: String = ""
  @State private var searchResults: [GMSAutocompletePrediction] = []
  private let token = GMSAutocompleteSessionToken.init()

  var body: some View {
    NavigationStack {
      List(searchResults, id: \.placeID) { result in
        NavigationLink(result.attributedFullText.string)  {
          CreatePlaceFromGooglePlaceView(
            isPresented: $shown,
            googlePlaceID: result.placeID)
        }
      }
    }.searchable(text: $searchText, prompt: "Search Google Places")
      .onChange(of: searchText) { newValue in
        // TODO: figure out how to debounce without Combine or just use Combine
        search(text: newValue)
      }

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
    filter.types = [kGMSPlaceTypeBakery, kGMSPlaceTypeMealTakeaway, kGMSPlaceTypeRestaurant]
    filter.locationBias = GMSPlaceRectangularLocationOption(
      CLLocationCoordinate2D(latitude: 34.156319, longitude: -117.831394),
      CLLocationCoordinate2D(latitude: 33.706586, longitude: -118.606194))

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
