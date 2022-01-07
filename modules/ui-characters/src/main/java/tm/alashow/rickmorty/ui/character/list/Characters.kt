/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import tm.alashow.rickmorty.ui.character.components.CharacterRow
import tm.alashow.rickmorty.ui.character.R
import tm.alashow.ui.components.AppTopBar
import tm.alashow.ui.components.ErrorBox
import tm.alashow.ui.components.FullScreenLoading
import tm.alashow.ui.isInitialLoading
import tm.alashow.ui.items
import tm.alashow.ui.loadingMoreRow
import tm.alashow.ui.simpleClickable
import tm.alashow.ui.theme.AppTheme

@Composable
fun Characters() {
    Characters(viewModel = hiltViewModel())
}

@Composable
private fun Characters(
    viewModel: CharactersViewModel,
) {
    val listState = rememberLazyListState()
    val viewState by rememberFlowWithLifecycle(viewModel.state)
    val pagingCharacters = rememberFlowWithLifecycle(viewModel.charactersPager).collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            val coroutine = rememberCoroutineScope()
            CharactersTopBar(
                onTitleClick = {
                    coroutine.launch {
                        listState.scrollToItem(0)
                    }
                }
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
    onTitleClick: () -> Unit = {},
) {
    AppTopBar(
        title = stringResource(R.string.characters_title),
        titleModifier = Modifier.simpleClickable(onClick = onTitleClick),
    )
}

@Composable
private fun CharactersContent(
    listState: LazyListState,
    pagingCharacters: LazyPagingItems<Character>,
) {
    if (pagingCharacters.isInitialLoading())
        FullScreenLoading()
    else
        LazyColumn(
            state = listState,
            contentPadding = LocalScaffoldPadding.current,
        ) {
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
    val isEmptyAndHasError = pagingCharacters.itemCount == 0 && pagingCharacters.loadState.refresh is LoadState.Error
    val error = (pagingCharacters.loadState.refresh as? LoadState.Error)?.error
    if (isEmptyAndHasError)
        ErrorBox(
            message = error.toUiMessage().asString(LocalContext.current),
            onRetryClick = pagingCharacters::retry,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.specs.padding),
        )
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.charactersList(
    pagingCharacters: LazyPagingItems<Character>
) {
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
