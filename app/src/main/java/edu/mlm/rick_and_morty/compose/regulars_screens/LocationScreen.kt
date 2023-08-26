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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.compose.destinations.LocationDetailsScreenDestination
import edu.mlm.rick_and_morty.compose.util.ErrorMessage
import edu.mlm.rick_and_morty.compose.util.list_item.LocationItem
import edu.mlm.rick_and_morty.service.dto.location.LocationAPI
import edu.mlm.rick_and_morty.view_model.MainViewModel

/**
 * A screen that displays locations obtained from the API.
 *
 * Provides users with the ability to filter locations and view their details.
 */
@Destination
@Composable
fun LocationScreen(
    viewModel: MainViewModel,
    navigator: DestinationsNavigator,
) {
    val nameFilter by viewModel.locationName()
    val locations by viewModel.locations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isUnexpectedErrorShown by viewModel.isUnexpectedErrorShown.collectAsState()
    val isHttpErrorShown by viewModel.isHttpErrorShown.collectAsState()
    val isDialogShown by viewModel.isDialogShown.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TextField(
                value = nameFilter,
                onValueChange = viewModel::updateLocationsNameFilter,
                placeholder = {
                    androidx.compose.material3.Text(
                        text = stringResource(R.string.search_locations),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    androidx.compose.material.Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    androidx.compose.material.Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            viewModel.updateLocationsNameFilter("")
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
            LocationList(locations = locations) {
                navigator.navigate(LocationDetailsScreenDestination(it))
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
                errorMessage = stringResource(id = R.string.no_locations_error),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

/**
 * Displays the list of [locations] and provides the [navigatesToDetail] function for the [LocationItem].
 */
@Composable
private fun LocationList(locations: List<LocationAPI>, navigatesToDetail: (LocationAPI) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        itemsIndexed(items = locations) { _, item ->
            LocationItem(location = item, navigatesToDetail = navigatesToDetail)
        }
    }
}

/**
 * Display the filter dialog.
 *
 * Retrieves [MainViewModel] for accessing its methods.
 */
@Composable
private fun FilterDialog(viewModel: MainViewModel, modifier: Modifier) {
    val typeFilter by viewModel.locationType()
    val dimensionFilter by viewModel.locationDimension()

    Dialog(onDismissRequest = viewModel::closeFilterDialog) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextField(
                        value = typeFilter,
                        onValueChange = viewModel::updateLocationsTypeFilter,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.type),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        textStyle = MaterialTheme.typography.labelMedium,
                        modifier = modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = dimensionFilter,
                        onValueChange = viewModel::updateDimensionsFilter,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.dimension),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        textStyle = MaterialTheme.typography.labelMedium,
                        modifier = modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Button(onClick = viewModel::closeFilterDialog) {
                            Text(text = "Close")
                        }
                        Button(onClick = viewModel::resetLocationsFilters) {
                            Text(text = "Reset")
                        }
                        Button(onClick = {
                            viewModel.refreshLocations()
                            viewModel.closeFilterDialog()
                        }) {
                            Text(text = "Filter")
                        }
                    }
                }
            }
        }
    }
}