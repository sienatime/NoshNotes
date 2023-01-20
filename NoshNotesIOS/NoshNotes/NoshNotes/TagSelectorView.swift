// Created by andy_bartholomew on 1/19/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import SwiftUI

struct TagSelectorView: View {
  var tags: [TagWithID]

  let rows = (0..<3).map { _ in
    GridItem(.flexible(maximum: 36), alignment: .leading)
  }

  var body: some View {
    ScrollView(.horizontal) {
      LazyHGrid(rows: rows, alignment: .top) {
        ForEach(tags) { tag in
          ChipButton(tag: tag)
        }
      }.padding()
    }
  }
}

struct ChipButton: View {
  let tag: TagWithID
  @State var isSelected: Bool = false

  var body: some View {
    Button(tag.name) {
      isSelected = !isSelected
      print(tag.id)
    }
    .buttonStyle(.bordered)
    .tint(isSelected ? Color.purple : Color.blue)
  }
}

