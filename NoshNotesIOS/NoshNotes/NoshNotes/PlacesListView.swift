// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlacesListView: View {
  // The tags you can select in order to filter the places
  let tags: [TagWithID]
  let places: [Place]

  init(tags: [TagWithID], places: [Place]) {
    self.tags = tags
    self.places = places

    tagsByID = tags.reduce(into: [:]) { partialResult, tagWithId in
      partialResult[tagWithId.id] = tagWithId.name
    }
  }
  private let tagsByID: [String: String]

  @State private var selectedTagIDs: Set<String> = []
  @State private var showingCreateModal: Bool = false

  private var filteredPlaces: [Place] {
    places.filter { place in
      // TODO: This feels like business logic that should live somewhere else.
      selectedTagIDs.isSubset(of: place.tagIDs)
    }
  }

  // TODO: move this logic outside this view. It might not know about all the tags on places.
  func tagNames(for ids: Set<String>) -> [String] {
    let names = ids.compactMap { tagsByID[$0] }
    return names.sorted()
  }

  var body: some View {
    NavigationStack {
      ZStack {
        VStack(spacing: 28) {
          tagSelectorView
          placesList
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

  var placesList: some View {
    ScrollView {
      LazyVStack(spacing: 20) {
        ForEach(filteredPlaces) { place in
          NavigationLink(destination: PlaceDetailView(place: place, tags: tags)) {
            PlaceCardView(place: place, tagNames: tagNames(for: place.tagIDs))
          }.tint(.black)
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
      places: [
        Place.forPreview(name: "Super Cool Place", note: "it's cool", tagIDs: ["1", "4"]),
        Place.forPreview(name: "Another Cool Place", note: "it's also cool", tagIDs: ["1", "4"]),
      ])
    PlacesListView(tags: [], places: [
      Place.forPreview(name: "Super Cool Place", note: nil, tagIDs: []),
    ])
  }
}
