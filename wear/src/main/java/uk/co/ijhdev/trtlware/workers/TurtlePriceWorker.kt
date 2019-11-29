package uk.co.ijhdev.trtlware.workers

import android.os.Handler
import android.os.Looper
import uk.co.ijhdev.trtlware.repo.TradePriceFinder

/**
 * Created by Seperot on 28/03/2018.
 */

class TurtlePriceWorker {

  private var tradePriceFinder = TradePriceFinder()
  private val mainHandler = Handler(Looper.getMainLooper())

  fun runTradeUpdate() {
    "BTC".let { tradePriceFinder.getValue(it) }
//    TurtleFace().Engine().setTrtlPrice("1 trtl = " + prefs.getString(ARG_CURRENCY, "")?.let{ tradePriceFinder.getValue(it)})
  }

  companion object {
    const val ARG_CURRENCY = "currency"
    var currentString : String? = "1 trtl = 1 trtl"
  }
}