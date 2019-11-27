package uk.co.ijhdev.trtlware.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Seperot on 27/11/2019.
 */
@RunWith(AndroidJUnit4::class)
class PricesTest : TestBase(){

    @Test
    fun testPrices() {
        val help = Prices.GetCurrency.create().getAllCurrency()
    }
}