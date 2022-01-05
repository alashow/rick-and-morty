/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import tm.alashow.navigation.screens.RootScreen
import tm.alashow.ui.theme.translucentSurfaceColor

@Composable
internal fun HomeBottomNavigation(
    selectedTab: RootScreen,
    onNavigationSelected: (RootScreen) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = HomeBottomNavigationHeight,
) {
    Surface(
        elevation = 8.dp,
        color = translucentSurfaceColor(),
        contentColor = contentColorFor(MaterialTheme.colors.surface),
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(height),
        ) {
            HomeNavigationItems.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        HomeNavigationItemIcon(
                            item = item,
                            selected = selectedTab == item.screen
                        )
                    },
                    label = { Text(text = stringResource(item.labelRes)) },
                    selected = selectedTab == item.screen,
                    onClick = { onNavigationSelected(item.screen) },
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}
