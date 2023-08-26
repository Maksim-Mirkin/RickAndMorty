package edu.mlm.rick_and_morty.compose.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.view_model.AbstractViewModel
import edu.mlm.rick_and_morty.compose.regulars_screens.FavoriteCharacterScreen
import edu.mlm.rick_and_morty.compose.regulars_screens.CharacterScreen

/**
 * Display the filter dialog on [CharacterScreen] & [FavoriteCharacterScreen]
 *
 * Retrieves [AbstractViewModel] for accessing its methods.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CharacterFilterDialog(viewModel: AbstractViewModel, modifier: Modifier) {
    var isStatusExpanded by remember { mutableStateOf(false) }
    var isGenderExpanded by remember { mutableStateOf(false) }
    val genderFilter by viewModel.charactersGender()
    val statusFilter by viewModel.charactersStatus()
    val speciesFilter by viewModel.charactersSpecies()
    val typeFilter by viewModel.charactersType()

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
                        value = speciesFilter,
                        onValueChange = viewModel::updateCharactersSpeciesFilter,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.species),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        textStyle = MaterialTheme.typography.labelMedium,
                        modifier = modifier.padding(vertical = 8.dp)
                    )
                    TextField(
                        value = typeFilter,
                        onValueChange = viewModel::updateCharactersTypeFilter,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.type),
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
                    ExposedDropdownMenuBox(
                        expanded = isStatusExpanded,
                        onExpandedChange = { isStatusExpanded = it },
                        modifier = modifier.padding(bottom = 8.dp)
                    ) {
                        TextField(
                            value = statusFilter,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStatusExpanded)
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                            textStyle = MaterialTheme.typography.labelMedium,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.status),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = modifier
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = isStatusExpanded,
                            onDismissRequest = { isStatusExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("") },
                                onClick = {
                                    viewModel.updateCharactersStatusFilter("")
                                    isStatusExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.alive),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {
                                    viewModel.updateCharactersStatusFilter("Alive")
                                    isStatusExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.dead),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {
                                    viewModel.updateCharactersStatusFilter("Dead")
                                    isStatusExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(id = R.string.unknown),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {
                                    viewModel.updateCharactersStatusFilter("Unknown")
                                    isStatusExpanded = false
                                }
                            )
                        }
                    }
                    ExposedDropdownMenuBox(
                        expanded = isGenderExpanded,
                        onExpandedChange = { isGenderExpanded = it },
                        modifier = modifier.padding(bottom = 8.dp)
                    ) {
                        TextField(
                            value = genderFilter,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenderExpanded)
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                            textStyle = MaterialTheme.typography.labelMedium,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.gender),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = modifier
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = isGenderExpanded,
                            onDismissRequest = { isGenderExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("") },
                                onClick = {
                                    viewModel.updateCharacterGenderFilter("")
                                    isGenderExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.male),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {
                                    viewModel.updateCharacterGenderFilter("Male")
                                    isGenderExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.female),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {
                                    viewModel.updateCharacterGenderFilter("Female")
                                    isGenderExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.genderless),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {
                                    viewModel.updateCharacterGenderFilter("Genderless")
                                    isGenderExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.unknown),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {
                                    viewModel.updateCharacterGenderFilter("Unknown")
                                    isGenderExpanded = false
                                }
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Button(onClick = viewModel::closeFilterDialog) {
                            Text(text = "Close")
                        }
                        Button(onClick = viewModel::resetCharactersFilters) {
                            Text(text = "Reset")
                        }
                        Button(onClick = {
                            viewModel.refreshCharacters()
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