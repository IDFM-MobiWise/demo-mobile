package com.idfm.hackathon.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.idfm.hackathon.ui.nav.ActionMenuItem
import com.idfm.hackathon.ui.nav.ToolbarController
import com.idfm.hackathon.ui.nav.ToolbarDescription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CanUp(
    val consolidatedCanUp: Boolean,
    val keyStates: Map<String, Boolean>,
    val notification: Int
)

sealed class UpState {
    data class UpRequested(val canUp: CanUp) : UpState() {
        fun onlyFor(key: String, state: Boolean = false): Boolean {
            return canUp.keyStates.count {
                it.value == state
            } == 1 && canUp.keyStates.containsKey(key)
        }
    }

    object Void : UpState()
}

sealed class MenuItemSelectedState {
    data class ItemSelected(
        val key: Any, // Whatever you want in there. Used to identify the menu item in the view.
        val uid: Long = _uid++ // To insure each menu click is unique, and triggers always despite same item
    ) : MenuItemSelectedState()

    object None : MenuItemSelectedState()

    companion object {
        private var _uid: Long = 0
    }
}


open class BaseViewModel : ViewModel(), ToolbarController {
    private var _notification = 0   // This is to decide when to force a notification
    private val _canUpMap = mutableMapOf<String, Boolean>()
    private var _canUp = UpState.Void
    private val _upRequested = MutableStateFlow<UpState>(_canUp)

    private var _toolbarDescription: ToolbarDescription = ToolbarDescription()
    private val _toolbarDescriptionState = MutableStateFlow(_toolbarDescription)

    private var _menuItemSelected: MenuItemSelectedState = MenuItemSelectedState.None
    private val _menuItemSelectedState = MutableStateFlow(_menuItemSelected)


    override fun onUp(navHostController: NavHostController) {
        if (canDoUp()) {
            navHostController.popBackStack()
        } else {
            notifyViewAboutUp()
        }
    }

    override fun canUp(can: Boolean) {
        internalCanUp(can, "default")
    }

    override fun canUp(can: Boolean, key: String) {
        internalCanUp(can, key)
    }

    override fun upRequested() = _upRequested

    override fun getToolbarDescriptionState(): StateFlow<ToolbarDescription> {
        return _toolbarDescriptionState
    }

    override fun menuItemSelectedState(): StateFlow<MenuItemSelectedState> {
        return _menuItemSelectedState
    }

    override fun consumeMenuItem() {
        _menuItemSelectedState.value = MenuItemSelectedState.None
    }

    override fun onMenuItemClick(item: ActionMenuItem) {
        // Override on demand
        Log.d(TAG, "Click on menu item: ${item.description}")
        _menuItemSelected = MenuItemSelectedState.ItemSelected(item.key)
        _menuItemSelectedState.value = _menuItemSelected
    }

    override fun onSearchMenuExpanded(expanded: Boolean) {
        Log.d(TAG, "Search menu expanded: $expanded")
    }

    override fun onSearch(query: String?) {
        Log.d(TAG, "Search query: $query")
    }

    protected fun setToolbarItems(menuItems: List<ActionMenuItem>) {
        _toolbarDescription = _toolbarDescription.copy(allMenuItems = menuItems)
        _toolbarDescriptionState.value = _toolbarDescription
    }

    protected fun notifyViewAboutUp() {
        _upRequested.value =
            UpState.UpRequested(
                CanUp(
                    consolidatedCanUp = false,
                    keyStates = _canUpMap,
                    notification = ++_notification
                )
            )
    }

    private fun internalCanUp(can: Boolean, key: String) {
        _canUpMap[key] = can
        _upRequested.value =
            UpState.Void // _canUp.copy(consolidatedCanUp = false, keyStates = _canUpMap)
    }

    private fun canDoUp(): Boolean {
        return _canUpMap.values.all { it }
    }

    companion object {
        val TAG = BaseViewModel::class.java.simpleName.toString()
    }
}