// Created by andy_bartholomew on 2/9/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI
import GooglePlaces

struct EditPlaceView: View {

  let name: String?
  let imageMetadata: GMSPlacePhotoMetadata?
  let allTags: [TagWithID]

  @Binding var note: String
  @Binding var selectedTagIDs: Set<String>

  var body: some View {
    ScrollView {
      VStack(spacing: 16) {
        if let name {
          Text(name).font(.title)
        }
        GooglePlaceImage(imageMetadata: imageMetadata)
        VStack(alignment: .leading, spacing: 16) {
          noteField
          Text("Tags")
          TagSelectorView(tags: allTags, selectedTagIDs: $selectedTagIDs, numRows: 4)
        }
      }
    }
  }

  private var noteField: some View {
    HStack {
      Text("Note:")
      TextField("Note", text: $note, prompt: Text("What looks good about this place?"))
        .textFieldStyle(.roundedBorder)
    }
  }
}

struct EditPlaceView_Previews: PreviewProvider {
  static var previews: some View {
    EditPlaceView(
      name: "Cool Place",
      imageMetadata: nil,
      allTags: [],
      note: .constant("it's cool"),
      selectedTagIDs: .constant([]))
  }
}
