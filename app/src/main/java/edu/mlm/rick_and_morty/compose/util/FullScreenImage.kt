package edu.mlm.rick_and_morty.compose.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import coil.compose.rememberAsyncImagePainter

/**
 * Adjusts the image to occupy the entire screen.
 */
@Composable
fun FullScreenImage(
    imageURL: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scrim(onDismiss, Modifier.fillMaxSize())
        ImageWithZoom(imageURL, Modifier.aspectRatio(1f))
    }
}

@Composable
private fun Scrim(onClose: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .pointerInput(onClose) { detectTapGestures { onClose() } }
            .semantics(mergeDescendants = true) {
                contentDescription = "character full size"
                onClick {
                    onClose()
                    true
                }
            }
            .background(Color.DarkGray.copy(alpha = 0.75f))
    )
}

@Composable
private fun ImageWithZoom(imageURL: String, modifier: Modifier = Modifier) {
    var zoomed by remember { mutableStateOf(false) }
    var zoomOffset by remember { mutableStateOf(Offset.Zero) }
    Image(
        painter = rememberAsyncImagePainter(model = imageURL),
        contentDescription = null,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        zoomOffset = if (zoomed) Offset.Zero else
                            calculateOffset(tapOffset, size)
                        zoomed = !zoomed
                    }
                )
            }
            .graphicsLayer {
                scaleX = if (zoomed) 2f else 1f
                scaleY = if (zoomed) 2f else 1f
                translationX = zoomOffset.x
                translationY = zoomOffset.y
            }
            .clip(CircleShape)
    )
}

private fun calculateOffset(tapOffset: Offset, size: IntSize): Offset {
    val offsetX = (-(tapOffset.x - (size.width / 2f)) * 2f)
        .coerceIn(-size.width / 2f, size.width / 2f)
    return Offset(offsetX, 0f)
}