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
class WeatherTest : TestBase(){

    private val mockWeather = """{"Temp": "82", "Icon": "02d"}"""

    @Test
    fun testWeather() {
        server.enqueue(MockResponse().setResponseCode(200).setBody(mockWeather))
        val help = Weather.GetWeather.create().getCurrentWeather("12", "12").execute()
        assertTrue(!help.body()?.icon.isNullOrEmpty())
        assertTrue(!help.body()?.temp.isNullOrEmpty())
    }
}