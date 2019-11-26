package uk.co.ijhdev.trtlware.workers

import android.content.Context
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Seperot on 28/03/2018.
 */

class PhoneBatteryWorker{

    fun getBatteryLevel(context: Context) : String {
        val bm = context.getSystemService(AppCompatActivity.BATTERY_SERVICE) as BatteryManager
        val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        return "$batLevel %"
    }
}