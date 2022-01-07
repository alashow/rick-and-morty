package tm.alashow.rickmorty.ui.character.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.material.placeholder
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.ui.components.CoverImage
import tm.alashow.ui.components.shimmer
import tm.alashow.ui.theme.AppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun CharacterRow(
    modifier: Modifier = Modifier,
    character: Character = Character(),
    rowHeight: Dp = 100.dp,
    isPlaceholder: Boolean = false,
    elevation: Dp = 8.dp,
    onClick: () -> Unit = {},
) {
    val loadingModifier = Modifier.placeholder(
        visible = isPlaceholder,
        highlight = shimmer(),
    )
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(rowHeight)
            .padding(
                horizontal = AppTheme.specs.paddingSmall,
                vertical = AppTheme.specs.paddingSmall,
            ),
        elevation = elevation,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.padding)
        ) {
            CoverImage(
                data = character.imageUrl,
                imageModifier = loadingModifier,
                shape = RectangleShape,
                elevation = 0.dp,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
                modifier = Modifier.padding(vertical = AppTheme.specs.padding)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.h6,
                    modifier = loadingModifier,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CharacterStatusDot(character, loadingModifier)
                    Text(
                        text = character.description,
                        maxLines = 3,
                        style = MaterialTheme.typography.body1,
                        modifier = loadingModifier,
                    )
                }
            }
        }
    }
}
