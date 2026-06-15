package com.jeandealmeida_dev.itunesplayer.ui.player

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.jeandealmeida_dev.itunesplayer.R
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.ui.components.TrackActionSheet
import com.jeandealmeida_dev.itunesplayer.ui.theme.TextCounter
import com.jeandealmeida_dev.itunesplayer.ui.theme.TextPlayerArtist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    track: Track,
    onBack: () -> Unit,
    onAlbumClick: () -> Unit = {},
    viewModel: PlayerViewModel = viewModel(),
) {
    LaunchedEffect(track.id) { viewModel.setTrack(track) }
    DisposableEffect(Unit) { onDispose { viewModel.pause() } }

    val uiState by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme
    var showActionSheet by remember { mutableStateOf(false) }
    val duration = uiState.durationMs.coerceAtLeast(1L)
    val progress = (uiState.positionMs.toFloat() / duration).coerceIn(0f, 1f)

    Scaffold(
        containerColor = colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.now_playing),
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = colors.onBackground,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showActionSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.cd_more_options),
                            tint = colors.onBackground,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = track.artworkUrl.replace("100x100", "600x600"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(12.dp)),
                )
                if (uiState.isPreparing) {
                    CircularProgressIndicator(color = colors.onBackground)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = track.title,
                style = MaterialTheme.typography.headlineLarge,
                color = colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable(onClick = onAlbumClick),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = track.artist,
                style = MaterialTheme.typography.titleMedium,
                color = TextPlayerArtist,
                modifier = Modifier.clickable(onClick = onAlbumClick),
            )
            Spacer(modifier = Modifier.height(12.dp))

            PlayerProgressSlider(
                progress = progress,
                positionMs = uiState.positionMs,
                durationMs = uiState.durationMs,
                onSeek = { viewModel.seekTo(it) },
            )


            Spacer(modifier = Modifier.height(24.dp))

            PlayerControls(
                isPlaying = uiState.isPlaying,
                isRepeat = uiState.isRepeat,
                onPlayPauseClick = viewModel::togglePlayPause,
                onRepeatClick = viewModel::toggleRepeat,
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showActionSheet) {
        TrackActionSheet(
            track = track,
            onViewAlbum = {
                showActionSheet = false
                onAlbumClick()
            },
            onDismiss = { showActionSheet = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerProgressSlider(
    progress: Float,
    positionMs: Long,
    durationMs: Long,
    onSeek: (Float) -> Unit,
) {
    Column {
        val activeColor: Color = Color.White.copy(alpha = 0.6f)
        val inactiveColor: Color = Color.White.copy(alpha = 0.25f)
        val thumbColor: Color = Color.White

        Slider(
            value = progress,
            onValueChange = onSeek,
            modifier = Modifier
                .fillMaxWidth()
                .layout { measurable, constraints ->
                    val thumbRadius = 9.dp.roundToPx()
                    val placeable = measurable.measure(
                        constraints.copy(maxWidth = constraints.maxWidth + thumbRadius * 2)
                    )
                    layout(constraints.maxWidth, placeable.height) {
                        placeable.placeRelative(-thumbRadius, 0)
                    }
                },
            thumb = {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(thumbColor),
                )
            },
            track = {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp),
                ) {
                    drawLine(
                        color = inactiveColor,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = size.height,
                        cap = StrokeCap.Round,
                    )
                    drawLine(
                        color = activeColor,
                        start = Offset(0f, size.height / 2),
                        end = Offset((size.width * progress).coerceAtLeast(0f), size.height / 2),
                        strokeWidth = size.height,
                        cap = StrokeCap.Round,
                    )
                }
            },
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = positionMs.toTimeLabel(),
                style = MaterialTheme.typography.bodySmall,
                color = TextCounter,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = durationMs.toTimeLabel(),
                style = MaterialTheme.typography.bodySmall,
                color = TextCounter,
            )
        }
    }
}

@Composable
private fun PlayerControls(
    isPlaying: Boolean,
    isRepeat: Boolean,
    onPlayPauseClick: () -> Unit,
    onRepeatClick: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(colors.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = onPlayPauseClick) {
                if (isPlaying) {
                    Icon(
                        painter = painterResource(R.drawable.ic_pause),
                        contentDescription = stringResource(R.string.cd_pause),
                        tint = colors.onBackground,
                        modifier = Modifier.size(36.dp),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = stringResource(R.string.cd_play),
                        tint = colors.onBackground,
                        modifier = Modifier.size(36.dp),
                    )
                }
            }
        }

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.ic_skip_previous),
                contentDescription = stringResource(R.string.cd_skip_previous),
                tint = colors.onBackground,
                modifier = Modifier.size(36.dp),
            )
        }

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.ic_skip_next),
                contentDescription = stringResource(R.string.cd_skip_next),
                tint = colors.onBackground,
                modifier = Modifier.size(36.dp),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        BadgedBox(
            badge = {
                if (isRepeat) {
                    Badge(
                        containerColor = colors.onBackground,
                        contentColor = colors.background,
                    ) {
                        Text("1", fontSize = 9.sp)
                    }
                }
            },
        ) {
            IconButton(onClick = onRepeatClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_play_on_repeat),
                    contentDescription = stringResource(R.string.cd_repeat),
                    tint = if (isRepeat) colors.onBackground else colors.onBackground.copy(alpha = 0.4f),
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}

private fun Long.toTimeLabel(): String {
    val minutes = this / 60_000
    val seconds = (this % 60_000) / 1_000
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}
