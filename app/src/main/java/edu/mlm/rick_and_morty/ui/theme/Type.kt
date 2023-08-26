package edu.mlm.rick_and_morty.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import edu.mlm.rick_and_morty.R

val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = FontFamily(
            Font(
                R.font.roboto_italic,
                weight = FontWeight.Bold,
                style = FontStyle.Italic
            )
        ),
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(
            Font(
                R.font.roboto_bold,
                weight = FontWeight.Bold,
            )
        ),
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        fontSize = 32.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(
            Font(
                R.font.roboto_bold,
                weight = FontWeight.Bold,
            )
        ),
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        fontSize = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(
            Font(
                R.font.roboto_bold,
                weight = FontWeight.Bold,
            )
        ),
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(
            Font(
                R.font.roboto_bold,
                weight = FontWeight.Normal,
            )
        ),
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(
            Font(
                R.font.roboto_black,
                weight = FontWeight.Normal,
                style = FontStyle.Normal
            )
        ),
        fontWeight = FontWeight.Thin,
        fontStyle = FontStyle.Normal,
        fontSize = 16.sp
    )
)
