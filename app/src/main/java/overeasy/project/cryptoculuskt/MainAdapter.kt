package overeasy.project.cryptoculuskt

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import overeasy.project.cryptoculuskt.coinInfo.CoinInfo
import overeasy.project.cryptoculuskt.ticker.TickerBithumb
import overeasy.project.cryptoculuskt.ticker.TickerCoinone
import overeasy.project.cryptoculuskt.ticker.TickerHuobi
import java.text.DecimalFormat
import kotlin.math.roundToInt

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    var coinInfos: ArrayList<CoinInfo> = ArrayList<CoinInfo>()

    var coinoneAddress: String = "https://api.coinone.co.kr/"
    var bithumbAddress: String = "https://api.bithumb.com/"
    var huobiAddress: String = "https://api-cloud.huobi.co.kr/"
    var URL: String = coinoneAddress

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageView: ImageView = itemView.findViewById(R.id.imageView)
        private var textView: TextView = itemView.findViewById(R.id.textView)
        private var textView2: TextView = itemView.findViewById(R.id.textView2)
        private var textView3: TextView = itemView.findViewById(R.id.textView3)
        private var textView4: TextView = itemView.findViewById(R.id.textView4)
        private var textView5: TextView = itemView.findViewById(R.id.textView5)
        private var textView6: TextView = itemView.findViewById(R.id.textView6)
        private var textView7: TextView = itemView.findViewById(R.id.textView7)

        private lateinit var formatter: DecimalFormat

        fun setItem(coinInfo: CoinInfo) {
            imageView!!.setImageResource(coinInfo.coinImageIndex)
            textView!!.text = coinInfo.coinName

            if (URL == coinoneAddress) {
                var ticker = coinInfo.coinData as TickerCoinone

                formatter = if (ticker!!.last.toDouble() - ticker!!.last.toDouble().roundToInt() != 0.0) DecimalFormat("###,##0.00") else DecimalFormat("###,###")

                textView2!!.text = "현재가 : " + formatter.format(ticker!!.last.toDouble()) + "원"
                textView3!!.text = "거래량 : " + formatter.format(ticker!!.volume.toDouble())

                textView4!!.text = "시가 : " + formatter.format(ticker!!.first.toDouble())
                textView5!!.text = "종가 : " + formatter.format(ticker!!.last.toDouble())
                textView6!!.text = "고가 : " + formatter.format(ticker!!.high.toDouble())
                textView7!!.text = "저가 : " + formatter.format(ticker!!.low.toDouble())
            }

            if (URL == bithumbAddress) {
                var ticker = coinInfo.coinData as TickerBithumb

                formatter = if (ticker!!.closing_price.toDouble() - ticker!!.closing_price.toDouble().roundToInt() != 0.0) DecimalFormat("###,##0.00") else DecimalFormat("###,###")

                textView2!!.text = "현재가 : " + formatter.format(ticker!!.closing_price.toDouble()) + "원"
                textView3!!.text = "거래량 : " + formatter.format(ticker!!.units_traded.toDouble())

                textView4!!.text = "시가 : " + formatter.format(ticker!!.opening_price.toDouble()) + "원"
                textView5!!.text = "종가 : " + formatter.format(ticker!!.closing_price.toDouble()) + "원"
                textView6!!.text = "고가 : " + formatter.format(ticker!!.max_price.toDouble()) + "원"
                textView7!!.text = "저가 : " + formatter.format(ticker!!.min_price.toDouble()) + "원"
            }

            if (URL == huobiAddress) {
                var ticker = coinInfo.coinData as TickerHuobi

                formatter = if (ticker!!.close.toDouble() - ticker!!.close.toDouble().roundToInt() != 0.0) DecimalFormat("###,##0.00") else DecimalFormat("###,###")

                textView2!!.text = "현재가 : " + formatter.format(ticker!!.close.toDouble()) + "원"
                textView3!!.text = "거래량 : " + formatter.format(ticker!!.amount.toDouble())

                textView4!!.text = "시가 : " + formatter.format(ticker!!.open.toDouble()) + "원"
                textView5!!.text = "종가 : " + formatter.format(ticker!!.close.toDouble()) + "원"
                textView6!!.text = "고가 : " + formatter.format(ticker!!.high.toDouble()) + "원"
                textView7!!.text = "저가 : " + formatter.format(ticker!!.low.toDouble()) + "원"
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(viewGroup.getContext())

        return ViewHolder(inflater.inflate(R.layout.coin_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setItem(coinInfos[position])
    }

    override fun getItemCount(): Int {
        return coinInfos.size
    }

    fun addItem(coinInfo: CoinInfo) {
        coinInfos.add(coinInfo)
    }

    fun println(data: String) {
        Log.d("MainAdapter", data)
    }
}