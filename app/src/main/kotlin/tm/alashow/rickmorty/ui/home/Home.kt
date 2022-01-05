/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ui.Scaffold
import tm.alashow.common.compose.LocalScaffoldState
import tm.alashow.navigation.screens.RootScreen
import tm.alashow.rickmorty.ui.AppNavigation
import tm.alashow.rickmorty.ui.currentScreenAsState
import tm.alashow.ui.DismissableSnackbarHost

val HomeBottomNavigationHeight = 56.dp

@Composable
internal fun Home(
    navController: NavHostController,
    scaffoldState: ScaffoldState = LocalScaffoldState.current,
) {
    val selectedTab by navController.currentScreenAsState()
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { DismissableSnackbarHost(it) },
        bottomBar = {
            HomeBottomNavigation(
                selectedTab = selectedTab,
                onNavigationSelected = { selected -> navController.selectRootScreen(selected) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) {
        AppNavigation(navController = navController)
    }
}

internal fun NavController.selectRootScreen(tab: RootScreen) {
    navigate(tab.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true

        val currentEntry = currentBackStackEntry
        val currentDestination = currentEntry?.destination
        val isReselected =
            currentDestination?.hierarchy?.any { it.route == tab.route } == true
        val isRootReselected =
            currentDestination?.route == tab.startScreen.route

        if (isReselected && !isRootReselected) {
            navigateUp()
        }
    }
}
