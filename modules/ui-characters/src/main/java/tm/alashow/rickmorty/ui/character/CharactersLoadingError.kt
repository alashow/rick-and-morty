/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import tm.alashow.base.util.asString
import tm.alashow.base.util.toUiMessage
import tm.alashow.domain.models.Fail
import tm.alashow.ui.components.ErrorBox
import tm.alashow.ui.theme.AppTheme

@Composable
internal fun CharactersLoadingError(
    details: Fail<*>,
    onRetry: () -> Unit = {},
) {
    ErrorBox(
        message = details.error.toUiMessage().asString(LocalContext.current),
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.specs.padding),
        onRetryClick = onRetry,
    )
}
