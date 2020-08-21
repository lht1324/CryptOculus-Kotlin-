package overeasy.project.cryptoculuskt.function

import overeasy.project.cryptoculuskt.coinInfo.CoinInfo

interface DataTransferMain {
    fun changeRestartApp(restartApp: Boolean)
    fun changeCoinInfos(coinInfos: ArrayList<CoinInfo?>)
    fun isEmptyCoinone(): Boolean
    fun isEmptyBithumb(): Boolean
    fun isEmptyHuobi(): Boolean
}