// Created by andy_bartholomew on 2/3/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation
import SwiftUI

extension String {

  var hexUInt64: UInt64 {
    var hexInt: UInt64 = 0
    Scanner(string: self).scanHexInt64(&hexInt)
    return hexInt
  }

  func droppingHexColorPrefixes() -> String {
    replacingOccurrences(of: "#", with: "").replacingOccurrences(of: "0x", with: "")
  }
}

extension Color {
  init(rgbHex hex: UInt64, opacity: Double = 1) {
    let red = CGFloat((hex >> 16) & 0xFF) / 255.0
    let green = CGFloat((hex >> 8) & 0xFF) / 255.0
    let blue = CGFloat(hex & 0xFF) / 255.0

    self.init(.sRGB, red: red, green: green, blue: blue, opacity: opacity)
  }

  static func makeColor(withRgbHexString hex: String) -> Color {
    let hexString = hex.droppingHexColorPrefixes()
    return Color(rgbHex: hexString.hexUInt64)
  }
}
