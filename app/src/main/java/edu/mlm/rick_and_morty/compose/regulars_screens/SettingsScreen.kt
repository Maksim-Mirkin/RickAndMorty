package edu.mlm.rick_and_morty.compose.regulars_screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import edu.mlm.rick_and_morty.R
import edu.mlm.rick_and_morty.view_model.FavoriteCharacterViewModel

/**
 * Provides users with the ability to clean database and read some info about app.
 */
@Destination
@Composable
fun SettingsScreen(viewModel: FavoriteCharacterViewModel) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isAboutUsClicked by remember { mutableStateOf(false) }
        Button(
            onClick = viewModel::clearCharacters,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(
                text = "Clean favorite characters",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(175.dp)
            )
        }
        AnimatedContent(
            targetState = isAboutUsClicked,
            content = {
                Button(
                    onClick = { isAboutUsClicked = !isAboutUsClicked }
                ) {
                    if (it) {
                        Text(
                            text = stringResource(R.string.about_us_extended),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(175.dp)
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.about_us),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(175.dp)
                        )
                    }
                }
            },
            label = ""
        )
    }
}