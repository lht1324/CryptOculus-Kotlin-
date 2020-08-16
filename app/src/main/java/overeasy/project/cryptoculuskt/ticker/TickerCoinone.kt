package overeasy.project.cryptoculuskt.ticker

import java.io.Serializable

class TickerCoinone : Ticker(), Serializable {
    var yesterday_last: String = "" // 요청 24시간 전 가격 (double)
    var yesterday_low: String = "" // 24 ~ 48 시간 내 최저가 (double)
    var high: String = "" // 24시간 내 최고가 (double)
    var currency: String = "" // 통화 (String)
    var yesterday_high: String = "" // 24 ~ 48시간 내 최고가 (double)
    var volume: String = "" // 24시간 내 거래량 (double)
    var yesterday_first: String = "" // 24 ~ 48시간 내 시가 (double)
    var last: String = "" // 요청 시 가격 (double)
    var yesterday_volume: String = "" // 24 ~ 48시간 내 거래량 (double)
    var low: String = "" // 24시간 내 최저가 (double)
    var first: String = "" // 시가 00시 기준 시가 (double)
}