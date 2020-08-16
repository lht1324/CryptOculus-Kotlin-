package overeasy.project.cryptoculuskt.function

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Retrofit2Interface {
    @GET("ticker?")
    fun getCoinone(@Query("currency") currency: String?): Call<Any>
    @GET("public/ticker/ALL_KRW")
    fun getBithumb(): Call<Any>
    @GET("market/tickers")
    fun getHuobi(): Call<Any>
}