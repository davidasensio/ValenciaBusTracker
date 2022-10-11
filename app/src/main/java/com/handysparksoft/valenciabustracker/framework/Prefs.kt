package com.handysparksoft.valenciabustracker.framework

import android.content.Context
import android.content.SharedPreferences

class Prefs constructor(context: Context) {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var isCountingClicks: Boolean
        get() = sharedPrefs.getBoolean(KEY_IS_COUNTING_CLICKS, true)
        set(value) = sharedPrefs.edit().putBoolean(KEY_IS_COUNTING_CLICKS, value).apply()

    var numberOfClicks: Int
        get() = sharedPrefs.getInt(KEY_NUMBER_OF_CLICKS, 0)
        set(value) = sharedPrefs.edit().putInt(KEY_NUMBER_OF_CLICKS, value).apply()

    companion object {
        const val PREFS_FILENAME = "com.handysparksoft.valenciabustracker.framework.prefs"

        private const val KEY_IS_COUNTING_CLICKS = "key_is_counting_clicks"
        private const val KEY_NUMBER_OF_CLICKS = "key_number_of_clicks"
    }
}
