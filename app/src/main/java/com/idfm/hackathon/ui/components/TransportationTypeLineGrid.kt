package com.idfm.hackathon.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idfm.hackathon.data.models.TransportationLine
import com.idfm.hackathon.data.models.TransportationType


@Composable
fun TransportationTypeLineGrid(modifier: Modifier, type: TransportationType) {

    val lines = remember {
        TransportationLine.entries.filter {
            it.type == type
        }
    }

    val listState = rememberLazyGridState()

    Box(modifier = Modifier.fillMaxWidth()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(64.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            state = listState
        ) {

            itemsIndexed(items = lines, key = { _, item -> item.line }) { _, line ->
                TransportationLineIcon(line = line)
            }
        }
    }
}


@Preview
@Composable
fun TransportationTypeLineGridMetroPreview() {
    TransportationTypeLineGrid(Modifier, TransportationType.METRO)
}

@Preview
@Composable
fun TransportationTypeLineGridRerPreview() {
    TransportationTypeLineGrid(Modifier, TransportationType.RER)
}

@Preview
@Composable
fun TransportationTypeLineGridTramPreview() {
    TransportationTypeLineGrid(Modifier, TransportationType.TRAM)
}