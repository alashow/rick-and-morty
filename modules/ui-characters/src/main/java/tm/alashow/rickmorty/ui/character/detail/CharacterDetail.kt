/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import tm.alashow.common.compose.rememberFlowWithLifecycle
import tm.alashow.domain.extensions.orBlank
import tm.alashow.navigation.LocalNavigator
import tm.alashow.navigation.Navigator
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.ui.character.CharacterStatusDot
import tm.alashow.rickmorty.ui.character.R
import tm.alashow.ui.components.*
import tm.alashow.ui.theme.AppTheme

@Composable
fun CharacterDetail() {
    CharacterDetail(viewModel = hiltViewModel())
}

@Composable
private fun CharacterDetail(
    viewModel: CharacterDetailViewModel,
    navigator: Navigator = LocalNavigator.current,
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
    Scaffold(
        topBar = {
            AppTopBar(
                title = viewState.character?.name ?: "",
                navigationIcon = {
                    AppBarNavigationIcon(onClick = navigator::goBack)
                }
            )
        },
    ) {
        CharacterDetailSwipeRefresh(
            onRefresh = viewModel::refresh,
        ) {
            if (viewState.isLoaded) {
                val character = viewState.characterDetails() ?: viewState.character
                if (character != null)
                    CharactersDetailContent(
                        character = character,
                        isDetailsLoading = viewState.isDetailsLoading
                    )
            } else FullScreenLoading()
        }
    }
}

@Composable
private fun CharacterDetailSwipeRefresh(
    onRefresh: () -> Unit,
    isRefreshing: Boolean = false,
    content: @Composable () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = onRefresh,
        indicatorPadding = LocalScaffoldPadding.current,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true
            )
        },
        content = content,
    )
}

@Composable
private fun CharactersDetailContent(
    character: Character,
    modifier: Modifier = Modifier,
    isDetailsLoading: Boolean = false,
    contentPadding: PaddingValues = LocalScaffoldPadding.current,
) {
    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        contentPadding = contentPadding,
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.specs.padding,
                        vertical = AppTheme.specs.paddingSmall,
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CoverImage(
                        character.imageUrl,
                        shape = CircleShape,
                        size = 250.dp,
                    )
                }

                Spacer(Modifier.height(AppTheme.specs.padding))

                CharacterDetailRow(
                    label = stringResource(R.string.character_status),
                    value = character.status,
                    separator = {
                        CharacterStatusDot(character)
                    }
                )
                CharacterDetailRow(
                    label = stringResource(R.string.character_species),
                    value = character.species
                )
                CharacterDetailRow(
                    label = stringResource(R.string.character_type),
                    value = character.type
                )

                Text(
                    stringResource(R.string.character_location),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        top = AppTheme.specs.padding,
                        bottom = AppTheme.specs.paddingSmall
                    )
                )

                with(character.location) {
                    CharacterDetailRow(
                        label = stringResource(R.string.character_location_name),
                        value = name
                    )
                    CharacterDetailRow(
                        label = stringResource(R.string.character_location_type),
                        value = type,
                        isDetailsLoading = isDetailsLoading,
                    )
                    CharacterDetailRow(
                        label = stringResource(R.string.character_location_dimension),
                        value = dimension,
                        isDetailsLoading = isDetailsLoading,
                    )
                    CharacterDetailRow(
                        label = stringResource(R.string.character_location_residents),
                        value = residents.size.toString().takeIf { it != "0" }.orBlank(),
                        isDetailsLoading = isDetailsLoading,
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isDetailsLoading: Boolean = false,
    separator: @Composable RowScope.() -> Unit = {},
    labelStyle: TextStyle = MaterialTheme.typography.body1,
    valueStyle: TextStyle = MaterialTheme.typography.body1,
) {
    val hasValue = value.isNotBlank()
    val loadingModifier = Modifier.placeholder(
        visible = !hasValue && isDetailsLoading,
        highlight = shimmer(),
    )
    if (hasValue || isDetailsLoading)
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(vertical = AppTheme.specs.paddingTiny)
                .then(loadingModifier)
        ) {
            Text(
                text = "$label:",
                style = labelStyle,
                fontWeight = FontWeight.Bold
            )
            separator()
            Text(
                text = value.replaceFirstChar { it.uppercase() },
                style = valueStyle
            )
        }
}
