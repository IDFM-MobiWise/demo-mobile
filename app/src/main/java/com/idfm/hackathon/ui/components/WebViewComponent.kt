package com.idfm.hackathon.ui.components

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComponent(modifier: Modifier, url: String) {
    Box(modifier) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient() // Ensures links open in the same WebView
                settings.javaScriptEnabled = true // Enable JavaScript if needed
                loadUrl(url) // Load the initial URL
            }
        })
    }
}