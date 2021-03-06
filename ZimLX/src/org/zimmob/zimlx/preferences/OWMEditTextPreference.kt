/*
 *     This file is part of Lawnchair Launcher.
 *
 *     Lawnchair Launcher is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Lawnchair Launcher is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Lawnchair Launcher.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.zimmob.zimlx.preferences

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.preference.EditTextPreference
import com.android.launcher3.Utilities
import org.zimmob.zimlx.ZimPreferences
import org.zimmob.zimlx.smartspace.OWMWeatherDataProvider

class OWMEditTextPreference(context: Context, attrs: AttributeSet?) :
        EditTextPreference(context, attrs), ZimPreferences.OnPreferenceChangeListener {
    private val isApi = key == "pref_weatherApiKey"
    private val prefs = Utilities.getZimPrefs(context)

    override fun onAttached() {
        super.onAttached()

        updateSummary()
        prefs.addOnPreferenceChangeListener("pref_smartspace_widget_provider", this)
    }

    override fun onDetached() {
        super.onDetached()

        prefs.removeOnPreferenceChangeListener("pref_smartspace_widget_provider", this)
    }

    override fun persistString(value: String?): Boolean {
        if (!TextUtils.isEmpty(value))
            return super.persistString(value).apply { updateSummary() }
        return false
    }

    override fun onValueChanged(key: String, prefs: ZimPreferences, force: Boolean) {
        if (key == "pref_smartspace_widget_provider") {
            isVisible = prefs.weatherProvider == OWMWeatherDataProvider::class.java.name
        }
    }

    private fun updateSummary() {
        summary = if (isApi) {
            prefs.weatherApiKey.replace("[\\d\\w]".toRegex(), "*")
        } else {
            prefs.weatherCity
        }
    }

}
