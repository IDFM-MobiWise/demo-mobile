package com.idfm.hackathon.ui.nav

import androidx.annotation.StringRes

enum class Screen(val text: String = "", @StringRes val resId: Int = -1) {
    Home("Home screen"),
    Chat("IDFM Copilote")
    ;
}