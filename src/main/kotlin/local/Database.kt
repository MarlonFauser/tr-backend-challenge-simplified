package local

import Candlestick
import ISIN

object Database {
    val candlesticks: MutableList<Candlestick> = mutableListOf()

    fun MutableList<Candlestick>.findNotClosed(isin: ISIN) = find { it.isin == isin && it.closeTimestamp == null }
    fun MutableList<Candlestick>.deleteBy(isin: ISIN) = removeAll(filterBy(isin))
    fun MutableList<Candlestick>.filterBy(isin: ISIN) = filter { it.isin == isin }
}