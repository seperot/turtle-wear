package uk.co.ijhdev.trtlware.network

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.util.concurrent.CountDownLatch


/**
 * Created by Seperot on 27/11/2019.
 */
open class TestBase {
  protected lateinit var server: MockWebServer
  protected lateinit var context: Application
  protected val countDownLatch by lazy {
    CountDownLatch(1)
  }

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext()
    server = MockWebServer()
    server.start()
    server.url("http://api.ijhdev.co.uk")

  }

  @After
  fun tearDown() {
    server.shutdown()
    server.close()
    countDownLatch.countDown()
  }
  companion object{
    const val SMALL_DELAY = 100L
    const val MAX_WAIT_TIME = 1L
  }

}