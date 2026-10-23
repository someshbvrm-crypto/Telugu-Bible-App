package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.Bookmark
import com.example.data.model.Flashcard
import com.example.data.model.Note
import com.example.data.model.ReadingPlan
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class TeluguBibleRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun saveNote_authenticatedUser_createsNoteSuccessfully(): Unit = runBlocking {
        val aliceUid = signInTestUser("alice_rule@test.com")
        val repository = FirestoreRepository(firestore, auth)

        val uniqueTitle = "యోహాను ధ్యానం ${UUID.randomUUID()}"
        val note = Note(
            userId = aliceUid,
            title = uniqueTitle,
            content = "దేవుడు లోకమును ఎంతో ప్రేమించెను.",
            bookName = "యోహాను",
            chapter = 3,
            verse = 16,
            tags = listOf("ప్రేమ", "నిత్యజీవము")
        )

        val noteId = withTimeout(5000L) {
            repository.saveNote(note)
        }
        assertTrue(noteId.isNotEmpty())

        val notes = withTimeout(5000L) {
            repository.observeNotes().first { list -> list.any { it.title == uniqueTitle } }
        }
        assertTrue(notes.any { it.title == uniqueTitle })
    }

    @Test
    fun addBookmark_authenticatedUser_persistsBookmark(): Unit = runBlocking {
        val aliceUid = signInTestUser("alice_bm@test.com")
        val repository = FirestoreRepository(firestore, auth)

        val uniqueBmId = "bm_${UUID.randomUUID().toString().replace("-", "_")}"
        val bookmark = Bookmark(
            id = uniqueBmId,
            userId = aliceUid,
            bookId = "PSA",
            bookName = "కీర్తనలు",
            chapter = 23,
            verse = 1,
            text = "యెహోవా నా కాపరి; నాకు లేమి కలుగదు.",
            color = "#FFD54F"
        )

        val bmId = withTimeout(5000L) {
            repository.addBookmark(bookmark)
        }
        assertTrue(bmId.isNotEmpty())
    }

    @Test
    fun saveReadingPlanProgress_authenticatedUser_updatesPlan(): Unit = runBlocking {
        val aliceUid = signInTestUser("alice_plan@test.com")
        val repository = FirestoreRepository(firestore, auth)

        val plan = ReadingPlan(
            userId = aliceUid,
            planName = "30-Day New Testament",
            currentDay = 2,
            totalDays = 30,
            completedDays = listOf(1),
            streakDays = 1,
            lastReadDate = "2026-10-23"
        )

        withTimeout(5000L) {
            repository.saveReadingPlanProgress(plan)
        }

        val plans = withTimeout(5000L) {
            repository.observeReadingPlans().first()
        }
        assertTrue(plans.isNotEmpty())
    }

    @Test
    fun createFlashcard_authenticatedUser_createsAndReadsFlashcard(): Unit = runBlocking {
        val aliceUid = signInTestUser("alice_fc@test.com")
        val repository = FirestoreRepository(firestore, auth)

        val flashcard = Flashcard(
            userId = aliceUid,
            frontPrompt = "కీర్తనలు 23:1 కంఠస్థ వాక్యము",
            backAnswer = "యెహోవా నా కాపరి; నాకు లేమి కలుగదు.",
            scriptureRef = "కీర్తనలు 23:1",
            reviewCount = 0,
            mastered = false
        )

        val fcId = withTimeout(5000L) {
            repository.createFlashcard(flashcard)
        }
        assertTrue(fcId.isNotEmpty())
    }

    @Test
    fun unauthenticated_observeNotes_throwsException(): Unit = runBlocking {
        auth.signOut()
        val repository = FirestoreRepository(firestore, auth)

        assertThrows(IllegalStateException::class.java) {
            runBlocking {
                repository.observeNotes().first()
            }
        }
    }
}
