package com.jeandealmeida_dev.itunesplayer.auto.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jeandealmeida_dev.itunesplayer.auto.R
import com.jeandealmeida_dev.itunesplayer.domain.model.Track

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTrackClick: (Track) -> Unit = {},
    viewModel: HomeViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.auto_music),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background),
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colors.onBackground,
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error.orEmpty(),
                        color = colors.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.tracks) { track ->
                            AutoTrackItem(
                                track = track,
                                onClick = { onTrackClick(track) },
                            )
                            HorizontalDivider(
                                color = colors.outlineVariant.copy(alpha = 0.3f),
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AutoTrackItem(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .heightIn(min = 72.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = track.artworkUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(6.dp)),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = track.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
