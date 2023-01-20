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
    ])

  }
}
