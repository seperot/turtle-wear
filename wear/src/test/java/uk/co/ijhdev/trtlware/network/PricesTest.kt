package uk.co.ijhdev.trtlware.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import okhttp3.mockwebserver.MockResponse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Created by Seperot on 27/11/2019.
 */
@Config(sdk = [28])
@RunWith(AndroidJUnit4::class)
class PricesTest : TestBase() {

  private val mockPrices = """{"USD": "$14.95", "BTC": "0.00003"}"""

  @Test
  fun testPrices() {
    server.enqueue(MockResponse().setResponseCode(200).setBody(mockPrices))
    val help = Prices.GetCurrency.create().getAllCurrency().execute()
    assertTrue(!help.body()?.usd.isNullOrEmpty())
    assertTrue(!help.body()?.btc.isNullOrEmpty())
  }
}