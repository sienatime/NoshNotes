// Created by andy_bartholomew on 2/3/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import SwiftUI

struct TagButton: View {
  let tag: TagWithID
  let isSelected: Bool
  let onSelect: (TagWithID) -> Void

  var body: some View {
    Button(action: { onSelect(tag) }) {
      Label(tag.name, systemImage: "fork.knife")
        .padding(.horizontal, 12)
        .padding(.vertical, 6)
    }
    .tint(.black)
    .background(Color(white: 0.8))
    .cornerRadius(16)
  }
}

struct TagButton_Previews: PreviewProvider {
  static var previews: some View {
    TagButton(tag: TagWithID(id: "123", tag: Tag(name: "Dinner")), isSelected: false) { _ in }
  }
}
