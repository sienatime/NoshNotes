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

  let saveAction: () -> Void

  @State private var newTagModalIsPresented: Bool = false

  var body: some View {
    ZStack {
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
      buttonFloater
    }.padding(.horizontal)
  }

  private var noteField: some View {
    HStack {
      Text("Note:")
      TextField("Note", text: $note, prompt: Text("What looks good about this place?"))
        .textFieldStyle(.roundedBorder)
    }
  }

  private var buttonFloater: some View {
    VStack {
      Spacer()
      buttonBar
    }
  }

  private var buttonBar: some View {
    HStack {
      addTagButton
      Spacer()
      Button("Save", action: saveAction)
        .buttonStyle(.borderedProminent)
    }
  }

  private var addTagButton: some View {
    Button("Add Tag") {
      newTagModalIsPresented = true
    }.sheet(isPresented: $newTagModalIsPresented) {
      NewTagView()
        .presentationDetents([.fraction(0.3)])
      // hard-coding this sheet to be 1/3 of the screen :P
    }.padding(.vertical, 10)
  }
}

struct EditPlaceView_Previews: PreviewProvider {
  static var previews: some View {
    EditPlaceView(
      name: "Cool Place",
      imageMetadata: nil,
      allTags: [],
      note: .constant("it's cool"),
      selectedTagIDs: .constant([]),
      saveAction: { print("save") })
  }
}
