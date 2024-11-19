package com.idfm.hackathon.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.idfm.hackathon.data.models.TransportationLine


@Composable
fun TransportationLineIcon(modifier: Modifier = Modifier, line: TransportationLine = TransportationLine.METRO_1) {

    val imageName = line.toImageResName()
    val context = LocalContext.current
    val resourceId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
    if (resourceId != 0) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun TransportationLineIconPreview() {
    TransportationLineIcon(Modifier, TransportationLine.METRO_8)
}