package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Telugu Bible", appName)
  }

  @Test
  fun `verify bible repository structure and first two letters`() {
    val books = com.example.data.repository.BibleRepository.books
    assertEquals(66, books.size)
    val genesis = books.first { it.id == "GEN" }
    assertEquals("ఆది", genesis.firstTwoLetters)
    val john = books.first { it.id == "JHN" }
    assertEquals("యోహా", john.firstTwoLetters)
    assertEquals(21, john.totalChapters)
    assertEquals(36, john.getVerseCountForChapter(3))
  }
}
