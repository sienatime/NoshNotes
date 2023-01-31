// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

@main
struct NoshNotesApp: App {
  @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

  @StateObject private var tagStore = TagStore()
  @StateObject private var placeStore = PlaceStore()

  var body: some Scene {
    WindowGroup {
      PlacesListView(tags: tagStore.tags, places: placeStore.allPlaces)
        .environmentObject(placeStore)
        .task {
          await tagStore.reloadTags()
        }.task {
          await placeStore.observeChanges()
        }
    }
  }
}
