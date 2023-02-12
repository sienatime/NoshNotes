// Created by andy_bartholomew on 2/10/23.
// Copyright © 2023 Airbnb Inc. All rights reserved.

import Foundation

enum LoadState<Content> {
  case loading
  case loaded(Content)
  case failed(Error)

  func map<NewContent>(_ transform: (Content) -> NewContent) -> LoadState<NewContent>
  {
    switch self {
    case .loading:
      return .loading
    case .loaded(let content):
      return .loaded(transform(content))
    case .failed(let error):
      return .failed(error)
    }
  }
}
