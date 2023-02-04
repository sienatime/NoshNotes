// Created by andy_bartholomew on 2/3/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import SwiftUI

struct TagButton: View {
  init(name: String, isSelected: Bool = false, onSelect: (() -> Void)? = nil) {
    self.name = name
    self.isSelected = isSelected
    self.onSelect = onSelect ?? {}
  }

  let name: String
  let isSelected: Bool
  let onSelect: () -> Void

  var body: some View {
    Button(action: onSelect) {
      Label(name, systemImage: "fork.knife")
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
    TagButton(name: "Dinner")
  }
}
