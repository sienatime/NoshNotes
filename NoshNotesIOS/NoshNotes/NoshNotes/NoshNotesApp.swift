// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

@main
struct NoshNotesApp: App {
  @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

  @StateObject private var tagStore = TagStore()
  
  var body: some Scene {
    WindowGroup {
      PlacesListView(tags: tagStore.tags).task {
        await tagStore.reloadTags()
      }
    }
  }
}
