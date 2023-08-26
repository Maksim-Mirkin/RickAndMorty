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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.service.dto.location.LocationAPI

/**
 * Displays the [location] and retrieves the [navigatesToDetail] to make the card clickable.
 */
@Composable
fun LocationItem(
    location: LocationAPI,
    modifier: Modifier = Modifier,
    navigatesToDetail: (LocationAPI) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp)
            .height(125.dp)
            .clickable { navigatesToDetail(location) },
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
                text = location.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = location.type,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = location.dimension,
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
fun LocationItemPreview() {
    val location = LocationAPI(
        id = 1,
        name = "Earth (C-137)",
        type = "Planet",
        dimension = "Dimension C-137",
        residents = listOf(
            "https://rickandmortyapi.com/api/character/38",
            "https://rickandmortyapi.com/api/character/45",
            "https://rickandmortyapi.com/api/character/71",
            "https://rickandmortyapi.com/api/character/82",
            "https://rickandmortyapi.com/api/character/83",
            "https://rickandmortyapi.com/api/character/92",
            "https://rickandmortyapi.com/api/character/112",
            "https://rickandmortyapi.com/api/character/114",
            "https://rickandmortyapi.com/api/character/116",
            "https://rickandmortyapi.com/api/character/117",
            "https://rickandmortyapi.com/api/character/120",
            "https://rickandmortyapi.com/api/character/127",
            "https://rickandmortyapi.com/api/character/155",
            "https://rickandmortyapi.com/api/character/169",
            "https://rickandmortyapi.com/api/character/175",
            "https://rickandmortyapi.com/api/character/179",
            "https://rickandmortyapi.com/api/character/186",
            "https://rickandmortyapi.com/api/character/201",
            "https://rickandmortyapi.com/api/character/216",
            "https://rickandmortyapi.com/api/character/239",
            "https://rickandmortyapi.com/api/character/271",
            "https://rickandmortyapi.com/api/character/302",
            "https://rickandmortyapi.com/api/character/303",
            "https://rickandmortyapi.com/api/character/338",
            "https://rickandmortyapi.com/api/character/343",
            "https://rickandmortyapi.com/api/character/356",
            "https://rickandmortyapi.com/api/character/394"
        ),
        url = "https://rickandmortyapi.com/api/location/1",
        created = "2017-11-10T12:42:04.162Z"
    )
    LocationItem(location = location) {}
}