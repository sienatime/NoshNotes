package com.siendy.noshnotes

import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.ui.components.TagState
import com.siendy.noshnotes.utils.partitionInto
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ListUtilsTest {
  @Test
  fun `partitionInto, when list is 0, it is empty`() {
    val tags = emptyList<TagState>()
    assertThat(
      tags.partitionInto(3),
      `is`(
        emptyList()
      )
    )
  }

  @Test
  fun `partitionInto, when list is 1, it is 1 list of 1 item`() {
    val tags = generateTagStates(1)
    println(tags)
    assertThat(
      tags.partitionInto(3),
      `is`(
        listOf(tags)
      )
    )
  }

  @Test
  fun `partitionInto, when list is 5, it is 3 lists of 2, 2, 1 items`() {
    val tags = generateTagStates(5)
    println(tags)
    assertThat(
      tags.partitionInto(3),
      `is`(
        listOf(
          listOf(TagState(Tag(name = "0")), TagState(Tag(name = "1"))),
          listOf(TagState(Tag(name = "2")), TagState(Tag(name = "3"))),
          listOf(TagState(Tag(name = "4")))
        )
      )
    )
  }

  @Test
  fun `partitionInto, when list is 8, it is 3 lists of 3, 3, 2 items`() {
    val tags = generateTagStates(8)
    println(tags)
    assertThat(
      tags.partitionInto(3),
      `is`(
        listOf(
          listOf(TagState(Tag(name = "0")), TagState(Tag(name = "1")), TagState(Tag(name = "2"))),
          listOf(TagState(Tag(name = "3")), TagState(Tag(name = "4")), TagState(Tag(name = "5"))),
          listOf(TagState(Tag(name = "6")), TagState(Tag(name = "7")))
        )
      )
    )
  }

  @Test
  fun `partitionInto, when list is 15, it is 3 lists of 5 items`() {
    val tags = generateTagStates(15)
    println(tags)
    assertThat(
      tags.partitionInto(3),
      `is`(
        listOf(
          listOf(TagState(Tag(name = "0")), TagState(Tag(name = "1")), TagState(Tag(name = "2")), TagState(Tag(name = "3")), TagState(Tag(name = "4"))),
          listOf(TagState(Tag(name = "5")), TagState(Tag(name = "6")), TagState(Tag(name = "7")), TagState(Tag(name = "8")), TagState(Tag(name = "9"))),
          listOf(TagState(Tag(name = "10")), TagState(Tag(name = "11")), TagState(Tag(name = "12")), TagState(Tag(name = "13")), TagState(Tag(name = "14")))
        )
      )
    )
  }

  private fun generateTagStates(numberOfTags: Int): List<TagState> {
    return (0 until numberOfTags).map {
      TagState(
        Tag(
          name = it.toString()
        )
      )
    }
  }
}
