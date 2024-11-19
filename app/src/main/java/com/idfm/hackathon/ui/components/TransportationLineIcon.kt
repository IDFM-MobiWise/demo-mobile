package com.idfm.hackathon.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idfm.hackathon.data.models.LineStatus
import com.idfm.hackathon.data.models.TransportationLine


@Composable
fun TransportationLineIcon(
    modifier: Modifier = Modifier,
    line: TransportationLine = TransportationLine.METRO_1,
    lineStatus: LineStatus = LineStatus.NORMAL,
    onClick: (TransportationLine) -> Unit
) {

    val imageName = line.toImageResName()
    val context = LocalContext.current
    val resourceId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
    if (resourceId != 0) {
        Box(
            modifier = Modifier
                .aspectRatio(1f) // Ensures the box is square
                .clip(RoundedCornerShape(14.dp)) // Rounded corners
                .border(lineStatus.thickness, lineStatus.color, RoundedCornerShape(14.dp)) // Outline border
                .clickable { onClick(line) }
                .padding(2.dp)
                .then(modifier)
        ) {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(4.dp)
                    .clickable {
                        onClick(line)
                    }
                    .then(modifier))

            if (lineStatus != LineStatus.NORMAL) {
                Icon(
                    painter = painterResource(id = lineStatus.resId),
                    contentDescription = "Status",
                    modifier = Modifier.size(24.dp).align(Alignment.BottomEnd),
                    tint = lineStatus.color,
                )
            }
        }
    }
}

@Preview
@Composable
fun TransportationLineIconPreview() {
    TransportationLineIcon(modifier = Modifier,
        line = TransportationLine.METRO_8,
        lineStatus = LineStatus.CLOSED,
        ) { _ -> }
}