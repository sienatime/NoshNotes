// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import SwiftUI

@main
struct NoshNotesApp: App {
  @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

  private var placeStore: PlaceStore = DefaultPlaceStore()
  
  var body: some Scene {
    WindowGroup {
      ContentView().task {
        await placeStore.getTags()
      }
    }
  }
}
