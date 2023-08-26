package edu.mlm.rick_and_morty.compose.util.list_item

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import edu.mlm.rick_and_morty.view_model.MainViewModel
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.service.dto.character.CharacterAPI

/**
 * Displays the [character] and retrieves the [navigateToDetails] to make the card clickable.
 */
@Composable
fun CharacterItem(
    character: CharacterAPI,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navigateToDetails: (CharacterAPI) -> Unit,
) {

    var isFavorite by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true){
        isFavorite = viewModel.isExist(character.id)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp)
            .height(375.dp)
            .clickable { navigateToDetails(character) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
        ) {
            Row {
                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = modifier
                            .weight(1f)
                            .padding(top = 8.dp, start = 8.dp)
                            .size(30.dp)
                            .align(Alignment.Top)
                            .clickable {
                                viewModel.removeCharacterFromFavorite(character)
                                isFavorite = !isFavorite
                            }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        modifier = modifier
                            .weight(1f)
                            .padding(top = 8.dp, start = 8.dp)
                            .size(30.dp)
                            .align(Alignment.Top)
                            .clickable {
                                viewModel.addCharacterToFavorite(character)
                                isFavorite = !isFavorite
                            }
                    )
                }


                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .weight(8f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_circle_24),
                    contentDescription = character.status,
                    tint = when (character.status) {
                        "Alive" -> Color.Green
                        "Dead" -> Color.Red
                        else -> MaterialTheme.colorScheme.inverseSurface
                    },
                    modifier = modifier
                        .weight(1f)
                        .padding(top = 8.dp)
                        .align(Alignment.Top)
                )
            }
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
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .weight(0.8f)
            )
            Text(
                text = stringResource(R.string.click_for_details),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}