/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import tm.alashow.common.compose.LocalScaffoldState
import tm.alashow.common.compose.rememberFlowWithLifecycle
import tm.alashow.navigation.NavigatorHost
import tm.alashow.navigation.rememberBottomSheetNavigator
import tm.alashow.rickmorty.ui.home.Home
import tm.alashow.rickmorty.ui.snackbar.SnackbarMessagesListener
import tm.alashow.ui.ThemeViewModel
import tm.alashow.ui.theme.AppTheme
import tm.alashow.ui.theme.DefaultThemeDark

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun RickAndMortyApp(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
) {
    CompositionLocalProvider(
        LocalScaffoldState provides scaffoldState,
    ) {
        ProvideWindowInsets(consumeWindowInsets = false) {
            AppCore {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                navController.navigatorProvider += bottomSheetNavigator
                ModalBottomSheetLayout(bottomSheetNavigator) {
                    Home(navController)
                }
            }
        }
    }
}

@Composable
private fun AppCore(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    SnackbarMessagesListener()
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(DefaultThemeDark)
    AppTheme(theme = themeState) {
        NavigatorHost {
            content()
        }
    }
}
