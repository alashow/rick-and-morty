/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import timber.log.Timber
import tm.alashow.ui.simpleClickable
import tm.alashow.ui.theme.AppBarAlphas
import tm.alashow.ui.theme.AppTheme
import tm.alashow.ui.theme.topAppBarTitleStyle
import tm.alashow.ui.theme.topAppBarTitleStyleSmall
import tm.alashow.ui.theme.translucentSurfaceColor

val AppBarHeight = 56.dp
private val AppBarHorizontalPadding = 4.dp

private val TitleInsetWithoutIcon = Modifier.width(16.dp - AppBarHorizontalPadding)
private val TitleIconModifier = Modifier.width(72.dp - AppBarHorizontalPadding)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    titleContent: @Composable () -> Unit = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
    collapsedProgress: Float = 1f,
    titleModifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    isSmallTitleStyle: Boolean = navigationIcon != null,
    filterVisible: Boolean = false,
    filterContent: @Composable RowScope.() -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    val appBarColor = translucentSurfaceColor()
    val backgroundColor = appBarColor.copy(alpha = collapsedProgress.coerceAtMost(AppBarAlphas.translucentBarAlpha()))
    val contentColor = contentColorFor(backgroundColor)
    val titleStyle = if (isSmallTitleStyle) topAppBarTitleStyleSmall() else topAppBarTitleStyle()
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .statusBarsPadding()
                .padding(vertical = if (filterVisible || isSmallTitleStyle) 4.dp else AppTheme.specs.padding)
                .simpleClickable { Timber.d("Caught app bar click through") },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .animateContentSize(spring(dampingRatio = Spring.DampingRatioLowBouncy)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (filterVisible) filterContent()
                else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                            if (navigationIcon == null) Spacer(TitleInsetWithoutIcon)
                            else Box(TitleIconModifier) {
                                navigationIcon.invoke()
                            }
                            Row(titleModifier.alpha(collapsedProgress)) {
                                ProvideTextStyle(titleStyle) {
                                    titleContent()
                                }
                            }
                        }
                    }
                    AppBarActionsRow(actions = actions, modifier = Modifier.padding(end = AppTheme.specs.padding))
                }
            }
        }
    }
}

@Composable
private fun AppBarActionsRow(
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
            content = actions
        )
    }
}

@Composable
fun AppBarNavigationIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = stringResource(R.string.generic_back),
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            rememberVectorPainter(Icons.Filled.ArrowBack),
            contentDescription = contentDescription,
        )
    }
}
