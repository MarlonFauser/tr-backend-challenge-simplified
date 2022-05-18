package local

import Candlestick
import ISIN

object Database {
    val candlesticks: MutableList<Candlestick> = mutableListOf()

    fun MutableList<Candlestick>.findNotClosed(isin: ISIN) = find { it.isin == isin && it.closeTimestamp == null }
    fun MutableList<Candlestick>.deleteBy(isin: ISIN) = remove(findBy(isin))
    fun MutableList<Candlestick>.findBy(isin: ISIN) = find { it.isin == isin }

    fun new(candlestick: Candlestick) = candlesticks.add(candlestick)
}