package overeasy.project.cryptoculuskt.coinInfo

import overeasy.project.cryptoculuskt.ticker.Ticker
import overeasy.project.cryptoculuskt.ticker.TickerBithumb
import java.io.Serializable

class CoinInfoBithumb(coinData: TickerBithumb?, coinName: String, coinImageIndex: Int) : CoinInfo(), Serializable {
    override var coinData: Ticker? = coinData
    override var coinName = coinName
    override var coinImageIndex = coinImageIndex
    override var coinViewCheck: Boolean = true
}