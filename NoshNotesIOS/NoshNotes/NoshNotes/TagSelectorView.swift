// Created by andy_bartholomew on 1/19/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import SwiftUI

struct TagSelectorView: View {
  let tags: [TagWithID]
  @Binding var selectedTagIDs: Set<String>

  // We divvy up each tag to one of three rows using modulo arithmetic
  private func tags(atIndex rowIndex: Int) -> [TagWithID] {
    tags.enumerated().filter { index, _ in
      index % 3 == rowIndex
    }.map { index, tag in
      tag
    }
  }

  var body: some View {
    ScrollView(.horizontal) {
      VStack(alignment: .leading) {
        ForEach(0..<3) { rowIndex in
          HStack {
            ForEach(tags(atIndex: rowIndex)) { tag in
              ChipButton(
                tag: tag,
                isSelected: selectedTagIDs.contains(tag.id),
                onSelect: onSelect)
            }
          }
        }
      }
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
  struct PreviewHost: View {
    let tags: [Tag]
    @State var selectedTagIDs: Set<String> = []

    var tagsWithId: [TagWithID] {
      tags.enumerated().map { index, tag in
        TagWithID(id: "\(index)", tag: tag)
      }
    }

    var body: some View {
      TagSelectorView(tags: tagsWithId, selectedTagIDs: $selectedTagIDs)
    }
  }

  static var previews: some View {
    PreviewHost(tags: [
      Tag(name: "Dinner"),
      Tag(name: "Lunch"),
      Tag(name: "Brunch"),
      Tag(name: "Sushi"),
      Tag(name: "Bar"),
      Tag(name: "Mediterranean"),
      Tag(name: "Japanese"),
    ])
    PreviewHost(tags: [
      Tag(name: "TODO"),
      Tag(name: "Fix"),
      Tag(name: "Flows"),
    ])
  }
}

