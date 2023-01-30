// Created by andy_bartholomew on 1/30/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct TagView: View {
  let text: String

  var body: some View {
    Text(text)
      .fontWeight(.light)
      .padding(.horizontal, 10)
      .padding(.vertical, 4)
      .background(Color(white: 0.8))
      .cornerRadius(20)
  }
}

struct TagView_Previews: PreviewProvider {
  static var previews: some View {
    TagView(text: "food")
  }
}
