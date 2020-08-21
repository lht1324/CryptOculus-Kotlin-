package overeasy.project.cryptoculuskt.function

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import overeasy.project.cryptoculuskt.R
import overeasy.project.cryptoculuskt.coinInfo.CoinInfo
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoBithumb
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoCoinone
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoHuobi
import overeasy.project.cryptoculuskt.currencys.Currencys
import overeasy.project.cryptoculuskt.currencys.CurrencysBithumb
import overeasy.project.cryptoculuskt.currencys.CurrencysCoinone
import overeasy.project.cryptoculuskt.currencys.CurrencysHuobi
import overeasy.project.cryptoculuskt.ticker.TickerHuobi
import java.lang.IndexOutOfBoundsException
import java.util.*

class ArrayMaker(
    private var mContext: Context,
    private var mCallback: DataTransferMain) {

    lateinit var coinInfosInput: ArrayList<CoinInfo?>
    var restartApp: Boolean = false
    var refreshed = false

    var coinoneAddress = "https://api.coinone.co.kr/"
    var bithumbAddress = "https://api.bithumb.com/"
    var huobiAddress = "https://api-cloud.huobi.co.kr/"
    var URL: String = coinoneAddress

    fun makeArray(currencyList: Currencys) : ArrayList<CoinInfo?> {
        lateinit var coinInfos: ArrayList<CoinInfo?>
        var pref: SharedPreferences = mContext.getSharedPreferences("coinInfosSave", MODE_PRIVATE)

        coinInfos = makeCoinInfos(currencyList)

        if (restartApp) {
            var positions = arrayOfNulls<Int>(coinInfos.size)
            var temp = ArrayList<CoinInfo?>()

            for (i in coinInfos.indices) {
                try {
                    if (URL == coinoneAddress) {
                        coinInfos[i]!!.coinViewCheck = pref.getBoolean("${coinInfos[i]!!.coinName}'s coinViewCheck in coinInfosCoinone", true)
                        positions[i] = pref.getInt("${coinInfos[i]!!.coinName}'s position in coinInfosCoinone", 0)
                        temp.add(null)
                    }
                    if (URL == bithumbAddress) {
                        coinInfos[i]!!.coinViewCheck = pref.getBoolean("${coinInfos[i]!!.coinName}'s coinViewCheck in coinInfosBithumb", true)
                        positions[i] = pref.getInt("${coinInfos[i]!!.coinName}'s position in coinInfosBithumb", 0)
                        temp.add(null)
                    }
                    if (URL == huobiAddress) {
                        coinInfos[i]!!.coinViewCheck = pref.getBoolean("${coinInfos[i]!!.coinName}'s coinViewCheck in coinInfosHuobi", true)
                        positions[i] = pref.getInt("${coinInfos[i]!!.coinName}'s position in coinInfosHuobi", 0)
                        temp.add(null)
                    }
                } catch(e: IndexOutOfBoundsException) {
                    break
                }
            }

            for (i in coinInfos.indices) // temp의 각 위치에 coinInfos의 원소를 넣는다
                temp[positions[i]!!] = coinInfos[i]

            coinInfos = temp

            if (!mCallback.isEmptyCoinone() && !mCallback.isEmptyBithumb() && !mCallback.isEmptyHuobi()) {
                restartApp = false
                mCallback.changeRestartApp(false)
            }
        }

        if (refreshed) {
            println("Condition statement (refreshed) is working")
            for (i in coinInfos.indices) {
                val temp1 = coinInfosInput[i]!!.coinName
                val temp2 = coinInfos[i]!!.coinName

                if (temp1 != temp2) {
                    for (j in coinInfos.indices) {
                        if (temp1 == coinInfos[j]!!.coinName)
                            Collections.swap(coinInfos, i, j)
                    }
                }
            }

            for (i in coinInfos.indices) // 순서 변경 후
                coinInfos[i]!!.coinViewCheck = coinInfosInput[i]!!.coinViewCheck
        }

        mCallback.changeCoinInfos(coinInfos)
        return coinInfos
    }

    private fun makeCoinInfos(currencysInput: Currencys): ArrayList<CoinInfo?> {
        var coinInfos = ArrayList<CoinInfo?>()

        if (URL == coinoneAddress) {
            var currencyList = currencysInput as CurrencysCoinone

            coinInfos.add(CoinInfoCoinone(currencyList.btc, "BTC / 비트코인", R.drawable.btc))
            coinInfos.add(CoinInfoCoinone(currencyList.eth, "ETH / 이더리움", R.drawable.eth))
            coinInfos.add(CoinInfoCoinone(currencyList.xrp, "XRP / 리플", R.drawable.xrp))
            coinInfos.add(CoinInfoCoinone(currencyList.bch, "BCH / 비트코인 캐시", R.drawable.bch))
            coinInfos.add(CoinInfoCoinone(currencyList.eos, "EOS / 이오스", R.drawable.eos))
            coinInfos.add(CoinInfoCoinone(currencyList.bsv, "BSV / 비트코인사토시비전", R.drawable.bsv))
            coinInfos.add(CoinInfoCoinone(currencyList.etc, "ETC / 이더리움 클래식", R.drawable.etc))
            coinInfos.add(CoinInfoCoinone(currencyList.ltc, "LTC / 라이트코인", R.drawable.ltc))
            coinInfos.add(CoinInfoCoinone(currencyList.prom, "PROM / 프로메테우스", R.drawable.prom))
            coinInfos.add(CoinInfoCoinone(currencyList.atom, "ATOM / 코스모스아톰", R.drawable.atom))
            coinInfos.add(CoinInfoCoinone(currencyList.xtz, "XTZ / 테조스", R.drawable.xtz))
            coinInfos.add(CoinInfoCoinone(currencyList.psc, "PSC / 폴스타코인", R.drawable.basic)) //
            coinInfos.add(CoinInfoCoinone(currencyList.pci, "PCI / 페이코인", R.drawable.pci))
            coinInfos.add(CoinInfoCoinone(currencyList.trx, "TRX / 트론", R.drawable.trx))
            coinInfos.add(CoinInfoCoinone(currencyList.fleta, "FLETA / 플레타", R.drawable.fleta))
            coinInfos.add(CoinInfoCoinone(currencyList.qtum, "QTUM / 퀀텀", R.drawable.qtum))
            coinInfos.add(CoinInfoCoinone(currencyList.luna, "LUNA / 루나", R.drawable.luna))
            coinInfos.add(CoinInfoCoinone(currencyList.knc, "KNC / 카이버", R.drawable.knc))
            coinInfos.add(CoinInfoCoinone(currencyList.kvi, "KVI / 케이브이아이", R.drawable.basic)) //
            coinInfos.add(CoinInfoCoinone(currencyList.egg, "EGG / 네스트리", R.drawable.egg))
            coinInfos.add(CoinInfoCoinone(currencyList.bna, "BNA / 바나나톡", R.drawable.bna))
            coinInfos.add(CoinInfoCoinone(currencyList.xlm, "XLM / 스텔라루멘", R.drawable.xlm))
            coinInfos.add(CoinInfoCoinone(currencyList.iota, "IOTA / 아이오타", R.drawable.iota))
            coinInfos.add(CoinInfoCoinone(currencyList.xpn, "XPN / 판테온X", R.drawable.xpn))
            coinInfos.add(CoinInfoCoinone(currencyList.gas, "GAS / 가스", R.drawable.gas))
            coinInfos.add(CoinInfoCoinone(currencyList.ogn, "OGN / 오리진 프로토콜", R.drawable.ogn))
            coinInfos.add(CoinInfoCoinone(currencyList.ong, "ONG / 온돌로지가스", R.drawable.ong))
            coinInfos.add(CoinInfoCoinone(currencyList.chz, "CHZ / 칠리즈", R.drawable.chz))
            coinInfos.add(CoinInfoCoinone(currencyList.data, "DATA / 스트리머", R.drawable.data))
            coinInfos.add(CoinInfoCoinone(currencyList.soc, "SOC / 소다코인", R.drawable.soc))
            coinInfos.add(CoinInfoCoinone(currencyList.zil, "ZIL / 질리카", R.drawable.zil))
            coinInfos.add(CoinInfoCoinone(currencyList.bat, "BAT / 베이직어텐션토큰", R.drawable.bat))
            coinInfos.add(CoinInfoCoinone(currencyList.zrx, "ZRX / 제로엑스", R.drawable.zrx))
            coinInfos.add(CoinInfoCoinone(currencyList.pxl, "PXL / 픽션네트워크", R.drawable.pxl))
            coinInfos.add(CoinInfoCoinone(currencyList.isr, "ISR / 인슈리움", R.drawable.isr))
            coinInfos.add(CoinInfoCoinone(currencyList.neo, "NEO / 네오", R.drawable.neo))
            coinInfos.add(CoinInfoCoinone(currencyList.redi, "REDI / 레디", R.drawable.redi))
            coinInfos.add(CoinInfoCoinone(currencyList.mbl, "MBL / 무비블록", R.drawable.mbl))
            coinInfos.add(CoinInfoCoinone(currencyList.omg, "OMG / 오미세고", R.drawable.omg))
            coinInfos.add(CoinInfoCoinone(currencyList.btt, "BTT / 비트토렌트", R.drawable.btt))
            coinInfos.add(CoinInfoCoinone(currencyList.drm, "DRM / 두드림체인", R.drawable.basic)) //
            coinInfos.add(CoinInfoCoinone(currencyList.spin, "SPIN / 스핀프로토콜", R.drawable.spin))
            coinInfos.add(CoinInfoCoinone(currencyList.ankr, "ANKR / 앵커 네크워크", R.drawable.ankr))
            coinInfos.add(CoinInfoCoinone(currencyList.stpt, "STPT / 에스티피", R.drawable.stpt))
            coinInfos.add(CoinInfoCoinone(currencyList.ont, "ONT / 온톨로지", R.drawable.ont))
            coinInfos.add(CoinInfoCoinone(currencyList.matic, "MATIC / 매틱 네트워크", R.drawable.matic))
            coinInfos.add(CoinInfoCoinone(currencyList.temco, "TEMCO / 템코", R.drawable.temco))
            coinInfos.add(CoinInfoCoinone(currencyList.ftm, "FTM / 팬텀", R.drawable.ftm))
            coinInfos.add(CoinInfoCoinone(currencyList.iotx, "IOTX / 아이오텍스", R.drawable.iotx))
            coinInfos.add(CoinInfoCoinone(currencyList.abl, "ABL / 에어블록", R.drawable.abl))
            coinInfos.add(CoinInfoCoinone(currencyList.pib, "PIB / 피블", R.drawable.pib))
            coinInfos.add(CoinInfoCoinone(currencyList.amo, "AMO / 아모코인", R.drawable.amo))
            coinInfos.add(CoinInfoCoinone(currencyList.troy, "TROY / 트로이", R.drawable.troy))
            coinInfos.add(CoinInfoCoinone(currencyList.clb, "CLB / 클라우드브릭", R.drawable.clb))
            coinInfos.add(CoinInfoCoinone(currencyList.orbs, "ORBS / 오브스", R.drawable.orbs))
            coinInfos.add(CoinInfoCoinone(currencyList.baas, "BAAS / 바스아이디", R.drawable.baas))
            coinInfos.add(CoinInfoCoinone(currencyList.hint, "HINT / 힌트체인", R.drawable.hint))
            coinInfos.add(CoinInfoCoinone(currencyList.hibs, "HIBS / 하이블럭스", R.drawable.hibs))
            coinInfos.add(CoinInfoCoinone(currencyList.dad, "DAD / 다드", R.drawable.dad))
            coinInfos.add(CoinInfoCoinone(currencyList.uos, "UOS / 울트라", R.drawable.uos))
            coinInfos.add(CoinInfoCoinone(currencyList.btg, "BTG / 비트코인 골드", R.drawable.btg))
            coinInfos.add(CoinInfoCoinone(currencyList.arpa, "ARPA / 알파 체인", R.drawable.arpa))
            coinInfos.add(CoinInfoCoinone(currencyList.axl, "AXL / 엑시얼", R.drawable.axl))
            coinInfos.add(CoinInfoCoinone(currencyList.hum, "HUM / 휴먼스케이프", R.drawable.hum))
            coinInfos.add(CoinInfoCoinone(currencyList.ksc, "KSC / 케이스타라이브", R.drawable.ksc))
            coinInfos.add(CoinInfoCoinone(currencyList.wiken, "WIKEN / 위드", R.drawable.wiken))
            coinInfos.add(CoinInfoCoinone(currencyList.ftt, "FTT / 에프티엑스 토큰", R.drawable.ftt))
            coinInfos.add(CoinInfoCoinone(currencyList.obsr, "OBSR / 옵저버", R.drawable.obsr))
            coinInfos.add(CoinInfoCoinone(currencyList.gom2, "GOM2 / 고머니2", R.drawable.gom2))
            coinInfos.add(CoinInfoCoinone(currencyList.klay, "KLAY / 클레이튼", R.drawable.klay))
            coinInfos.add(CoinInfoCoinone(currencyList.kdag, "KDAG / 킹디에이쥐", R.drawable.kdag))
            coinInfos.add(CoinInfoCoinone(currencyList.isdt, "ISDT / 아이스타더스트", R.drawable.basic))
            coinInfos.add(CoinInfoCoinone(currencyList.snx, "SNX / 신세틱스 네트워크", R.drawable.snx))
            coinInfos.add(CoinInfoCoinone(currencyList.dvx, "DVX / 데리벡스", R.drawable.dvx))
            coinInfos.add(CoinInfoCoinone(currencyList.mch, "MCH / 미콘캐시", R.drawable.mch))
            coinInfos.add(CoinInfoCoinone(currencyList.exe, "EXE / 8X8 프로토콜", R.drawable.exe))
            coinInfos.add(CoinInfoCoinone(currencyList.lbxc, "LBXC / 럭스 바이오", R.drawable.lbxc))
            coinInfos.add(CoinInfoCoinone(currencyList.show, "SHOW / 쇼고", R.drawable.basic))
            coinInfos.add(CoinInfoCoinone(currencyList.get, "GET / 겟 프로토콜", R.drawable.get))
            coinInfos.add(CoinInfoCoinone(currencyList.mov, "MOV / 모티브", R.drawable.mov))
            coinInfos.add(CoinInfoCoinone(currencyList.asm, "ASM / 어셈블 프로토콜", R.drawable.asm))
            coinInfos.add(CoinInfoCoinone(currencyList.bora, "BORA / 보라", R.drawable.bora))
            coinInfos.add(CoinInfoCoinone(currencyList.rnx, "RNX / 루넥스", R.drawable.rnx))
            coinInfos.add(CoinInfoCoinone(currencyList.kava, "KAVA / 카바", R.drawable.kava))
            coinInfos.add(CoinInfoCoinone(currencyList.tmtg, "TMTG / 더마이다스터치골드", R.drawable.tmtg))
            coinInfos.add(CoinInfoCoinone(currencyList.ibp, "IBP / 아이비피 토큰", R.drawable.ibp))
            coinInfos.add(CoinInfoCoinone(currencyList.qtcon, "QTCON / 퀴즈톡", R.drawable.qtcon))
            coinInfos.add(CoinInfoCoinone(currencyList.jst, "JST / 저스트", R.drawable.jst))
            coinInfos.add(CoinInfoCoinone(currencyList.mnr, "MNR / 미네랄", R.drawable.mnr))
            coinInfos.add(CoinInfoCoinone(currencyList.trcl, "TRCL / 트리클", R.drawable.trcl))
            coinInfos.add(CoinInfoCoinone(currencyList.kai, "KAI / 카르디아체인", R.drawable.kai))
            coinInfos.add(CoinInfoCoinone(currencyList.comp, "COMP / 컴파운드", R.drawable.comp))
            coinInfos.add(CoinInfoCoinone(currencyList.nfup, "NFUP / 엔에프유피", R.drawable.basic))
            coinInfos.add(CoinInfoCoinone(currencyList.cos, "COS / 콘텐토스", R.drawable.cos))
            coinInfos.add(CoinInfoCoinone(currencyList.six, "SIX / 식스", R.drawable.six))
            coinInfos.add(CoinInfoCoinone(currencyList.fet, "FET / 페치", R.drawable.fet))
            coinInfos.add(CoinInfoCoinone(currencyList.bzrx, "BZRX / 비지엑스 프로토콜", R.drawable.bzrx))
            coinInfos.add(CoinInfoCoinone(currencyList.krt, "KRT / 테라 KRT", R.drawable.krt))
            coinInfos.add(CoinInfoCoinone(currencyList.mta, "MTA / 메타", R.drawable.mta))
        }

        if (URL == bithumbAddress) {
            var currencyList = currencysInput as CurrencysBithumb

            coinInfos.add(CoinInfoBithumb(currencyList.XRP, "XRP / 리플", R.drawable.xrp))
            coinInfos.add(CoinInfoBithumb(currencyList.BTC, "BTC / 비트코인", R.drawable.btc))
            coinInfos.add(CoinInfoBithumb(currencyList.ETH, "ETH / 이더리움", R.drawable.eth))
            coinInfos.add(CoinInfoBithumb(currencyList.XLM, "XLM / 스텔라루멘", R.drawable.xlm))
            coinInfos.add(CoinInfoBithumb(currencyList.EOS, "EOS / 이오스", R.drawable.eos))
            coinInfos.add(CoinInfoBithumb(currencyList.MBL, "MBL / 무비블록", R.drawable.mbl))
            coinInfos.add(CoinInfoBithumb(currencyList.BNP, "BNP / 베네핏", R.drawable.bnp)) // 상장 폐지
            coinInfos.add(CoinInfoBithumb(currencyList.BSV, "BSV / 비트코인에스브이", R.drawable.bsv))
            coinInfos.add(CoinInfoBithumb(currencyList.XSR, "XSR / 젠서", R.drawable.xsr))
            coinInfos.add(CoinInfoBithumb(currencyList.WICC, "WICC / 웨이키체인", R.drawable.wicc))
            coinInfos.add(CoinInfoBithumb(currencyList.BCH, "BCH / 비트코인 캐시", R.drawable.bch))
            coinInfos.add(CoinInfoBithumb(currencyList.MCO, "MCO / 크립토닷컴", R.drawable.mco))
            coinInfos.add(CoinInfoBithumb(currencyList.REP, "REP / 어거", R.drawable.rep))
            coinInfos.add(CoinInfoBithumb(currencyList.LTC, "LTC / 라이트코인", R.drawable.ltc))
            coinInfos.add(CoinInfoBithumb(currencyList.TRX, "TRX / 트론", R.drawable.trx))
            coinInfos.add(CoinInfoBithumb(currencyList.POWR, "POWR / 파워렛저", R.drawable.powr))
            coinInfos.add(CoinInfoBithumb(currencyList.WAVES, "WAVES / 웨이브", R.drawable.waves))
            coinInfos.add(CoinInfoBithumb(currencyList.AMO, "AMO / 아모코인", R.drawable.amo))
            coinInfos.add(CoinInfoBithumb(currencyList.SOC, "SOC / 소다코인", R.drawable.soc))
            coinInfos.add(CoinInfoBithumb(currencyList.AE, "AE / 애터니티", R.drawable.ae))
            coinInfos.add(CoinInfoBithumb(currencyList.FLETA, "FLETA / 플레타", R.drawable.fleta))
            coinInfos.add(CoinInfoBithumb(currencyList.APIX, "APIX / 아픽스", R.drawable.apix)) // 폐지
            coinInfos.add(CoinInfoBithumb(currencyList.BCD, "BCD / 비트코인 다이아몬드", R.drawable.bcd))
            coinInfos.add(CoinInfoBithumb(currencyList.STRAT, "STRAT / 스트라티스", R.drawable.strat))
            coinInfos.add(CoinInfoBithumb(currencyList.ZEC, "ZEC / 제트캐시", R.drawable.zec))
            coinInfos.add(CoinInfoBithumb(currencyList.ANKR, "ANKR / 앵커", R.drawable.ankr))
            coinInfos.add(CoinInfoBithumb(currencyList.BAT, "BAT / 베이직어텐션토큰", R.drawable.bat))
            coinInfos.add(CoinInfoBithumb(currencyList.GXC, "GXC / 지엑스체인", R.drawable.gxc))
            coinInfos.add(CoinInfoBithumb(currencyList.WPX, "WPX / 더블유플러스", R.drawable.wpx))
            coinInfos.add(CoinInfoBithumb(currencyList.ETC, "ETC / 이더리움 클래식", R.drawable.etc))
            coinInfos.add(CoinInfoBithumb(currencyList.HDAC, "HDAC / 에이치닥", R.drawable.hdac))
            coinInfos.add(CoinInfoBithumb(currencyList.THETA, "THETA / 쎄타토큰", R.drawable.theta))
            coinInfos.add(CoinInfoBithumb(currencyList.SXP, "SXP / 스와이프", R.drawable.sxp))
            coinInfos.add(CoinInfoBithumb(currencyList.ADA, "ADA / 에이다", R.drawable.ada))
            coinInfos.add(CoinInfoBithumb(currencyList.DASH, "DASH / 대시", R.drawable.dash))
            coinInfos.add(CoinInfoBithumb(currencyList.XMR, "XMR / 모네로", R.drawable.xmr))
            coinInfos.add(CoinInfoBithumb(currencyList.LINK, "LINK / 체인링크", R.drawable.link))
            coinInfos.add(CoinInfoBithumb(currencyList.WAXP, "WAXP / 왁스", R.drawable.waxp))
            coinInfos.add(CoinInfoBithumb(currencyList.KNC, "KNC / 카이버 네트워크", R.drawable.knc))
            coinInfos.add(CoinInfoBithumb(currencyList.VET, "VET / 비체인", R.drawable.vet))
            coinInfos.add(CoinInfoBithumb(currencyList.BTT, "BTT / 비트토렌트", R.drawable.btt))
            coinInfos.add(CoinInfoBithumb(currencyList.ZIL, "ZIL / 질리카", R.drawable.zil))
            coinInfos.add(CoinInfoBithumb(currencyList.AOA, "AOA / 오로라", R.drawable.aoa))
            coinInfos.add(CoinInfoBithumb(currencyList.ITC, "ITC / 아이오티체인", R.drawable.itc))
            coinInfos.add(CoinInfoBithumb(currencyList.LUNA, "LUNA / 루나", R.drawable.luna))
            coinInfos.add(CoinInfoBithumb(currencyList.QTUM, "QTUM / 퀀텀", R.drawable.qtum))
            coinInfos.add(CoinInfoBithumb(currencyList.MTL, "MTL / 메탈", R.drawable.mtl))
            coinInfos.add(CoinInfoBithumb(currencyList.ORBS, "ORBS / 오브스", R.drawable.orbs))
            coinInfos.add(CoinInfoBithumb(currencyList.FAB, "FAB / 패블릭", R.drawable.fab))
            coinInfos.add(CoinInfoBithumb(currencyList.BTG, "BTG / 비트코인 골드", R.drawable.btg))
            coinInfos.add(CoinInfoBithumb(currencyList.TMTG, "TMTG / 더마이다스터치골드", R.drawable.tmtg))
            coinInfos.add(CoinInfoBithumb(currencyList.FCT, "FCT / 피르마체인", R.drawable.fct))
            coinInfos.add(CoinInfoBithumb(currencyList.FNB, "FNB / 에프앤비프로토콜", R.drawable.fnb))
            coinInfos.add(CoinInfoBithumb(currencyList.ICX, "ICX / 아이콘", R.drawable.icx))
            coinInfos.add(CoinInfoBithumb(currencyList.LRC, "LRC / 루프링", R.drawable.lrc))
            coinInfos.add(CoinInfoBithumb(currencyList.LOOM, "LOOM / 룸네트워크", R.drawable.loom))
            coinInfos.add(CoinInfoBithumb(currencyList.IPX, "IPX / 타키온프로토콜", R.drawable.ipx))
            coinInfos.add(CoinInfoBithumb(currencyList.NPXS, "NPXS / 펀디엑스", R.drawable.npxs))
            coinInfos.add(CoinInfoBithumb(currencyList.IOST, "IOST / 이오스트", R.drawable.iost))
            coinInfos.add(CoinInfoBithumb(currencyList.FZZ, "FZZ / 피즈토큰", R.drawable.fzz))
            coinInfos.add(CoinInfoBithumb(currencyList.DAD, "DAD / 다드", R.drawable.dad))
            coinInfos.add(CoinInfoBithumb(currencyList.CON, "CON / 코넌", R.drawable.conun))
            coinInfos.add(CoinInfoBithumb(currencyList.BZNT, "BZNT / 베잔트", R.drawable.bznt))
            coinInfos.add(CoinInfoBithumb(currencyList.TRUE, "TRUE / 트루체인", R.drawable.truechain))
            coinInfos.add(CoinInfoBithumb(currencyList.EM, "EM / 이마이너", R.drawable.em))
            coinInfos.add(CoinInfoBithumb(currencyList.ENJ, "ENJ / 엔진코인", R.drawable.enj))
            coinInfos.add(CoinInfoBithumb(currencyList.ELF, "ELF / 엘프", R.drawable.elf))
            coinInfos.add(CoinInfoBithumb(currencyList.FX, "FX / 펑션엑스", R.drawable.fx))
            coinInfos.add(CoinInfoBithumb(currencyList.WET, "WET / 위쇼토큰", R.drawable.wet))
            coinInfos.add(CoinInfoBithumb(currencyList.PCM, "PCM / 프레시움", R.drawable.pcm))
            coinInfos.add(CoinInfoBithumb(currencyList.DVP, "DVP / 디브이피", R.drawable.dvp))
            coinInfos.add(CoinInfoBithumb(currencyList.SNT, "SNT / 스테이터스네트워크토큰", R.drawable.snt))
            coinInfos.add(CoinInfoBithumb(currencyList.CTXC, "CTXC / 코르텍스", R.drawable.ctxc))
            coinInfos.add(CoinInfoBithumb(currencyList.HYC, "HYC / 하이콘", R.drawable.hyc))
            coinInfos.add(CoinInfoBithumb(currencyList.MIX, "MIX / 믹스마블", R.drawable.mix))
            coinInfos.add(CoinInfoBithumb(currencyList.MXC, "MXC / 머신익스체인지코인", R.drawable.mxc))
            coinInfos.add(CoinInfoBithumb(currencyList.CRO, "CRO / 크립토닷컴체인", R.drawable.cro))
            coinInfos.add(CoinInfoBithumb(currencyList.WOM, "WOM / 왐토큰", R.drawable.wom))
            coinInfos.add(CoinInfoBithumb(currencyList.PIVX, "PIVX / 피벡스", R.drawable.pivx))
            coinInfos.add(CoinInfoBithumb(currencyList.INS, "INS / 아이앤에스", R.drawable.ins))
            coinInfos.add(CoinInfoBithumb(currencyList.OMG, "OMG / 오미세고", R.drawable.omg))
            coinInfos.add(CoinInfoBithumb(currencyList.QKC, "QKC / 쿼크체인", R.drawable.qkc))
            coinInfos.add(CoinInfoBithumb(currencyList.OGO, "OGO / 오리고", R.drawable.ogo))
            coinInfos.add(CoinInfoBithumb(currencyList.CHR, "CHR / 크로미아", R.drawable.chr))
            coinInfos.add(CoinInfoBithumb(currencyList.DAC, "DAC / 다빈치", R.drawable.dac))
            coinInfos.add(CoinInfoBithumb(currencyList.STEEM, "STEEM / 스팀", R.drawable.steem))
            coinInfos.add(CoinInfoBithumb(currencyList.VALOR, "VALOR / 밸러토큰", R.drawable.valor))
            coinInfos.add(CoinInfoBithumb(currencyList.LBA, "LBA / 크레드", R.drawable.lba))
            coinInfos.add(CoinInfoBithumb(currencyList.TRV, "TRV / 트러스트버스", R.drawable.trv))
            coinInfos.add(CoinInfoBithumb(currencyList.XVG, "XVG / 버지", R.drawable.xvg))
            coinInfos.add(CoinInfoBithumb(currencyList.GNT, "GNT / 골렘", R.drawable.gnt))
            coinInfos.add(CoinInfoBithumb(currencyList.LAMB, "LAMB / 람다", R.drawable.lamb))
            coinInfos.add(CoinInfoBithumb(currencyList.RNT, "RNT / 원루트 네트워크", R.drawable.rnt))
            coinInfos.add(CoinInfoBithumb(currencyList.ZRX, "ZRX / 제로엑스", R.drawable.zrx))
            coinInfos.add(CoinInfoBithumb(currencyList.WTC, "WTC / 월튼체인", R.drawable.wtc))
            coinInfos.add(CoinInfoBithumb(currencyList.XEM, "XEM / 넴", R.drawable.xem))
            coinInfos.add(CoinInfoBithumb(currencyList.BHP, "BHP / 비에이치피", R.drawable.bhp))
        }

        if (URL == huobiAddress) {
            var currencyList = currencysInput as CurrencysHuobi

            coinInfos.add(CoinInfoHuobi(currencyList.btc, "BTC / 비트코인", R.drawable.btc))
            coinInfos.add(CoinInfoHuobi(currencyList.usdt, "USDT / 테더", R.drawable.usdt))
            coinInfos.add(CoinInfoHuobi(currencyList.eth, "ETH / 이더리움", R.drawable.eth))
            coinInfos.add(CoinInfoHuobi(currencyList.eos, "EOS / 이오스", R.drawable.eos))
            coinInfos.add(CoinInfoHuobi(currencyList.xrp, "XRP / 리플", R.drawable.xrp))
            coinInfos.add(CoinInfoHuobi(currencyList.pci, "PCI / 페이코인", R.drawable.pci))
            coinInfos.add(CoinInfoHuobi(currencyList.bch, "BCH / 비트코인캐시", R.drawable.bch))
            coinInfos.add(CoinInfoHuobi(currencyList.bsv, "BSV / 비트코인SV", R.drawable.bsv))
            coinInfos.add(CoinInfoHuobi(currencyList.ada, "ADA / 카르다노", R.drawable.ada))
            coinInfos.add(CoinInfoHuobi(currencyList.ht, "HT / 후오비토큰", R.drawable.ht))
            coinInfos.add(CoinInfoHuobi(currencyList.trx, "TRX / 트론", R.drawable.trx))
            coinInfos.add(CoinInfoHuobi(currencyList.xlm, "XLM / 스텔라루멘", R.drawable.xlm))
            coinInfos.add(CoinInfoHuobi(currencyList.ltc, "LTC / 라이트코인", R.drawable.ltc))
            coinInfos.add(CoinInfoHuobi(currencyList.iost, "IOST / 아이오에스토큰", R.drawable.iost))
            coinInfos.add(CoinInfoHuobi(currencyList.ont, "ONT / 온톨로지", R.drawable.ont))
            coinInfos.add(CoinInfoHuobi(currencyList.grs, "GRS / 그로스톨코인", R.drawable.grs))
            coinInfos.add(CoinInfoHuobi(currencyList.btm, "BTM / 바이텀", R.drawable.btm))
            coinInfos.add(CoinInfoHuobi(currencyList.solve, "SOLVE / 솔브케어", R.drawable.solve))
            coinInfos.add(CoinInfoHuobi(currencyList.uip, "UIP / 언리미티드IP", R.drawable.uip))
            coinInfos.add(CoinInfoHuobi(currencyList.mvl, "MVL / 엠블", R.drawable.mvl))
            coinInfos.add(CoinInfoHuobi(currencyList.fit, "FIT / FIT 토큰", R.drawable.fit))
            coinInfos.add(CoinInfoHuobi(currencyList.skm, "SKM / 스크럼블 네트워크", R.drawable.skm))
        }

        for (i in coinInfos.indices) {
            try {
                if (coinInfos[i]?.coinData == null) {
                    // var temp = coinInfos[i]!!
                    coinInfos.removeAt(i)
                }
            } catch (e: IndexOutOfBoundsException) {
                break
            }
        }

        return coinInfos
    }

    private fun println(data: String) {
        Log.d("ArrayMaker", data)
    }
}