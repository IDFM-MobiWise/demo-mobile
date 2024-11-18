package com.idfm.hackathon.ui.genericcomponents

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idfm.hackathon.R
import com.idfm.hackathon.ui.theme.Dimen
import com.idfm.hackathon.ui.theme.HackathonIdFMTheme

data class BadgeTextStyle(
    val textSize: TextUnit = 10.sp,
    val textColor: Color = Color.White,
)

data class BadgeBackgroundStyle(
    val badgeSize: Dp = 14.dp,
    val badgeBackgroundColor: Color = Color.Black,
    val badgeOutlineColor: Color = Color.White,
    val badgeOutlineThickness: Dp = Dimen.strokeThickness,
    val iconTint:Color = Color.Black
)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BadgeImageComponent(
    @DrawableRes drawableRes: Int? = null,
    imageVector: ImageVector? = null,
    text: String,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    badgeTextStyle: BadgeTextStyle = BadgeTextStyle(
        textColor = MaterialTheme.colorScheme.primary
    ),
    badgeBackgroundStyle: BadgeBackgroundStyle = BadgeBackgroundStyle(
        badgeBackgroundColor = MaterialTheme.colorScheme.secondary,
        badgeOutlineColor = MaterialTheme.colorScheme.onSecondary,
        iconTint = MaterialTheme.colorScheme.secondary
    ),
    badgePosition: Alignment = Alignment.TopEnd
) {

    BoxWithConstraints(modifier = modifier) {
        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                "",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .then(iconModifier),
                tint = badgeBackgroundStyle.iconTint
            )
        } else if (drawableRes != null) {
            Icon(
                painter = painterResource(id = drawableRes),
                "",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .then(iconModifier),
                tint = badgeBackgroundStyle.iconTint
            )
        }

        TextWithCircle(
            Modifier.align(badgePosition),
            textModifier,
            text,
            badgeTextStyle,
            badgeBackgroundStyle,
        )
    }
}


@Composable
fun TextWithCircle(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    letter: String,
    badgeTextStyle: BadgeTextStyle = BadgeTextStyle(),
    badgeBackgroundStyle: BadgeBackgroundStyle = BadgeBackgroundStyle(),
) {
    Box(
        modifier = Modifier
            .size(badgeBackgroundStyle.badgeSize)
            .then(modifier),
        contentAlignment = Alignment.Center

    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = badgeBackgroundStyle.badgeBackgroundColor, // Circle color
                radius = size.width / 2, // Radius of the circle
                center = center,
                style = Fill)

            drawCircle(
                color = badgeBackgroundStyle.badgeOutlineColor, // Circle color
                radius = size.width / 2, // Radius of the circle
                center = center,
                style = Stroke(width = badgeBackgroundStyle.badgeOutlineThickness.toPx()))
        }

        Text(modifier = textModifier,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            ),
            text = letter,
            color = badgeTextStyle.textColor,
            fontSize = badgeTextStyle.textSize,
            textAlign = TextAlign.Center
        )
    }
}


/**
 ************************
 * PREVIEW SECTION
 * **********************
 **/
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0XFFFFFFFF)
@Composable
fun BadgeImageComponentPreviewWithRes() {
    HackathonIdFMTheme {
        BadgeImageComponent(drawableRes = R.drawable.baseline_attach_file_24, text = "3")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0XFFFFFFFF)
@Composable
fun BadgeImageComponentPreviewWithImageVector() {
    HackathonIdFMTheme {
        BadgeImageComponent(imageVector = Icons.Default.AddCircle, text = "3")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0XFFFFFFFF)
@Composable
fun BadgeImageComponentSmallTextPreview() {
    HackathonIdFMTheme {
        BadgeImageComponent(
            R.drawable.baseline_attach_file_24,
            text = "3",
            badgeTextStyle = BadgeTextStyle(textSize = 8.sp),
            badgeBackgroundStyle = BadgeBackgroundStyle(badgeSize = 10.dp)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0XFFFFFFFF)
@Composable
fun BadgeImageComponentLargeTextTextPreview() {
    HackathonIdFMTheme {
        BadgeImageComponent(
            R.drawable.baseline_attach_file_24,
            text = "9+",
            badgeTextStyle = BadgeTextStyle(textSize = 10.sp, textColor = Color.Red),
            iconModifier = Modifier
                .size(48.dp)
                .padding(8.dp),
            badgeBackgroundStyle = BadgeBackgroundStyle(badgeSize = 20.dp)
        )
    }
}