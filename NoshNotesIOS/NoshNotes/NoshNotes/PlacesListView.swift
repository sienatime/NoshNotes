// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlacesListView: View {
  // The tags you can select in order to filter the places
  let tags: [TagWithID]
  let places: LoadState<[Place]>

  init(tags: [TagWithID], places: LoadState<[Place]>) {
    self.tags = tags
    self.places = places
  }

  @State private var selectedTagIDs: Set<String> = []
  @State private var showingCreateModal: Bool = false

  private var filteredPlaces: LoadState<[Place]> {
    places.map { loadedPlaces in
      loadedPlaces.filter { place in
        // TODO: This feels like business logic that should live somewhere else.
        selectedTagIDs.isSubset(of: place.tagIDs)
      }
    }
  }

  var body: some View {
    NavigationStack {
      ZStack {
        VStack(spacing: 16) {
          tagSelectorView
          listView
          Spacer()
        }
        buttonFloater
          .sheet(isPresented: $showingCreateModal) {
            SearchPlacesView(shown: $showingCreateModal)
          }
      }
    }
  }

  var tagSelectorView: some View {
    TagSelectorView(tags: tags, selectedTagIDs: $selectedTagIDs)
      .frame(maxHeight: 120)
      .padding(.horizontal)
  }

  @ViewBuilder
  var listView: some View {
    switch filteredPlaces {
    case .loading:
      ProgressView()
    case .loaded(let loadedPlaces):
      listView(places: loadedPlaces)
    case .failed(let error):
      Text(error.localizedDescription).foregroundColor(.red)
    }
  }

  func listView(places: [Place]) -> some View {
    ScrollView {
      LazyVStack(spacing: 20) {
        ForEach(places) { place in
          NavigationLink(destination: PlaceDetailView(place: place, allTags: tags)) {
            PlaceCardView(place: place)
          }.tint(.primary)
        }
      }
    }
  }

  var buttonFloater: some View {
    VStack {
      Spacer()

      HStack {
        Spacer()
        newPlaceButton

      }.padding(.horizontal)
    }.padding(.vertical)
  }

  var newPlaceButton: some View {
    Button("+") {
      showingCreateModal = true
    }
    .font(.system(.largeTitle))
    .frame(width: 54, height: 54)
    .background(Color.blue)
    .foregroundColor(.white)
    .cornerRadius(27)
  }
}

struct PlacesListView_Previews: PreviewProvider {
  static var previews: some View {
    PlacesListView(
      tags: [
        TagWithID(id: "1", tag: Tag.makeForPreview(name: "Dinner")),
        TagWithID(id: "2", tag: Tag.makeForPreview(name: "Lunch")),
        TagWithID(id: "3", tag: Tag.makeForPreview(name: "Brunch")),
        TagWithID(id: "4", tag: Tag.makeForPreview(name: "Sushi")),
        TagWithID(id: "5", tag: Tag.makeForPreview(name: "Bar")),
        TagWithID(id: "6", tag: Tag.makeForPreview(name: "Mediterranean")),
        TagWithID(id: "7", tag: Tag.makeForPreview(name: "Japanese")),
      ],
      places: .loaded([
        Place.forPreview(
          name: "Super Cool Place",
          note: "it's cool",
          tags: [
            TagWithID(id: "1", tag: Tag.makeForPreview(name: "Dinner")),
            TagWithID(id: "4", tag: Tag.makeForPreview(name: "Sushi")),
          ]
        ),
        Place.forPreview(
          name: "Another Cool Place",
          note: "it's also cool",
          tags: [
            TagWithID(id: "1", tag: Tag.makeForPreview(name: "Dinner")),
            TagWithID(id: "4", tag: Tag.makeForPreview(name: "Sushi")),
          ]
        ),
      ]))
    PlacesListView(tags: [
    ], places: .loaded([
      Place.forPreview(name: "Super Cool Place", note: nil, tags: []),
    ]))
    PlacesListView(tags: [
      TagWithID(id: "1", tag: Tag.makeForPreview(name: "Dinner")),
    ], places: .loading)
    PlacesListView(tags: [
      TagWithID(id: "1", tag: Tag.makeForPreview(name: "Dinner")),
    ], places: .failed(FirebaseError.childrenType))
  }
}
