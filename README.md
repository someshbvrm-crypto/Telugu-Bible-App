# Telugu Bible (పరిశుద్ధ గ్రంథము)

An elegant, modern Android application for reading, studying, and meditating on the Holy Bible in Telugu (పరిశుద్ధ గ్రంథము). Built using **Kotlin**, **Jetpack Compose**, **Material Design 3**, and **Firebase Firestore** with offline-first capabilities.

---

## 📖 Highlights & Features

### 1. Telugu Scripture Reader
* **Authentic Telugu Typography:** Optimized line heights and custom font sizing for comfortable reading of Telugu script (తెలుగు లిపి).
* **Verse Actions:** Tap any verse to open an action sheet with options to **Bookmark**, **Add Study Note**, **Copy to Clipboard**, or **Share**.
* **Smooth Verse Navigation:** Smooth auto-scrolls directly to the selected target verse with a transient highlight effect.

### 2. Left Navigation Pane (OT / NT Subsections & 2-Column Selector)
* **Testament Tabs:** Toggle instantly between the **Old Testament (పాత నిబంధన - 39 Books)** and **New Testament (క్రొత్త నిబంధన - 27 Books)**.
* **Canonical Subsections:**
  * **Old Testament:** ధర్మశాస్త్ర గ్రంథాలు (Law), చారిత్రక గ్రంథాలు (History), కావ్య గ్రంథాలు / జ్ఞానవాక్యాలు (Poetry & Wisdom), ప్రధాన & చిన్న ప్రవక్తలు (Major & Minor Prophets).
  * **New Testament:** సువార్తలు (Gospels), సంఘ చరిత్ర (Acts), పౌలు పత్రికలు (Pauline Epistles), సాధారణ పత్రికలు (General Epistles), ప్రవచన గ్రంథము (Revelation).
* **Two-Letter Telugu Badges:** Each book is presented with a 2-letter Telugu badge abbreviation for rapid visual scanning (e.g., `ఆది` for ఆదికాండము, `కీర్త` for కీర్తనలు, `యోహా` for యోహాను సువార్త, `మత్త` for మత్తయి).
* **Two-Column Chapter & Verse Picker:**
  * Tapping a book opens a two-column selector.
  * **Left Column:** List of Chapters (అధ్యాయాలు).
  * **Right Column:** List of Verses (వచనాలు) for the active chapter.
  * Tapping any verse instantly closes the drawer and navigates to the exact scripture location in the reader.

### 3. Study Notes & Highlights
* Create and edit personal notes linked to specific books, chapters, and verses.
* Filter and search through your devotional notes by date or scripture reference.
* Full offline storage with automatic Firebase cloud backup when signed in.

### 4. Bookmarks Collection
* Save cherished verses for quick meditation and reference.
* Direct jump-to-reader capability from any saved bookmark card.

### 5. Devotional Reading Plans (30-Day Challenges)
* Structured daily plans including:
  * **Gospels in 30 Days (సువార్తలు 30 రోజులలో)**
  * **Psalms of Peace & Comfort (కీర్తనల ధ్యానం)**
  * **Proverbs for Wisdom (సామెతల జ్ఞానం)**
  * **Epistles of Faith (విశ్వాస యాత్ర)**
* Interactive day-by-day checklist tracking with real-time percentage progress.

### 6. Scripture Memory Flashcards
* Interactive flashcard system to aid in memorizing key Telugu verses.
* Tap to flip and reveal the full Telugu scripture text.
* Mark cards as "Memorized" to track your mastery score.

### 7. Search & Daily Verse (దిన వచనము)
* Fast keyword search across books, chapters, and verses.
* Daily inspirational Telugu verse modal and home card for daily encouragement.

### 8. Cloud Sync & Offline-First Resilience
* Complete bundled Telugu Bible repository for full offline functionality.
* Optional Google Sign-In via Android Credential Manager to synchronize notes, bookmarks, and reading plan progress across devices using Firebase Firestore.

---

## 🛠 Tech Stack & Architecture

* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material Design 3 (M3)](https://m3.material.io)
* **Language:** Kotlin 2.0+ with Coroutines & StateFlow
* **Architecture:** MVVM (Model-View-ViewModel) with Clean Architecture principles
* **Local Persistence:** In-memory repository with bundled canonical Telugu scriptures and JSON mapping
* **Cloud Persistence:** Firebase Firestore with zero-trust security rules (`firestore.rules`)
* **Authentication:** Google Sign-In with Android Jetpack `CredentialManager`
* **Edge-to-Edge:** `enableEdgeToEdge()` with full `WindowInsets` handling
* **Testing:** Local JVM unit and integration tests using [Robolectric](https://robolectric.org/)

---

## 📁 Project Structure

```
app/src/main/java/com/example/
├── MainActivity.kt                  # Edge-to-edge Activity entry point
├── ui/
│   ├── BibleApp.kt                  # Root application container & navigation
│   ├── reader/
│   │   ├── BibleReaderScreen.kt     # Scripture reading view & verse sheet
│   │   └── BibleLeftPane.kt         # OT/NT drawer with 2-letter badges & 2-column picker
│   ├── notes/
│   │   └── NotesScreen.kt           # Devotional notes management
│   ├── bookmarks/
│   │   └── BookmarksScreen.kt       # Saved verses collection
│   ├── plans/
│   │   └── ReadingPlansScreen.kt    # 30-day reading challenge tracker
│   ├── flashcards/
│   │   └── FlashcardsScreen.kt      # Scripture memory flashcards
│   ├── search/
│   │   └── SearchScreen.kt          # Bible text search
│   ├── auth/
│   │   └── AuthScreen.kt            # Google Sign-In & user profile
│   └── theme/
│       ├── Color.kt                 # Theme palette
│       ├── Theme.kt                 # Material 3 dynamic color scheme
│       └── Type.kt                  # Typography tuned for Telugu fonts
└── data/
    ├── model/
    │   ├── BibleModels.kt           # Testament, Book, Chapter, Verse data classes
    │   ├── UserDataModels.kt        # Note, Bookmark, ReadingPlan, Flashcard models
    │   └── TeluguBibleData.kt       # Telugu canonical metadata & bundled scriptures
    └── repository/
        ├── TeluguBibleRepository.kt # Core reader repository & search engine
        └── FirestoreRepository.kt   # Cloud sync and offline fallback provider
```

---

## 🚀 Building & Running the Project

### Prerequisites
* **Android SDK:** Compile SDK 36, Min SDK 26, Target SDK 36
* **JDK:** Java 17+
* **Gradle:** 8.x+ (run using system `gradle`)

### Build the Debug APK
```bash
gradle assembleDebug
```

### Run Unit & Robolectric Tests
```bash
gradle :app:testDebugUnitTest
```

---

## 🔒 Security & Privacy

* **Firestore Rules:** All user data (bookmarks, notes, reading plan progress, flashcards) is scoped strictly to authenticated users under `/users/{userId}/*` in `firestore.rules`.
* **Zero Storage Permissions:** Operates without requiring broad file or storage permissions.

---

## 📜 License & Acknowledgments

* **Scripture Content:** Telugu Bible (పరిశుద్ధ గ్రంథము) public domain / open scripture resources.
* Developed for believers and Telugu scripture students worldwide.
