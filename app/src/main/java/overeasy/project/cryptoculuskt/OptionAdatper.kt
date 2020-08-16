package overeasy.project.cryptoculuskt

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.Filter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import overeasy.project.cryptoculuskt.coinInfo.CoinInfo
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoBithumb
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoCoinone
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoHuobi
import overeasy.project.cryptoculuskt.function.DataTransferOption
import overeasy.project.cryptoculuskt.function.ItemTouchHelperCallback
import overeasy.project.cryptoculuskt.ticker.TickerCoinone
import java.util.*
import kotlin.collections.ArrayList

class OptionAdapter(private var mCallback: DataTransferOption) : RecyclerView.Adapter<OptionAdapter.ViewHolder>(), ItemTouchHelperCallback.ItemTouchHelperAdapter  {
    var coinInfos: ArrayList<CoinInfo?> = ArrayList<CoinInfo?>()
    var filteredCoinInfos: ArrayList<CoinInfo?> = ArrayList<CoinInfo?>()
    var unFilteredCoinInfos: ArrayList<CoinInfo?> = ArrayList<CoinInfo?>()

    var coinoneAddress = "https://api.coinone.co.kr/"
    var bithumbAddress = "https://api.bithumb.com/"
    var huobiAddress = "https://api-cloud.huobi.co.kr/"
    var URL = coinoneAddress

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var checkedTextView: CheckedTextView = itemView.findViewById(R.id.checkedTextView)

        /* fun setItemCoinone(coinInfo: CoinInfoCoinone) {
            imageView.setImageResource(coinInfo.coinImageIndex)
            checkedTextView.text = "${(coinInfo.coinData as TickerCoinone)!!.currency.toUpperCase()} / ${coinInfo.coinName}"
            checkedTextView.setChecked(coinInfo.coinViewCheck)
        }

        fun setItemBithumb(coinInfo: CoinInfoBithumb) {
            imageView.setImageResource(coinInfo.coinImageIndex)
            checkedTextView.text = coinInfo.coinName
            checkedTextView.setChecked(coinInfo.coinViewCheck)
        }

        fun setItemHuobi(coinInfo: CoinInfoHuobi) {
            imageView.setImageResource(coinInfo.coinImageIndex)
            checkedTextView.text = coinInfo.coinName
            checkedTextView.setChecked(coinInfo.coinViewCheck)
        } */

        fun setItem(coinInfo: CoinInfo) {
            imageView.setImageResource(coinInfo.coinImageIndex)
            checkedTextView.text = coinInfo.coinName
            checkedTextView.isChecked = coinInfo.coinViewCheck
            // ArrayMaker에서 코인원 coinName 넣을 때 그냥 currency랑 합쳐서 넣어버려?
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): OptionAdapter.ViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(viewGroup.getContext())
        var itemView: View = inflater.inflate(R.layout.option_item, viewGroup, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setItem(filteredCoinInfos[position]!!)

        if (filteredCoinInfos[position]!!.coinViewCheck)
            mCallback.changeSelectAll(false)

        viewHolder.checkedTextView.setOnClickListener {
            val checkedTextView = it as CheckedTextView
            checkedTextView.toggle()
            filteredCoinInfos[position]!!.coinViewCheck = checkedTextView.isChecked

            val temp1 = filteredCoinInfos[position]!!.coinName

            for (i in coinInfos.indices) {
                val temp2 = coinInfos[i]!!.coinName
                if (temp1 == temp2)
                    coinInfos[i]!!.coinViewCheck = checkedTextView.isChecked
            }

            if (checkedTextView.isChecked) {
                for (i in filteredCoinInfos.indices) {
                    if (!filteredCoinInfos[i]!!.coinViewCheck)
                        break
                    if ((i == filteredCoinInfos.size - 1) and filteredCoinInfos[i]!!.coinViewCheck)
                        mCallback.changeSelectAll(true)
                }
            }

            else {
                mCallback.changeSelectAll(false)
            }
        }
        /* if (URL == coinoneAddress) {
            viewHolder.setItemCoinone(filteredCoinInfos[position] as CoinInfoCoinone?)
            if (!(filteredCoinInfos[position] as CoinInfoCoinone).getCoinViewCheck()) mCallback.changeSelectAll(
                false
            )
            viewHolder.checkedTextView.setOnClickListener { view ->
                val checkedTextView = view as CheckedTextView
                checkedTextView.toggle()
                (filteredCoinInfos[finalPosition] as CoinInfoCoinone).setCoinViewCheck(checkedTextView.isChecked)
                for (i in coinInfos.indices) {
                    val temp1: String =
                        (filteredCoinInfos[finalPosition] as CoinInfoCoinone).getCoinName()
                    val temp2: String =
                        (coinInfos[i] as CoinInfoCoinone).getCoinName()
                    if (temp1 == temp2) (coinInfos[i] as CoinInfoCoinone).setCoinViewCheck(
                        checkedTextView.isChecked
                    )
                }
                if (checkedTextView.isChecked) { // checkedTextView가 체크됐을 때
                    for (i in filteredCoinInfos.indices) {
                        if (!(filteredCoinInfos[i] as CoinInfoCoinone).getCoinViewCheck()) break
                        if (i == filteredCoinInfos.size - 1 && (filteredCoinInfos[i] as CoinInfoCoinone).getCoinViewCheck()) mCallback.changeSelectAll(
                            true
                        )
                    }
                }
                if (!checkedTextView.isChecked) { // checkedTextView의 체크가 풀렸을 때
                    mCallback.changeSelectAll(false)
                }
            }
        }

        if (URL == bithumbAddress) {
            viewHolder.setItemBithumb(filteredCoinInfos[position] as CoinInfoBithumb?)
            if (!(filteredCoinInfos[position] as CoinInfoBithumb).getCoinViewCheck()) mCallback.changeSelectAll(
                false
            )
            viewHolder.checkedTextView.setOnClickListener { view ->
                val checkedTextView = view as CheckedTextView
                checkedTextView.toggle()
                (filteredCoinInfos[finalPosition] as CoinInfoBithumb).setCoinViewCheck(
                    checkedTextView.isChecked
                )
                for (i in coinInfos.indices) {
                    val temp1: String =
                        (filteredCoinInfos[finalPosition] as CoinInfoBithumb).getCoinName()
                    val temp2: String =
                        (coinInfos[i] as CoinInfoBithumb).getCoinName()
                    if (temp1 == temp2) (coinInfos[i] as CoinInfoBithumb).setCoinViewCheck(
                        checkedTextView.isChecked
                    )
                }
                if (checkedTextView.isChecked) {
                    for (i in filteredCoinInfos.indices) {
                        if (!(filteredCoinInfos[i] as CoinInfoBithumb).getCoinViewCheck()) break
                        if (i == filteredCoinInfos.size - 1 && (filteredCoinInfos[i] as CoinInfoBithumb).getCoinViewCheck()) mCallback.changeSelectAll(
                            true
                        )
                    }
                }
                if (!checkedTextView.isChecked) {
                    mCallback.changeSelectAll(false)
                }
            }
        }

        if (URL == huobiAddress) {
            viewHolder.setItemHuobi(filteredCoinInfos[position] as CoinInfoHuobi?)
            if (!(filteredCoinInfos[position] as CoinInfoHuobi).getCoinViewCheck()) mCallback.changeSelectAll(
                false
            )
            viewHolder.checkedTextView.setOnClickListener { view ->
                val checkedTextView = view as CheckedTextView
                checkedTextView.toggle()
                (filteredCoinInfos[finalPosition] as CoinInfoHuobi).setCoinViewCheck(
                    checkedTextView.isChecked
                )
                for (i in coinInfos.indices) {
                    val temp1: String =
                        (filteredCoinInfos[finalPosition] as CoinInfoHuobi).getCoinName()
                    val temp2: String = (coinInfos[i] as CoinInfoHuobi).getCoinName()
                    if (temp1 == temp2) (coinInfos[i] as CoinInfoHuobi).setCoinViewCheck(
                        checkedTextView.isChecked
                    )
                }
                if (checkedTextView.isChecked) { // checkedTextView가 체크됐을 때
                    for (i in filteredCoinInfos.indices) {
                        if (!(filteredCoinInfos[i] as CoinInfoHuobi).getCoinViewCheck()) break
                        if (i == filteredCoinInfos.size - 1 && (filteredCoinInfos[i] as CoinInfoHuobi).getCoinViewCheck()) mCallback.changeSelectAll(
                            true
                        )
                    }
                }
                if (!checkedTextView.isChecked) { // checkedTextView의 체크가 풀렸을 때
                    mCallback.changeSelectAll(false)
                }
            }
        } */
    }

    override fun getItemCount(): Int {
        return filteredCoinInfos.size
    }

    fun addItem(coinInfo: CoinInfo) {
        coinInfos.add(coinInfo)
        filteredCoinInfos.add(coinInfo)
        unFilteredCoinInfos.add(coinInfo)
    }

    fun selectAll(checkAll: Boolean) {
        if (checkAll) {
            for (i in filteredCoinInfos.indices) {
                filteredCoinInfos[i]!!.coinViewCheck = true

                if (mCallback.getEditTextIsEmpty())
                    coinInfos[i]!!.coinViewCheck = true
            }
        }
        else {
            for (i in filteredCoinInfos.indices) {
                filteredCoinInfos[i]!!.coinViewCheck = false

                if (mCallback.getEditTextIsEmpty())
                    coinInfos[i]!!.coinViewCheck = false
            }
        }
        /* if (URL == coinoneAddress) {
            if (checkAll) {
                for (i in filteredCoinInfos.indices) {
                    (filteredCoinInfos[i] as CoinInfoCoinone).coinViewCheck = true

                    if (mCallback.getEditTextIsEmpty())
                        (coinInfos[i] as CoinInfoCoinone).coinViewCheck = true
                }
            }
            else {
                for (i in filteredCoinInfos.indices) {
                    (filteredCoinInfos[i] as CoinInfoCoinone).coinViewCheck = false

                    if (mCallback.getEditTextIsEmpty())
                        (coinInfos[i] as CoinInfoCoinone).coinViewCheck = false

                }
            }
        }
        if (URL == bithumbAddress) {
            if (checkAll) {
                for (i in filteredCoinInfos.indices) {
                    (filteredCoinInfos[i] as CoinInfoBithumb).coinViewCheck = true

                    if (mCallback.getEditTextIsEmpty())
                        (coinInfos[i] as CoinInfoBithumb).coinViewCheck = true
                }
            }
            else {
                for (i in filteredCoinInfos.indices) {
                    (filteredCoinInfos[i] as CoinInfoBithumb).coinViewCheck = false

                    if (mCallback.getEditTextIsEmpty())
                        (coinInfos[i] as CoinInfoBithumb).coinViewCheck = false

                }
            }
        }
        if (URL == huobiAddress) {
            if (checkAll) {
                for (i in filteredCoinInfos.indices) {
                    (filteredCoinInfos[i] as CoinInfoHuobi).coinViewCheck = true

                    if (mCallback.getEditTextIsEmpty())
                        (coinInfos[i] as CoinInfoHuobi).coinViewCheck = true
                }
            }
            else {
                for (i in filteredCoinInfos.indices) {
                    (filteredCoinInfos[i] as CoinInfoHuobi).coinViewCheck = false

                    if (mCallback.getEditTextIsEmpty())
                        (coinInfos[i] as CoinInfoHuobi).coinViewCheck = false

                }
            }
        } */
    }

    fun sortCoinInfos() {
        for (i in filteredCoinInfos.indices) {
            var temp1 = filteredCoinInfos[i]!!.coinName
            var temp2 = coinInfos[i]!!.coinName

            if (temp1 != temp2) { // 순서가 바뀐 것
                for (j in coinInfos.indices) {
                    if (temp1 == coinInfos[j]!!.coinName)
                        Collections.swap(coinInfos, i, j)
                }
            }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                var charString = constraint.toString()

                // 상속은 만능이 아니다
                // 공통으로 묶을 수 있는 것만 묶자

                if (charString.isEmpty())
                    filteredCoinInfos = unFilteredCoinInfos

                else {
                    var filteringList: ArrayList<CoinInfo?> = ArrayList<CoinInfo?>()

                    var count: Int = 0

                    for (i in unFilteredCoinInfos.indices) {
                        var coinInfo = unFilteredCoinInfos[i]
                        var temp = ""

                        if (URL == coinoneAddress)
                            temp = "${(coinInfo!!.coinData as TickerCoinone)!!.currency.toUpperCase()} / ${coinInfo!!.coinName}"
                        else
                            temp = coinInfo!!.coinName

                        // var temp = coinInfo!!.coinName

                        if (temp.toLowerCase().contains(charString.toLowerCase()))
                            filteringList.add(unFilteredCoinInfos[i])
                    }

                    for (i in filteringList.indices) {
                        if (filteringList[i]!!.coinViewCheck) {
                            if (count != i) {
                                var temp = filteringList[i]
                                filteringList[i] = null

                                for (j in i - 1 downTo count)
                                    Collections.swap(filteringList, j, j + 1)

                                coinInfos[count] = temp!!

                                count++
                            }
                            else
                                count++
                        }
                    }

                    filteredCoinInfos = filteringList
                }

                var filterResults = FilterResults()

                if (coinInfos.size != filteredCoinInfos.size) // 검색창이 비었을 때, editText.isEmpty()
                    filterResults.values = coinInfos
                else
                    filterResults.values = filteredCoinInfos

                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                coinInfos = results.values as ArrayList<CoinInfo?>
                notifyDataSetChanged()
            }
        }
    }
    /* fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                var charString = constraint.toString()

                // 상속은 만능이 아니다
                // 공통으로 묶을 수 있는 것만 묶자
                if (URL == coinoneAddress) {
                    for (i in unFilteredCoinInfos.indices) {
                        unFilteredCoinInfos[i].coinData = unFilteredCoinInfos[i].coinData as TickerCoinone
                    }
                }
                if (charString.isEmpty())
                    filteredCoinInfos = unFilteredCoinInfos

                else {
                    var filteringList: ArrayList<Any?> = ArrayList<Any?>()

                    var count: Int = 0

                    if (URL == coinoneAddress) {
                        for (i in unFilteredCoinInfos.indices) {
                            var coinInfo = unFilteredCoinInfos[i]
                            var temp = "${coinInfo.coinData!!.currency.toUpperCase()} / ${coinInfo.coinName}"

                            if (temp.toLowerCase().contains(charString.toLowerCase()))
                                filteringList.add(unFilteredCoinInfos[i])
                        }

                        for (i in filteringList.indices) {
                            if ((filteringList[i] as CoinInfoCoinone).coinViewCheck) {
                                if (count != i) {
                                    var temp = filteringList[i] as CoinInfoCoinone
                                    filteringList.set(i, null)

                                    for (j in i - 1 downTo count)
                                        Collections.swap(filteringList, j, j + 1)

                                    coinInfos.set(count, temp!!)

                                    count++
                                }
                                else
                                    count++
                            }
                        }
                    }

                    if (URL == bithumbAddress) {
                        for (i in unFilteredCoinInfos.indices) {
                            var coinInfo = unFilteredCoinInfos[i] as CoinInfoBithumb
                            var temp = coinInfo.coinName

                            if (temp.toLowerCase().contains(charString.toLowerCase()))
                                filteringList.add(unFilteredCoinInfos[i])
                        }

                        for (i in filteringList.indices) {
                            if ((filteringList[i] as CoinInfoBithumb).coinViewCheck) {
                                if (count != i) {
                                    var temp = filteringList[i] as CoinInfoBithumb

                                    filteringList.set(i, null)

                                    for (j in i - 1 downTo count)
                                        Collections.swap(filteringList, j, j + 1)

                                    coinInfos.set(count, temp!!)

                                    count++
                                }
                                else
                                    count++
                            }
                        }
                    }

                    if (URL == huobiAddress) {
                        for (i in unFilteredCoinInfos.indices) {
                            var coinInfo = unFilteredCoinInfos[i] as CoinInfoHuobi
                            var temp = coinInfo.coinName

                            if (temp.toLowerCase().contains(charString.toLowerCase()))
                                filteringList.add(unFilteredCoinInfos[i])
                        }

                        for (i in filteringList.indices) {
                            if ((filteringList[i] as CoinInfoHuobi).coinViewCheck) {
                                if (count != i) {
                                    var temp = filteringList[i] as CoinInfoHuobi

                                    filteringList.set(i, null)

                                    for (j in i - 1 downTo count)
                                        Collections.swap(filteringList, j, j + 1)

                                    coinInfos.set(count, temp!!)

                                    count++
                                }
                                else
                                    count++
                            }
                        }
                    }

                    filteredCoinInfos = filteringList
                }

                var filterResults: FilterResults = FilterResults()

                if (coinInfos.size != filteredCoinInfos.size) // 검색창이 비었을 때, editText.isEmpty()
                    filterResults.values = coinInfos
                else
                    filterResults.values = filteredCoinInfos

                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                coinInfos = results.values as ArrayList<Any?>
                notifyDataSetChanged()
            }
        }
    } */

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(filteredCoinInfos, i, i + 1)
                Collections.swap(coinInfos, i, i + 1)
            }
        }
        else {
            for (i in fromPosition downTo (toPosition + 1)) {
                Collections.swap(filteredCoinInfos, i, i - 1)
                Collections.swap(coinInfos, i, i - 1)
            }
        }
        notifyDataSetChanged()
        return true
    }

    fun println(data: String) {
        Log.d("OptionAdapter", data)
    }
}
