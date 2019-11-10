package uk.co.ijhdev.trtlware.Workers

import uk.co.ijhdev.trtlware.Utils.TradePriceFinder

/**
 * Created by Seperot on 28/03/2018.
 */

class TrtlPriceWorker {

  private var tradePriceFinder = TradePriceFinder()

  fun runTradeUpdate() : String {
    val priceString = "1 trtl = " + "BTC".let{ tradePriceFinder.getValue(it)}
    return priceString
//        TurtleFace().Engine().setTrtlPrice("1 trtl = " + prefs.getString(ARG_CURRENCY, "")?.let{ tradePriceFinder.getValue(it)})
  }
  companion object {
    const val ARG_CURRENCY = "currency"
    const val RETURN_TRTL = "trtl"
  }
}