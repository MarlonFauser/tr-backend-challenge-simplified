package candlestick

import Candlestick
import CandlestickManager
import local.Database

class CandlestickManagerImpl : CandlestickManager {
    override fun getCandlesticks(isin: String): List<Candlestick> = Database.candlesticks.filter { it.isin == isin }
}