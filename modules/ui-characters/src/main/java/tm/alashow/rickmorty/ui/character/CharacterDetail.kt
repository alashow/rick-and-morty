/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import tm.alashow.base.util.asString
import tm.alashow.base.util.extensions.orNA
import tm.alashow.base.util.toUiMessage
import tm.alashow.common.compose.rememberFlowWithLifecycle
import tm.alashow.domain.models.Fail
import tm.alashow.domain.models.Success
import tm.alashow.domain.models.Uninitialized
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.ui.components.CoverImage
import tm.alashow.ui.components.ErrorBox
import tm.alashow.ui.components.FullScreenLoading
import tm.alashow.ui.theme.AppTheme

@Composable
fun CharacterDetail() {
    CharacterDetail(viewModel = hiltViewModel())
}

@Composable
private fun CharacterDetail(
    viewModel: CharacterDetailViewModel,
) {
    val asyncDetails by rememberFlowWithLifecycle(viewModel.characterDetailsState).collectAsState(Uninitialized)

    // TODO: remove crossfade
    Crossfade(asyncDetails) {
        when (val details = it) {
            is Success -> CharacterDetailContent(details())
            is Fail -> CharacterLoadingError(details, onRetry = viewModel::refresh)
            else -> FullScreenLoading()
        }
    }
}

@Composable
private fun CharacterDetailContent(character: Character) {
    Column(
        Modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .padding(AppTheme.specs.padding)
            .verticalScroll(rememberScrollState())
    ) {

        CoverImage(
            character.imageUrl,
            shape = CircleShape,
            size = 250.dp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(AppTheme.specs.padding))

        CharacterDetailRow(
            label = stringResource(R.string.character_name),
            value = character.name
        )
        CharacterDetailRow(
            label = stringResource(R.string.character_status),
            value = character.status
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
                value = type
            )
            CharacterDetailRow(
                label = stringResource(R.string.character_location_dimension),
                value = dimension
            )
            CharacterDetailRow(
                label = stringResource(R.string.character_location_residents),
                value = residents.size.toString()
            )
        }
    }
}

@Composable
private fun CharacterDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    labelStyle: TextStyle = MaterialTheme.typography.body1,
    valueStyle: TextStyle = MaterialTheme.typography.body1,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier.padding(vertical = AppTheme.specs.paddingTiny)
    ) {
        Text(
            "$label:", style = labelStyle,
            fontWeight = FontWeight.Bold
        )
        Text(
            value.orNA(ifBlank = true).replaceFirstChar { it.uppercase() },
            style = valueStyle
        )
    }
}

@Composable
private fun CharacterLoadingError(
    details: Fail<Character>,
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
