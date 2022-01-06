/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.google.accompanist.insets.ui.Scaffold
import kotlinx.coroutines.launch
import tm.alashow.common.compose.rememberFlowWithLifecycle
import tm.alashow.navigation.LocalNavigator
import tm.alashow.navigation.screens.LeafScreen
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.ui.character.CharactersRow
import tm.alashow.rickmorty.ui.character.R
import tm.alashow.ui.components.AppTopBar
import tm.alashow.ui.components.FullScreenLoading
import tm.alashow.ui.isInitialLoading
import tm.alashow.ui.items
import tm.alashow.ui.loadingMoreRow
import tm.alashow.ui.simpleClickable

@Composable
fun Characters() {
    Characters(viewModel = hiltViewModel())
}

@Composable
private fun Characters(
    viewModel: CharactersViewModel,
) {
    val coroutine = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val pagingCharacters = rememberFlowWithLifecycle(viewModel.charactersPager).collectAsLazyPagingItems()

    Scaffold(
        topBar = {
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

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.charactersList(
    pagingCharacters: LazyPagingItems<Character>
) {
    items(pagingCharacters, key = { _, c -> c.id }) { character ->
        val navigator = LocalNavigator.current
        CharactersRow(
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
}
