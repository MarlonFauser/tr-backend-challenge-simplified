package candlestick

import Candlestick
import CandlestickManager
import local.Database
import local.Database.filterBy

class CandlestickManagerImpl : CandlestickManager {
    override fun getCandlesticks(isin: String): List<Candlestick> = Database.candlesticks.filterBy(isin)
}