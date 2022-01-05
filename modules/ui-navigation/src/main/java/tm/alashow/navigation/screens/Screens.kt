/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.navigation.screens

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import tm.alashow.Config
import tm.alashow.rickmorty.domain.entities.CharacterId

const val CHARACTER_ID_KEY = "character_id"

interface Screen {
    val route: String
}

val ROOT_SCREENS = listOf(RootScreen.Characters)

sealed class RootScreen(
    override val route: String,
    val startScreen: LeafScreen,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) : Screen {
    object Characters : RootScreen("characters_root", LeafScreen.Characters())
}

sealed class LeafScreen(
    override val route: String,
    open val root: RootScreen? = null,
    protected open val path: String = "",
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) : Screen {

    fun createRoute(root: RootScreen? = null) = when (val rootPath = (root ?: this.root)?.route) {
        is String -> "$rootPath/$route"
        else -> route
    }

    data class Characters(
        override val route: String = "characters/"
    ) : LeafScreen(
        route,
        RootScreen.Characters,
    ) {
        companion object {
            fun buildUri() = "${Config.BASE_URL}characters".toUri()
        }
    }

    data class CharacterDetails(
        override val route: String = "characters/{$CHARACTER_ID_KEY}",
        override val root: RootScreen = RootScreen.Characters
    ) : LeafScreen(
        route, root,
        arguments = listOf(
            navArgument(CHARACTER_ID_KEY) {
                type = NavType.StringType
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "${Config.BASE_URL}characters/{$CHARACTER_ID_KEY}"
            }
        )
    ) {
        companion object {
            fun buildRoute(id: CharacterId, root: RootScreen = RootScreen.Characters) = "${root.route}/characters/$id"
            fun buildUri(id: CharacterId) = "${Config.BASE_URL}characters/$id".toUri()
        }
    }
}

fun NavGraphBuilder.composableScreen(screen: LeafScreen, content: @Composable (NavBackStackEntry) -> Unit) {
    composable(screen.createRoute(), screen.arguments, screen.deepLinks, content)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.bottomSheetScreen(screen: LeafScreen, content: @Composable ColumnScope.(NavBackStackEntry) -> Unit) =
    bottomSheet(screen.createRoute(), screen.arguments, screen.deepLinks, content)

// https://stackoverflow.com/a/64961032/2897341
@Composable
inline fun <reified VM : ViewModel> NavBackStackEntry.scopedViewModel(navController: NavController): VM {
    val parentId = destination.parent!!.id
    val parentBackStackEntry = navController.getBackStackEntry(parentId)
    return hiltViewModel(parentBackStackEntry)
}
