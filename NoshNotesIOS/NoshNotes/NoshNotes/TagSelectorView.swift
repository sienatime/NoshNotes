// Created by andy_bartholomew on 1/19/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import SwiftUI
import SwiftUIChipGroup

struct TagSelectorView: View {
  var tags: [TagWithID]

  var body: some View {
    ChipGroup(
      chips: tags.map { BetterChipItem(id: $0.id, name: $0.name, isSelected: false) },
      width: 250,
      selection: .multi
    ) { chip in
      print(chip.id)
    }
//    List(tags) { tag in
//      Text(tag.name)
//    }
//    .padding()
  }
}

// I hate that ChipItemProtocol requires AnyObject and also mutable properties.
class BetterChipItem: ChipItemProtocol {
  init(id: String, name: String, isSelected: Bool = false) {
    self.id = id
    self.name = name
    self.isSelected = isSelected
  }

  var id: String
  var name: String
  var isSelected: Bool
}
