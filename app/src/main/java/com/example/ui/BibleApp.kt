package com.example.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import com.example.data.model.BibleBook
import com.example.data.model.BibleVerse
import com.example.data.model.Bookmark
import com.example.data.model.Flashcard
import com.example.data.model.Note
import com.example.data.model.ReadingPlan
import com.example.data.repository.BibleRepository
import com.example.data.repository.FirestoreRepository
import com.example.ui.auth.GoogleSignInHelper
import com.example.ui.bookmarks.BookmarksScreen
import com.example.ui.flashcards.FlashcardsScreen
import com.example.ui.notes.NotesScreen
import com.example.ui.notifications.NotificationHelper
import com.example.ui.plans.ReadingPlansScreen
import com.example.ui.reader.BibleReaderScreen
import com.example.ui.search.BibleSearchScreen
import com.example.ui.theme.TeluguBibleTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

enum class BibleTab(val label: String) {
    READER("చదవండి"),
    PLANS("ప్రణాళిక"),
    NOTES("నోట్స్"),
    BOOKMARKS("బుక్‌మార్క్స్"),
    FLASHCARDS("జ్ఞాపికలు"),
    SEARCH("శోధన")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleApp() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    val systemDark = isSystemInDarkTheme()
    var isDarkMode by remember { mutableStateOf(systemDark) }

    // Firebase Auth session
    var currentUser by remember { mutableStateOf<FirebaseUser?>(Firebase.auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    // Auto sign-in on app launch
    LaunchedEffect(Unit) {
        NotificationHelper.createNotificationChannel(context)
        GoogleSignInHelper.attemptAutoSignIn(
            context = context,
            credentialManager = credentialManager,
            onAuthSuccess = { currentUser = Firebase.auth.currentUser },
            onUnauthenticated = { /* user continues with local storage */ },
            scope = coroutineScope
        )
    }

    // Android 13+ Notification Permission Launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val daily = BibleRepository.getTodayVerse()
            NotificationHelper.showDailyVerseNotification(context, daily)
            Toast.makeText(context, "నోటిఫికేషన్ ప్రారంభించబడింది!", Toast.LENGTH_SHORT).show()
        }
    }

    // Selected Navigation Tab
    var currentTab by remember { mutableStateOf(BibleTab.READER) }

    // Reader state
    var currentBook by remember { mutableStateOf(BibleRepository.books.first { it.id == "JHN" }) } // Start in John (యోహాను)
    var currentChapter by remember { mutableIntStateOf(3) }
    var targetVerse by remember { mutableStateOf<Int?>(null) }

    // Repositories
    val firestoreRepository = remember { FirestoreRepository(context) }

    // Local in-memory caches (for offline resilience and instant reading)
    var localNotes by remember {
        mutableStateOf(
            listOf(
                Note(
                    id = "seed_1",
                    title = "దేవుని నిరుపమానమైన ప్రేమ",
                    content = "యోహాను 3:16 లో దేవుడు సమస్త మానవాళి కొరకు తన అద్వితీయకుమారుని త్యాగము చేసెను. ఇది నిత్యజీవపు వాగ్దానము.",
                    bookName = "యోహాను",
                    chapter = 3,
                    verse = 16,
                    tags = listOf("ప్రేమ", "నిత్యజీవము", "రక్షణ")
                ),
                Note(
                    id = "seed_2",
                    title = "మంచి కాపరి యొక్క భద్రత",
                    content = "యెహోవా నా కాపరి; ఏ అపాయమునకు భయపడను. ఆయన కృపాక్షేమములు నా వెంట వచ్చును.",
                    bookName = "కీర్తనలు",
                    chapter = 23,
                    verse = 1,
                    tags = listOf("విశ్వాసము", "శాంతి")
                )
            )
        )
    }

    var localBookmarks by remember {
        mutableStateOf(
            listOf(
                Bookmark(
                    id = "seed_bm_1",
                    bookId = "JHN",
                    bookName = "యోహాను",
                    chapter = 3,
                    verse = 16,
                    text = "దేవుడు లోకమును ఎంతో ప్రేమించెను. కాగా ఆయన తన అద్వితీయకుమారునిగా పుట్టిన వానియందు విశ్వాసముంచు ప్రతివాడును నశింపక నిత్యజీవము పొందునట్లు ఆయనను అనుగ్రహించెను.",
                    color = "#FFD54F"
                ),
                Bookmark(
                    id = "seed_bm_2",
                    bookId = "PSA",
                    bookName = "కీర్తనలు",
                    chapter = 23,
                    verse = 1,
                    text = "యెహోవా నా కాపరి; నాకు లేమి కలుగదు.",
                    color = "#A5D6A7"
                ),
                Bookmark(
                    id = "seed_bm_3",
                    bookId = "PHP",
                    bookName = "ఫిలిప్పీయులకు",
                    chapter = 4,
                    verse = 13,
                    text = "నన్ను బలపరచువానియందే నేను సమస్తమును చేయగలను.",
                    color = "#90CAF9"
                )
            )
        )
    }

    var localReadingPlan by remember {
        mutableStateOf(
            ReadingPlan(
                id = "default_plan",
                planName = "30-Day New Testament Journey",
                currentDay = 7,
                totalDays = 30,
                completedDays = listOf(1, 2, 3, 4, 5, 6),
                streakDays = 6,
                lastReadDate = "2026-10-23"
            )
        )
    }

    var localFlashcards by remember {
        mutableStateOf(
            listOf(
                Flashcard(
                    id = "seed_fc_1",
                    frontPrompt = "దేవుని నిత్యజీవపు వాగ్దానం ఏ వాక్యములో కలదు?",
                    backAnswer = "దేవుడు లోకమును ఎంతో ప్రేమించెను... నశింపక నిత్యజీవము పొందునట్లు ఆయనను అనుగ్రహించెను. (యోహాను 3:16)",
                    scriptureRef = "యోహాను 3:16",
                    reviewCount = 3,
                    mastered = true
                ),
                Flashcard(
                    id = "seed_fc_2",
                    frontPrompt = "యెహోవా నా కాపరి... తరువాతి వాక్యమును చెప్పుము",
                    backAnswer = "నాకు లేమి కలుగదు. పచ్చికగల చోట్లను ఆయన నన్ను పరుండజేయుచున్నాడు. (కీర్తనలు 23:1-2)",
                    scriptureRef = "కీర్తనలు 23:1",
                    reviewCount = 2,
                    mastered = true
                ),
                Flashcard(
                    id = "seed_fc_3",
                    frontPrompt = "మనలను బలపరచువానియందు మనము ఏమి చేయగలము?",
                    backAnswer = "నన్ను బలపరచువానియందే నేను సమస్తమును చేయగలను. (ఫిలిప్పీయులకు 4:13)",
                    scriptureRef = "ఫిలిప్పీయులకు 4:13",
                    reviewCount = 1,
                    mastered = false
                ),
                Flashcard(
                    id = "seed_fc_4",
                    frontPrompt = "ఆత్మ విషయమై దీనులైనవారు ధన్యులు ఎందుకు?",
                    backAnswer = "పరలోకరాజ్యము వారిది. (మత్తయి 5:3)",
                    scriptureRef = "మత్తయి 5:3",
                    reviewCount = 1,
                    mastered = false
                )
            )
        )
    }

    // Real-time Firestore sync observers when user is authenticated
    val user = currentUser
    if (user != null) {
        val firestoreNotes by firestoreRepository.observeNotes()
            .catch { emit(emptyList()) }
            .collectAsState(initial = emptyList())

        val firestoreBookmarks by firestoreRepository.observeBookmarks()
            .catch { emit(emptyList()) }
            .collectAsState(initial = emptyList())

        val firestorePlans by firestoreRepository.observeReadingPlans()
            .catch { emit(emptyList()) }
            .collectAsState(initial = emptyList())

        val firestoreFlashcards by firestoreRepository.observeFlashcards()
            .catch { emit(emptyList()) }
            .collectAsState(initial = emptyList())

        // Merge when cloud data arrives
        if (firestoreNotes.isNotEmpty()) localNotes = firestoreNotes
        if (firestoreBookmarks.isNotEmpty()) localBookmarks = firestoreBookmarks
        if (firestorePlans.isNotEmpty()) localReadingPlan = firestorePlans.first()
        if (firestoreFlashcards.isNotEmpty()) localFlashcards = firestoreFlashcards
    }

    // Dialog Modals
    var showAuthDialog by remember { mutableStateOf(false) }
    var showDailyVerseDialog by remember { mutableStateOf(false) }
    var isSigningIn by remember { mutableStateOf(false) }

    TeluguBibleTheme(darkTheme = isDarkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "పరిశుద్ధ గ్రంథము",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                // Sync indicator badge
                                Icon(
                                    imageVector = if (currentUser != null) Icons.Default.CloudDone else Icons.Default.CloudOff,
                                    contentDescription = if (currentUser != null) "Cloud Synced" else "Offline / Local",
                                    tint = if (currentUser != null) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { showAuthDialog = true }
                                )
                            }
                            Text(
                                text = "Telugu Bible • Daily Study & Sync",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        // Search quick button
                        IconButton(
                            onClick = { currentTab = BibleTab.SEARCH },
                            modifier = Modifier.testTag("appbar_search_button")
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "శోధన")
                        }

                        // Daily Verse quick dialog
                        IconButton(
                            onClick = { showDailyVerseDialog = true },
                            modifier = Modifier.testTag("appbar_daily_verse_button")
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = "నేటి వాక్యము",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Daily notification reminder test button
                        IconButton(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val permissionCheck = ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                        val daily = BibleRepository.getTodayVerse()
                                        NotificationHelper.showDailyVerseNotification(context, daily)
                                        Toast.makeText(context, "🔔 నేటి వాక్యము నోటిఫికేషన్ పంపబడింది!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                } else {
                                    val daily = BibleRepository.getTodayVerse()
                                    NotificationHelper.showDailyVerseNotification(context, daily)
                                    Toast.makeText(context, "🔔 నేటి వాక్యము నోటిఫికేషన్ పంపబడింది!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.testTag("appbar_notification_test_button")
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "రిమైండర్ టెస్ట్")
                        }

                        // Dark mode toggle
                        IconButton(
                            onClick = { isDarkMode = !isDarkMode },
                            modifier = Modifier.testTag("appbar_dark_mode_toggle")
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Dark mode toggle"
                            )
                        }

                        // Google Sign-In / Account Sync
                        IconButton(
                            onClick = { showAuthDialog = true },
                            modifier = Modifier.testTag("appbar_account_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "ఖాతా & సింక్",
                                tint = if (currentUser != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.testTag("main_navigation_bar")
                ) {
                    NavigationBarItem(
                        selected = currentTab == BibleTab.READER,
                        onClick = { currentTab = BibleTab.READER },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                        label = { Text(BibleTab.READER.label) },
                        modifier = Modifier.testTag("nav_reader")
                    )

                    NavigationBarItem(
                        selected = currentTab == BibleTab.PLANS,
                        onClick = { currentTab = BibleTab.PLANS },
                        icon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                        label = { Text(BibleTab.PLANS.label) },
                        modifier = Modifier.testTag("nav_plans")
                    )

                    NavigationBarItem(
                        selected = currentTab == BibleTab.NOTES,
                        onClick = { currentTab = BibleTab.NOTES },
                        icon = { Icon(Icons.Default.NoteAlt, contentDescription = null) },
                        label = { Text(BibleTab.NOTES.label) },
                        modifier = Modifier.testTag("nav_notes")
                    )

                    NavigationBarItem(
                        selected = currentTab == BibleTab.BOOKMARKS,
                        onClick = { currentTab = BibleTab.BOOKMARKS },
                        icon = { Icon(Icons.Default.Bookmark, contentDescription = null) },
                        label = { Text(BibleTab.BOOKMARKS.label) },
                        modifier = Modifier.testTag("nav_bookmarks")
                    )

                    NavigationBarItem(
                        selected = currentTab == BibleTab.FLASHCARDS,
                        onClick = { currentTab = BibleTab.FLASHCARDS },
                        icon = { Icon(Icons.Default.Style, contentDescription = null) },
                        label = { Text(BibleTab.FLASHCARDS.label) },
                        modifier = Modifier.testTag("nav_flashcards")
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentTab) {
                    BibleTab.READER -> {
                        BibleReaderScreen(
                            currentBook = currentBook,
                            currentChapter = currentChapter,
                            targetVerse = targetVerse,
                            onBookChange = { currentBook = it },
                            onChapterChange = { currentChapter = it },
                            onTargetVerseConsumed = { targetVerse = null },
                            bookmarks = localBookmarks,
                            onToggleBookmark = { verse, colorHex ->
                                if (colorHex.isBlank()) {
                                    // Remove bookmark
                                    val existing = localBookmarks.find {
                                        it.bookId == verse.bookId && it.chapter == verse.chapter && it.verse == verse.verse
                                    }
                                    if (existing != null) {
                                        localBookmarks = localBookmarks.filter { it.id != existing.id }
                                        if (currentUser != null) {
                                            coroutineScope.launch {
                                                try {
                                                    firestoreRepository.deleteBookmark(existing.id)
                                                } catch (_: Exception) {}
                                            }
                                        }
                                    }
                                } else {
                                    // Add or update bookmark
                                    val newBm = Bookmark(
                                        id = "${verse.bookId}_${verse.chapter}_${verse.verse}",
                                        userId = currentUser?.uid ?: "local_user",
                                        bookId = verse.bookId,
                                        bookName = verse.bookNameTelugu,
                                        chapter = verse.chapter,
                                        verse = verse.verse,
                                        text = verse.textTelugu,
                                        color = colorHex
                                    )
                                    localBookmarks = localBookmarks.filter { it.id != newBm.id } + newBm
                                    if (currentUser != null) {
                                        coroutineScope.launch {
                                            try {
                                                firestoreRepository.addBookmark(newBm)
                                            } catch (_: Exception) {}
                                        }
                                    }
                                }
                            },
                            onCreateNoteFromVerse = { verse ->
                                val newNote = Note(
                                    title = "${verse.bookNameTelugu} ${verse.chapter}:${verse.verse} ధ్యానము",
                                    content = "\"${verse.textTelugu}\"",
                                    bookName = verse.bookNameTelugu,
                                    chapter = verse.chapter,
                                    verse = verse.verse,
                                    tags = listOf("వాక్య_ధ్యానము")
                                )
                                localNotes = listOf(newNote) + localNotes
                                currentTab = BibleTab.NOTES
                                if (currentUser != null) {
                                    coroutineScope.launch {
                                        try {
                                            firestoreRepository.saveNote(newNote)
                                        } catch (_: Exception) {}
                                    }
                                }
                                Toast.makeText(context, "నోట్ వ్రాయడానికి తెరవబడింది", Toast.LENGTH_SHORT).show()
                            },
                            onCreateFlashcardFromVerse = { verse ->
                                val newFc = Flashcard(
                                    frontPrompt = "${verse.bookNameTelugu} ${verse.chapter}:${verse.verse} కంఠస్థ వాక్యము",
                                    backAnswer = verse.textTelugu,
                                    scriptureRef = "${verse.bookNameTelugu} ${verse.chapter}:${verse.verse}",
                                    reviewCount = 0,
                                    mastered = false
                                )
                                localFlashcards = listOf(newFc) + localFlashcards
                                currentTab = BibleTab.FLASHCARDS
                                if (currentUser != null) {
                                    coroutineScope.launch {
                                        try {
                                            firestoreRepository.createFlashcard(newFc)
                                        } catch (_: Exception) {}
                                    }
                                }
                                Toast.makeText(context, "జ్ఞాపిక కార్డు తయారుచేయబడింది!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    BibleTab.PLANS -> {
                        ReadingPlansScreen(
                            currentPlan = localReadingPlan,
                            onToggleDayCompleted = { day ->
                                val completed = localReadingPlan.completedDays.toMutableList()
                                val newCompleted = if (completed.contains(day)) {
                                    completed.remove(day)
                                    completed
                                } else {
                                    completed.add(day)
                                    completed
                                }
                                val updatedPlan = localReadingPlan.copy(
                                    completedDays = newCompleted,
                                    streakDays = (newCompleted.size).coerceAtLeast(1)
                                )
                                localReadingPlan = updatedPlan
                                if (currentUser != null) {
                                    coroutineScope.launch {
                                        try {
                                            firestoreRepository.saveReadingPlanProgress(updatedPlan)
                                        } catch (_: Exception) {}
                                    }
                                }
                            },
                            onNavigateToScripture = { bookId, chapter ->
                                val foundBook = BibleRepository.books.find { it.id == bookId } ?: currentBook
                                currentBook = foundBook
                                currentChapter = chapter
                                currentTab = BibleTab.READER
                            }
                        )
                    }

                    BibleTab.NOTES -> {
                        NotesScreen(
                            notes = localNotes,
                            onSaveNote = { note ->
                                localNotes = if (note.id.isBlank()) {
                                    listOf(note.copy(id = "local_${System.currentTimeMillis()}")) + localNotes
                                } else {
                                    localNotes.map { if (it.id == note.id) note else it }
                                }
                                if (currentUser != null) {
                                    coroutineScope.launch {
                                        try {
                                            firestoreRepository.saveNote(note)
                                        } catch (_: Exception) {}
                                    }
                                }
                                Toast.makeText(context, "నోట్ భద్రపరచబడింది", Toast.LENGTH_SHORT).show()
                            },
                            onDeleteNote = { noteId ->
                                localNotes = localNotes.filter { it.id != noteId }
                                if (currentUser != null) {
                                    coroutineScope.launch {
                                        try {
                                            firestoreRepository.deleteNote(noteId)
                                        } catch (_: Exception) {}
                                    }
                                }
                                Toast.makeText(context, "నోట్ తొలగించబడింది", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    BibleTab.BOOKMARKS -> {
                        BookmarksScreen(
                            bookmarks = localBookmarks,
                            onNavigateToVerse = { bookId, chapter, verseNum ->
                                val foundBook = BibleRepository.books.find { it.id == bookId } ?: currentBook
                                currentBook = foundBook
                                currentChapter = chapter
                                targetVerse = verseNum
                                currentTab = BibleTab.READER
                            },
                            onDeleteBookmark = { bmId ->
                                localBookmarks = localBookmarks.filter { it.id != bmId }
                                if (currentUser != null) {
                                    coroutineScope.launch {
                                        try {
                                            firestoreRepository.deleteBookmark(bmId)
                                        } catch (_: Exception) {}
                                    }
                                }
                                Toast.makeText(context, "బుక్‌మార్క్ తీసివేయబడింది", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    BibleTab.FLASHCARDS -> {
                        FlashcardsScreen(
                            flashcards = localFlashcards,
                            onCreateFlashcard = { fc ->
                                val created = fc.copy(id = "local_fc_${System.currentTimeMillis()}")
                                localFlashcards = listOf(created) + localFlashcards
                                if (currentUser != null) {
                                    coroutineScope.launch {
                                        try {
                                            firestoreRepository.createFlashcard(fc)
                                        } catch (_: Exception) {}
                                    }
                                }
                                Toast.makeText(context, "జ్ఞాపిక కార్డ్ చేర్చబడింది", Toast.LENGTH_SHORT).show()
                            },
                            onUpdateReview = { fcId, count, mastered ->
                                localFlashcards = localFlashcards.map {
                                    if (it.id == fcId) it.copy(reviewCount = count, mastered = mastered) else it
                                }
                                if (currentUser != null) {
                                    coroutineScope.launch {
                                        try {
                                            firestoreRepository.updateFlashcardReview(fcId, count, mastered)
                                        } catch (_: Exception) {}
                                    }
                                }
                            },
                            onDeleteFlashcard = { fcId ->
                                localFlashcards = localFlashcards.filter { it.id != fcId }
                                if (currentUser != null) {
                                    coroutineScope.launch {
                                        try {
                                            firestoreRepository.deleteFlashcard(fcId)
                                        } catch (_: Exception) {}
                                    }
                                }
                                Toast.makeText(context, "జ్ఞాపిక తొలగించబడింది", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    BibleTab.SEARCH -> {
                        BibleSearchScreen(
                            onNavigateToScripture = { bookId, chapter, verseNum ->
                                val foundBook = BibleRepository.books.find { it.id == bookId } ?: currentBook
                                currentBook = foundBook
                                currentChapter = chapter
                                targetVerse = verseNum
                                currentTab = BibleTab.READER
                            }
                        )
                    }
                }
            }
        }

        // Daily Verse Dialog ("నేటి వాక్యము")
        if (showDailyVerseDialog) {
            val today = BibleRepository.getTodayVerse()
            AlertDialog(
                onDismissRequest = { showDailyVerseDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("🕊️ నేటి దేవుని వాక్యము")
                    }
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = today.reference,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "\"${today.textTelugu}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 26.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = today.devotionalThought,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val parts = today.reference.split(" ")
                            val bookName = parts.firstOrNull() ?: "యోహాను"
                            val chVerse = parts.getOrNull(1) ?: "3:16"
                            val ch = chVerse.split(":").firstOrNull()?.toIntOrNull() ?: 1
                            val vNum = chVerse.split(":").getOrNull(1)?.toIntOrNull()
                            val foundBook = BibleRepository.books.find { it.nameTelugu == bookName }
                                ?: BibleRepository.books.find { it.id == "JHN" }!!
                            currentBook = foundBook
                            currentChapter = ch
                            targetVerse = vNum
                            currentTab = BibleTab.READER
                            showDailyVerseDialog = false
                        }
                    ) {
                        Text("అధ్యాయం చదవండి")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDailyVerseDialog = false }) {
                        Text("మూసివేయి")
                    }
                }
            )
        }

        // Account & Cloud Sync Dialog
        if (showAuthDialog) {
            AlertDialog(
                onDismissRequest = { showAuthDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("క్లౌడ్ సింక్ & ఖాతా (Cloud Sync)")
                    }
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (currentUser != null) {
                            Text(
                                text = "✅ కనెక్ట్ చేయబడింది:",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentUser?.email ?: "Google Account",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "మీ నోట్స్, బుక్‌మార్క్‌లు, రీడింగ్ ప్లాన్ మరియు జ్ఞాపికలు క్లౌడ్‌లో సురక్షితంగా సింక్ చేయబడుతున్నాయి.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = "మీ అన్ని పరికరాలలో నోట్స్, బుక్‌మార్క్స్ మరియు పఠన ప్రగతిని నిరంతరాయంగా సింక్ చేసుకోవడానికి Google తో సైన్ ఇన్ చేయండి:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• ఆఫ్‌లైన్‌లో కూడా పూర్తి బైబిల్ చదువుకోవచ్చు.\n• ఆన్‌లైన్ వచ్చినప్పుడు ఆటోమేటిక్‌గా డేటా సింక్ అవుతుంది.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                confirmButton = {
                    if (currentUser == null) {
                        Button(
                            onClick = {
                                isSigningIn = true
                                GoogleSignInHelper.onGoogleSignInClicked(
                                    context = context,
                                    credentialManager = credentialManager,
                                    onAuthSuccess = {
                                        isSigningIn = false
                                        currentUser = Firebase.auth.currentUser
                                        showAuthDialog = false
                                        Toast.makeText(context, "Google తో విజయవంతంగా సైన్ ఇన్ అయ్యారు!", Toast.LENGTH_SHORT).show()
                                    },
                                    onAuthError = { error ->
                                        isSigningIn = false
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    },
                                    scope = coroutineScope,
                                    onAuthCancelled = {
                                        isSigningIn = false
                                    }
                                )
                            },
                            enabled = !isSigningIn,
                            modifier = Modifier.testTag("google_signin_button")
                        ) {
                            if (isSigningIn) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text("Sign in with Google")
                        }
                    } else {
                        Button(
                            onClick = {
                                GoogleSignInHelper.signOut(
                                    credentialManager = credentialManager,
                                    onSignOutComplete = {
                                        currentUser = null
                                        showAuthDialog = false
                                        Toast.makeText(context, "సైన్ అవుట్ అయ్యారు", Toast.LENGTH_SHORT).show()
                                    },
                                    scope = coroutineScope
                                )
                            }
                        ) {
                            Text("సైన్ అవుట్ (Sign Out)")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAuthDialog = false }) {
                        Text("మూసివేయి")
                    }
                }
            )
        }
    }
}
