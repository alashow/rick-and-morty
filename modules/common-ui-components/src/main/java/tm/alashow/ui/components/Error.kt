/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tm.alashow.ui.theme.AppTheme

@Composable
fun EmptyErrorBox(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.error_empty_title),
    message: String = stringResource(R.string.error_empty),
    retryLabel: String = stringResource(R.string.error_retry),
    retryVisible: Boolean = true,
    onRetryClick: () -> Unit = {},
) {
    ErrorBox(
        title = title,
        message = message,
        retryLabel = retryLabel,
        retryVisible = retryVisible,
        onRetryClick = onRetryClick,
        modifier = modifier
    )
}

@Preview
@Composable
fun ErrorBox(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.error_title),
    message: String = stringResource(R.string.error_unknown),
    retryLabel: String = stringResource(R.string.error_retry),
    retryVisible: Boolean = true,
    onRetryClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingTiny),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = AppTheme.specs.paddingLarge)
        ) {
            Image(
                painter = painterResource(id = R.drawable.morty_face),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = AppTheme.specs.paddingLarge)
            )
            Text(title, style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
            Text(message)
            if (retryVisible)
                TextRoundedButton(
                    onClick = onRetryClick,
                    text = retryLabel,
                    modifier = Modifier.padding(top = AppTheme.specs.padding)
                )
        }
    }
}
