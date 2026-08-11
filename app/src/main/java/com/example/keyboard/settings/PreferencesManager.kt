package com.example.keyboard.settings

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("bangla_pro_keyboard_prefs", Context.MODE_PRIVATE)

    var currentLanguage: String
        get() = prefs.getString(KEY_LANGUAGE, LANG_BANGLA) ?: LANG_BANGLA
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    var hapticFeedbackEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTIC, true)
        set(value) = prefs.edit().putBoolean(KEY_HAPTIC, value).apply()

    var keySoundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND, false)
        set(value) = prefs.edit().putBoolean(KEY_SOUND, value).apply()

    var keyboardTheme: String
        get() = prefs.getString(KEY_THEME, THEME_DARK) ?: THEME_DARK
        set(value) = prefs.edit().putString(KEY_THEME, value).apply()

    var keyboardHeightScale: Float
        get() = prefs.getFloat(KEY_HEIGHT_SCALE, 1.0f)
        set(value) = prefs.edit().putFloat(KEY_HEIGHT_SCALE, value).apply()

    var spacebarSwipeCursorEnabled: Boolean
        get() = prefs.getBoolean(KEY_SPACEBAR_SWIPE, true)
        set(value) = prefs.edit().putBoolean(KEY_SPACEBAR_SWIPE, value).apply()

    var showNumberRow: Boolean
        get() = prefs.getBoolean(KEY_SHOW_NUMBER_ROW, false)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_NUMBER_ROW, value).apply()

    companion object {
        const val KEY_LANGUAGE = "key_language"
        const val KEY_HAPTIC = "key_haptic"
        const val KEY_SOUND = "key_sound"
        const val KEY_THEME = "key_theme"
        const val KEY_HEIGHT_SCALE = "key_height_scale"
        const val KEY_SPACEBAR_SWIPE = "key_spacebar_swipe"
        const val KEY_SHOW_NUMBER_ROW = "key_show_number_row"

        const val LANG_BANGLA = "bn"
        const val LANG_ENGLISH = "en"

        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_AMOLED = "amoled"
    }
}
