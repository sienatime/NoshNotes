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
      Text(tag.name)
        .padding(.horizontal, 16)
        .padding(.vertical, 4)
    }
    .frame(height: 32)
    .background(Color(white: 0.8))
    .cornerRadius(16)
  }
}

