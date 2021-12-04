package de.mm20.launcher2.ui.screens.settings

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RadioButtonChecked
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.mm20.launcher2.ktx.isAtLeastApiLevel
import de.mm20.launcher2.preferences.dataStore
import de.mm20.launcher2.ui.R
import de.mm20.launcher2.ui.component.preferences.ColorPreference
import de.mm20.launcher2.ui.component.preferences.Preference
import de.mm20.launcher2.ui.component.preferences.PreferenceCategory
import de.mm20.launcher2.ui.component.preferences.PreferenceScreen
import de.mm20.launcher2.ui.theme.colors.*
import de.mm20.launcher2.ui.theme.wallpaperColorsAsState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import de.mm20.launcher2.preferences.Settings.AppearanceSettings.ColorScheme as ColorSchemeOption

@Composable
fun SettingsColorsScreen() {
    val context = LocalContext.current
    val dataStore = context.dataStore
    val scope = rememberCoroutineScope()
    val customColors by customColorsAsState()
    val colorScheme by remember {
        dataStore.data.map {
            it.appearance.colorScheme
        }
    }.collectAsState(initial = ColorSchemeOption.Default)
    PreferenceScreen(title = stringResource(id = R.string.preference_screen_colors)) {
        item {
            val schemes = mutableListOf(
                ColorSchemeItem(
                    ColorSchemeOption.Default,
                    DefaultColorPalette(),
                    stringResource(id = R.string.preference_colors_default)
                ),
                ColorSchemeItem(
                    ColorSchemeOption.MM20,
                    MM20ColorPalette(),
                    stringResource(id = R.string.preference_colors_mm20)
                ),
                ColorSchemeItem(
                    ColorSchemeOption.BlackAndWhite,
                    BlackWhiteColorPalette(),
                    stringResource(id = R.string.preference_colors_bw)
                )
            )
            if (isAtLeastApiLevel(Build.VERSION_CODES.S)) {
                schemes.add(
                    ColorSchemeItem(
                        ColorSchemeOption.MaterialYou,
                        SystemColorPalette(context),
                        stringResource(id = R.string.preference_colors_mdyou)
                    )
                )
            }
            if (isAtLeastApiLevel(Build.VERSION_CODES.O_MR1)) {
                val wallpaperColors by wallpaperColorsAsState()
                schemes.add(
                    ColorSchemeItem(
                        ColorSchemeOption.Wallpaper,
                        WallpaperColorPalette(wallpaperColors),
                        stringResource(id = R.string.preference_colors_wallpaper)
                    )
                )
            }
            schemes.add(
                ColorSchemeItem(
                    ColorSchemeOption.Custom,
                    CustomColorPalette(customColors),
                    stringResource(id = R.string.preference_colors_custom)
                )
            )
            PreferenceCategory {
                for (scheme in schemes) {
                    Preference(
                        title = scheme.label,
                        icon = if (colorScheme == scheme.value) Icons.Rounded.RadioButtonChecked else Icons.Rounded.RadioButtonUnchecked,
                        controls = {
                            ColorSchemePreview(scheme.colorPalette)
                        },
                        onClick = {
                            scope.launch {
                                dataStore.updateData {
                                    it.toBuilder()
                                        .setAppearance(
                                            it.appearance.toBuilder().setColorScheme(scheme.value)
                                        )
                                        .build()
                                }
                            }
                        }
                    )
                }
            }
        }
        if (colorScheme == ColorSchemeOption.Custom) {
            item {
                PreferenceCategory(title = stringResource(R.string.preference_category_custom_colors)) {
                    ColorPreference(
                        title = "Neutral1",
                        value = customColors.neutral1,
                        onValueChanged = { newValue ->
                            scope.launch {
                                dataStore.updateData {
                                    it.toBuilder().setAppearance(
                                        it.appearance.toBuilder().setCustomColors(
                                            it.appearance.customColors.toBuilder()
                                                .setNeutral1(newValue.toArgb())
                                        )
                                    ).build()
                                }
                            }
                        }
                    )
                    ColorPreference(
                        title = "Neutral2",
                        value = customColors.neutral2,
                        onValueChanged = { newValue ->
                            scope.launch {
                                dataStore.updateData {
                                    it.toBuilder().setAppearance(
                                        it.appearance.toBuilder().setCustomColors(
                                            it.appearance.customColors.toBuilder()
                                                .setNeutral2(newValue.toArgb())
                                        )
                                    ).build()
                                }
                            }
                        }
                    )
                    ColorPreference(
                        title = "Accent1",
                        value = customColors.accent1,
                        onValueChanged = { newValue ->
                            scope.launch {
                                dataStore.updateData {
                                    it.toBuilder().setAppearance(
                                        it.appearance.toBuilder().setCustomColors(
                                            it.appearance.customColors.toBuilder()
                                                .setAccent1(newValue.toArgb())
                                        )
                                    ).build()
                                }
                            }
                        }
                    )
                    ColorPreference(
                        title = "Accent2",
                        value = customColors.accent2,
                        onValueChanged = { newValue ->
                            scope.launch {
                                dataStore.updateData {
                                    it.toBuilder().setAppearance(
                                        it.appearance.toBuilder().setCustomColors(
                                            it.appearance.customColors.toBuilder()
                                                .setAccent2(newValue.toArgb())
                                        )
                                    ).build()
                                }
                            }
                        }
                    )
                    ColorPreference(
                        title = "Accent3",
                        value = customColors.accent3,
                        onValueChanged = { newValue ->
                            scope.launch {
                                dataStore.updateData {
                                    it.toBuilder().setAppearance(
                                        it.appearance.toBuilder().setCustomColors(
                                            it.appearance.customColors.toBuilder()
                                                .setAccent3(newValue.toArgb())
                                        )
                                    ).build()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorSchemePreview(colorPalette: ColorPalette) {
    val isDark = !androidx.compose.material.MaterialTheme.colors.isLight
    val neutral1 = if (isDark) colorPalette.neutral.shade20 else colorPalette.neutral.shade90
    val neutral2 = if (isDark) colorPalette.neutralVariant.shade20 else colorPalette.neutralVariant.shade90
    val accent1 = if (isDark) colorPalette.primary.shade70 else colorPalette.primary.shade50
    val accent2 = if (isDark) colorPalette.secondary.shade70 else colorPalette.secondary.shade50
    val accent3 = if (isDark) colorPalette.tertiary.shade70 else colorPalette.tertiary.shade50
    Box(
        modifier = Modifier.height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .background(neutral1)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .background(neutral2)
            )
        }

        Row(
            modifier = Modifier.height(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(accent1)
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(16.dp)
                    .background(accent2)
            )
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(accent3)
            )
        }

    }
}

private data class ColorSchemeItem(
    val value: ColorSchemeOption,
    val colorPalette: ColorPalette,
    val label: String,
)