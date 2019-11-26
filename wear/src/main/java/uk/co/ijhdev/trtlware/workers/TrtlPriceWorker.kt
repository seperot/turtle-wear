package uk.co.ijhdev.trtlware.workers

import uk.co.ijhdev.trtlware.repo.TradePriceFinder

/**
 * Created by Seperot on 28/03/2018.
 */

class TrtlPriceWorker {

  private var tradePriceFinder = TradePriceFinder()

  fun runTradeUpdate() : String {
    val priceString = "1 trtl = " + currentString
    runInBackground()
    return priceString
//        TurtleFace().Engine().setTrtlPrice("1 trtl = " + prefs.getString(ARG_CURRENCY, "")?.let{ tradePriceFinder.getValue(it)})
  }

  private fun runInBackground() {
    Thread {
      currentString = "USD".let { tradePriceFinder.getValue(it) }
    }.start()
  }

  companion object {
    const val ARG_CURRENCY = "currency"
    const val RETURN_TRTL = "trtl"
    var currentString : String? = "1 trtl"
  }
}