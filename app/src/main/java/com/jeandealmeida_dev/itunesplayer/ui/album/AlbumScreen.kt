package com.jeandealmeida_dev.itunesplayer.ui.album

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.jeandealmeida_dev.itunesplayer.R
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.ui.components.TrackItem
import com.jeandealmeida_dev.itunesplayer.ui.components.TrackListShimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    track: Track,
    onBack: () -> Unit,
    onTrackClick: (Track) -> Unit = {},
    viewModel: AlbumViewModel = viewModel(),
) {
    LaunchedEffect(track.collectionId) { viewModel.loadAlbum(track.collectionId) }

    val uiState by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = track.album,
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background),
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
        ) {
            item {
                AlbumHeader(track = track)
            }
            if (uiState.isLoading) {
                item { TrackListShimmer() }
            } else {
                items(uiState.tracks) { albumTrack ->
                    TrackItem(
                        track = albumTrack,
                        showOptions = false,
                        onClick = { onTrackClick(albumTrack) },
                    )
                }
            }
        }
    }
}

@Composable
private fun AlbumHeader(track: Track) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = track.artworkUrl.replace("100x100", "600x600"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(12.dp)),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = track.album,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = track.artist,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}