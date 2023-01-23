// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlacesListView: View {
  var tags: [TagWithID]
  var places: [Place]

  var body: some View {
    VStack {
      TagSelectorView(tags: tags)
        .frame(maxHeight: 160)
      List(places) { place in
        Text(place.name)
      }
    }
  }
}

struct PlacesListView_Previews: PreviewProvider {
  static var previews: some View {
    PlacesListView(tags: [], places: [
      Place(id: "1", name: "Super Cool Place"),
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
        Place(id: "1", name: "Super Cool Place"),
      ])

  }
}
