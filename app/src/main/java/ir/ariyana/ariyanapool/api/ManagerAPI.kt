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

    fun managerRequestChartData(
        period : String,
        fsym : String,
        callbackAPI: CallbackAPI<Pair<ArrayList<DataChart.Data.Data>, DataChart.Data.Data>>
    ) {

        var histo = ""
        var limit = 30
        var aggregate = 1

        when(period) {

            HOUR -> {
                histo = HISTO_MINUTE
                limit = 60
                aggregate = 12
            }

            HOURS24 -> {
                histo = HISTO_HOUR
                limit = 24
            }

            WEEK -> {
                histo = HISTO_DAY
                limit = 7
            }

            MONTH -> {
                histo = HISTO_DAY
                limit = 30
            }

            MONTH3 -> {
                histo = HISTO_DAY
                limit = 90
            }

            YEAR -> {
                histo = HISTO_DAY
                aggregate = 12
            }

            ALL -> {
                histo = HISTO_DAY
                aggregate = 30
                limit = 2000
            }
        }

        serviceAPI
            .requestCharData(histo, fsym, limit, aggregate)
            .enqueue(object : Callback<DataChart> {

                override fun onResponse(call: Call<DataChart>, response: Response<DataChart>) {

                    val result = response.body()!!
                    val dataOne = result.data.data
                    val dataTwo = result.data.data.maxByOrNull { it.close.toFloat() }
                    val data = Pair(ArrayList(dataOne), dataTwo!!)
                    callbackAPI.onSuccessfulRequest(data)
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