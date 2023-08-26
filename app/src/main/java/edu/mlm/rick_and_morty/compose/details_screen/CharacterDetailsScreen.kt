package edu.mlm.rick_and_morty.compose.details_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import edu.mlm.rick_and_morty.view_model.MainViewModel
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.compose.destinations.EpisodeDetailsScreenDestination
import edu.mlm.rick_and_morty.compose.destinations.LocationDetailsScreenDestination
import edu.mlm.rick_and_morty.compose.util.list_item.EpisodeItem
import edu.mlm.rick_and_morty.compose.util.ErrorMessage
import edu.mlm.rick_and_morty.compose.util.FullScreenImage
import edu.mlm.rick_and_morty.service.dto.character.CharacterAPI
import edu.mlm.rick_and_morty.service.dto.episode.EpisodeAPI

/**
 * Displays all available details about the [character].
 *
 * Provides users with the ability to either save or delete character from the database,
 * view details of the character's origin or last known location, and view details of episodes that
 * the character has participated in.
 */
@Destination
@Composable
fun CharacterDetailsScreen(
    character: CharacterAPI,
    viewModel: MainViewModel,
    navigator: DestinationsNavigator,
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val isErrorShown by viewModel.isUnexpectedErrorShown.collectAsState()
    val episodes by viewModel.episodes()
    val showType = character.type.isNotBlank()
    var isFavorite by remember { mutableStateOf(false) }
    var activeAvatarMaxSize by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        isFavorite = viewModel.isExist(character.id)
        viewModel.getEpisodeList(character.episode)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { navigator.popBackStack() },
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = CircleShape
                            )
                            .weight(1f)
                            .size(36.dp)
                    )
                }
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(8f)
                )
                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, start = 8.dp)
                            .size(36.dp)
                            .align(Alignment.Top)
                            .weight(1f)
                            .clickable {
                                viewModel.removeCharacterFromFavorite(character)
                                isFavorite = !isFavorite
                            }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, start = 8.dp)
                            .size(36.dp)
                            .align(Alignment.Top)
                            .weight(1f)
                            .clickable {
                                viewModel.addCharacterToFavorite(character)
                                isFavorite = !isFavorite
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = character.image)
                            .apply(block = fun ImageRequest.Builder.() {
                                scale(Scale.FILL)
                                placeholder(R.drawable.placeholder)
                                transformations(CircleCropTransformation())
                            }).build()
                    ),
                    contentDescription = character.name,
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { activeAvatarMaxSize = !activeAvatarMaxSize }
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.height(200.dp)
                ) {
                    Text(text = character.gender)
                    if (showType) {
                        Text(text = character.type, textAlign = TextAlign.Center)
                    }
                    Text(text = character.species)
                }
            }
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_circle_24),
                    contentDescription = character.status,
                    tint = when (character.status) {
                        "Alive" -> Color.Green
                        "Dead" -> Color.Red
                        else -> MaterialTheme.colorScheme.inverseSurface
                    },
                    modifier = Modifier.size(16.dp)
                )
                Text(text = character.status, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                val originName = character.origin.name
                val originURL = character.origin.url
                Text(text = stringResource(R.string.origin))
                Text(
                    text = originName,
                    modifier = Modifier.clickable {
                        if (originURL.isNotBlank()) {
                            val locationId = originURL.substringAfterLast('/').toInt()
                            viewModel.getLocation(locationId) {
                                navigator.navigate(LocationDetailsScreenDestination(it))
                            }
                        }
                    })
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                val locationName = character.location.name
                val locationURL = character.location.url
                Text(text = stringResource(R.string.last_known_location))
                Text(
                    text = locationName,
                    modifier = Modifier.clickable {
                        if (locationURL.isNotBlank()) {
                            val locationId = locationURL.substringAfterLast('/').toInt()
                            viewModel.getLocation(locationId) {
                                navigator.navigate(LocationDetailsScreenDestination(it))
                            }
                        }
                    })
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Participated", style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            EpisodeList(episodes = episodes) {
                navigator.navigate(EpisodeDetailsScreenDestination(it))
            }
            if (isErrorShown) {
                ErrorMessage(
                    errorMessage = stringResource(id = R.string.unexpected_error)
                )
            }
            AnimatedVisibility(
                visible = isLoading,
            ) {
                CircularProgressIndicator(
                    color = androidx.compose.material.MaterialTheme.colors.secondaryVariant
                )
            }
        }
    }
    if (activeAvatarMaxSize) {
        FullScreenImage(
            imageURL = character.image,
            onDismiss = { activeAvatarMaxSize = !activeAvatarMaxSize }
        )
    }
}

/**
 * Displays the list of [episodes] and provides the [navigateToDetails] function for the [EpisodeItem].
 */
@Composable
private fun EpisodeList(episodes: List<EpisodeAPI>, navigateToDetails: (EpisodeAPI) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.heightIn(0.dp, screenHeight - screenHeight / 4)
    ) {
        itemsIndexed(items = episodes) { _, item ->
            EpisodeItem(episode = item) {
                navigateToDetails(it)
            }
        }
    }
}