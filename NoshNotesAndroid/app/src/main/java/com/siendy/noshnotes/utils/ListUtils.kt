package com.siendy.noshnotes.utils

import kotlin.math.ceil

fun <T> List<T>.partitionInto(numberOfPartitions: Int): List<List<T>> {
  val itemsPerRow: Int = ceil(this.size / numberOfPartitions.toDouble()).toInt()

  val rows: MutableList<List<T>> = mutableListOf()

  (0..numberOfPartitions).forEach { row ->
    val start = itemsPerRow * row
    if (start >= this.size) {
      return@forEach
    }
    var end = itemsPerRow * (row + 1)
    if (end > this.size) {
      end = this.size
    }
    rows.add(this.subList(start, end))
  }
  return rows
}
