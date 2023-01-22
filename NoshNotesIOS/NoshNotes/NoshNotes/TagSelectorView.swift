// Created by andy_bartholomew on 1/19/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import SwiftUI

struct TagSelectorView: View {
  var tags: [TagWithID]
  @State var selectedTagIDs: Set<String> = []

  let rows = (0..<3).map { _ in
    GridItem(.flexible(maximum: 36), alignment: .leading)
  }

  var body: some View {
    ScrollView(.horizontal) {
      LazyHGrid(rows: rows, alignment: .top) {
        ForEach(tags) { tag in
          ChipButton(
            tag: tag,
            isSelected: selectedTagIDs.contains(tag.id),
            onSelect:  { tag in
              if selectedTagIDs.contains(tag.id) {
                selectedTagIDs.remove(tag.id)
              } else {
                selectedTagIDs.insert(tag.id)
              }
            })
        }
      }.padding()
    }
  }
}

struct ChipButton: View {
  let tag: TagWithID
  let isSelected: Bool
  let onSelect: (TagWithID) -> Void

  var body: some View {
    Button(tag.name) {
      onSelect(tag)
    }
    .buttonStyle(.bordered)
    .tint(isSelected ? Color.purple : Color.blue)
  }
}

