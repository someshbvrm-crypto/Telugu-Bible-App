package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.error.OperationType
import com.example.data.error.handleFirestoreError
import com.example.data.model.Bookmark
import com.example.data.model.Flashcard
import com.example.data.model.Note
import com.example.data.model.ReadingPlan
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val db: FirebaseFirestore,
    private val auth: com.google.firebase.auth.FirebaseAuth = Firebase.auth
) {
    // Secondary constructor resolving named database ID from string resources
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        ),
        Firebase.auth
    )

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    // --- NOTES ---
    fun observeNotes(): Flow<List<Note>> {
        val uid = requireUserId()
        val path = "users/$uid/notes"
        return db.collection("users").document(uid).collection("notes")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(Note::class.java) }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                throw error
            }
    }

    suspend fun saveNote(note: Note): String {
        val uid = requireUserId()
        val collection = db.collection("users").document(uid).collection("notes")
        return try {
            if (note.id.isBlank()) {
                val docRef = collection.document()
                val payload = hashMapOf<String, Any>(
                    "userId" to uid,
                    "title" to note.title,
                    "content" to note.content,
                    "bookName" to note.bookName,
                    "chapter" to note.chapter,
                    "verse" to note.verse,
                    "tags" to note.tags,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
                docRef.id
            } else {
                val docRef = collection.document(note.id)
                val payload = hashMapOf<String, Any>(
                    "userId" to uid,
                    "title" to note.title,
                    "content" to note.content,
                    "bookName" to note.bookName,
                    "chapter" to note.chapter,
                    "verse" to note.verse,
                    "tags" to note.tags,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.update(payload).await()
                note.id
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, if (note.id.isBlank()) OperationType.CREATE else OperationType.UPDATE, collection.path)
            throw e
        }
    }

    suspend fun deleteNote(noteId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("notes").document(noteId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
    }

    // --- BOOKMARKS ---
    fun observeBookmarks(): Flow<List<Bookmark>> {
        val uid = requireUserId()
        val path = "users/$uid/bookmarks"
        return db.collection("users").document(uid).collection("bookmarks")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(Bookmark::class.java) }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                throw error
            }
    }

    suspend fun addBookmark(bookmark: Bookmark): String {
        val uid = requireUserId()
        val collection = db.collection("users").document(uid).collection("bookmarks")
        val docId = if (bookmark.id.isNotBlank()) bookmark.id else "${bookmark.bookId}_${bookmark.chapter}_${bookmark.verse}"
        val docRef = collection.document(docId)
        val payload = hashMapOf<String, Any>(
            "userId" to uid,
            "bookId" to bookmark.bookId,
            "bookName" to bookmark.bookName,
            "chapter" to bookmark.chapter,
            "verse" to bookmark.verse,
            "text" to bookmark.text,
            "color" to bookmark.color,
            "createdAt" to FieldValue.serverTimestamp()
        )
        return try {
            docRef.set(payload).await()
            docId
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun deleteBookmark(bookmarkId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("bookmarks").document(bookmarkId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
    }

    // --- READING PLANS ---
    fun observeReadingPlans(): Flow<List<ReadingPlan>> {
        val uid = requireUserId()
        val path = "users/$uid/readingPlans"
        return db.collection("users").document(uid).collection("readingPlans")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(ReadingPlan::class.java) }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                throw error
            }
    }

    suspend fun saveReadingPlanProgress(plan: ReadingPlan) {
        val uid = requireUserId()
        val docId = if (plan.id.isNotBlank()) plan.id else "default_plan"
        val docRef = db.collection("users").document(uid).collection("readingPlans").document(docId)
        val payload = hashMapOf<String, Any>(
            "userId" to uid,
            "planName" to plan.planName,
            "currentDay" to plan.currentDay,
            "totalDays" to plan.totalDays,
            "completedDays" to plan.completedDays,
            "streakDays" to plan.streakDays,
            "lastReadDate" to plan.lastReadDate,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    // --- FLASHCARDS ---
    fun observeFlashcards(): Flow<List<Flashcard>> {
        val uid = requireUserId()
        val path = "users/$uid/flashcards"
        return db.collection("users").document(uid).collection("flashcards")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(Flashcard::class.java) }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                throw error
            }
    }

    suspend fun createFlashcard(flashcard: Flashcard): String {
        val uid = requireUserId()
        val collection = db.collection("users").document(uid).collection("flashcards")
        val docRef = collection.document()
        val payload = hashMapOf<String, Any>(
            "userId" to uid,
            "frontPrompt" to flashcard.frontPrompt,
            "backAnswer" to flashcard.backAnswer,
            "scriptureRef" to flashcard.scriptureRef,
            "reviewCount" to flashcard.reviewCount,
            "mastered" to flashcard.mastered,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        return try {
            docRef.set(payload).await()
            docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun updateFlashcardReview(flashcardId: String, reviewCount: Int, mastered: Boolean) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("flashcards").document(flashcardId)
        val payload = hashMapOf<String, Any>(
            "reviewCount" to reviewCount,
            "mastered" to mastered,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.update(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    suspend fun deleteFlashcard(flashcardId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("flashcards").document(flashcardId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
    }
}
