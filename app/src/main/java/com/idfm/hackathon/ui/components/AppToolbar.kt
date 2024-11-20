package com.idfm.hackathon.ui.components

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.idfm.hackathon.R
import com.idfm.hackathon.ui.genericcomponents.BadgeBackgroundStyle
import com.idfm.hackathon.ui.genericcomponents.BadgeImageComponent
import com.idfm.hackathon.ui.genericcomponents.BadgeTextStyle
import com.idfm.hackathon.ui.nav.ActionMenuItem
import com.idfm.hackathon.ui.nav.Screen
import com.idfm.hackathon.ui.nav.ToolbarController
import com.idfm.hackathon.ui.nav.ToolbarDescription
import com.idfm.hackathon.ui.theme.HackathonIdFMTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    toolbarController: ToolbarController? = null,
    toolbarDescription: ToolbarDescription? = null
) {
    val searchExpandedScreenMap = rememberSaveable {
        mutableMapOf<Screen, Boolean>()
    }

    if (!searchExpandedScreenMap.contains(currentScreen)) {
        searchExpandedScreenMap[currentScreen] = false
    }

    var searchExpanded by remember {
        mutableStateOf(searchExpandedScreenMap[currentScreen] ?: false)
    }
    TopAppBar(
        title = {
            if (!searchExpanded)
                Text(if (currentScreen.resId > 0) stringResource(id = currentScreen.resId) else currentScreen.text)
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            navigationIconContentColor = MaterialTheme.colorScheme.onSecondary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
        ),
        modifier = modifier,
        navigationIcon = {
            if (!searchExpanded && canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            var overflowExpanded by remember {
                mutableStateOf(false)
            }

            toolbarDescription?.splitItems()?.let { split ->
                // Show first the icons
                split.first.forEach {

                    if (it is ActionMenuItem.WithIcon.AlwaysShown.Search) {
                        SearchBox(
                            hint = stringResource(id = it.hint),
                            startExpanded = searchExpanded,
                            initialSearch = "",
                            onExpanded = { searchExp ->
                                searchExpandedScreenMap[currentScreen] = searchExp
                                searchExpanded = searchExp
                                toolbarController?.onSearchMenuExpanded(searchExp)
                            },
                            onSearch = { textFieldValue ->
                                val exp = true
                                searchExpandedScreenMap[currentScreen] = exp
                                if (searchExpanded != exp) {
                                    searchExpanded = exp
                                }
                                toolbarController?.onSearch(textFieldValue)
                            })
                    } else {
                        IconButton(onClick = {
                            toolbarController?.onMenuItemClick(it)
                        }) {
                            MenuIcon(it)
                        }
                    }
                }

                // If overflow, the "..." vertical item
                if (split.second.isNotEmpty()) {
                    IconButton(onClick = {
                        overflowExpanded = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "more options",
                        )
                    }

                    // If the context menu is open, show the items
                    DropdownMenu(
                        expanded = overflowExpanded,
                        onDismissRequest = { overflowExpanded = false }) {
                        split.second.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(stringResource(id = item.description))
                                },
                                onClick = {
                                    toolbarController?.onMenuItemClick(item)
                                    overflowExpanded = false
                                })
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MenuIcon(menuIcon: ActionMenuItem.WithIcon) {
    val icon = menuIcon.icon
    val iconRes = menuIcon.iconRes

    if (menuIcon.badgeValue != null) {
        BadgeImageComponent(
            drawableRes = iconRes,
            imageVector = menuIcon.icon,
            text = menuIcon.badgeValue,
            badgeBackgroundStyle = BadgeBackgroundStyle(
                badgeBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                badgeOutlineColor = MaterialTheme.colorScheme.secondary,
                iconTint = MaterialTheme.colorScheme.onSecondary
            ),
            badgeTextStyle = BadgeTextStyle(
                textColor = MaterialTheme.colorScheme.primary
            )
        )
    } else {
        if (icon != null) {
            Icon(
                tint = MaterialTheme.colorScheme.onSecondary,
                imageVector = icon,
                contentDescription = stringResource(id = menuIcon.description)
            )
        } else if (iconRes != null) {
            Icon(
                tint = MaterialTheme.colorScheme.onSecondary,
                painter = painterResource(iconRes),
                contentDescription = stringResource(id = menuIcon.description)
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MenuIconPreview() {
    HackathonIdFMTheme {
        MenuIcon(
            menuIcon = ActionMenuItem.WithIcon.AlwaysShown.Normal(
                _description = R.string.common_button_delete,
                _key = "some key",
                _icon = Icons.Filled.Delete
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MenuIconPreviewWithCounter() {
    HackathonIdFMTheme {
        MenuIcon(
            menuIcon = ActionMenuItem.WithIcon.AlwaysShown.Normal(
                _description = R.string.common_button_delete,
                _key = "some key",
                _icon = Icons.Filled.Call,
                _badgeValue = "1"
            )
        )
    }
}