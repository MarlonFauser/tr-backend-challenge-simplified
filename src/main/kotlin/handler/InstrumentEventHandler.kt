package handler

import InstrumentEvent
import local.Database
import local.Database.deleteBy

object InstrumentEventHandler {
    fun handle(event: InstrumentEvent) = event.apply {
        when (type) {
            InstrumentEvent.Type.DELETE -> {
                println("### Deleting by InstrumentEvent [${data.isin}] ###")
                Database.candlesticks.deleteBy(data.isin)
            }
            InstrumentEvent.Type.ADD -> {}
        }
    }
}