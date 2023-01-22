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
            onSelect: onSelect)
        }
      }.padding()
    }
  }

  private func onSelect(tag: TagWithID) {
    if selectedTagIDs.contains(tag.id) {
      selectedTagIDs.remove(tag.id)
    } else {
      selectedTagIDs.insert(tag.id)
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

struct TagSelectorView_Previews: PreviewProvider {
  static var previews: some View {
    TagSelectorView(tags: [
      TagWithID(id: "1", tag: Tag(name: "Dinner")),
      TagWithID(id: "2", tag: Tag(name: "Lunch")),
      TagWithID(id: "3", tag: Tag(name: "Brunch")),
      TagWithID(id: "4", tag: Tag(name: "Sushi")),
      TagWithID(id: "5", tag: Tag(name: "Bar")),
      TagWithID(id: "6", tag: Tag(name: "Mediterranean")),
      TagWithID(id: "7", tag: Tag(name: "Japanese")),
    ])
  }
}

