import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant
import java.time.Instant.now

data class InstrumentEvent(val type: Type, val data: Instrument) {
    enum class Type {
        ADD,
        DELETE
    }
}

data class QuoteEvent(val data: Quote, val instant: Instant = now())

data class Instrument(val isin: ISIN, val description: String)
typealias ISIN = String

data class Quote(val isin: ISIN, val price: Price)
typealias Price = Double


interface CandlestickManager {
    fun getCandlesticks(isin: String): List<Candlestick>
}

data class Candlestick(
    @JsonIgnore
    val isin: ISIN,
    val openTimestamp: Instant,
    var openPrice: Price,
    var highPrice: Price,
    var lowPrice: Price,
    var closingPrice: Price,
    var closeTimestamp: Instant? = null,
) {
    fun needsToClose(eventInstant: Instant) = eventInstant >= openTimestamp.plusNanos(NANOS_TO_CLOSE)

    fun close(eventInstant: Instant) { closeTimestamp = eventInstant }

    fun setPrices(eventPrice: Price) {
        if (eventPrice > highPrice) highPrice = eventPrice
        if (eventPrice < lowPrice) lowPrice = eventPrice
        closingPrice = eventPrice
    }

    companion object {
        private const val NANOS_TO_CLOSE = 60L * 1000000000L
    }
}