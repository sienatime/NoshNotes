// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct PlacesListView: View {
  var tags: [Tag]

  var body: some View {
    VStack {
      Text("Hello, \(tags.count) tag(s)!")
    }
    .padding()
  }
}

struct PlacesListView_Previews: PreviewProvider {
  static var previews: some View {
    PlacesListView(tags: [])
    PlacesListView(tags: [Tag(name: "Dinner")])
  }
}
