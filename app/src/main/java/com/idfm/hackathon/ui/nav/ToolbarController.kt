package com.idfm.hackathon.ui.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.idfm.hackathon.ui.MenuItemSelectedState
import com.idfm.hackathon.ui.UpState
import kotlinx.coroutines.flow.StateFlow


sealed class ActionMenuItem(
    @StringRes val description: Int,
    val key: Any,
    var icon: ImageVector? = null,
    var iconRes: Int? = null,
    val badgeValue: String? = null
) {

    sealed class WithIcon(
        @StringRes description: Int,
        key: Any,
        icon: ImageVector? = null,
        iconRes: Int? = null,
        badgeValue: String? = null,
    ) : ActionMenuItem(
        description = description,
        key = key,
        icon = icon,
        iconRes = iconRes,
        badgeValue = badgeValue
    ) {

        sealed class AlwaysShown(
            @StringRes description: Int,
            key: Any,
            icon: ImageVector?,
            iconRes: Int?,
            badgeValue: String? = null
        ) :
            WithIcon(description, key, icon, iconRes, badgeValue) {
            data class Normal(
                @StringRes private val _description: Int,
                private val _key: Any,
                private val _icon: ImageVector? = null,
                private val _iconRes: Int? = null,
                private val _badgeValue: String? = null
            ) : AlwaysShown(
                description = _description,
                key = _key,
                icon = _icon,
                iconRes = _iconRes,
                badgeValue = _badgeValue
            )

            data class Search(@StringRes val hint: Int) :
                AlwaysShown(
                    description = hint,
                    key = "search",
                    icon = Icons.Default.Search,
                    iconRes = null,
                    badgeValue = null
                )
        }

        data class ShownIfRoom(
            @StringRes private val _description: Int,
            private val _key: Any,
            private val _icon: ImageVector? = null,
            private val _iconRes: Int? = null,
            private val _badgeValue: String? = null
        ) : WithIcon(
            description = _description,
            key = _key,
            icon = _icon,
            iconRes = _iconRes,
            badgeValue = _badgeValue
        )
    }

    data class NeverShown(
        @StringRes private val _description: Int,
        private val _key: Any,
        private val _icon: ImageVector? = null,
        private val _iconRes: Int? = null,
        private val _badgeValue: String? = null
    ) :
        ActionMenuItem(
            description = _description,
            key = _key,
            icon = _icon,
            iconRes = _iconRes,
            badgeValue = _badgeValue
        )
}

data class ToolbarDescription(
    val name: String? = null,
    var maxVisibleItems: Int = 3,
    val allMenuItems: List<ActionMenuItem> = listOf()
) {
    // Returns a pair of list.
    // First one are the items to show.
    // Second are the items that will be in the context menu (called overflow)
    fun splitItems(): Pair<List<ActionMenuItem.WithIcon>, List<ActionMenuItem>> {

        val alwaysShownItems: MutableList<ActionMenuItem.WithIcon> =
            allMenuItems.filterIsInstance<ActionMenuItem.WithIcon.AlwaysShown>().toMutableList()

        // Sort alwaysShownItems in a way that Search is always first
        alwaysShownItems.sortBy { it !is ActionMenuItem.WithIcon.AlwaysShown.Search }

        val ifRoomItems: MutableList<ActionMenuItem.WithIcon.ShownIfRoom> =
            allMenuItems.filterIsInstance<ActionMenuItem.WithIcon.ShownIfRoom>().toMutableList()

        val overflowItems = allMenuItems.filterIsInstance<ActionMenuItem.NeverShown>()

        val hasOverflow = overflowItems.isNotEmpty() ||
                (alwaysShownItems.size + ifRoomItems.size) > maxVisibleItems

        val nbVisible = maxVisibleItems - (if (hasOverflow) 1 else 0)

        val listVisible = mutableListOf<ActionMenuItem.WithIcon>()
        val listOverflow = mutableListOf<ActionMenuItem>()

        // First, for the always show.
        alwaysShownItems.forEach {
            if (listVisible.size < nbVisible) {
                listVisible.add(it)
            } else {
                listOverflow.add(it)
            }
        }

        ifRoomItems.forEach {
            if (listVisible.size < nbVisible) {
                listVisible.add(it)
            } else {
                listOverflow.add(it)
            }
        }

        listOverflow.addAll(overflowItems)

        return Pair(listVisible, listOverflow)
    }
}


interface ToolbarController {
    fun onUp(navHostController: NavHostController)
    fun canUp(can: Boolean)
    fun canUp(can: Boolean, key: String)
    fun upRequested(): StateFlow<UpState>

    fun getToolbarDescriptionState(): StateFlow<ToolbarDescription>
    fun menuItemSelectedState(): StateFlow<MenuItemSelectedState>
    fun onMenuItemClick(item: ActionMenuItem)
    fun onSearchMenuExpanded(expanded: Boolean)
    fun onSearch(query: String?)
    fun consumeMenuItem()
}