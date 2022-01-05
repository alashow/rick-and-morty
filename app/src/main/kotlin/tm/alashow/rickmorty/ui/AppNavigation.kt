/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import tm.alashow.common.compose.collectEvent
import tm.alashow.navigation.LocalNavigator
import tm.alashow.navigation.NavigationEvent
import tm.alashow.navigation.Navigator
import tm.alashow.navigation.screens.LeafScreen
import tm.alashow.navigation.screens.RootScreen
import tm.alashow.navigation.screens.composableScreen
import tm.alashow.rickmorty.ui.character.CharacterDetail

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    navigator: Navigator = LocalNavigator.current,
) {
    collectEvent(navigator.queue) { event ->
        when (event) {
            is NavigationEvent.Destination -> {
                navController.navigate(event.route)
            }
            is NavigationEvent.Back -> navController.navigateUp()
            else -> Unit
        }
    }

    NavHost(
        navController = navController,
        startDestination = RootScreen.Characters.route,
        modifier = modifier
    ) {
        addCharactersRoot(navController)
    }
}

private fun NavGraphBuilder.addCharactersRoot(navController: NavController) {
    navigation(
        route = RootScreen.Characters.route,
        startDestination = LeafScreen.Characters().createRoute()
    ) {
        addCharacters(navController)
        addCharacterDetails(navController, RootScreen.Characters)
    }
}

private fun NavGraphBuilder.addCharacters(navController: NavController) {
    composableScreen(LeafScreen.Characters()) {
        CharacterDetail()
    }
}

private fun NavGraphBuilder.addCharacterDetails(navController: NavController, root: RootScreen) {
    composableScreen(LeafScreen.CharacterDetails(root = root)) {
        CharacterDetail()
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
internal fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Characters) }
    val rootScreens = listOf(RootScreen.Characters)
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            rootScreens.firstOrNull { rs -> destination.hierarchy.any { it.route == rs.route } }?.let {
                selectedItem.value = it
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}
