package com.example.ui.reader

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BibleBook
import com.example.data.model.Testament
import com.example.data.repository.BibleRepository

@Composable
fun BibleLeftPaneContent(
    currentBook: BibleBook,
    currentChapter: Int,
    onSelectBookAndChapter: (BibleBook, Int, Int?) -> Unit,
    onClosePane: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTestament by remember { mutableStateOf(currentBook.testament) }
    // When a book is clicked, selectedBookForNavigation is set to show two columns (Chapters & Verses)
    var selectedBookForNavigation by remember { mutableStateOf<BibleBook?>(null) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxHeight()
            .width(360.dp)
            .testTag("bible_left_pane")
    ) {
        AnimatedContent(
            targetState = selectedBookForNavigation,
            transitionSpec = {
                if (targetState != null) {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            },
            label = "pane_switch_transition"
        ) { activeBook ->
            if (activeBook == null) {
                // VIEW 1: Left pane with OT and NT subsections and books with first two letters
                BookListView(
                    selectedTestament = selectedTestament,
                    onTestamentChange = { selectedTestament = it },
                    currentBook = currentBook,
                    onBookClick = { book ->
                        selectedBookForNavigation = book
                    },
                    onClosePane = onClosePane
                )
            } else {
                // VIEW 2: Two columns showing list of Chapters and Verses
                ChapterAndVerseTwoColumnView(
                    book = activeBook,
                    initialChapter = if (activeBook.id == currentBook.id) currentChapter else 1,
                    onBackToBooks = { selectedBookForNavigation = null },
                    onSelectVerse = { ch, verseNum ->
                        onSelectBookAndChapter(activeBook, ch, verseNum)
                        onClosePane()
                    },
                    onSelectWholeChapter = { ch ->
                        onSelectBookAndChapter(activeBook, ch, 1)
                        onClosePane()
                    }
                )
            }
        }
    }
}

@Composable
private fun BookListView(
    selectedTestament: Testament,
    onTestamentChange: (Testament) -> Unit,
    currentBook: BibleBook,
    onBookClick: (BibleBook) -> Unit,
    onClosePane: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        // Pane Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "పరిశుద్ధ గ్రంథ విభాగములు",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "గ్రంథాల పట్టిక (Directory)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onClosePane) {
                Icon(Icons.Default.Close, contentDescription = "Close Pane")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // OT and NT Segmented Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val otCount = remember { BibleRepository.books.count { it.testament == Testament.OLD } }
            val ntCount = remember { BibleRepository.books.count { it.testament == Testament.NEW } }

            FilterChip(
                selected = selectedTestament == Testament.OLD,
                onClick = { onTestamentChange(Testament.OLD) },
                label = { Text("పాత నిబంధన (OT - $otCount)") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ot_filter_tab")
            )
            FilterChip(
                selected = selectedTestament == Testament.NEW,
                onClick = { onTestamentChange(Testament.NEW) },
                label = { Text("క్రొత్త నిబంధన (NT - $ntCount)") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("nt_filter_tab")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(8.dp))

        // Subsections and Books List
        val booksForTestament = remember(selectedTestament) {
            BibleRepository.books.filter { it.testament == selectedTestament }
        }
        val groupedSubsections = remember(booksForTestament) {
            booksForTestament.groupBy { it.category }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .testTag("books_subsections_list")
        ) {
            groupedSubsections.forEach { (subsectionName, booksInGroup) ->
                item(key = "header_$subsectionName") {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = subsectionName,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                items(booksInGroup, key = { it.id }) { book ->
                    val isSelected = book.id == currentBook.id
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clickable { onBookClick(book) }
                            .testTag("book_item_${book.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // First Two Letters Badge
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.secondaryContainer
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = book.firstTwoLetters,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = book.nameTelugu,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${book.nameEnglish} • ${book.totalChapters} అధ్యాయాలు",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "View Chapters & Verses",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ChapterAndVerseTwoColumnView(
    book: BibleBook,
    initialChapter: Int,
    onBackToBooks: () -> Unit,
    onSelectVerse: (Int, Int) -> Unit,
    onSelectWholeChapter: (Int) -> Unit
) {
    var selectedChapter by remember { mutableIntStateOf(initialChapter.coerceIn(1, book.totalChapters)) }
    val verseCount = remember(book.id, selectedChapter) {
        book.getVerseCountForChapter(selectedChapter)
    }

    val chaptersListState = rememberLazyListState()
    val versesListState = rememberLazyListState()

    // Scroll to chapter when selected
    LaunchedEffect(selectedChapter) {
        chaptersListState.animateScrollToItem((selectedChapter - 1).coerceAtLeast(0))
        versesListState.scrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .testTag("two_column_chapter_verse_view")
    ) {
        // Back Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackToBooks,
                modifier = Modifier.testTag("back_to_books_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "గ్రంథాల జాబితాకు వెళ్లండి"
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = book.firstTwoLetters,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = book.nameTelugu,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${book.nameEnglish} • మొత్తం ${book.totalChapters} అధ్యాయాలు",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick "Read Whole Chapter" Button
        Button(
            onClick = { onSelectWholeChapter(selectedChapter) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("read_whole_chapter_button")
        ) {
            Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("అధ్యాయం $selectedChapter మొత్తం చదవండి")
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(8.dp))

        // Two Column Headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "అధ్యాయాలు (Chapters)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "వచనములు (Verses in $selectedChapter)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1.2f)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // TWO COLUMNS ROW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // COLUMN 1: List of Chapters (అధ్యాయములు)
            LazyColumn(
                state = chaptersListState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .padding(4.dp)
                    .testTag("column_chapters_list")
            ) {
                items(book.totalChapters) { index ->
                    val ch = index + 1
                    val isChSelected = ch == selectedChapter

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isChSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .clickable { selectedChapter = ch }
                            .testTag("chapter_item_$ch")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 7.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "అధ్యాయం $ch",
                                fontWeight = if (isChSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp,
                                color = if (isChSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )
                            if (isChSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // COLUMN 2: List of Verses (వచనములు)
            LazyColumn(
                state = versesListState,
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .padding(4.dp)
                    .testTag("column_verses_list")
            ) {
                items(verseCount) { vIndex ->
                    val vNum = vIndex + 1

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .clickable { onSelectVerse(selectedChapter, vNum) }
                            .testTag("verse_item_$vNum")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$vNum",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "వచనం $vNum",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
