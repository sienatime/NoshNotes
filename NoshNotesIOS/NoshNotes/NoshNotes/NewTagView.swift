// Created by andy_bartholomew on 2/6/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

struct NewTagView: View {

  init() {
    _tagName = .init(initialValue: "")
  }

  @State private var tagName: String

  @Environment(\.dismiss) var dismiss: DismissAction
  @EnvironmentObject var tagStore: TagStore

  var body: some View {
    VStack(alignment: .leading, spacing: 16) {
      Text("New Tag Name")
      TextField("New Tag Name", text: $tagName, prompt: Text("Tag Name"))
        .textFieldStyle(.roundedBorder)
      Text("Icon Picker Placeholder")
      Text("Color Picker Placeholder")
      Spacer()
      HStack(spacing: 32) {
        Spacer()
        Button("Cancel", action: cancel)
        Button("Save", action: save).buttonStyle(.borderedProminent)
      }
    }.padding(.horizontal)
      .padding(.top, 32)
      .navigationTitle("New Tag")
  }

  func save() {
    Task {
      do {
        try await tagStore.createTag(name: tagName)
        dismiss()
      } catch {
        print(error)
      }
    }
  }

  func cancel() {
    dismiss()
  }
}

struct NewTagView_Previews: PreviewProvider {
  static var previews: some View {
    NewTagView()
  }
}
