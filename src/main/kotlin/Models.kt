import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant
import java.time.Instant.now

data class InstrumentEvent(val type: Type, val data: Instrument) {
    enum class Type {
        ADD,
        DELETE
    }
}

data class QuoteEvent(val data: Quote) {
    var instant: Instant = now()
}

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
    var closeTimestamp: Instant? = null,
    var openPrice: Price? = null,
    var highPrice: Price? = null,
    var lowPrice: Price? = null,
    var closingPrice: Price? = null,
) {
    fun needsToClose(eventInstant: Instant) = eventInstant >= openTimestamp.plusNanos(NANOS_TO_CLOSE)

    fun close(eventInstant: Instant) { closeTimestamp = eventInstant }

    fun setPrices(eventPrice: Price) {
        // println("### Updating [${isin}] prices ####")
        if (openPrice == null) openPrice = eventPrice
        if (highPrice == null || eventPrice > highPrice!!) highPrice = eventPrice
        if (lowPrice == null || eventPrice < lowPrice!!) lowPrice = eventPrice
        closingPrice = eventPrice
    }

    companion object {
        private val NANOS_TO_CLOSE = 60L * 1000000000L
    }
}