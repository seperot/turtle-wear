package uk.co.ijhdev.trtlware.watchface

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.wearable.watchface.CanvasWatchFaceService
import android.support.wearable.watchface.WatchFaceService
import android.support.wearable.watchface.WatchFaceStyle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import uk.co.ijhdev.trtlware.R
import uk.co.ijhdev.trtlware.workers.PhoneBatteryWorker
import uk.co.ijhdev.trtlware.workers.TurtlePriceWorker
import uk.co.ijhdev.trtlware.workers.WeatherWorker
import uk.co.ijhdev.trtlware.workers.WeatherWorker.Companion.tempString
import uk.co.ijhdev.trtlware.workers.WeatherWorker.Companion.weatherString
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Seperot on 26/03/2018.
 */

class TurtleFace : CanvasWatchFaceService() {

  lateinit var watchLayout: View
  var specW: Int = 0
  var specH: Int = 0
  private val displaySize = Point()

  companion object {
    private const val INTERACTIVE_UPDATE_RATE_MS = 1000
    private const val MSG_UPDATE_TIME = 0
  }

  override fun onCreateEngine(): Engine {
    return Engine()
  }

  private class EngineHandler(reference: Engine) : Handler() {
    private val mWeakReference: WeakReference<Engine> = WeakReference(reference)

    override fun handleMessage(msg: Message) {
      val engine = mWeakReference.get()
      if (engine != null) {
        when (msg.what) {
          MSG_UPDATE_TIME -> engine.handleUpdateTimeMessage()
        }
      }
    }
  }

  inner class Engine : CanvasWatchFaceService.Engine() {

    private lateinit var mCalendar: Calendar
    private var mRegisteredTimeZoneReceiver = false
    private var mXOffset: Float = 0F
    private var mYOffset: Float = 0F
    private var mLowBitAmbient: Boolean = false
    private var mBurnInProtection: Boolean = false
    private val mUpdateTimeHandler: Handler = EngineHandler(this)

    private val mTimeZoneReceiver: BroadcastReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        mCalendar.timeZone = TimeZone.getDefault()
        invalidate()
      }
    }

    override fun onCreate(holder: SurfaceHolder) {
      super.onCreate(holder)
      TurtlePriceWorker().runTradeUpdate()
      WeatherWorker().getWeather()
      setWatchFaceStyle(WatchFaceStyle.Builder(this@TurtleFace)
              .setAcceptsTapEvents(true)
              .build())

      mCalendar = Calendar.getInstance()

      val resources = this@TurtleFace.resources
      mYOffset = resources.getDimension(R.dimen.digital_y_offset)

      val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
      watchLayout = inflater.inflate(R.layout.watchface, null)
      val display = getSystemService(Context.WINDOW_SERVICE) as WindowManager
      display.defaultDisplay.getSize(displaySize)
    }

    override fun onDestroy() {
      mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME)
      super.onDestroy()
    }

    override fun onPropertiesChanged(properties: Bundle) {
      super.onPropertiesChanged(properties)
      mLowBitAmbient = properties.getBoolean(
              WatchFaceService.PROPERTY_LOW_BIT_AMBIENT, false)
      mBurnInProtection = properties.getBoolean(
              WatchFaceService.PROPERTY_BURN_IN_PROTECTION, false)
    }

    override fun onTimeTick() {
      super.onTimeTick()
      invalidate()
    }

    override fun onAmbientModeChanged(inAmbientMode: Boolean) {
      super.onAmbientModeChanged(inAmbientMode)
      updateTimer()
      val bottomLeft: FrameLayout = watchLayout.findViewById(R.id.bottomleft)
      val bottomRight: FrameLayout = watchLayout.findViewById(R.id.bottomright)
      val topLeft: FrameLayout = watchLayout.findViewById(R.id.topleft)
      val topRight: FrameLayout = watchLayout.findViewById(R.id.topright)
      val price: TextView = watchLayout.findViewById(R.id.price_ticker)
      val logo: ImageView = watchLayout.findViewById(R.id.logo)
      if (inAmbientMode) {
        bottomLeft.visibility = View.INVISIBLE
        bottomRight.visibility = View.INVISIBLE
        topLeft.visibility = View.INVISIBLE
        topRight.visibility = View.INVISIBLE
        price.visibility = View.INVISIBLE
        logo.setImageResource(R.drawable.logo_white)
      } else {
        setPhoneBattery(PhoneBatteryWorker().getBatteryLevel(this@TurtleFace.applicationContext))
        setTrtlPrice()
        setWeather()
        bottomLeft.visibility = View.VISIBLE
        bottomRight.visibility = View.VISIBLE
        topLeft.visibility = View.VISIBLE
        topRight.visibility = View.VISIBLE
        price.visibility = View.VISIBLE
        logo.setImageResource(R.drawable.logo)
      }
    }

    override fun onTapCommand(tapType: Int, x: Int, y: Int, eventTime: Long) {
      when (tapType) {
        WatchFaceService.TAP_TYPE_TOUCH -> {/* unused */}
        WatchFaceService.TAP_TYPE_TOUCH_CANCEL -> {/* unused */}
        WatchFaceService.TAP_TYPE_TAP -> {/* unused */}
      }
      invalidate()
    }

    override fun onDraw(canvas: Canvas, bounds: Rect) {
      setTimeandDate()
      setWatchBattery()
      watchLayout.measure(specW, specH)
      watchLayout.layout(0, 0, watchLayout.measuredWidth, watchLayout.measuredHeight)
      canvas.save()
      canvas.translate(mXOffset, mYOffset - 40)
      watchLayout.draw(canvas)
      canvas.restore()
    }

    private fun setTimeandDate() {
      val now = System.currentTimeMillis()
      mCalendar.timeInMillis = now
      val date: TextView = watchLayout.findViewById(R.id.date_number)
      date.text = String.format("%02d/%02d", mCalendar.get(Calendar.DAY_OF_MONTH), mCalendar.get(Calendar.MONTH) + 1)
      val hour: TextView = watchLayout.findViewById(R.id.hourtime)
      hour.text = String.format("%02d", mCalendar.get(Calendar.HOUR_OF_DAY))
      val min: TextView = watchLayout.findViewById(R.id.mintime)
      min.text = String.format("%02d", mCalendar.get(Calendar.MINUTE))
    }

    private fun setWatchBattery() {
      val bm = getSystemService(BATTERY_SERVICE) as BatteryManager
      val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

      val watch: TextView = watchLayout.findViewById(R.id.watch_power)
      watch.text = "$batLevel %"
    }

    private fun setPhoneBattery(phoneBat: String?) {
      val phone: TextView = watchLayout.findViewById(R.id.phone_power)
      phone.text = phoneBat
    }

    private fun setWeather() {
      val temperature: TextView = watchLayout.findViewById(R.id.temp_number)
      temperature.text = tempString
      val id = resources.getIdentifier("$weatherString", "drawable", packageName)
      val drawable = resources.getDrawable(id, null)
      val weatherIco: ImageView = watchLayout.findViewById(R.id.weather_ico)
      weatherIco.setImageDrawable(drawable)
    }

    private fun setTrtlPrice() {
      val price: TextView = watchLayout.findViewById(R.id.price_ticker)
      price.text = TurtlePriceWorker.currentString
    }

    override fun onVisibilityChanged(visible: Boolean) {
      super.onVisibilityChanged(visible)

      if (visible) {
        registerReceiver()
        mCalendar.timeZone = TimeZone.getDefault()
        invalidate()
      } else {
        unregisterReceiver()
      }
      updateTimer()
    }

    private fun registerReceiver() {
      if (mRegisteredTimeZoneReceiver) {
        return
      }
      mRegisteredTimeZoneReceiver = true
      val filter = IntentFilter(Intent.ACTION_TIMEZONE_CHANGED)
      this@TurtleFace.registerReceiver(mTimeZoneReceiver, filter)
    }

    private fun unregisterReceiver() {
      if (!mRegisteredTimeZoneReceiver) {
        return
      }
      mRegisteredTimeZoneReceiver = false
      this@TurtleFace.unregisterReceiver(mTimeZoneReceiver)
    }

    override fun onApplyWindowInsets(insets: WindowInsets) {
      super.onApplyWindowInsets(insets)

      val isRound = insets.isRound
      if (isRound) {
        // Shrink the face to fit on a round screen
        mYOffset = displaySize.x * 0.1f
        displaySize.y -= 2 * mXOffset.roundToInt()
        displaySize.x -= 2 * mXOffset.roundToInt()
      } else {
        mXOffset = 0f
      }
      specW = View.MeasureSpec.makeMeasureSpec(displaySize.x, View.MeasureSpec.EXACTLY)
      specH = View.MeasureSpec.makeMeasureSpec(displaySize.y, View.MeasureSpec.EXACTLY)
    }

    private fun updateTimer() {
      mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME)
      if (shouldTimerBeRunning()) {
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME)
      }
    }

    private fun shouldTimerBeRunning(): Boolean {
      return isVisible && !isInAmbientMode
    }

    fun handleUpdateTimeMessage() {
      invalidate()
      if (shouldTimerBeRunning()) {
        val timeMs = System.currentTimeMillis()
        val delayMs = INTERACTIVE_UPDATE_RATE_MS - timeMs % INTERACTIVE_UPDATE_RATE_MS
        mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs)
      }
    }
  }
}