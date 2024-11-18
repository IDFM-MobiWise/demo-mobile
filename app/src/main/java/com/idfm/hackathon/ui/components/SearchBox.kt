package com.idfm.hackathon.ui.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.ContentAlpha
import com.idfm.hackathon.ui.theme.HackathonIdFMTheme
import kotlinx.coroutines.delay


@Composable
fun SearchBox(
    modifier: Modifier = Modifier,
    hint: String = "Search...",
    initialSearch: String? = null,
    autoSearchDelay: Long? = 2000,
    startExpanded: Boolean = false,
    onExpanded: ((Boolean) -> Unit),
    onSearch: (String) -> Unit = { _ -> }
) {
    var current by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(initialSearch ?: "")
        )
    }

    var expanded by rememberSaveable {
        mutableStateOf(startExpanded)
    }

    val focusRequester = remember { FocusRequester() }

    // This is to avoid calling onSearch too often
    if (expanded) {
        if (current.text.isNotEmpty() && autoSearchDelay != null) {
            LaunchedEffect(current) {
                Log.d("SearchBox", "Safety delay")
                delay(autoSearchDelay) // Wait for 3 seconds after text update
                onSearch(current.text)
            }
        } else {
            onSearch(current.text)
        }
    }
    onExpanded(expanded)

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(
            visible = expanded,
            modifier = Modifier
                .weight(1.0f)
                .then(modifier),
            enter = slideInHorizontally(initialOffsetX = {
                it
            }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = {
                it
            }) + fadeOut()
        ) {
            TextField(
                modifier = Modifier
                    .focusRequester(focusRequester),
                value = current,
                singleLine = true,
                placeholder = { Text(hint, modifier = Modifier.alpha(ContentAlpha.medium)) },
                onValueChange = {
                    current = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch(current.text)
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onSecondary,
                    focusedTextColor = MaterialTheme.colorScheme.surfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                leadingIcon = {
                    Icon(
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier
                            .alpha(ContentAlpha.medium)
                            .clickable(
                                onClick = {
                                    onSearch(current.text)
                                }
                            ),
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
            )

            // To insure editor has focus when displayed the first time
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }

        IconButton(onClick = {
            if (!expanded) {
                expanded = true
            } else if (current.text.isBlank()) {
                expanded = false
            } else {
                current = TextFieldValue(text = "")
                onSearch(current.text)
            }
        }) {
            val icon = if (expanded) Icons.Default.Close else Icons.Default.Search
            Icon(
                imageVector = icon,
                contentDescription = "Collapsed search"
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SearchBoxPreviewExpanded() {
    HackathonIdFMTheme {
        SearchBox(Modifier, startExpanded = true, onExpanded = {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SearchBoxPreviewNotExpanded() {
    HackathonIdFMTheme {
        SearchBox(Modifier, onExpanded = {})
    }
}