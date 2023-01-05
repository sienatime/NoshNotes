package com.siendy.noshnotes.ui.components

import com.siendy.noshnotes.data.models.Tag

data class TagState(
  val tag: Tag,
  val selected: Boolean = false,
  val clickable: Boolean = false
)
