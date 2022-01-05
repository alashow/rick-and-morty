/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CharacterDetail() {
    CharacterDetail(viewModel = hiltViewModel())
}

@Composable
private fun CharacterDetail(
    viewModel: CharacterDetailViewModel,
) {

}
