package com.idfm.hackathon.ui.nav

import com.idfm.hackathon.R

class MenuItems {

    companion object {

        fun menuMicOn(active: Boolean): ActionMenuItem {
            return ActionMenuItem.WithIcon.AlwaysShown.Normal(
                _description = R.string.menu_item_mic_on,
                _key = "mic_on_$active",
                _iconRes = if (active) R.drawable.ic_mic_on_active else R.drawable.ic_mic_on_not_active
            )
        }

        fun menuMicOff(): ActionMenuItem {
            return ActionMenuItem.WithIcon.AlwaysShown.Normal(
                _description = R.string.menu_item_mic_off,
                _key = "mic_off",
                _iconRes = R.drawable.ic_mic_off
            )
        }
    }
}