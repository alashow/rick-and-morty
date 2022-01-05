/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.base.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class DarkModePreference { ON, OFF, AUTO }
enum class ColorPalettePreference {
    Default,
    Black,
}

/**
 * This should be located in app module, but for some ungodly reason kotlinx-serialization plugin isn't working for app module.
 */
@Parcelize
@Serializable
data class ThemeState(
    @SerialName("darkMode")
    var darkModePreference: DarkModePreference = DarkModePreference.AUTO,
    @SerialName("colorPalette")
    var colorPalettePreference: ColorPalettePreference = ColorPalettePreference.Default
) : Parcelable {
    val isDarkMode get() = darkModePreference == DarkModePreference.ON
}
