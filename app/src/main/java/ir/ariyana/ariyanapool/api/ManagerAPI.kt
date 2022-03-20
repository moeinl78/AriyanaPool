package ir.ariyana.ariyanapool.api

import ir.ariyana.ariyanapool.data.news.DataNews
import ir.ariyana.ariyanapool.data.trend_crypto.TrendCrypto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://min-api.cryptocompare.com/data/"
const val BASE_URL_IMAGE = "https://www.cryptocompare.com"
const val API_KEY = "4193081fd30e216fe400a1a2fd0a2d150946a62dba29a043d8b70e65f0e02563"

class ManagerAPI {
    private val serviceAPI : ServiceAPI
    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        serviceAPI = retrofit.create(ServiceAPI::class.java)
    }

    fun managerRequestNews(callbackAPI: CallbackAPI<DataNews>) {

        serviceAPI
            .requestNews()
            .enqueue(object : Callback<DataNews> {

                override fun onResponse(call: Call<DataNews>, response: Response<DataNews>) {
                    val result = response.body()!!
                    callbackAPI.onSuccessfulRequest(result)
                }

                override fun onFailure(call: Call<DataNews>, t: Throwable) {
                    val errorMessage = t.message!!
                    callbackAPI.onFailedRequest(errorMessage)
                }
            })
    }

    fun managerRequestTrendCrypto(callbackAPI: CallbackAPI<TrendCrypto>) {

        serviceAPI
            .requestTrendCrypto()
            .enqueue(object : Callback<TrendCrypto> {

                override fun onResponse(call: Call<TrendCrypto>, response: Response<TrendCrypto>) {
                    val result = response.body()!!
                    callbackAPI.onSuccessfulRequest(result)
                }

                override fun onFailure(call: Call<TrendCrypto>, t: Throwable) {
                    val errorMessage = t.message!!
                    callbackAPI.onFailedRequest(errorMessage)
                }
            })
    }

    interface CallbackAPI<T> {

        fun onSuccessfulRequest(data : T)
        fun onFailedRequest(error : String)
    }
}