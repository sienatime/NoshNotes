// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlacesListView: View {
  var tags: [TagWithID]

  var body: some View {
    TagSelectorView(tags: tags)
  }
}

struct PlacesListView_Previews: PreviewProvider {
  static var previews: some View {
    PlacesListView(tags: [
      TagWithID(id: "1", tag: Tag(name: "Dinner")),
      TagWithID(id: "2", tag: Tag(name: "Lunch")),
      TagWithID(id: "3", tag: Tag(name: "Brunch")),
      TagWithID(id: "4", tag: Tag(name: "Sushi")),
      TagWithID(id: "5", tag: Tag(name: "Bar")),
      TagWithID(id: "6", tag: Tag(name: "Mediterranean")),
    ])

  }
}
