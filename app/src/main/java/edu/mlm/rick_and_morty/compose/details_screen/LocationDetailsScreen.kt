package edu.mlm.rick_and_morty.compose.details_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import edu.mlm.rick_and_morty.view_model.MainViewModel
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.compose.util.CharacterList
import edu.mlm.rick_and_morty.compose.destinations.CharacterDetailsScreenDestination
import edu.mlm.rick_and_morty.compose.util.ErrorMessage
import edu.mlm.rick_and_morty.service.dto.location.LocationAPI

/**
 * Displays all available details about the [location].
 *
 * Provides users with the ability to either save or delete characters from the database and view
 * their details.
 */
@Destination
@Composable
fun LocationDetailsScreen(
    location: LocationAPI,
    viewModel: MainViewModel,
    navigator: DestinationsNavigator,
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val isErrorShown by viewModel.isUnexpectedErrorShown.collectAsState()
    val characters by viewModel.characters()
    LaunchedEffect(key1 = true) {
        viewModel.getCharacterList(location.residents)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                    text = location.name,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(9f)
                        .offset(((-20).dp))
                )
            }
            Text(text = location.type, style = MaterialTheme.typography.bodyMedium)
            Text(text = location.dimension, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = stringResource(R.string.participants),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            CharacterList(
                characters = characters,
                viewModel = viewModel
            ) {
                navigator.navigate(CharacterDetailsScreenDestination(it))
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
}