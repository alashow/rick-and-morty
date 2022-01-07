package tm.alashow.rickmorty.ui.character.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.placeholder.material.placeholder
import tm.alashow.ui.components.shimmer
import tm.alashow.ui.theme.AppTheme

@Composable
internal fun CharacterDetailRow(
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

@Composable
internal fun CharacterDetailLabel(label: String, modifier: Modifier = Modifier) {
    Text(
        text = label,
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(
            top = AppTheme.specs.padding,
            bottom = AppTheme.specs.paddingSmall
        )
    )
}
