package com.idfm.hackathon.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MapViewComponent(modifier: Modifier) {
    Box(modifier) {
        AndroidView(factory = { context ->
            org.maplibre.android.maps.MapView(context).apply {
                getMapAsync { mapboxMap ->
                    mapboxMap.setStyle("https://api.maptiler.com/maps/streets/style")
                }
            }
        })
    }
}