package overeasy.project.cryptoculuskt

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.CheckedTextView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import overeasy.project.cryptoculuskt.coinInfo.CoinInfo
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoBithumb
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoCoinone
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoHuobi
import overeasy.project.cryptoculuskt.function.DataTransferOption
import overeasy.project.cryptoculuskt.function.ItemTouchHelperCallback
import java.util.*
import kotlin.collections.ArrayList

class OptionActivity : AppCompatActivity(), DataTransferOption, TextWatcher {
    var optionAdapter: OptionAdapter = OptionAdapter(this)
    var recyclerView: RecyclerView? = null
    var checkedTextView: CheckedTextView = findViewById(R.id.checkedTextView)
    var editText: EditText = findViewById(R.id.editText)

    var callback: ItemTouchHelper.Callback? = null
    var itemTouchHelper: ItemTouchHelper? = null

    var coinInfos: ArrayList<CoinInfo?> = ArrayList<CoinInfo?>()

    var coinoneAddress = "https://api.coinone.co.kr/"
    var bithumbAddress = "https://api.bithumb.com/"
    var huobiAddress = "https://api-cloud.huobi.co.kr/"
    var URL: String? = coinoneAddress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        var intent: Intent = intent

        URL = intent.extras!!.getString("URL")

        var temp: ArrayList<CoinInfo?> = intent.getSerializableExtra("coinInfo") as ArrayList<CoinInfo?>

        for (i in temp.indices)
            coinInfos.add(temp[i])
        /* if (URL == coinoneAddress) {
            var temp: ArrayList<CoinInfoCoinone> = intent.getSerializableExtra("coinInfosCoinone") as ArrayList<CoinInfoCoinone>

            for (i in temp.indices)
                coinInfos.add(temp[i])
        }

        if (URL == bithumbAddress) {
            var temp: ArrayList<CoinInfoBithumb> = intent.getSerializableExtra("coinInfosBithumb") as ArrayList<CoinInfoBithumb>

            for (i in temp.indices)
                coinInfos.add(temp[i])
        }

        if (URL == huobiAddress) {
            var temp: ArrayList<CoinInfoHuobi> = intent.getSerializableExtra("coinInfosHuobi") as ArrayList<CoinInfoHuobi>

            for (i in temp.indices)
                coinInfos.add(temp[i])
        } */

        editText.addTextChangedListener(this)

        checkedTextView.setOnClickListener {
            checkedTextView.toggle()
            optionAdapter.selectAll(checkedTextView.isChecked)
            optionAdapter.notifyDataSetChanged()
        }

        setData()
        init()
    }

    override fun onBackPressed() {
        var intent: Intent = Intent()
        intent.putExtra("URL", URL)

        optionAdapter.sortCoinInfos()

        if (URL == coinoneAddress)
            intent.putExtra("coinInfosCoinone", optionAdapter.coinInfos)
        if (URL == bithumbAddress)
            intent.putExtra("coinInfosBithumb", optionAdapter.coinInfos)
        if (URL == huobiAddress)
            intent.putExtra("coinInfosHuobi", optionAdapter.coinInfos)

        setResult(2, intent)
        finish()
        super.onBackPressed()
    }

    fun setData() {
        callback = ItemTouchHelperCallback(optionAdapter)
        itemTouchHelper = ItemTouchHelper(callback!!)

        optionAdapter.URL = URL!!

        var count: Int = 0

        for (i in coinInfos.indices) {
            if (coinInfos[i]!!.coinViewCheck) {
                if (count != i) {
                    var temp = coinInfos[i]
                    coinInfos[i] = null

                    for (j in (i - 1) downTo count)
                        Collections.swap(coinInfos, j, j + 1)

                    coinInfos[count] = temp

                    count++
                }
                else
                    count++
            }
        }

        for (i in coinInfos.indices) {
            if (!(coinInfos[i] as CoinInfoCoinone).coinViewCheck)
                checkedTextView.isChecked = false
            if ((coinInfos[i] as CoinInfoCoinone).coinViewCheck and (i == coinInfos.size - 1))
                checkedTextView.isChecked = true

            optionAdapter.addItem(coinInfos[i]!!)
        }

        /* if (URL == coinoneAddress) {
            for (i in coinInfos.indices) {
                if ((coinInfos[i] as CoinInfoCoinone).coinViewCheck) {
                    if (count != i) {
                        var temp: CoinInfoCoinone = coinInfos[i] as CoinInfoCoinone
                        coinInfos.set(i, null)

                        for (j in (i - 1) downTo count)
                            Collections.swap(coinInfos, j, j + 1)

                        coinInfos[count] = temp

                        count++
                    }
                    else
                        count++
                }
            }

            for (i in coinInfos.indices) {
                if (!(coinInfos[i] as CoinInfoCoinone).coinViewCheck)
                    checkedTextView.isChecked = false
                if ((coinInfos[i] as CoinInfoCoinone).coinViewCheck and (i == coinInfos.size - 1))
                    checkedTextView.isChecked = true

                optionAdapter.addItem(coinInfos[i]!!)
            }
        }

        if (URL == bithumbAddress) {
            for (i in coinInfos.indices) {
                if ((coinInfos[i] as CoinInfoBithumb).coinViewCheck) {
                    if (count != i) {
                        var temp: CoinInfoBithumb = coinInfos[i] as CoinInfoBithumb
                        coinInfos.set(i, null)

                        for (j in (i - 1) downTo count)
                            Collections.swap(coinInfos, j, j + 1)

                        coinInfos[count] = temp

                        count++
                    }
                    else
                        count++
                }
            }

            for (i in coinInfos.indices) {
                if (!(coinInfos[i] as CoinInfoBithumb).coinViewCheck)
                    checkedTextView.isChecked = false
                if ((coinInfos[i] as CoinInfoBithumb).coinViewCheck and (i == coinInfos.size - 1))
                    checkedTextView.isChecked = true

                optionAdapter.addItem(coinInfos[i]!!)
            }
        }

        if (URL == huobiAddress) {
            for (i in coinInfos.indices) {
                if ((coinInfos[i] as CoinInfoHuobi).coinViewCheck) {
                    if (count != i) {
                        var temp: CoinInfoHuobi = coinInfos[i] as CoinInfoHuobi
                        coinInfos.set(i, null)

                        for (j in (i - 1) downTo count)
                            Collections.swap(coinInfos, j, j + 1)

                        coinInfos[count] = temp

                        count++
                    }
                    else
                        count++
                }
            }

            for (i in coinInfos.indices) {
                if (!(coinInfos[i] as CoinInfoHuobi).coinViewCheck)
                    checkedTextView.isChecked = false
                if ((coinInfos[i] as CoinInfoHuobi).coinViewCheck and (i == coinInfos.size - 1))
                    checkedTextView.isChecked = true

                optionAdapter.addItem(coinInfos[i]!!)
            }
        } */
    }

    fun init() {
        recyclerView = findViewById(R.id.recyclerView)

        var layoutManager: LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager

        if (URL == coinoneAddress) {
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(0xFF0079FE.toInt()))
            supportActionBar!!.title = "Coinone"
        }
        else if (URL == bithumbAddress) {
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(0xFFF37321.toInt()))
            supportActionBar!!.title = "Bithumb"
        }
        else {
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(0xFF1C2143.toInt()))
            supportActionBar!!.title = "Huobi"
        }

        itemTouchHelper!!.attachToRecyclerView(recyclerView)
        recyclerView!!.adapter = optionAdapter
    }

    override fun changeSelectAll(selectAll: Boolean) {
        checkedTextView.isChecked = selectAll
    }

    override fun getEditTextIsEmpty(): Boolean {
        return editText.text.isEmpty()
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        optionAdapter.getFilter().filter(charSequence)

        if (charSequence == "") {
            setData()
            init()
        }
    }

    override fun afterTextChanged(editable: Editable) {

    }

    fun println(data: String) {
        Log.d("OptionActivity", data)
    }
}
