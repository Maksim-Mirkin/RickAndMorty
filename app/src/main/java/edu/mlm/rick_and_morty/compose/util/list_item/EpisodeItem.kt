package edu.mlm.rick_and_morty.compose.util.list_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.service.dto.episode.EpisodeAPI

/**
 * Displays the [episode] and retrieves the [navigateToDetails] to make the card clickable.
 */
@Composable
fun EpisodeItem(
    episode: EpisodeAPI,
    modifier: Modifier = Modifier,
    navigateToDetails: (EpisodeAPI) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp)
            .height(125.dp)
            .clickable { navigateToDetails(episode) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = episode.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = episode.episode,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = episode.airDate,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.click_for_details),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
fun EpisodeItemPreview() {
    val episodeAPI = EpisodeAPI(
        id = 1,
        name = "Pilot",
        airDate = "December 2, 2013",
        episode = "S01E01",

        characters = listOf(
            "https://rickandmortyapi.com/api/character/1",
            "https://rickandmortyapi.com/api/character/2",
            "https://rickandmortyapi.com/api/character/35",
            "https://rickandmortyapi.com/api/character/38",
            "https://rickandmortyapi.com/api/character/62",
            "https://rickandmortyapi.com/api/character/92",
            "https://rickandmortyapi.com/api/character/127",
            "https://rickandmortyapi.com/api/character/144",
            "https://rickandmortyapi.com/api/character/158",
            "https://rickandmortyapi.com/api/character/175",
            "https://rickandmortyapi.com/api/character/179",
            "https://rickandmortyapi.com/api/character/181",
            "https://rickandmortyapi.com/api/character/239",
            "https://rickandmortyapi.com/api/character/249",
            "https://rickandmortyapi.com/api/character/271",
            "https://rickandmortyapi.com/api/character/338",
            "https://rickandmortyapi.com/api/character/394",
            "https://rickandmortyapi.com/api/character/395",
            "https://rickandmortyapi.com/api/character/435"
        ),
        url = "https://rickandmortyapi.com/api/episode/1",
        created = "2017-11-10T12:56:33.798Z"
    )
    EpisodeItem(episode = episodeAPI) {}
}