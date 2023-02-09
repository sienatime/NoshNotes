// Created by andy_bartholomew on 1/19/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import SwiftUI

struct TagSelectorView: View {

  init(tags: [TagWithID], selectedTagIDs: Binding<Set<String>>, numRows: Int = 3) {
    self.tags = tags
    self.numRows = numRows
    self._selectedTagIDs = selectedTagIDs
  }

  let tags: [TagWithID]
  let numRows: Int
  @Binding var selectedTagIDs: Set<String>

  // We divvy up each tag to a row using modulo arithmetic
  private func tags(atIndex rowIndex: Int) -> [TagWithID] {
    tags.enumerated().filter { index, _ in
      index % numRows == rowIndex
    }.map { index, tag in
      tag
    }
  }

  var body: some View {
    ScrollView(.horizontal) {
      VStack(alignment: .leading) {
        ForEach(0..<numRows, id: \.self) { rowIndex in
          tagRow(at: rowIndex)
        }
      }.padding(.horizontal)
    }
  }

  func tagRow(at rowIndex: Int) -> some View {
    HStack {
      ForEach(tags(atIndex: rowIndex)) { tag in
        tagButton(tag: tag)
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
    let numRows: Int
    @State var selectedTagIDs: Set<String> = []

    var tagsWithId: [TagWithID] {
      tags.enumerated().map { index, tag in
        TagWithID(id: "\(index)", tag: tag)
      }
    }

    var body: some View {
      TagSelectorView(tags: tagsWithId, selectedTagIDs: $selectedTagIDs, numRows: numRows)
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
    ], numRows: 3)
    PreviewHost(tags: [
      Tag.makeForPreview(name: "Dinner"),
      Tag.makeForPreview(name: "Lunch"),
      Tag.makeForPreview(name: "Brunch"),
      Tag.makeForPreview(name: "Sushi"),
      Tag.makeForPreview(name: "Bar"),
      Tag.makeForPreview(name: "Mediterranean"),
      Tag.makeForPreview(name: "Japanese")
    ], numRows: 4)
  }
}

