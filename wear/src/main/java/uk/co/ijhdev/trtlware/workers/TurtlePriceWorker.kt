package uk.co.ijhdev.trtlware.workers

import android.content.Context
import android.os.Handler
import android.os.Looper
import uk.co.ijhdev.trtlware.repo.TradePriceFinder
import uk.co.ijhdev.trtlware.settings.SharedPreferenceHandler

/**
 * Created by Seperot on 28/03/2018.
 */

class TurtlePriceWorker {

  private var tradePriceFinder = TradePriceFinder()

  fun runTradeUpdate(context: Context) {
    SharedPreferenceHandler().getCoinType(context)?.let { tradePriceFinder.getValue(it) }
  }

  companion object {
    const val ARG_CURRENCY = "currency"
    var currentString : String? = "1 trtl = 1 trtl"
  }
}