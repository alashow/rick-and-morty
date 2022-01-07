/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import tm.alashow.ui.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> SelectableDropdownMenu(
    items: List<T>,
    selectedItem: T,
    onItemSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    selectedItems: Set<T> = setOf(selectedItem),
    multipleSelectionsLabel: @Composable (Set<T>) -> String = { _ -> stringResource(R.string.dropdown_selected_multiple) },
    itemLabelMapper: @Composable (T) -> String = { it.toString().replace("_", " ") },
    itemPrefixMapper: @Composable (RowScope.(T) -> Unit)? = null,
    itemSuffixMapper: @Composable (RowScope.(T) -> Unit)? = null,
    subtitles: List<String?>? = null,
    dropdownText: String? = null,
    maxDropdownHeight: Dp = Dp.Unspecified,
    selectedDropdownIconColor: Color = LocalContentColor.current,
    leadingIcon: ImageVector? = null,
    leadingIconColor: Color = LocalContentColor.current,
    border: BorderStroke? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val dropIcon = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown

    Column {
        OutlinedButton(
            onClick = { expanded = !expanded },
            colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current),
            contentPadding = PaddingValues(AppTheme.specs.paddingSmall),
            border = border,
            modifier = modifier
        ) {
            if (leadingIcon != null) {
                Icon(
                    painter = rememberVectorPainter(leadingIcon),
                    contentDescription = null,
                    modifier = Modifier.width(AppTheme.specs.iconSizeTiny),
                    tint = leadingIconColor,
                )
                Spacer(Modifier.width(AppTheme.specs.paddingSmall))
            }
            Text(
                text = when {
                    dropdownText != null -> dropdownText
                    else -> when (selectedItems.size) {
                        0 -> "    "
                        1 -> itemLabelMapper(selectedItems.first())
                        else -> multipleSelectionsLabel(selectedItems)
                    }
                },
            )
            Spacer(Modifier.width(AppTheme.specs.paddingSmall))
            Icon(
                painter = rememberVectorPainter(dropIcon),
                contentDescription = null,
                tint = if (selectedItem == null) LocalContentColor.current else selectedDropdownIconColor,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .heightIn(max = maxDropdownHeight)
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = !expanded
                        onItemSelect(item)
                    },
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val contentColor = if (item in selectedItems) MaterialTheme.colors.secondary else MaterialTheme.colors.onBackground
                            CompositionLocalProvider(LocalContentColor provides contentColor) {
                                if (itemPrefixMapper != null)
                                    itemPrefixMapper(item)
                                Text(itemLabelMapper(item))
                                if (itemSuffixMapper != null)
                                    itemSuffixMapper(item)
                            }
                        }

                        if (subtitles != null) {
                            val subtitle = subtitles[index]
                            if (subtitle != null)
                                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                                    Text(text = subtitle, style = MaterialTheme.typography.caption)
                                }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoreVerticalIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    contentDescription: String? = null
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = rememberVectorPainter(Icons.Default.MoreVert),
            contentDescription = contentDescription,
        )
    }
}
