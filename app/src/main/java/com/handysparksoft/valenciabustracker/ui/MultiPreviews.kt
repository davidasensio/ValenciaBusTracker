package com.handysparksoft.valenciabustracker.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotations that represents different configurations.
 */
@Preview(name = "01. Light Theme", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "02. Dark Theme", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class ThemePreviews

@Preview(name = "03. Large font", group = "font scales", fontScale = 1.5f)
@Preview(name = "04. Small font", group = "font scales", fontScale = 0.5f)
annotation class FontScalePreviews

@Preview(name = "05. English", group = "locales", locale = "en")
@Preview(name = "06. Spanish", group = "locales", locale = "es")
annotation class LocalePreviews

@Preview(name = "07. Phone", group = "devices", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "08. Landscape", group = "devices", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "09. Foldable", group = "devices", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "10. Tablet", group = "devices", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
annotation class DevicePreviews

@Preview(name = "01. Light Theme", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "02. Dark Theme", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "03. Spanish", group = "locales", locale = "es")
@Preview(name = "04. Large font", group = "font scales", fontScale = 1.5f)
@Preview(name = "05. Landscape", group = "devices", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
annotation class CombinedPreviews

@ThemePreviews
@FontScalePreviews
@LocalePreviews
@DevicePreviews
annotation class AllPreviews
