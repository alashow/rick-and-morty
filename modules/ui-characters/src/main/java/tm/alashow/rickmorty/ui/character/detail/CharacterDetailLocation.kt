package tm.alashow.rickmorty.ui.character.detail

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tm.alashow.domain.extensions.blankIfZero
import tm.alashow.navigation.LocalNavigator
import tm.alashow.navigation.Navigator
import tm.alashow.navigation.screens.LeafScreen
import tm.alashow.rickmorty.domain.entities.CharacterId
import tm.alashow.rickmorty.domain.entities.Location
import tm.alashow.rickmorty.ui.character.R
import tm.alashow.rickmorty.ui.character.components.CharacterColumn
import tm.alashow.ui.theme.AppTheme

@Composable
internal fun CharacterLocationSection(
    label: String,
    residentsLabel: String,
    currentCharacterId: CharacterId,
    location: Location,
    isDetailsLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(horizontal = AppTheme.specs.padding)) {
        CharacterDetailLabel(label)
        CharacterLocation(
            location = location,
            isDetailsLoading = isDetailsLoading
        )
    }
    CharacterLocationResidents(
        currentCharacterId = currentCharacterId,
        location = location,
        label = residentsLabel,
        isDetailsLoading = isDetailsLoading,
    )
}

@Composable
internal fun CharacterLocation(
    location: Location,
    isDetailsLoading: Boolean
) {
    with(location) {
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
            label = stringResource(R.string.character_location_numberOfResidents),
            value = residents.size.blankIfZero(),
            isDetailsLoading = isDetailsLoading,
        )
    }
}

@Composable
internal fun CharacterLocationResidents(
    currentCharacterId: CharacterId,
    location: Location,
    label: String,
    isDetailsLoading: Boolean,
    modifier: Modifier = Modifier,
    navigator: Navigator = LocalNavigator.current,
) {
    val residents = location.residentsCharacters.filterNot { it.id == currentCharacterId }
    if (residents.isNotEmpty())
        CharacterDetailLabel(
            label = label,
            modifier = modifier.padding(start = AppTheme.specs.padding),
        )
    AnimatedVisibility(
        visible = !isDetailsLoading,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        LazyRow(contentPadding = PaddingValues(horizontal = AppTheme.specs.paddingSmall)) {
            items(residents, key = { it.id }) {
                CharacterColumn(
                    character = it,
                    onClick = { navigator.navigate(LeafScreen.CharacterDetails.buildRoute(it.id)) },
                )
            }
        }
    }
}

