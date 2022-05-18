package handler

import Candlestick
import QuoteEvent
import local.Database
import local.Database.findNotClosed

object QuoteEventHandler {
    fun handle(event: QuoteEvent) = event.apply {
        val notClosedCandlestick = Database.candlesticks.findNotClosed(data.isin)?.apply {
            if (needsToClose(instant)) {
                println("### Closing by QuoteEvent       [${data.isin}] ###")
                close(instant)
            } else {
                setPrices(data.price)
            }
        }

        if (notClosedCandlestick == null) {
            println("### Adding by QuoteEvent        [${data.isin}] ###")
            Database.candlesticks.add(
                Candlestick(
                    isin = data.isin,
                    openTimestamp = instant,
                    openPrice = data.price,
                    highPrice = data.price,
                    closingPrice = data.price,
                    lowPrice = data.price
                )
            )
        }
    }
}