package overeasy.project.cryptoculuskt.ticker

import java.io.Serializable

class TickerHuobi : Ticker(), Serializable {
    var symbol: String = ""
    var open: String = ""
    var high: String = ""
    var low: String = ""
    var close: String = ""
    var amount: String = ""
    var vol: String = ""
    var count: String = ""
    var bid: String = ""
    var bidSize: String = ""
    var ask: String = ""
    var askSize: String = ""
}