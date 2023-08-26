package edu.mlm.rick_and_morty.compose.regulars_screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.compose.destinations.FavoriteCharacterDetailsScreenDestination
import edu.mlm.rick_and_morty.compose.util.CharacterFilterDialog
import edu.mlm.rick_and_morty.compose.util.ErrorMessage
import edu.mlm.rick_and_morty.compose.util.list_item.FavoriteCharacterItem
import edu.mlm.rick_and_morty.data.entity.CharacterEntity
import edu.mlm.rick_and_morty.view_model.FavoriteCharacterViewModel

/**
 * A screen that displays characters obtained from the Room Database.
 *
 * Provides users with the ability to filter characters, delete them from the database
 * and view their details.
 */
@Destination
@Composable
fun FavoriteCharacterScreen(
    viewModel: FavoriteCharacterViewModel,
    navigator: DestinationsNavigator,
) {
    val nameFilter by viewModel.charactersName()
    val characters by viewModel.characters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isUnexpectedErrorShown by viewModel.isUnexpectedErrorShown.collectAsState()
    val isEmptyDatabaseError by viewModel.isEmptyDatabaseError.collectAsState()
    val isNoSuchCharactersInDb by viewModel.isNoSuchCharactersInDb.collectAsState()
    val isDialogShown by viewModel.isDialogShown.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TextField(
                value = nameFilter,
                onValueChange = viewModel::updateCharactersNameFilter,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_character),
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
                            viewModel.updateCharactersNameFilter("")
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
            CharacterList(
                characters = characters,
                viewModel = viewModel,
            ) {
                navigator.navigate(
                    FavoriteCharacterDetailsScreenDestination(it)
                )
            }
        }
        AnimatedVisibility(
            visible = isLoading,
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
        if (isDialogShown) {
            CharacterFilterDialog(
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
        if (isEmptyDatabaseError) {
            ErrorMessage(
                errorMessage = stringResource(id = R.string.empty_database_error),
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (isNoSuchCharactersInDb) {
            ErrorMessage(
                errorMessage = stringResource(id = R.string.no_characters_error),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

/**
 * Displays the list of [characters] and provides the [navigateToDetails] function for
 * the [FavoriteCharacterItem].
 */
@Composable
private fun CharacterList(
    characters: List<CharacterEntity>,
    viewModel: FavoriteCharacterViewModel,
    navigateToDetails: (CharacterEntity) -> Unit,
) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        itemsIndexed(items = characters) { _, item ->
            FavoriteCharacterItem(
                character = item,
                viewModel = viewModel,
                navigateToDetails = navigateToDetails
            )
        }
    }
}