// Created by andy_bartholomew on 2/3/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import SwiftUI

struct TagButton: View {
  init(
    name: String,
    icon: String? = nil,
    textColor: String? = nil,
    backgroundColor: String? = nil,
    isSelected: Bool = false,
    onSelect: (() -> Void)? = nil)
  {
    self.name = name
    self.icon = icon
    self.textColor = textColor ?? "#444444"
    self.backgroundColor = backgroundColor ?? "#EDEDED"
    self.isSelected = isSelected
    self.onSelect = onSelect ?? {}
  }

  let name: String
  let icon: String?
  let textColor: String
  let backgroundColor: String
  let isSelected: Bool
  let onSelect: () -> Void

  var body: some View {
    // TODO: extract label for tags on cards
    Button(action: onSelect) {
      Label(name, systemImage: systemImage(for: icon))
        .fontWeight(.semibold)
        .padding(.horizontal, 12)
        .padding(.vertical, 6)
        .tint(Color.makeColor(withRgbHexString: textColor))
        .background(background)
        .cornerRadius(16)
    }
  }

  var background: some View {
    Color.makeColor(withRgbHexString: backgroundColor)
      .brightness(isSelected ? -0.1 : 0.0)
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
    TagButton(name: "Dinner", isSelected: true)
    TagButton(name: "Lunch", backgroundColor: "#E1BEE7")
  }
}
