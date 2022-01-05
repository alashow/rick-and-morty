/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import tm.alashow.navigation.screens.RootScreen
import tm.alashow.rickmorty.R

internal val HomeNavigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        screen = RootScreen.Characters,
        labelResId = R.string.characters_title,
        contentDescriptionResId = R.string.characters_title,
        iconImageVector = Icons.Outlined.People,
        selectedImageVector = Icons.Filled.People,
    ),
)
