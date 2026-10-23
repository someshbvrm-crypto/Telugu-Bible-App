package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class Testament {
    OLD, NEW
}

data class BibleVerse(
    val bookId: String,
    val bookNameTelugu: String,
    val bookNameEnglish: String,
    val chapter: Int,
    val verse: Int,
    val textTelugu: String,
    val textEnglish: String = ""
) {
    val referenceText: String
        get() = "$bookNameTelugu $chapter:$verse"
}

data class BibleBook(
    val id: String,
    val nameTelugu: String,
    val nameEnglish: String,
    val abbreviation: String,
    val testament: Testament,
    val totalChapters: Int,
    val category: String,
    val firstTwoLetters: String = abbreviation.take(2)
) {
    fun getVerseCountForChapter(chapter: Int): Int {
        return when (id) {
            "PSA" -> when (chapter) {
                23 -> 6
                91 -> 16
                119 -> 176
                121 -> 8
                else -> 20
            }
            "JHN" -> when (chapter) {
                1 -> 51
                3 -> 36
                14 -> 31
                else -> 30
            }
            "MAT" -> when (chapter) {
                5 -> 48
                6 -> 34
                7 -> 29
                else -> 30
            }
            "ROM" -> when (chapter) {
                8 -> 39
                12 -> 21
                else -> 25
            }
            "1CO" -> if (chapter == 13) 13 else 28
            "PHP" -> if (chapter == 4) 23 else 20
            "GEN" -> if (chapter == 1) 31 else 28
            "REV" -> if (chapter == 21) 27 else 25
            else -> 25
        }
    }
}

data class DailyVerse(
    val title: String,
    val reference: String,
    val textTelugu: String,
    val textEnglish: String,
    val devotionalThought: String
)

@IgnoreExtraProperties
data class Note(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val bookName: String = "",
    val chapter: Int = 1,
    val verse: Int = 1,
    val tags: List<String> = emptyList(),
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Bookmark(
    @DocumentId val id: String = "",
    val userId: String = "",
    val bookId: String = "",
    val bookName: String = "",
    val chapter: Int = 1,
    val verse: Int = 1,
    val text: String = "",
    val color: String = "#FFD54F", // Amber/Gold default
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ReadingPlan(
    @DocumentId val id: String = "",
    val userId: String = "",
    val planName: String = "",
    val currentDay: Int = 1,
    val totalDays: Int = 30,
    val completedDays: List<Int> = emptyList(),
    val streakDays: Int = 0,
    val lastReadDate: String = "",
    @ServerTimestamp val updatedAt: Timestamp? = null
)

data class ReadingDayItem(
    val dayNumber: Int,
    val titleTelugu: String,
    val portions: List<String>,
    val memoryVerse: String
)

@IgnoreExtraProperties
data class Flashcard(
    @DocumentId val id: String = "",
    val userId: String = "",
    val frontPrompt: String = "",
    val backAnswer: String = "",
    val scriptureRef: String = "",
    val reviewCount: Int = 0,
    val mastered: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
