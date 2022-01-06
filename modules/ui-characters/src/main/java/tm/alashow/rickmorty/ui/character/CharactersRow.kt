/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.material.placeholder
import tm.alashow.domain.extensions.capitalize
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.ui.components.CoverImage
import tm.alashow.ui.components.shimmer
import tm.alashow.ui.theme.AppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun CharacterRow(
    modifier: Modifier = Modifier,
    character: Character = Character(),
    rowHeight: Dp = 100.dp,
    isPlaceholder: Boolean = false,
    elevation: Dp = 8.dp,
    onClick: () -> Unit = {},
) {
    val loadingModifier = Modifier.placeholder(
        visible = isPlaceholder,
        highlight = shimmer(),
    )
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(rowHeight)
            .padding(
                horizontal = AppTheme.specs.paddingSmall,
                vertical = AppTheme.specs.paddingSmall,
            ),
        elevation = elevation,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.padding)
        ) {
            CoverImage(
                data = character.imageUrl,
                imageModifier = loadingModifier,
                shape = RectangleShape,
                elevation = 0.dp,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
                modifier = Modifier.padding(vertical = AppTheme.specs.padding)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.h6,
                    modifier = loadingModifier,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CharacterStatusDot(character, loadingModifier)
                    Text(
                        text = character.description,
                        maxLines = 3,
                        style = MaterialTheme.typography.body1,
                        modifier = loadingModifier,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun CharacterColumn(
    modifier: Modifier = Modifier,
    character: Character = Character(),
    columnWidth: Dp = 160.dp,
    isPlaceholder: Boolean = false,
    elevation: Dp = 8.dp,
    onClick: () -> Unit = {},
) {
    val loadingModifier = Modifier.placeholder(
        visible = isPlaceholder,
        highlight = shimmer(),
    )
    Card(
        onClick = onClick,
        modifier = modifier
            .width(columnWidth)
            .padding(
                horizontal = AppTheme.specs.paddingSmall,
                vertical = AppTheme.specs.paddingSmall,
            ),
        elevation = elevation,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.specs.padding)
        ) {
            CoverImage(
                data = character.imageUrl,
                imageModifier = loadingModifier,
                shape = RectangleShape,
                elevation = 0.dp,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
                modifier = Modifier.padding(
                    start = AppTheme.specs.padding,
                    end = AppTheme.specs.padding,
                    bottom = AppTheme.specs.padding,
                )
            ) {
                Text(
                    text = character.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2,
                    modifier = loadingModifier,
                )
                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.caption) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CharacterStatusDot(character, loadingModifier)
                        Text(
                            text = character.status.capitalize(),
                            modifier = loadingModifier,
                        )
                    }
                }
            }
        }
    }
}

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
