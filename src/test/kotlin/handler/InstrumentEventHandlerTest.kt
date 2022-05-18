package handler

import Candlestick
import Instrument
import InstrumentEvent
import local.Database
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Instant
import kotlin.test.assertEquals

class InstrumentEventHandlerTest {
    val stubbedIsin = "FWQ587J0Y523"

    @Test
    fun `should delete all candlesticks with the given isin when type DELETE`() {
        repeat(10) {
            Database.candlesticks.add(
                Candlestick(
                    isin = stubbedIsin,
                    openTimestamp = Instant.now(),
                    closeTimestamp = Instant.now().plusSeconds(60),
                    openPrice = it + 1.0,
                    highPrice = it + 3.0,
                    lowPrice = it + 0.5,
                    closingPrice = it + 2.5,
                )
            )
        }

        assertEquals(10, Database.candlesticks.size)

        InstrumentEventHandler.handle(
            InstrumentEvent(
                type = InstrumentEvent.Type.DELETE,
                data = Instrument(stubbedIsin, "Lorem Ipso")
            )
        )

        assertEquals(0, Database.candlesticks.size)
    }

    @Test
    fun `should do nothin when type ADD`() {
        assertEquals(0, Database.candlesticks.size)

        assertDoesNotThrow {
            InstrumentEventHandler.handle(
                InstrumentEvent(
                    type = InstrumentEvent.Type.ADD,
                    data = Instrument(stubbedIsin, "Lorem Ipso")
                )
            )
        }

        assertEquals(0, Database.candlesticks.size)
    }
}
