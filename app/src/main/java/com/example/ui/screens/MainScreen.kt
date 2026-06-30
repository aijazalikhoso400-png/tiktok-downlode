package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DownloadHistory
import com.example.data.network.TikWmData
import com.example.ui.TikTokViewModel
import com.example.ui.VideoDetailState
import com.example.utils.TikTokUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Custom Immersive UI Design Scheme
val ImmersiveBackground = Color(0xFF0F0F0F)
val ImmersiveSurface = Color(0xFF171717)
val ImmersiveFuchsia = Color(0xFFD946EF) // fuchsia-500
val ImmersiveFuchsiaDark = Color(0xFFC026D3) // fuchsia-600
val ImmersiveCyan = Color(0xFF25F4EE)
val ImmersivePurple = Color(0xFF7E22CE) // purple-700
val ImmersiveBorder = Color(0x19FFFFFF) // white/10
val TextWhite = Color(0xFFF5F5FA)
val TextMuted = Color(0x99F5F5FA)

val ImmersivePurpleGrad = Brush.linearGradient(
    listOf(Color(0xFFC026D3), Color(0xFF7E22CE))
)

@Composable
fun MainScreen(
    viewModel: TikTokViewModel,
    modifier: Modifier = Modifier,
    initialSharedText: String? = null
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val inputUrl by viewModel.inputUrl.collectAsState()
    val videoState by viewModel.videoState.collectAsState()
    val historyList by viewModel.downloadHistory.collectAsState()

    // Handle shared text trigger on startup
    LaunchedEffect(initialSharedText) {
        if (!initialSharedText.isNullOrEmpty()) {
            val extracted = TikTokUtils.extractTikTokUrl(initialSharedText)
            if (extracted != null) {
                viewModel.onUrlChange(extracted)
                viewModel.fetchVideo(extracted)
            } else {
                viewModel.onUrlChange(initialSharedText)
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ImmersiveBackground),
        containerColor = ImmersiveBackground,
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Top Spacer for Status bar
            item {
                Spacer(modifier = Modifier.height(12.dp).statusBarsPadding())
                HeaderView()
            }

            // Input field Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, ImmersiveBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Paste TikTok Link",
                            color = TextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = inputUrl,
                            onValueChange = { viewModel.onUrlChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("url_input_field"),
                            placeholder = { Text("Paste TikTok link here...", color = TextMuted, fontSize = 13.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedBorderColor = ImmersiveFuchsia,
                                unfocusedBorderColor = Color(0x33FFFFFF),
                                focusedContainerColor = Color(0xFF0F0F0F),
                                unfocusedContainerColor = Color(0xFF0F0F0F)
                            ),
                            trailingIcon = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(end = 6.dp)
                                ) {
                                    if (inputUrl.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.clearInput() }, modifier = Modifier.size(36.dp)) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Clear input",
                                                tint = TextMuted,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    Button(
                                        onClick = {
                                            val clipText = clipboardManager.getText()?.text
                                            if (!clipText.isNullOrEmpty()) {
                                                viewModel.onUrlChange(clipText)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.White,
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                        modifier = Modifier
                                            .height(36.dp)
                                            .testTag("paste_button")
                                    ) {
                                        Text(
                                            text = "PASTE",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            },
                            shape = RoundedCornerShape(16.dp)
                        )

                        Button(
                            onClick = { viewModel.fetchVideo(inputUrl) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("fetch_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = ImmersiveFuchsiaDark),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Search icon",
                                tint = TextWhite
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Analyze Video",
                                color = TextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            // Interactive Video details / Loading / Error UI
            item {
                AnimatedVisibility(
                    visible = videoState != VideoDetailState.Idle,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { 40 }),
                    exit = fadeOut()
                ) {
                    when (val state = videoState) {
                        is VideoDetailState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CircularProgressIndicator(color = ImmersiveFuchsia)
                                    Text("Analyzing URL and fetching media...", color = TextMuted, fontSize = 13.sp)
                                }
                            }
                        }
                        is VideoDetailState.Success -> {
                            VideoDetailCard(data = state.data, context = context)
                        }
                        is VideoDetailState.Error -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF3B1E22)),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.dp, ImmersiveFuchsia)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1.0f)) {
                                        Text(
                                            text = "Oops! Processing Error",
                                            color = ImmersiveFuchsia,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = state.message,
                                            color = TextWhite,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }

            // History Label
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History icon",
                            tint = ImmersiveFuchsia,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Recent Downloads",
                            color = Color(0xFF64748B), // slate-500
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // History List items
            if (historyList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, ImmersiveBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "No Downloads Yet",
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Your parsed videos will show up here for fast access.",
                                color = TextMuted,
                                fontSize = 13.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            } else {
                items(historyList, key = { it.id }) { item ->
                    HistoryItemCard(
                        history = item,
                        onDelete = { viewModel.deleteHistoryItem(item.id) },
                        onShare = {
                            shareText(context, "Check out this TikTok video downloaded via TikSave: ${item.videoUrl}")
                        },
                        onDownload = {
                            val fileName = "tiktok_video_${item.tiktokId}.mp4"
                            TikTokUtils.startDownload(context, item.videoUrl, item.title, fileName)
                        }
                    )
                }
            }

            // Bottom Spacing for Navigation gestures
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun HeaderView() {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ImmersivePurpleGrad),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Logo icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "TikSave",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "PRO DOWNLOADER",
                    color = Color(0xFF64748B), // slate-500
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        IconButton(
            onClick = {
                android.widget.Toast.makeText(
                    context, 
                    "TikSave Pro • Settings Auto-Optimized for Ultra-Fast Downloads", 
                    android.widget.Toast.LENGTH_LONG
                ).show()
            },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF171717))
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color(0xFF94A3B8) // slate-400
            )
        }
    }
}

@Composable
fun VideoDetailCard(data: TikWmData, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("video_detail_card"),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
        shape = RoundedCornerShape(32.dp),
        border = BorderStroke(1.dp, ImmersiveBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // FULL-BLEED IMMERSIVE COVER WITH OVERLAYS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                // Background Cover Image
                AsyncImage(
                    model = data.cover,
                    contentDescription = "Video cover",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.55f
                )

                // Scrim Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Play Overlay icon in the center
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play icon overlay",
                        tint = TextWhite,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Top Right HD Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(100.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "1080P HD",
                        color = TextWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                // Bottom Content Overlaid (Author info & Video stats)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Author info Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = data.author?.avatarUrl,
                            contentDescription = "Author avatar",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color(0x66FFFFFF), CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "@${data.author?.uniqueId ?: "user"}",
                            color = TextWhite,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }

                    // Video title/desc
                    Text(
                        text = data.title?.ifEmpty { "No description" } ?: "No description",
                        color = TextWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Quick Info Stat Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VideoStatBadge(label = "Duration", value = "${data.duration ?: 0}s", modifier = Modifier.weight(1f))
                VideoStatBadge(label = "Likes", value = formatStatsCount(data.diggCount), modifier = Modifier.weight(1f))
                VideoStatBadge(label = "Comments", value = formatStatsCount(data.commentCount), modifier = Modifier.weight(1f))
            }

            // ACTION BUTTONS GRID AS PER IMMERSIVE UI DESIGN HTML
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // NO WATERMARK Button
                Button(
                    onClick = {
                        val fileName = "tiktok_no_watermark_${data.id}.mp4"
                        TikTokUtils.startDownload(context, data.play ?: "", data.title ?: "video", fileName)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("download_no_wm_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ImmersiveFuchsiaDark),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = "Movie icon",
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "NO WATERMARK",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                // MP3 ONLY Button
                Button(
                    onClick = {
                        val fileName = "tiktok_audio_${data.id}.mp3"
                        TikTokUtils.startDownload(context, data.musicUrl ?: "", "Music: " + (data.title ?: "audio"), fileName)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("download_audio_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF262626)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0x1AFFFFFF))
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Music icon",
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "MP3 ONLY",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Secondary Buttons for HD Option and Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Download HD Video (Full width if no HD option, but we only show if hdplay is available)
                if (!data.hdplay.isNullOrEmpty()) {
                    Button(
                        onClick = {
                            val fileName = "tiktok_hd_${data.id}.mp4"
                            TikTokUtils.startDownload(context, data.hdplay, data.title ?: "video", fileName)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("download_hd_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ImmersiveCyan),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download HD",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Download HD",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                // Share Button
                OutlinedButton(
                    onClick = {
                        shareText(context, "Check out this amazing TikTok: https://www.tiktok.com/@${data.author?.uniqueId}/video/${data.id}")
                    },
                    modifier = Modifier
                        .weight(if (!data.hdplay.isNullOrEmpty()) 1f else 2f)
                        .height(48.dp)
                        .testTag("share_video_button"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                    border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share video",
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Share Video",
                        color = TextWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(
    history: DownloadHistory,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onDownload: () -> Unit
) {
    val dateString = try {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(history.timestamp))
    } catch (e: Exception) {
        ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDownload() }
            .testTag("history_item_card_${history.id}"),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, ImmersiveBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = history.coverUrl,
                contentDescription = "Cover preview",
                modifier = Modifier
                    .size(width = 64.dp, height = 84.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1.0f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = history.title.ifEmpty { "No description" },
                    color = TextWhite,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "@${history.authorNickname}",
                    color = ImmersiveFuchsia,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = dateString,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onShare, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share item",
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete item",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VideoStatBadge(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF202020))
            .border(1.dp, Color(0x10FFFFFF), RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VideoStatLabel(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextMuted, fontSize = 12.sp)
        Text(text = value, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

fun formatStatsCount(count: Int?): String {
    if (count == null) return "0"
    return when {
        count >= 1_000_000 -> String.format(Locale.getDefault(), "%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format(Locale.getDefault(), "%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}

fun shareText(context: Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Video Link")
    context.startActivity(shareIntent)
}
