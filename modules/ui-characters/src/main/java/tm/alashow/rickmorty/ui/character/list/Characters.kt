/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.google.accompanist.insets.ui.Scaffold
import kotlinx.coroutines.launch
import tm.alashow.base.util.asString
import tm.alashow.base.util.toUiMessage
import tm.alashow.common.compose.LocalScaffoldState
import tm.alashow.common.compose.rememberFlowWithLifecycle
import tm.alashow.navigation.LocalNavigator
import tm.alashow.navigation.screens.LeafScreen
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.domain.models.errors.EmptyResultException
import tm.alashow.rickmorty.ui.character.R
import tm.alashow.rickmorty.ui.character.components.CharacterRow
import tm.alashow.ui.*
import tm.alashow.ui.components.*
import tm.alashow.ui.theme.AppTheme

@Composable
fun Characters() {
    Characters(viewModel = hiltViewModel())
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Characters(
    viewModel: CharactersViewModel,
) {
    val pagingCharacters = rememberFlowWithLifecycle(viewModel.charactersPager).collectAsLazyPagingItems()
    val viewState by rememberFlowWithLifecycle(viewModel.state)
    val coroutine = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val scrollToItem = { i: Int -> coroutine.launch { listState.scrollToItem(i) } }

    Scaffold(
        topBar = {
            CharactersTopBar(
                titleModifier = Modifier.combinedClickable(
                    // scroll to the top on click,
                    onClick = { scrollToItem(0) },
                    // to the end on double click
                    onDoubleClick = { scrollToItem(pagingCharacters.itemCount) },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ),
                filtersContent = {
                    CharactersFiltersRow(
                        filterOptions = viewState.filterOptions,
                        filters = viewState.filters,
                        onStatusSelect = viewModel::setStatusFilter,
                        onSpeciesSelect = viewModel::setSpeciesFilter,
                        onTypeSelect = viewModel::setTypeFilter,
                        onGenderSelect = viewModel::setGenderFilter,
                        onOriginSelect = viewModel::setOriginFilter,
                        onLocationSelect = viewModel::setLocationFilter,
                    )
                },
                hasActiveFilters = viewState.filters.hasFilters,
                clearFilters = viewModel::clearFilters,
            )
        }
    ) {
        CharactersContent(
            pagingCharacters = pagingCharacters,
            listState = listState
        )
        CharactersListError(pagingCharacters = pagingCharacters)
        CharactersListSnackbarErrors(
            error = viewState.error,
            onRetry = pagingCharacters::retry,
            onDismiss = viewModel::clearError
        )
    }
}

@Composable
private fun CharactersTopBar(
    filtersContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    hasActiveFilters: Boolean = false,
    clearFilters: () -> Unit = {},
) {
    var filterVisible by remember { mutableStateOf(false) }
    Column(modifier) {
        AppTopBar(
            title = stringResource(R.string.characters_title),
            titleModifier = titleModifier,
            isSmallTitleStyle = filterVisible,
            actions = {
                IconButton(
                    onClick = { filterVisible = !filterVisible },
                    onLongClick = clearFilters,
                ) {
                    Icon(
                        if (filterVisible) Icons.Filled.Close else Icons.Filled.FilterAlt,
                        tint = if (hasActiveFilters) MaterialTheme.colors.secondary else LocalContentColor.current,
                        contentDescription = stringResource(R.string.characters_filters),
                    )
                }
            }
        )
        if (filterVisible)
            filtersContent()
    }
}

@Composable
private fun CharactersContent(
    listState: LazyListState,
    pagingCharacters: LazyPagingItems<Character>,
) {
    if (pagingCharacters.isInitialLoading()) FullScreenLoading()
    else LazyColumn(state = listState, contentPadding = LocalScaffoldPadding.current) {
        charactersList(pagingCharacters)
    }
}

@Composable
private fun CharactersListSnackbarErrors(
    error: Throwable?,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    scaffoldState: ScaffoldState = LocalScaffoldState.current,
) {
    val message = error.toUiMessage().asString(LocalContext.current)
    val retryLabel = stringResource(R.string.error_retry)
    LaunchedEffect(error) {
        error?.let {
            if (error !is EmptyResultException)
                when (scaffoldState.snackbarHostState.showSnackbar(message, retryLabel, SnackbarDuration.Indefinite)) {
                    SnackbarResult.ActionPerformed -> onRetry()
                    SnackbarResult.Dismissed -> onDismiss()
                }
        }
    }
}

@Composable
private fun CharactersListError(
    pagingCharacters: LazyPagingItems<Character>,
    modifier: Modifier = Modifier
) {
    val boxModifier = modifier
        .fillMaxSize()
        .padding(horizontal = AppTheme.specs.padding)
    val isEmpty = pagingCharacters.itemCount == 0
    val hasError = pagingCharacters.loadState.refresh is LoadState.Error
    val error = (pagingCharacters.loadState.refresh as? LoadState.Error)?.error
    if (isEmpty && hasError && error !is EmptyResultException) {
        ErrorBox(
            message = error.toUiMessage().asString(LocalContext.current),
            onRetryClick = pagingCharacters::retry,
            modifier = boxModifier,
        )
    } else if (!pagingCharacters.isLoading() && isEmpty)
        Delayed {
            EmptyErrorBox(
                boxModifier,
                retryVisible = false
            )
        }
}

private fun LazyListScope.charactersList(pagingCharacters: LazyPagingItems<Character>) {
    items(pagingCharacters, key = { _, c -> c.id }) { character ->
        val navigator = LocalNavigator.current
        CharacterRow(
            character = character ?: Character(),
            isPlaceholder = character == null,
            onClick = {
                character?.let {
                    navigator.navigate(LeafScreen.CharacterDetails.buildRoute(it.id))
                }
            },
        )
    }
    loadingMoreRow(pagingCharacters)
    item { Spacer(Modifier.height(AppTheme.specs.padding)) }
}
