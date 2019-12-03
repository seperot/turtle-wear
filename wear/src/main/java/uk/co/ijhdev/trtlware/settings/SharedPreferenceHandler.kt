package uk.co.ijhdev.trtlware.settings

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Seperot on 03/12/2019.
 */
class SharedPreferenceHandler {

  private val prefsName = "_prefs"
  private val tempPref = "_tempPrefs"
  private val coinPref = "_coinPrefs"

  fun getTempType(context: Context?): String? {
    return context?.getSharedPreferences(prefsName, Context.MODE_PRIVATE)?.getString(tempPref, "")
  }

  fun getCoinType(context: Context?): String? {
    return context?.getSharedPreferences(prefsName, Context.MODE_PRIVATE)?.getString(coinPref, "")
  }

  fun saveTempType(context: Context?, date: String?) {
    if (context != null) {
      val editor = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE).edit()
      editor.putString(tempPref, date)
      editor.apply()
    }
  }

  fun saveCoinType(context: Context?, date: String?) {
    if (context != null) {
      val editor = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE).edit()
      editor.putString(coinPref, date)
      editor.apply()
    }
  }
}