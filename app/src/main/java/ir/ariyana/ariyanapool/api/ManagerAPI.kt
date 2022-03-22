package ir.ariyana.ariyanapool.api

import ir.ariyana.ariyanapool.data.chart.DataChart
import ir.ariyana.ariyanapool.data.news.DataNews
import ir.ariyana.ariyanapool.data.trend_crypto.TrendCrypto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    fun managerRequestNews(callbackAPI: CallbackAPI<ArrayList<Pair<String, String>>>) {

        serviceAPI
            .requestNews()
            .enqueue(object : Callback<DataNews> {

                override fun onResponse(call: Call<DataNews>, response: Response<DataNews>) {
                    val result = response.body()!!
                    val dataSet : ArrayList<Pair<String, String>> = arrayListOf()

                    result.data.forEach { item ->
                        dataSet.add(Pair(item.title, item.url))
                    }
                    callbackAPI.onSuccessfulRequest(dataSet)
                }

                override fun onFailure(call: Call<DataNews>, t: Throwable) {
                    val errorMessage = t.message!!
                    callbackAPI.onFailedRequest(errorMessage)
                }
            })
    }

    fun managerRequestTrendCrypto(callbackAPI: CallbackAPI<ArrayList<TrendCrypto.Data>>) {

        serviceAPI
            .requestTrendCrypto()
            .enqueue(object : Callback<TrendCrypto> {

                override fun onResponse(call: Call<TrendCrypto>, response: Response<TrendCrypto>) {
                    val result = response.body()!!
                    callbackAPI.onSuccessfulRequest(ArrayList(result.data))
                }

                override fun onFailure(call: Call<TrendCrypto>, t: Throwable) {
                    callbackAPI.onFailedRequest(t.message!!)
                }

            })
    }

    fun managerRequestChartData(period : String, fsym : String, limit : Int, aggregate : Int, callbackAPI: CallbackAPI<DataChart.Data>) {

        serviceAPI
            .requestCharData(period, fsym, limit, aggregate)
            .enqueue(object : Callback<DataChart> {

                override fun onResponse(call: Call<DataChart>, response: Response<DataChart>) {
                    val result = response.body()!!
                    callbackAPI.onSuccessfulRequest(result.data)
                }

                override fun onFailure(call: Call<DataChart>, t: Throwable) {
                    callbackAPI.onFailedRequest(t.message!!)
                }

            })
    }

    interface CallbackAPI<T> {

        fun onSuccessfulRequest(data : T)
        fun onFailedRequest(error : String)
    }
}