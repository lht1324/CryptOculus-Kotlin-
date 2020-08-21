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
    var coinInfos = ArrayList<CoinInfo?>()
    var filteredCoinInfos = ArrayList<CoinInfo?>()
    var unFilteredCoinInfos = ArrayList<CoinInfo?>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var checkedTextView: CheckedTextView = itemView.findViewById(R.id.checkedTextView)

        fun setItem(coinInfo: CoinInfo) {
            imageView.setImageResource(coinInfo.coinImageIndex)
            checkedTextView.text = coinInfo.coinName
            checkedTextView.isChecked = coinInfo.coinViewCheck
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(viewGroup.getContext())
        var itemView: View = inflater.inflate(R.layout.option_item, viewGroup, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setItem(filteredCoinInfos[position]!!)

        if (!filteredCoinInfos[position]!!.coinViewCheck)
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
                    if ((i == filteredCoinInfos.size - 1) && filteredCoinInfos[i]!!.coinViewCheck)
                        mCallback.changeSelectAll(true)
                }
            }

            else {
                mCallback.changeSelectAll(false)
            }
        }
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

                if (charString.isEmpty())
                    filteredCoinInfos = unFilteredCoinInfos

                else {
                    var filteringList = ArrayList<CoinInfo?>()

                    var count: Int = 0

                    for (i in unFilteredCoinInfos.indices) {
                        var coinInfo = unFilteredCoinInfos[i]
                        var temp = coinInfo!!.coinName

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
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    fun println(data: String) {
        Log.d("OptionAdapter", data)
    }
}