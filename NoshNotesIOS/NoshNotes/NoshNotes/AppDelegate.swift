// Created by andy_bartholomew on 1/16/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import FirebaseCore
import Foundation
import GooglePlaces
import SwiftUI

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool
  {
    FirebaseApp.configure()
    GMSPlacesClient.provideAPIKey("AIzaSyBOpFP6JvDulE8Qf4B17yODOnWifA4bj2Q")

    return true
  }
}
