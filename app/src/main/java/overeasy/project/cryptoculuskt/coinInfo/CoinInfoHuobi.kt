package overeasy.project.cryptoculuskt.coinInfo

import overeasy.project.cryptoculuskt.ticker.Ticker
import overeasy.project.cryptoculuskt.ticker.TickerHuobi
import java.io.Serializable

class CoinInfoHuobi(coinData: TickerHuobi?, coinName: String, coinImageIndex: Int) : CoinInfo(), Serializable {
    override var coinData: Ticker? = coinData
    override var coinName = coinName
    override var coinImageIndex = coinImageIndex
    override var coinViewCheck: Boolean = true
}