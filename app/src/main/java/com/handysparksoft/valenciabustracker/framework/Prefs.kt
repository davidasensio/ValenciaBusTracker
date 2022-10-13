package com.handysparksoft.valenciabustracker.framework

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Prefs constructor(context: Context) {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var isCountingClicks by sharedPrefs.delegate(defaultValue = true)
    var numberOfClicks by sharedPrefs.delegate(defaultValue = 0)

    companion object {
        const val PREFS_FILENAME = "com.handysparksoft.valenciabustracker.framework.prefs"
    }
}

private fun <Type> SharedPreferences.delegate(
    defaultValue: Type,
    key: String? = null
): ReadWriteProperty<Any, Type> {
    return object : ReadWriteProperty<Any, Type> {

        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: Any, property: KProperty<*>): Type {
            val finalKey = key ?: property.name
            val result: Any? = when (defaultValue) {
                is String -> getString(finalKey, defaultValue)
                is Boolean -> getBoolean(finalKey, defaultValue)
                is Int -> getInt(finalKey, defaultValue)
                is Long -> getLong(finalKey, defaultValue)
                is Float -> getFloat(finalKey, defaultValue)
                else -> throw IllegalArgumentException("This type cannot be read from Preferences")
            }
            return result as Type
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Type) {
            with(edit()) {
                val finalKey = key ?: property.name
                when (value) {
                    is String -> putString(finalKey, value)
                    is Boolean -> putBoolean(finalKey, value)
                    is Int -> putInt(finalKey, value)
                    is Long -> putLong(finalKey, value)
                    is Float -> putFloat(finalKey, value)
                    else -> throw IllegalArgumentException("This type cannot be saved into Preferences")
                }
            }.apply()
        }
    }
}
