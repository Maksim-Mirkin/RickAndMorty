package edu.mlm.rick_and_morty.compose.navigation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.mlm.rick_and_morty.view_model.MainViewModel
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.compose.destinations.CharacterScreenDestination
import edu.mlm.rick_and_morty.compose.destinations.EpisodeScreenDestination
import edu.mlm.rick_and_morty.compose.destinations.FavoriteCharacterScreenDestination
import edu.mlm.rick_and_morty.compose.destinations.LocationScreenDestination
import edu.mlm.rick_and_morty.compose.destinations.SettingsScreenDestination
import edu.mlm.rick_and_morty.view_model.FavoriteCharacterViewModel

private enum class BottomIcons {
    CHARACTER, EPISODE, LOCATION, FAVORITE, SETTINGS
}

/**
 * Provides users with the ability to navigate between screens.
 */
@Composable
fun BottomAppBar(
    navController: NavController = rememberNavController(),
    mainViewModel: MainViewModel,
    favoriteCharacterViewModel: FavoriteCharacterViewModel,
) {
    val selected = remember { mutableStateOf(BottomIcons.CHARACTER) }
    BottomAppBar(modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    selected.value = BottomIcons.CHARACTER
                    navController.navigate(CharacterScreenDestination.route) {
                        popUpTo(CharacterScreenDestination.route) { inclusive = true }
                        mainViewModel.refreshCharacters()
                    }
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_people),
                        contentDescription = "Character Screen",
                        tint = if (selected.value == BottomIcons.CHARACTER)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.background
                    )
                }
                IconButton(onClick = {
                    selected.value = BottomIcons.EPISODE
                    navController.navigate(EpisodeScreenDestination.route) {
                        popUpTo(EpisodeScreenDestination.route) { inclusive = true }
                        mainViewModel.refreshEpisodes()
                    }
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_list),
                        contentDescription = "Character Screen",
                        tint = if (selected.value == BottomIcons.EPISODE)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.background
                    )
                }
                IconButton(onClick = {
                    selected.value = BottomIcons.LOCATION
                    navController.navigate(LocationScreenDestination.route) {
                        popUpTo(LocationScreenDestination.route) { inclusive = true }
                        mainViewModel.refreshLocations()
                    }
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_location_city),
                        contentDescription = "Character Screen",
                        tint = if (selected.value == BottomIcons.LOCATION)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.background
                    )
                }
                IconButton(onClick = {
                    selected.value = BottomIcons.FAVORITE
                    navController.navigate(FavoriteCharacterScreenDestination.route) {
                        popUpTo(FavoriteCharacterScreenDestination.route) { inclusive = true }
                        favoriteCharacterViewModel.refreshCharacters()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite Character Screen",
                        tint = if (selected.value == BottomIcons.FAVORITE)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.background
                    )
                }
                IconButton(onClick = {
                    selected.value = BottomIcons.SETTINGS
                    navController.navigate(SettingsScreenDestination.route) {
                        popUpTo(SettingsScreenDestination.route) { inclusive = true }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings Screen",
                        tint = if (selected.value == BottomIcons.SETTINGS)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    )
}