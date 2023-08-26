package edu.mlm.rick_and_morty.compose.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import edu.mlm.rick_and_morty.compose.util.list_item.CharacterItem
import edu.mlm.rick_and_morty.service.dto.character.CharacterAPI
import edu.mlm.rick_and_morty.view_model.MainViewModel

/**
 * Displays the list of [characters] and provides the [navigateToDetails] function for the [CharacterItem].
 */
@Composable
fun CharacterList(
    characters: List<CharacterAPI>,
    viewModel: MainViewModel,
    navigateToDetails: (CharacterAPI) -> Unit,
) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        itemsIndexed(items = characters) { _, item ->
            CharacterItem(
                character = item,
                viewModel = viewModel,
                navigateToDetails = navigateToDetails
            )
        }
    }
}