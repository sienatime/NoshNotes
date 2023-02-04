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
              tagButton(tag: tag)
            }
          }
        }
      }
    }
  }

  private func tagButton(tag: TagWithID) -> some View {
    Button {
      onSelect(tag: tag)
    } label: {
      TagView(
        name: tag.name,
        icon: tag.icon,
        textColor: tag.tag.textColor,
        backgroundColor: tag.tag.backgroundColor,
        isSelected: selectedTagIDs.contains(tag.id))
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
      Tag.makeForPreview(name: "Dinner"),
      Tag.makeForPreview(name: "Lunch"),
      Tag.makeForPreview(name: "Brunch"),
      Tag.makeForPreview(name: "Sushi"),
      Tag.makeForPreview(name: "Bar"),
      Tag.makeForPreview(name: "Mediterranean"),
      Tag.makeForPreview(name: "Japanese"),
    ])
    PreviewHost(tags: [
      Tag.makeForPreview(name: "TODO"),
      Tag.makeForPreview(name: "Fix"),
      Tag.makeForPreview(name: "Flows"),
    ])
  }
}

