package overeasy.project.cryptoculuskt.coinInfo

import overeasy.project.cryptoculuskt.ticker.Ticker
import overeasy.project.cryptoculuskt.ticker.TickerCoinone
import java.io.Serializable

class CoinInfoCoinone(coinData: TickerCoinone?, coinName: String, coinImageIndex: Int) : CoinInfo(), Serializable {
    override var coinData: Ticker? = coinData
    override var coinName = coinName
    override var coinImageIndex = coinImageIndex
    override var coinViewCheck: Boolean = true
}