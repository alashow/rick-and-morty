/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.debounce
import tm.alashow.domain.extensions.toFloat
import tm.alashow.ui.components.ProgressIndicator
import tm.alashow.ui.theme.AppTheme

/**
 * Paginated items with keys support.
 * @see androidx.paging.compose.items
 */
inline fun <T : Any> LazyListScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    noinline key: ((index: Int, item: T) -> Any) = { i, _ -> i },
    crossinline itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(
        lazyPagingItems.itemCount,
        { index ->
            when (val item = lazyPagingItems.peek(index)) {
                item != null -> key(index, item)
                else -> index
            }
        }
    ) { index ->
        itemContent(lazyPagingItems[index])
    }
}

@Composable
fun LazyListState.rememberIsScrollingState(
    debounceMillis: Long = 200L,
    transform: (Boolean) -> Float = { it.not().toFloat() }
): State<Float> {
    val isScrolling = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        snapshotFlow { isScrollInProgress }
            .debounce(debounceMillis)
            .collect { isScrolling.animateTo(transform(it)) }
    }
    return isScrolling.asState()
}

fun LazyPagingItems<*>.isSourceLoading() = loadState.source.refresh == LoadState.Loading
fun LazyPagingItems<*>.isMediatorLoading() = loadState.mediator?.refresh == LoadState.Loading
fun LazyPagingItems<*>.isLoading() = isSourceLoading() || isMediatorLoading()
fun LazyPagingItems<*>.isInitialLoading() = (isSourceLoading() || isMediatorLoading()) && itemCount == 0
fun LazyPagingItems<*>.isRefreshing() = (isSourceLoading() || isMediatorLoading()) && itemCount > 0
fun LazyPagingItems<*>.isAppendLoading() = loadState.append == LoadState.Loading
fun LazyPagingItems<*>.isLoadingMore() = !isInitialLoading() && (isMediatorLoading() || isAppendLoading())

fun <T : Any> LazyListScope.loadingMoreRow(pagingItems: LazyPagingItems<T>, height: Dp = 100.dp, modifier: Modifier = Modifier) {
    loadingMore(pagingItems, modifier.height(height))
}

fun <T : Any> LazyListScope.loadingMore(pagingItems: LazyPagingItems<T>, modifier: Modifier = Modifier) {
    val isLoadingMore = pagingItems.isLoadingMore()
    if (isLoadingMore)
        item("loading_more") {
            Box(
                modifier
                    .fillMaxWidth()
                    .padding(AppTheme.specs.padding)
            ) {
                ProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
}
