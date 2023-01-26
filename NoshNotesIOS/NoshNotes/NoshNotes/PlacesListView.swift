// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlacesListView: View {
  // The tags you can select in order to filter the places
  let tags: [TagWithID]
  let places: [Place]

  @State var selectedTagIDs: Set<String> = []

  var filteredPlaces: [Place] {
    places.filter { place in
      // TODO: This feels like business logic that should live somewhere else.
      selectedTagIDs.isSubset(of: place.tagIDs)
    }
  }

  // TODO: move this logic outside this view. It might not know about all the tags on places. 
  func tagNames(for ids: Set<String>) -> [String] {
    let tagsByID: [String: String] = tags.reduce(into: [:]) { partialResult, tagWithId in
      partialResult[tagWithId.id] = tagWithId.name
    }

    let names = ids.compactMap { tagsByID[$0] }
    return names.sorted()
  }

  var body: some View {
    VStack {
      TagSelectorView(tags: tags, selectedTagIDs: $selectedTagIDs)
        .frame(maxHeight: 160)
      List(filteredPlaces) { place in
        PlaceCardView(place: place, tags: tagNames(for: place.tagIDs))
      }
    }
  }
}

struct PlacesListView_Previews: PreviewProvider {
  static var previews: some View {
    PlacesListView(tags: [], places: [
      Place(id: "1", name: "Super Cool Place", note: nil, tagIDs: []),
    ])
    PlacesListView(
      tags: [
        TagWithID(id: "1", tag: Tag(name: "Dinner")),
        TagWithID(id: "2", tag: Tag(name: "Lunch")),
        TagWithID(id: "3", tag: Tag(name: "Brunch")),
        TagWithID(id: "4", tag: Tag(name: "Sushi")),
        TagWithID(id: "5", tag: Tag(name: "Bar")),
        TagWithID(id: "6", tag: Tag(name: "Mediterranean")),
        TagWithID(id: "7", tag: Tag(name: "Japanese")),
      ],
      places: [
        Place(id: "1", name: "Super Cool Place", note: "it's cool", tagIDs: ["1", "4"]),
      ])

  }
}
