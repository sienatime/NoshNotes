// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

@main
struct NoshNotesApp: App {
  @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

  private var tagStore: TagStore = DefaultTagStore()
  
  var body: some Scene {
    WindowGroup {
      PlacesListView(tags: []).task {
        let tags = await tagStore.getTags()
        print(tags)
      }
    }
  }
}
