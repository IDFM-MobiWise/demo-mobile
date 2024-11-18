package com.idfm.hackathon.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import com.example.composeroom.R

class MenuItems {

    companion object {
        fun menuSettings(): ActionMenuItem {
            return ActionMenuItem.WithIcon.AlwaysShown.Normal(
                _description = R.string.menu_item_settings,
                _key = "settings",
                _icon = Icons.Filled.Settings
            )
        }
    }
}