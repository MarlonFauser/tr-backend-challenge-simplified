import handler.QuoteEventHandler
import local.Database
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class QuoteEventHandlerTest {
    val stubbedIsin = "FWQ587J0Y523"
    val stubbedFutureInstant = Instant.parse("2100-01-15T18:00:00.00Z")

    @Test
    fun `should create a candlestick`() {
        assertEquals(0, Database.candlesticks.size)

        QuoteEventHandler.handle(
            QuoteEvent(
                data = Quote(stubbedIsin, 10.0),
            )
        )

        assertEquals(1, Database.candlesticks.size)

        val candlestick = Database.candlesticks.first()
        assertEquals(stubbedIsin, candlestick.isin)
        assertEquals(10.0, candlestick.openPrice)
        assertEquals(10.0, candlestick.highPrice)
        assertEquals(10.0, candlestick.lowPrice)
        assertEquals(10.0, candlestick.closingPrice)
        assertEquals(null, candlestick.closeTimestamp)
    }

    @Test
    fun `should update a opened candlestick and let it opened`() {
        repeat(10) {
            Database.candlesticks.add(
                Candlestick(
                    isin = stubbedIsin,
                    openTimestamp = Instant.now(),
                    closeTimestamp = Instant.now().plusSeconds(60L),
                    openPrice = it + 1.0,
                    highPrice = it + 3.0,
                    lowPrice = it + 0.5,
                    closingPrice = it + 2.5,
                )
            )
        }
        Database.candlesticks.last().apply {
            this.closeTimestamp = null
        }

        assertEquals(null, Database.candlesticks.last().closeTimestamp)

        QuoteEventHandler.handle(
            QuoteEvent(
                data = Quote(stubbedIsin, 50.0)
            )
        )

        assertNotEquals(50.0, Database.candlesticks.last().lowPrice)
        assertNotEquals(50.0, Database.candlesticks.last().openPrice)

        assertEquals(50.0, Database.candlesticks.last().closingPrice)
        assertEquals(50.0, Database.candlesticks.last().highPrice)
    }

    @Test
    fun `should close a opened candlestick and not update it`() {
        repeat(10) {
            Database.candlesticks.add(
                Candlestick(
                    isin = stubbedIsin,
                    openTimestamp = Instant.now(),
                    closeTimestamp = Instant.now().plusSeconds(60L),
                    openPrice = it + 1.0,
                    highPrice = it + 3.0,
                    lowPrice = it + 0.5,
                    closingPrice = it + 2.5,
                )
            )
        }
        Database.candlesticks.last().apply {
            this.closeTimestamp = null
        }

        assertEquals(null, Database.candlesticks.last().closeTimestamp)

        QuoteEventHandler.handle(
            QuoteEvent(
                data = Quote(stubbedIsin, 50.0),
                instant = stubbedFutureInstant
            )
        )

        assertNotEquals(50.0, Database.candlesticks.last().lowPrice)
        assertNotEquals(50.0, Database.candlesticks.last().openPrice)
        assertNotEquals(50.0, Database.candlesticks.last().closingPrice)
        assertNotEquals(50.0, Database.candlesticks.last().highPrice)

        assertEquals(stubbedFutureInstant, Database.candlesticks.last().closeTimestamp)
    }

    @BeforeEach
    fun cleanUpDB() {
        Database.candlesticks.clear()
    }
}
