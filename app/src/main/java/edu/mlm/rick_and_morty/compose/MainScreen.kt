package edu.mlm.rick_and_morty.compose

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import edu.mlm.rick_and_morty.compose.destinations.CharacterScreenDestination
import edu.mlm.rick_and_morty.compose.destinations.EpisodeScreenDestination
import edu.mlm.rick_and_morty.compose.destinations.FavoriteCharacterDetailsScreenDestination
import edu.mlm.rick_and_morty.compose.destinations.FavoriteCharacterScreenDestination
import edu.mlm.rick_and_morty.compose.destinations.LocationScreenDestination
import edu.mlm.rick_and_morty.compose.destinations.SettingsScreenDestination
import edu.mlm.rick_and_morty.compose.navigation.BottomAppBar
import edu.mlm.rick_and_morty.ui.theme.RickAndMortyTheme
import edu.mlm.rick_and_morty.view_model.FavoriteCharacterViewModel
import edu.mlm.rick_and_morty.view_model.MainViewModel

/**
 * Receives [activity] to make the MainViewModel accessible to all destinations requiring the
 * [MainViewModel].
 *
 * Creates objects of [MainViewModel] & [FavoriteCharacterViewModel], and then provides them to
 * the corresponding destinations.
 *
 * Manages the entire navigation system of the app.
 *
 * Holds a floating action button and displays it on relevant screens to show the filter dialog.
 */
@Composable
fun MainScreen(activity: ComponentActivity) {
    RickAndMortyTheme {
        val navController = rememberNavController()
        var showFabBar by rememberSaveable { mutableStateOf(true) }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        var isFavoriteCharacterScreen by remember { mutableStateOf(false) }
        val mainViewModel = hiltViewModel<MainViewModel>(activity)
        val favoriteCharacterViewModel = hiltViewModel<FavoriteCharacterViewModel>()
        showFabBar = when (navBackStackEntry?.destination?.route) {
            CharacterScreenDestination.route,
            EpisodeScreenDestination.route,
            LocationScreenDestination.route,
            -> {
                isFavoriteCharacterScreen = false
                true
            }

            FavoriteCharacterScreenDestination.route -> {
                isFavoriteCharacterScreen = true
                true
            }

            else -> {
                isFavoriteCharacterScreen = false
                false
            }
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                floatingActionButton = {
                    if (showFabBar) {
                        FloatingActionButton(onClick = {
                            if (isFavoriteCharacterScreen) {
                                favoriteCharacterViewModel.showFilterDialog()
                            } else {
                                mainViewModel.showFilterDialog()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        favoriteCharacterViewModel = favoriteCharacterViewModel
                    )
                }
            ) { paddingValues ->
                DestinationsNavHost(
                    navController = navController,
                    navGraph = NavGraphs.root,
                    modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                    dependenciesContainerBuilder = {
                        dependency(FavoriteCharacterScreenDestination) {
                            favoriteCharacterViewModel
                        }
                        dependency(FavoriteCharacterDetailsScreenDestination) {
                            favoriteCharacterViewModel
                        }
                        dependency(SettingsScreenDestination) {
                            favoriteCharacterViewModel
                        }
                        dependency(mainViewModel)
                    }
                )
            }
        }
    }
}