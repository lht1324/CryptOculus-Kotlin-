package overeasy.project.cryptoculuskt.coinInfo

import overeasy.project.cryptoculuskt.ticker.Ticker
import java.io.Serializable

open class CoinInfo : Serializable {
    open var coinData: Ticker? = null
    open var coinName: String = ""
    open var coinImageIndex: Int = 0
    open var coinViewCheck : Boolean = true
}