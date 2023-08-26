package edu.mlm.rick_and_morty.compose.regulars_screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.compose.destinations.EpisodeDetailsScreenDestination
import edu.mlm.rick_and_morty.compose.util.ErrorMessage
import edu.mlm.rick_and_morty.compose.util.list_item.EpisodeItem
import edu.mlm.rick_and_morty.service.dto.episode.EpisodeAPI
import edu.mlm.rick_and_morty.view_model.MainViewModel

/**
 * A screen that displays episodes obtained from the API.
 *
 * Provides users with the ability to filter episodes and view their details.
 */
@Destination
@Composable
fun EpisodeScreen(
    viewModel: MainViewModel,
    navigator: DestinationsNavigator,
) {
    val episodeNameFilter by viewModel.episodesName()
    val episodes by viewModel.episodes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isUnexpectedErrorShown by viewModel.isUnexpectedErrorShown.collectAsState()
    val isHttpErrorShown by viewModel.isHttpErrorShown.collectAsState()
    val isDialogShown by viewModel.isDialogShown.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TextField(
                value = episodeNameFilter,
                onValueChange = viewModel::updateEpisodeNameFilter,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_episode),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            viewModel.updateEpisodeNameFilter("")
                        })
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.labelMedium,
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            EpisodeList(episodes = episodes) {
                navigator.navigate(EpisodeDetailsScreenDestination(it))

            }
        }
        AnimatedVisibility(
            visible = isLoading,
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
        if (isDialogShown) {
            FilterDialog(
                viewModel = viewModel,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (isUnexpectedErrorShown) {
            ErrorMessage(
                errorMessage = stringResource(id = R.string.unexpected_error),
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (isHttpErrorShown) {
            ErrorMessage(
                errorMessage = stringResource(id = R.string.no_episodes_error),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

/**
 * Displays the list of [episodes] and provides the [navigateToDetails] function for the [EpisodeItem].
 */
@Composable
private fun EpisodeList(episodes: List<EpisodeAPI>, navigateToDetails: (EpisodeAPI) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        itemsIndexed(items = episodes) { _, item ->
            EpisodeItem(episode = item) { navigateToDetails(item) }
        }
    }
}

/**
 * Display the filter dialog.
 *
 * Retrieves [MainViewModel] for accessing its methods.
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun FilterDialog(viewModel: MainViewModel, modifier: Modifier) {
    var isSeasonExpanded by remember { mutableStateOf(false) }
    var isEpisodeExpanded by remember { mutableStateOf(false) }
    val seasonNumber by viewModel.seasonNumber()
    val episodeNumber by viewModel.episodeNumber()

    Dialog(onDismissRequest = viewModel::closeFilterDialog) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = isSeasonExpanded,
                        onExpandedChange = { isSeasonExpanded = it }) {
                        TextField(
                            value = seasonNumber,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSeasonExpanded)
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                            textStyle = MaterialTheme.typography.labelMedium,
                            placeholder = {
                                Text(
                                    text = "S",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = modifier
                                .menuAnchor()
                                .width(100.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = isSeasonExpanded,
                            onDismissRequest = { isSeasonExpanded = false }
                        ) {
                            for (i in 1..5) {
                                DropdownMenuItem(
                                    text = { Text(text = "$i") },
                                    onClick = {
                                        viewModel.updateSeasonsNumberFilter("$i")
                                        isSeasonExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    ExposedDropdownMenuBox(
                        expanded = isEpisodeExpanded,
                        onExpandedChange = { isEpisodeExpanded = it }) {
                        TextField(
                            value = episodeNumber,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isEpisodeExpanded)
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                            textStyle = MaterialTheme.typography.labelMedium,
                            placeholder = {
                                Text(
                                    text = "E",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = modifier
                                .menuAnchor()
                                .width(100.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = isEpisodeExpanded,
                            onDismissRequest = { isEpisodeExpanded = false }
                        ) {
                            for (i in 1..10) {
                                DropdownMenuItem(
                                    text = { Text(text = "$i") },
                                    onClick = {
                                        viewModel.updateEpisodesNumberFilter("$i")
                                        isEpisodeExpanded = false
                                    }
                                )
                            }
                            if (seasonNumber.isNotBlank() && seasonNumber.toInt() == 1) {
                                DropdownMenuItem(
                                    text = { Text(text = "11") },
                                    onClick = {
                                        viewModel.updateEpisodesNumberFilter("11")
                                        isEpisodeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                ) {
                    Button(onClick = viewModel::closeFilterDialog) {
                        Text(text = "Close")
                    }
                    Button(onClick = viewModel::resetEpisodesFilters) {
                        Text(text = "Reset")
                    }
                    Button(onClick = {
                        viewModel.refreshEpisodes()
                        viewModel.closeFilterDialog()
                    }) {
                        Text(text = "Filter")
                    }
                }
            }
        }
    }
}