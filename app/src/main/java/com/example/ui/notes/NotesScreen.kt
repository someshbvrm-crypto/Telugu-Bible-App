package com.example.ui.notes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Note
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NotesScreen(
    notes: List<Note>,
    onSaveNote: (Note) -> Unit,
    onDeleteNote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedTagFilter by remember { mutableStateOf<String?>(null) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var showExportSheet by remember { mutableStateOf(false) }

    val allTags = remember(notes) {
        notes.flatMap { it.tags }.distinct()
    }

    val filteredNotes = remember(notes, searchQuery, selectedTagFilter) {
        notes.filter { note ->
            val matchesQuery = searchQuery.isBlank() ||
                    note.title.contains(searchQuery, ignoreCase = true) ||
                    note.content.contains(searchQuery, ignoreCase = true) ||
                    note.bookName.contains(searchQuery, ignoreCase = true)
            val matchesTag = selectedTagFilter == null || note.tags.contains(selectedTagFilter)
            matchesQuery && matchesTag
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("notes_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar & Export Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("నోట్లలో వెతకండి...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("notes_search_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { showExportSheet = true },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp))
                        .testTag("export_notes_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "నోట్స్ ఎగుమతి (Export)",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Tag Filter Chips
            if (allTags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = selectedTagFilter == null,
                        onClick = { selectedTagFilter = null },
                        label = { Text("అన్నీ") }
                    )
                    allTags.forEach { tag ->
                        FilterChip(
                            selected = selectedTagFilter == tag,
                            onClick = {
                                selectedTagFilter = if (selectedTagFilter == tag) null else tag
                            },
                            label = { Text(tag) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredNotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "📝 ఇంకా నోట్స్ ఏవీ లేవు",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "వాక్యములను చదువుతున్నప్పుడు లేదా క్రింది '+' బటన్ ద్వారా నోట్స్ వ్రాసుకోండి.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredNotes, key = { it.id.ifBlank { "${it.title}_${it.createdAt?.seconds}" } }) { note ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("note_item_card")
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "${note.bookName} ${note.chapter}:${note.verse}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Row {
                                        IconButton(
                                            onClick = { editingNote = note },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = "ఎడిట్", modifier = Modifier.size(18.dp))
                                        }
                                        IconButton(
                                            onClick = { onDeleteNote(note.id) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "తొలగించు",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = note.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (note.tags.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        note.tags.forEach { tag ->
                                            Text(
                                                text = "#$tag",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }

        // Add Note FAB
        FloatingActionButton(
            onClick = {
                editingNote = Note(
                    title = "",
                    content = "",
                    bookName = "యోహాను",
                    chapter = 3,
                    verse = 16,
                    tags = listOf("ధ్యాయము")
                )
            },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("create_note_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "క్రొత్త నోట్", tint = MaterialTheme.colorScheme.onPrimary)
        }
    }

    // Edit / Create Note Dialog
    editingNote?.let { noteToEdit ->
        var editTitle by remember { mutableStateOf(noteToEdit.title) }
        var editContent by remember { mutableStateOf(noteToEdit.content) }
        var editBook by remember { mutableStateOf(noteToEdit.bookName) }
        var editChapter by remember { mutableStateOf(noteToEdit.chapter.toString()) }
        var editVerse by remember { mutableStateOf(noteToEdit.verse.toString()) }
        var editTagsText by remember { mutableStateOf(noteToEdit.tags.joinToString(", ")) }

        AlertDialog(
            onDismissRequest = { editingNote = null },
            title = { Text(if (noteToEdit.id.isBlank()) "క్రొత్త స్టడీ నోట్" else "నోట్ సవరణ") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("శీర్షిక (Title)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_dialog_title")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = editBook,
                            onValueChange = { editBook = it },
                            label = { Text("గ్రంథము") },
                            modifier = Modifier.weight(2f)
                        )
                        OutlinedTextField(
                            value = editChapter,
                            onValueChange = { editChapter = it },
                            label = { Text("అధ్యాయం") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editVerse,
                            onValueChange = { editVerse = it },
                            label = { Text("వాక్యం") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editContent,
                        onValueChange = { editContent = it },
                        label = { Text("వివరణ / ఆలోచనలు (Content)") },
                        minLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_dialog_content")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editTagsText,
                        onValueChange = { editTagsText = it },
                        label = { Text("ట్యాగ్‌లు (కామాలతో విభజించండి)") },
                        placeholder = { Text("ప్రార్థన, విశ్వాసము, ప్రేమ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedTags = editTagsText.split(",")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                        val updated = noteToEdit.copy(
                            title = editTitle.ifBlank { "అధ్యయనం" },
                            content = editContent,
                            bookName = editBook.ifBlank { "యోహాను" },
                            chapter = editChapter.toIntOrNull() ?: 1,
                            verse = editVerse.toIntOrNull() ?: 1,
                            tags = parsedTags
                        )
                        onSaveNote(updated)
                        editingNote = null
                    },
                    modifier = Modifier.testTag("save_note_button")
                ) {
                    Text("భద్రపరచు (Save)")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingNote = null }) {
                    Text("రద్దు")
                }
            }
        )
    }

    // Modal Bottom Sheet: Export Notes in Various Formats (JSON, TXT, Markdown, CSV)
    if (showExportSheet) {
        ModalBottomSheet(onDismissRequest = { showExportSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "నోట్స్ ఎగుమతి (Export Study Notes)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "మీ పరిశుద్ధ గ్రంథ అధ్యయన నోట్లను వివిధ ఫార్మాట్లలో సేవ్ చేసుకోండి:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                val exportPlainTxt = buildString {
                    appendLine("=== పరిశుద్ధ గ్రంథ అధ్యయన నోట్స్ (Telugu Bible Notes) ===")
                    appendLine("మొత్తం నోట్స్: ${notes.size}")
                    appendLine()
                    notes.forEachIndexed { index, n ->
                        appendLine("${index + 1}. ${n.title} [${n.bookName} ${n.chapter}:${n.verse}]")
                        appendLine("ట్యాగ్‌లు: ${n.tags.joinToString(", ")}")
                        appendLine(n.content)
                        appendLine("--------------------------------------------------")
                    }
                }

                val exportMarkdown = buildString {
                    appendLine("# పరిశుద్ధ గ్రంథ అధ్యయన నోట్స్ (Telugu Bible Notes)")
                    appendLine()
                    notes.forEach { n ->
                        appendLine("## ${n.title}")
                        appendLine("**వాక్యము:** `${n.bookName} ${n.chapter}:${n.verse}`  ")
                        if (n.tags.isNotEmpty()) {
                            appendLine("**ట్యాగ్‌లు:** ${n.tags.joinToString(", ") { "`#$it`" }}  ")
                        }
                        appendLine()
                        appendLine(n.content)
                        appendLine()
                        appendLine("---")
                    }
                }

                val exportJson = try {
                    val arr = JSONArray()
                    notes.forEach { n ->
                        val obj = JSONObject().apply {
                            put("title", n.title)
                            put("bookName", n.bookName)
                            put("chapter", n.chapter)
                            put("verse", n.verse)
                            put("content", n.content)
                            put("tags", JSONArray(n.tags))
                        }
                        arr.put(obj)
                    }
                    arr.toString(2)
                } catch (_: Exception) {
                    "{}"
                }

                val exportCsv = buildString {
                    appendLine("\"Title\",\"Book\",\"Chapter\",\"Verse\",\"Content\",\"Tags\"")
                    notes.forEach { n ->
                        val cleanContent = n.content.replace("\"", "\"\"")
                        val cleanTitle = n.title.replace("\"", "\"\"")
                        val cleanTags = n.tags.joinToString("; ").replace("\"", "\"\"")
                        appendLine("\"$cleanTitle\",\"${n.bookName}\",${n.chapter},${n.verse},\"$cleanContent\",\"$cleanTags\"")
                    }
                }

                // Export Options Cards
                val formats = listOf(
                    Triple("📄 Plain Text (.txt)", "సులభంగా చదువుకోవడానికి అనువైనది", exportPlainTxt),
                    Triple("📝 Markdown (.md)", "Obsidian, Notion లేదా బ్లాగ్ కొరకు అనువైనది", exportMarkdown),
                    Triple("⚙️ JSON Format (.json)", "ఇతర యాప్‌లతో డేటా సింక్ మరియు బ్యాకప్ కొరకు", exportJson),
                    Triple("📊 CSV Table (.csv)", "Excel లేదా Google Sheets కొరకు అనువైనది", exportCsv)
                )

                formats.forEach { (title, subtitle, exportData) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Row {
                                TextButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText(title, exportData))
                                        Toast.makeText(context, "$title కాపీ చేయబడింది!", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Text("కాపీ")
                                }
                                TextButton(
                                    onClick = {
                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT, exportData)
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "$title ఎగుమతి"))
                                    }
                                ) {
                                    Text("షేర్")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
