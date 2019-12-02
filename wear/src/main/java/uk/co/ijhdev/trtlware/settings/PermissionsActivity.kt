package uk.co.ijhdev.trtlware.settings

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.wear.ambient.AmbientModeSupport
import uk.co.ijhdev.trtlware.R

/**
 * Created by Seperot on 02/12/2019.
 */
class PermissionsActivity : FragmentActivity(),
    AmbientModeSupport.AmbientCallbackProvider {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AmbientModeSupport.attach(this)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, ControlsPrefFragment())
            .commit()
    }

    class ControlsPrefFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.prefs_control, rootKey)
        }
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
        return MyAmbientCallback()
    }

    private inner class MyAmbientCallback : AmbientModeSupport.AmbientCallback()
}