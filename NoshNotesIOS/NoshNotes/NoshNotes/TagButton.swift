// Created by andy_bartholomew on 2/3/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import SwiftUI

struct TagButton: View {
  init(
    name: String,
    icon: String? = nil,
    isSelected: Bool = false,
    onSelect: (() -> Void)? = nil)
  {
    self.name = name
    self.icon = icon
    self.isSelected = isSelected
    self.onSelect = onSelect ?? {}
  }

  let name: String
  let icon: String?
  let isSelected: Bool
  let onSelect: () -> Void

  var body: some View {
    Button(action: onSelect) {
      Label(name, systemImage: systemImage(for: icon))
        .padding(.horizontal, 12)
        .padding(.vertical, 6)
        .tint(.black)
    }
    .background(Color(white: 0.8))
    .cornerRadius(16)
  }

  private func systemImage(for iconName: String?) -> String {
    guard let iconName else {
      return "fork.knife"
    }
    let mapping = [
      "pin" : "mappin",
      "dinner" : "fork.knife",
      "lunch" : "carrot",
      "breakfast" : "takeoutbag.and.cup.and.straw",
      "bar" : "wineglass",
      "coffee" : "cup.and.saucer",
      "restaurant" : "fork.knife",
      "dessert" : "birthday.cake",
      "heart" : "heart",
    ]

    return mapping[iconName] ?? "fork.knife"
  }
}

struct TagButton_Previews: PreviewProvider {
  static var previews: some View {
    TagButton(name: "Dinner")
  }
}
