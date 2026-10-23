package com.example.ui.reader

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.model.BibleBook
import com.example.data.model.BibleVerse
import com.example.data.model.Bookmark
import com.example.data.repository.BibleRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BibleReaderScreen(
    currentBook: BibleBook,
    currentChapter: Int,
    targetVerse: Int? = null,
    onBookChange: (BibleBook) -> Unit,
    onChapterChange: (Int) -> Unit,
    onTargetVerseConsumed: () -> Unit = {},
    bookmarks: List<Bookmark>,
    onToggleBookmark: (BibleVerse, String) -> Unit,
    onCreateNoteFromVerse: (BibleVerse) -> Unit,
    onCreateFlashcardFromVerse: (BibleVerse) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var showTextSettings by remember { mutableStateOf(false) }
    var selectedVerseForAction by remember { mutableStateOf<BibleVerse?>(null) }
    var fontSizeSp by remember { mutableFloatStateOf(19f) }
    var showEnglishParallel by remember { mutableStateOf(false) }

    val verses = remember(currentBook.id, currentChapter) {
        BibleRepository.getVerses(currentBook.id, currentChapter)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val versesListState = rememberLazyListState()

    // Smooth scroll to target verse if requested (from left pane, bookmarks, search, etc.)
    LaunchedEffect(targetVerse, currentBook.id, currentChapter) {
        if (targetVerse != null && targetVerse > 0) {
            // Index 0 is the chapter banner header, so target index is targetVerse
            val targetIndex = targetVerse.coerceIn(1, verses.size)
            versesListState.animateScrollToItem(targetIndex)
            onTargetVerseConsumed()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 360.dp)
            ) {
                // Left pane with OT and NT tabs, subsections, first-two-letters badges,
                // and two-column Chapters & Verses selection
                BibleLeftPaneContent(
                    currentBook = currentBook,
                    currentChapter = currentChapter,
                    onSelectBookAndChapter = { book, chapter, verseNum ->
                        onBookChange(book)
                        onChapterChange(chapter)
                        coroutineScope.launch {
                            drawerState.close()
                            if (verseNum != null) {
                                val targetIndex = verseNum.coerceIn(1, book.getVerseCountForChapter(chapter))
                                versesListState.animateScrollToItem(targetIndex)
                            } else {
                                versesListState.scrollToItem(0)
                            }
                        }
                    },
                    onClosePane = {
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        },
        modifier = modifier.testTag("bible_reader_drawer")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("bible_reader_screen")
        ) {
            // Reader Header / Navigation Bar
            Surface(
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Pane Menu Toggle & Book/Chapter Selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                coroutineScope.launch {
                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                }
                            }
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                            .testTag("book_selector_button")
                    ) {
                        // Left Pane Hamburger / Directory Icon
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "పరిశుద్ధ గ్రంథ విభాగములు తెరవండి (ఎడమ ప్యానెల్)",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // First two letters badge
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentBook.firstTwoLetters,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${currentBook.nameTelugu} $currentChapter",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "${currentBook.nameEnglish} • గ్రంథాల పట్టిక",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Controls: Font Settings & Chapter Arrows
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { showTextSettings = true },
                            modifier = Modifier.testTag("font_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = "అక్షరాల పరిమాణం",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = {
                                if (currentChapter > 1) {
                                    onChapterChange(currentChapter - 1)
                                    coroutineScope.launch { versesListState.scrollToItem(0) }
                                }
                            },
                            enabled = currentChapter > 1,
                            modifier = Modifier.testTag("prev_chapter_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "మునుపటి అధ్యాయము"
                            )
                        }

                        IconButton(
                            onClick = {
                                if (currentChapter < currentBook.totalChapters) {
                                    onChapterChange(currentChapter + 1)
                                    coroutineScope.launch { versesListState.scrollToItem(0) }
                                }
                            },
                            enabled = currentChapter < currentBook.totalChapters,
                            modifier = Modifier.testTag("next_chapter_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "తదుపరి అధ్యాయము"
                            )
                        }
                    }
                }
            }

            // Scripture Verses List
            LazyColumn(
                state = versesListState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .testTag("verses_list")
            ) {
                item(key = "chapter_banner_${currentBook.id}_$currentChapter") {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Chapter Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = currentBook.firstTwoLetters,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentBook.nameTelugu,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "అధ్యాయము $currentChapter (${currentBook.nameEnglish})",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                }

                items(verses, key = { "${it.bookId}_${it.chapter}_${it.verse}" }) { verse ->
                    val existingBookmark = bookmarks.find {
                        it.bookId == verse.bookId && it.chapter == verse.chapter && it.verse == verse.verse
                    }
                    val highlightColor = existingBookmark?.color?.let { hex ->
                        try {
                            Color(android.graphics.Color.parseColor(hex)).copy(alpha = 0.35f)
                        } catch (_: Exception) {
                            null
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .then(
                                if (highlightColor != null) Modifier.background(highlightColor)
                                else Modifier
                            )
                            .clickable { selectedVerseForAction = verse }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag("verse_row_${verse.verse}")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Verse number badge
                            Text(
                                text = "${verse.verse}",
                                fontSize = (fontSizeSp * 0.75f).sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .width(32.dp)
                                    .padding(top = 2.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = verse.textTelugu,
                                    fontSize = fontSizeSp.sp,
                                    lineHeight = (fontSizeSp * 1.55f).sp,
                                    fontFamily = FontFamily.Serif,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Normal
                                )

                                if (showEnglishParallel && verse.textEnglish.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = verse.textEnglish,
                                        fontSize = (fontSizeSp * 0.85f).sp,
                                        lineHeight = (fontSizeSp * 1.3f).sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (existingBookmark != null) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = "బుక్‌మార్క్ చేయబడింది",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(top = 2.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(28.dp))
                    // Chapter footer navigation
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (currentChapter > 1) {
                            OutlinedButton(
                                onClick = {
                                    onChapterChange(currentChapter - 1)
                                    coroutineScope.launch { versesListState.scrollToItem(0) }
                                },
                                modifier = Modifier.testTag("footer_prev_button")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("అధ్యాయం ${currentChapter - 1}")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        if (currentChapter < currentBook.totalChapters) {
                            Button(
                                onClick = {
                                    onChapterChange(currentChapter + 1)
                                    coroutineScope.launch { versesListState.scrollToItem(0) }
                                },
                                modifier = Modifier.testTag("footer_next_button")
                            ) {
                                Text("అధ్యాయం ${currentChapter + 1}")
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet: Verse Quick Actions
    selectedVerseForAction?.let { verse ->
        val currentBm = bookmarks.find {
            it.bookId == verse.bookId && it.chapter == verse.chapter && it.verse == verse.verse
        }

        ModalBottomSheet(
            onDismissRequest = { selectedVerseForAction = null },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${verse.bookNameTelugu} ${verse.chapter}:${verse.verse}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { selectedVerseForAction = null }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = verse.textTelugu,
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Highlight Color Palette
                Text(
                    text = "రంగుతో హైలైట్ / బుక్‌మార్క్ చేయండి:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val colors = listOf(
                        "#FFD54F" to "బంగారు (Gold)",
                        "#A5D6A7" to "ఆకుపచ్చ (Mint)",
                        "#90CAF9" to "ఆకాశనీలం (Sky)",
                        "#F48FB1" to "గులాబీ (Rose)",
                        "#CE93D8" to "ఊదా (Lavender)"
                    )
                    colors.forEach { (hex, name) ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(android.graphics.Color.parseColor(hex)))
                                .clickable {
                                    onToggleBookmark(verse, hex)
                                    selectedVerseForAction = null
                                    Toast.makeText(context, "$name బుక్‌మార్క్ చేయబడింది", Toast.LENGTH_SHORT).show()
                                }
                        )
                    }

                    if (currentBm != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                onToggleBookmark(verse, "")
                                selectedVerseForAction = null
                                Toast.makeText(context, "బుక్‌మార్క్ తొలగించబడింది", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Text("తొలగించు")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons Row
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = {
                            val target = selectedVerseForAction
                            selectedVerseForAction = null
                            if (target != null) onCreateNoteFromVerse(target)
                        },
                        leadingIcon = { Icon(Icons.Default.EditNote, contentDescription = null) },
                        label = { Text("నోట్ రాయండి") },
                        modifier = Modifier.testTag("action_add_note")
                    )

                    AssistChip(
                        onClick = {
                            val target = selectedVerseForAction
                            selectedVerseForAction = null
                            if (target != null) onCreateFlashcardFromVerse(target)
                        },
                        leadingIcon = { Icon(Icons.Default.Style, contentDescription = null) },
                        label = { Text("జ్ఞాపిక / ఫ్లాష్‌కార్డ్") },
                        modifier = Modifier.testTag("action_add_flashcard")
                    )

                    AssistChip(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Bible Verse", "${verse.textTelugu} - ${verse.referenceText}")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "వాక్యం కాపీ చేయబడింది", Toast.LENGTH_SHORT).show()
                            selectedVerseForAction = null
                        },
                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
                        label = { Text("కాపీ చేయండి") },
                        modifier = Modifier.testTag("action_copy_verse")
                    )

                    AssistChip(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, verse.referenceText)
                                putExtra(Intent.EXTRA_TEXT, "\"${verse.textTelugu}\"\n- ${verse.referenceText}")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "వాక్యమును షేర్ చేయండి"))
                            selectedVerseForAction = null
                        },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                        label = { Text("షేర్ చేయండి") },
                        modifier = Modifier.testTag("action_share_verse")
                    )
                }
            }
        }
    }

    // Modal Bottom Sheet: Text Settings (Font size & English toggle)
    if (showTextSettings) {
        ModalBottomSheet(
            onDismissRequest = { showTextSettings = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "పఠన సెట్టింగులు (Reading Settings)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "అక్షరాల పరిమాణం: ${fontSizeSp.toInt()} sp",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(16f to "చిన్నది (16)", 19f to "సాధారణం (19)", 22f to "పెద్దది (22)", 26f to "మరింత పెద్దది (26)").forEach { (size, label) ->
                        FilterChip(
                            selected = fontSizeSp == size,
                            onClick = { fontSizeSp = size },
                            label = { Text(label) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ఆంగ్ల సమాంతర వచనము (English Parallel)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "తెలుగు వాక్యము కింద ఆంగ్ల వాక్యమును చూపును",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    FilterChip(
                        selected = showEnglishParallel,
                        onClick = { showEnglishParallel = !showEnglishParallel },
                        label = { Text(if (showEnglishParallel) "ఆన్" else "ఆఫ్") }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
