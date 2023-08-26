package edu.mlm.rick_and_morty.compose.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

/**
 * Display error message to the user with given [errorMessage].
 */
@Composable
fun ErrorMessage(errorMessage: String, modifier: Modifier = Modifier) {
    Text(
        text = errorMessage,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}