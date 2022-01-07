/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.ui.theme.AppTheme

@Composable
@Preview
fun CharacterStatusDot(
    modifier: Modifier = Modifier,
    character: Character = Character(),
) {
    CharacterStatusDot(
        isAlive = character.isAlive,
        isDead = character.isDead,
        modifier = modifier,
    )
}

@Composable
fun CharacterStatusDot(
    isAlive: Boolean,
    isDead: Boolean,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier
            .size(AppTheme.specs.paddingSmall)
            .clip(CircleShape)
            .background(
                when {
                    isAlive -> Color.Green
                    isDead -> Color.Red
                    else -> Color.Gray
                }
            )
    )
}
