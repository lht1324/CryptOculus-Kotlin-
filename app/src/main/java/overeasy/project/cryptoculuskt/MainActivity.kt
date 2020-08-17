package overeasy.project.cryptoculuskt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import overeasy.project.cryptoculuskt.coinInfo.CoinInfo
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoBithumb
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoCoinone
import overeasy.project.cryptoculuskt.coinInfo.CoinInfoHuobi
import overeasy.project.cryptoculuskt.currencys.Currencys
import overeasy.project.cryptoculuskt.currencys.CurrencysBithumb
import overeasy.project.cryptoculuskt.currencys.CurrencysCoinone
import overeasy.project.cryptoculuskt.currencys.CurrencysHuobi
import overeasy.project.cryptoculuskt.function.ArrayMaker
import overeasy.project.cryptoculuskt.function.DataTransferMain
import overeasy.project.cryptoculuskt.function.DataTransferOption
import overeasy.project.cryptoculuskt.function.Retrofit2Interface
import overeasy.project.cryptoculuskt.ticker.TickerFormatBithumb
import overeasy.project.cryptoculuskt.ticker.TickerFormatHuobi
import overeasy.project.cryptoculuskt.ticker.TickerHuobi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity(), DataTransferMain {
    var recyclerView: RecyclerView? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var spinner: Spinner? = null
    var button: Button? = null
    var spinnerAdapter: ArrayAdapter<String>? = null
    var adapter: MainAdapter? = null
    var refreshedCoinone = false
    var refreshedBithumb = false
    var refreshedHuobi = false
    var restartApp = false
    private val sendIntent = 1
    private val getIntent = 2

    var spinnerAdapterItems = ArrayList<String>()
    var coinInfos: ArrayList<CoinInfo?> = ArrayList<CoinInfo?>()
    lateinit var coinViewChecksCoinone: Array<Boolean?>
    lateinit var coinViewChecksBithumb: Array<Boolean?>
    lateinit var coinViewChecksHuobi: Array<Boolean?>

    var coinoneAddress = "https://api.coinone.co.kr/"
    var bithumbAddress = "https://api.bithumb.com/"
    var huobiAddress = "https://api-cloud.huobi.co.kr/"
    var URL = coinoneAddress // 코인원인지 빗썸 상태인지 구분하는데 사용
    var chartName = "BTC"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        spinner = findViewById(R.id.spinner)
        button = findViewById(R.id.button)

        spinner!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val result = StringBuffer()
                val temp = spinner!!.getItemAtPosition(position) as String

                for (i in temp.indices) {
                    if (temp[i].toInt() in 65 until 122)
                        result.append(temp[i])
                }

                chartName = result.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                
            }
        }

        button!!.setOnClickListener {
            var intent = Intent()

            if (URL == coinoneAddress)
                intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://coinone.co.kr/chart?site=coinone${chartName.toLowerCase()}&unit_time=15m"))
            if (URL == bithumbAddress)
                intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://m.bithumb.com/trade/chart/${chartName}_KRW"))
            if (URL == huobiAddress)
                intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.huobi.co.kr/ko-kr/exchange/${chartName.toLowerCase()}_krw/"))

            startActivity(intent)
        }
        
        recyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        
        var pref = getSharedPreferences("restartCheck", Context.MODE_PRIVATE)
        restartApp = pref.getBoolean("restartApp", false)
        
        if (restartApp) {
            pref = getSharedPreferences("URL", MODE_PRIVATE)
            URL = pref.getString("URL", coinoneAddress)!!
        }
        
        getData()
        init()

        swipeRefreshLayout!!.setOnRefreshListener {
            
            getData()
            init()
            
            swipeRefreshLayout!!.isRefreshing = false
        }
    }
    
    override fun onBackPressed() {
        var pref: SharedPreferences = getSharedPreferences("saveCoinone", MODE_PRIVATE)
        var editor: SharedPreferences.Editor = pref.edit()

        // SharedPreferences로 저장해야 하는 게 뭐지?
        // position이랑 coinViewCheck잖아?
        // 그냥 coinViewCheck 배열만 만들까?
        // coinInfos는 하나만 써도 되잖아
        /* if ((URL == coinoneAddress) and coinInfosCoinone.isNotEmpty()) {
            pref = getSharedPreferences("saveCoinone", MODE_PRIVATE)
            editor = pref.edit()

            editor.putBoolean("isEmptyCoinone", false) // 재시작 후 coinInfos의 정보를 읽어올 때 오류 방지용
        }

        if (coinInfosBithumb.isNotEmpty()) {
            pref = getSharedPreferences("saveBithumb", MODE_PRIVATE)
            editor = pref.edit()

            editor.putBoolean("isEmptyBithumb", false)
        }

        if (coinInfosHuobi.isNotEmpty()) {
            pref = getSharedPreferences("saveHuobi", MODE_PRIVATE)
            editor = pref.edit()

            editor.putBoolean("isEmptyHuobi", false)
        }

        for (i in coinInfos.indices) {
            editor.putInt("${coinInfos[i]!!.coinName} position", i)
            editor.putBoolean(coinInfos[i]!!.coinName, coinInfos[i]!!.coinViewCheck)
        } */

        editor.commit()

        pref = getSharedPreferences("URL", MODE_PRIVATE)
        editor = pref.edit()
        editor.putString("URL", URL)
        editor.commit()

        pref = getSharedPreferences("restartCheck", MODE_PRIVATE)
        editor = pref.edit()

        editor.putBoolean("restartApp", true)
        editor.commit()

        finish()
        super.onBackPressed()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == sendIntent) {
            if (resultCode == getIntent) {
                println("Datatransfer successed.")

                URL = intent!!.extras!!.getString("URL")!!

                coinInfos = intent.getSerializableExtra("coinInfos") as ArrayList<CoinInfo?> // 정상

                // 여기서 계속 초기화해서 그런 거 아냐?
                if (URL == coinoneAddress) {
                    refreshedCoinone = true

                    coinViewChecksCoinone = arrayOfNulls(coinInfos.size)

                    for (i in coinInfos.indices)
                        coinViewChecksCoinone[i] = coinInfos[i]!!.coinViewCheck
                }

                if (URL == bithumbAddress) {
                    refreshedBithumb = true

                    coinViewChecksBithumb = arrayOfNulls(coinInfos.size)

                    for (i in coinInfos.indices)
                        coinViewChecksBithumb[i] = coinInfos[i]!!.coinViewCheck
                }

                if (URL == huobiAddress) {
                    refreshedHuobi = true

                    coinViewChecksHuobi = arrayOfNulls(coinInfos.size)

                    for (i in coinInfos.indices)
                        coinViewChecksHuobi[i] = coinInfos[i]!!.coinViewCheck
                }

                getData()
                init()
            }
            else
                println("Data transfer failed.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id: Int = item.itemId

        if (id == R.id.coinone || id == R.id.bithumb || id == R.id.huobi) {
            if (id == R.id.coinone)
                URL = coinoneAddress
            if (id == R.id.bithumb)
                URL = bithumbAddress
            if (id == R.id.huobi)
                URL = huobiAddress

            getData()
            init()

            return true
        }

        if (id == R.id.option) {
            var intent = Intent(applicationContext, OptionActivity::class.java)
            intent.putExtra("URL", URL)

            intent.putExtra("coinInfos", coinInfos)

            startActivityForResult(intent, sendIntent)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getData() {
        spinnerAdapterItems = ArrayList<String>()
        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, spinnerAdapterItems)
        adapter = MainAdapter()

        adapter!!.URL = URL

        var builder = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())

        var retrofit: Retrofit = builder.build()
        var client: Retrofit2Interface = retrofit.create(Retrofit2Interface::class.java)
        var call: Call<Any> = client.getCoinone("all")

        if (URL == coinoneAddress)
            call = client.getCoinone("all")
        if (URL == bithumbAddress)
            call = client.getBithumb()
        if (URL == huobiAddress)
            call = client.getHuobi()

        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                responseProcess(response.body().toString())
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                println("Retrofit calling failed.")
            }
        })
    }

    fun responseProcess(response: String) {
        var gson = Gson()
        var arrayMaker = ArrayMaker(restartApp, refreshedCoinone, refreshedBithumb, refreshedHuobi, coinInfos, URL, this, this)

        println("responseProcess is working. refreshedCoinone = $refreshedCoinone, refreshedBithumb = $refreshedBithumb, refreshedHuobi = $refreshedHuobi")
        if (refreshedCoinone or refreshedBithumb or refreshedHuobi) { // 이거 제대로 안 먹는데?
            // 빗썸 갔다오니까 refreshedCoinone이 false가 된다
            // 재밌네
            // ArrayMaker가 문제네
            if (URL == coinoneAddress) {
                for (i in coinInfos.indices)
                    println("$i. ${coinInfos[i]!!.coinName}'s coinViewCheck = ${coinInfos[i]!!.coinViewCheck}, coinViewChecksCoinone[$i] = ${coinViewChecksCoinone[i]}")
                arrayMaker.coinViewChecks = coinViewChecksCoinone
            }
            if (URL == bithumbAddress) {
                for (i in coinInfos.indices)
                    println("$i. ${coinInfos[i]!!.coinName}'s coinViewCheck = ${coinInfos[i]!!.coinViewCheck}, coinViewChecksBithumb[$i] = ${coinViewChecksBithumb[i]}")
                arrayMaker.coinViewChecks = coinViewChecksBithumb
            }
            if (URL == huobiAddress) {
                for (i in coinInfos.indices)
                    println("$i. ${coinInfos[i]!!.coinName}'s coinViewCheck = ${coinInfos[i]!!.coinViewCheck}, coinViewChecksHuobi[$i] = ${coinViewChecksHuobi[i]}")
                arrayMaker.coinViewChecks = coinViewChecksHuobi
            }
        }

        var currencyList = Currencys()

        if (URL == coinoneAddress)
            currencyList = gson.fromJson(response, CurrencysCoinone::class.java)

        if (URL == bithumbAddress)
            currencyList = gson.fromJson(response, TickerFormatBithumb::class.java).data

        if (URL == huobiAddress) {
            var tickerFormatHuobi = gson.fromJson(response, TickerFormatHuobi::class.java)
            var tickersHuobi = ArrayList<TickerHuobi?>()
            currencyList = CurrencysHuobi()

            for (i in tickerFormatHuobi.data.indices) {
                val ticker = tickerFormatHuobi.data[i]!!

                if (ticker.symbol.contains("krw"))
                    tickersHuobi.add(ticker)
            }

            for (i in tickersHuobi.indices) {
                var ticker: TickerHuobi = tickersHuobi[i]!!

                if (ticker.symbol.contains("grs")) currencyList.grs = ticker
                if (ticker.symbol.contains("xrp")) currencyList.xrp = ticker
                if (ticker.symbol.contains("eth")) currencyList.eth = ticker
                if (ticker.symbol.contains("mvl")) currencyList.mvl = ticker
                if (ticker.symbol.contains("ada")) currencyList.ada = ticker
                if (ticker.symbol.contains("fit")) currencyList.fit = ticker
                if (ticker.symbol.contains("bsv")) currencyList.bsv = ticker
                if (ticker.symbol.contains("btm")) currencyList.btm = ticker
                if (ticker.symbol.contains("ht")) currencyList.ht = ticker
                if (ticker.symbol.contains("usdt"))currencyList.usdt = ticker
                if (ticker.symbol.contains("iost"))currencyList.iost = ticker
                if (ticker.symbol.contains("ont")) currencyList.ont = ticker
                if (ticker.symbol.contains("pci")) currencyList.pci = ticker
                if (ticker.symbol.contains("solve")) currencyList.solve = ticker
                if (ticker.symbol.contains("uip")) currencyList.uip = ticker
                if (ticker.symbol.contains("xlm")) currencyList.xlm = ticker
                if (ticker.symbol.contains("ltc")) currencyList.ltc = ticker
                if (ticker.symbol.contains("eos")) currencyList.eos = ticker
                if (ticker.symbol.contains("skm")) currencyList.skm = ticker
                if (ticker.symbol.contains("btc")) currencyList.btc = ticker
                if (ticker.symbol.contains("bch")) currencyList.bch = ticker
                if (ticker.symbol.contains("trx")) currencyList.trx = ticker
            }
        }

        coinInfos = arrayMaker.makeArray(currencyList)

        for (i in coinInfos.indices) {
            if (coinInfos[i]?.coinData != null && coinInfos[i]!!.coinViewCheck) {
                adapter!!.addItem(coinInfos[i]!!)
                spinnerAdapter!!.add(coinInfos[i]!!.coinName)
            }
        }

        adapter!!.notifyDataSetChanged()
        spinnerAdapter!!.notifyDataSetChanged()
    }
    
    private fun init() {
        adapter!!.URL = URL

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

        spinner!!.adapter = spinnerAdapter
        recyclerView!!.adapter  = adapter
    }

    override fun changeRestartApp(restartApp: Boolean) {
        this.restartApp = restartApp
    }

    override fun changeRefreshed(refreshed: Boolean) {
        if (URL == coinoneAddress)
            this.refreshedCoinone = refreshed
        if (URL == bithumbAddress)
            this.refreshedBithumb = refreshed
        if (URL == huobiAddress)
            this.refreshedHuobi = refreshed
    }

    private fun println(data: String) {
        Log.d("MainActivity", data)
    }
}