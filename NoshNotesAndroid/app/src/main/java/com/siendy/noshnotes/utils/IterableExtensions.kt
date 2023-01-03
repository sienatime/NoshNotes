package com.siendy.noshnotes.utils

fun <T> Iterable<T>.containsAny(values: Set<T>): Boolean {
  return this.any(values::contains)
}
