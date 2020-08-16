package overeasy.project.cryptoculuskt.ticker

import java.io.Serializable

class TickerBithumb : Ticker(), Serializable {
    var opening_price: String = ""  // 시가 00시 기준, 12시 가격
    var closing_price: String = ""  // 종가 00시 기준, 현재 가격
    var min_price: String = ""  // 저가 00시 기준
    var max_price: String = ""  // 고가 00시 기준
    var units_traded: String = ""  // 거래량 00시 기준
    var acc_trade_value: String = ""  // 거래금액 00시 기준
    var prev_closing_price: String = ""  // 전일종가
    var units_traded_24H: String = ""  // 최근 24시간 거래량
    var acc_trade_value_24H: String = ""  // 최근 24시간 거래금액
    var fluctate_24H: String = ""  // 최근 24시간 변동가
    var fluctate_rate_24H: String = ""  // 최근 24시간 변동률
}