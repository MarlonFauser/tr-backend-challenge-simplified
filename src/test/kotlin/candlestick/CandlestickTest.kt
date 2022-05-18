package candlestick

import Candlestick
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CandlestickTest {
    @Test
    fun `should need to close if 60 seconds ahead`() {
        val oneMinuteAhead = Instant.now().plusSeconds(61)

        assertTrue {
            Candlestick(
                isin = "stubbedIsin",
                openTimestamp = Instant.now(),
                openPrice = 1.0,
                highPrice = 3.0,
                lowPrice = 0.5,
                closingPrice = 2.5,
            ).needsToClose(oneMinuteAhead)
        }
    }

    @Test
    fun `should do not need to close if 30 seconds ahead`() {
        val oneMinuteAhead = Instant.now().plusSeconds(30)

        assertFalse {
            Candlestick(
                isin = "stubbedIsin",
                openTimestamp = Instant.now(),
                openPrice = 1.0,
                highPrice = 3.0,
                lowPrice = 0.5,
                closingPrice = 2.5,
            ).needsToClose(oneMinuteAhead)
        }
    }

    @Test
    fun `should set highPrice if eventPrice is greater than the current highPrice and also the closingPrice`() {
        val candlestick = Candlestick(
            isin = "stubbedIsin",
            openTimestamp = Instant.now(),
            openPrice = 1.0,
            highPrice = 3.0,
            lowPrice = 0.5,
            closingPrice = 2.5,
        )

        candlestick.setPrices(10.0)

        assertEquals(10.0, candlestick.highPrice)
        assertEquals(10.0, candlestick.closingPrice)
        assertEquals(0.5, candlestick.lowPrice)
        assertEquals(1.0, candlestick.openPrice)
    }

    @Test
    fun `should set lowPrice if eventPrice is lower than the current lowPrice and also the closingPrice`() {
        val candlestick = Candlestick(
            isin = "stubbedIsin",
            openTimestamp = Instant.now(),
            openPrice = 1.0,
            highPrice = 3.0,
            lowPrice = 0.5,
            closingPrice = 2.5,
        )

        candlestick.setPrices(0.2)

        assertEquals(3.0, candlestick.highPrice)
        assertEquals(0.2, candlestick.closingPrice)
        assertEquals(0.2, candlestick.lowPrice)
        assertEquals(1.0, candlestick.openPrice)
    }

    @Test
    fun `should close (set closeTimestamp)`() {
        val now = Instant.now()
        val candlestick = Candlestick(
            isin = "stubbedIsin",
            openTimestamp = now,
            openPrice = 1.0,
            highPrice = 3.0,
            lowPrice = 0.5,
            closingPrice = 2.5,
        )

        candlestick.close(now.plusSeconds(61))

        assertEquals(3.0, candlestick.highPrice)
        assertEquals(2.5, candlestick.closingPrice)
        assertEquals(0.5, candlestick.lowPrice)
        assertEquals(1.0, candlestick.openPrice)
        assertEquals(now.plusSeconds(61), candlestick.closeTimestamp)
    }
}
