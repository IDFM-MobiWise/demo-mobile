package com.idfm.hackathon.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.idfm.hackathon.ui.components.AppToolbar

@Composable
fun WithTopBar(
    screen: Screen,
    navController: NavHostController,
    toolbarController: ToolbarController? = null,
    content: @Composable () -> Unit
) {
    val desc = if (toolbarController != null) {
        val toolbarDescription by toolbarController.getToolbarDescriptionState()
            .collectAsState(initial = ToolbarDescription())
        toolbarDescription
    } else {
        ToolbarDescription()
    }

    Scaffold(
        topBar = {
            AppToolbar(
                currentScreen = screen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    toolbarController?.onUp(navController) ?: run {
                        navController.navigateUp()
                    }
                },
                toolbarController = toolbarController,
                toolbarDescription = desc
            )
        }
    ) { innerPadding ->
        Column(Modifier.background(Color.White)) {
            Spacer(modifier = Modifier.padding(innerPadding))
            Surface(Modifier.background(color = MaterialTheme.colorScheme.background)) {
                content()
            }
        }

    }
}