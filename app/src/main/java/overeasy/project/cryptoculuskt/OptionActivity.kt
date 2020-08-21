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
    private lateinit var recyclerView: RecyclerView
    private lateinit var editText: EditText
    lateinit var checkedTextView: CheckedTextView
    lateinit var optionAdapter: OptionAdapter

    var callback: ItemTouchHelper.Callback? = null
    var itemTouchHelper: ItemTouchHelper? = null

    var coinInfos = ArrayList<CoinInfo?>()

    var coinoneAddress = "https://api.coinone.co.kr/"
    var bithumbAddress = "https://api.bithumb.com/"
    var huobiAddress = "https://api-cloud.huobi.co.kr/"
    var URL: String? = coinoneAddress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        recyclerView = findViewById(R.id.recyclerView)
        editText = findViewById(R.id.editText)
        checkedTextView = findViewById(R.id.checkedTextView)
        optionAdapter = OptionAdapter(this)

        var intent: Intent = intent

        URL = intent.extras!!.getString("URL")

        coinInfos = intent.getSerializableExtra("optionAdapterItems") as ArrayList<CoinInfo?>

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

        intent.putExtra("mainAdapterItems", optionAdapter.coinInfos)

        setResult(2, intent)
        finish()
        super.onBackPressed()
    }

    fun setData() {
        callback = ItemTouchHelperCallback(optionAdapter)
        itemTouchHelper = ItemTouchHelper(callback!!)

        var count = 0

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
            if (!coinInfos[i]!!.coinViewCheck)
                checkedTextView.isChecked = false
            if (coinInfos[i]!!.coinViewCheck && (i == coinInfos.size - 1))
                checkedTextView.isChecked = true

            optionAdapter.addItem(coinInfos[i]!!)
        }
    }

    fun init() {
        recyclerView = findViewById(R.id.recyclerView)

        var layoutManager: LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager

        if (URL == coinoneAddress) {
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(0xFF0079FE.toInt()))
            supportActionBar!!.title = "Coinone"
        }
        if (URL == bithumbAddress) {
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(0xFFF37321.toInt()))
            supportActionBar!!.title = "Bithumb"
        }
        if (URL == huobiAddress) {
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