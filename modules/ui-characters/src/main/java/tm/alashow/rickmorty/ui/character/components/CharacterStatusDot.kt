/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.ui.theme.AppTheme

@Composable
@Preview
fun CharacterStatusDot(
    character: Character = Character(),
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = when {
            character.isAlive -> Color.Green
            character.isDead -> Color.Red
            else -> Color.Gray
        },
        content = {
            Spacer(Modifier.size(AppTheme.specs.paddingSmall))
        },
    )
}
