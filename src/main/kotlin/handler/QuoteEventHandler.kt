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
            Database.new(Candlestick(data.isin, instant))
        }
    }
}