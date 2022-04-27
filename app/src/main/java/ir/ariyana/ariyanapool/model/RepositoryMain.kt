package ir.ariyana.ariyanapool.model

import io.reactivex.Single
import ir.ariyana.ariyanapool.model.api.*
import ir.ariyana.ariyanapool.model.data.chart.DataChart
import ir.ariyana.ariyanapool.model.data.news.DataNews
import ir.ariyana.ariyanapool.model.data.trend_crypto.TrendCrypto
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RepositoryMain {

    private val serviceAPI : ServiceAPI

    init {

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        serviceAPI = retrofit.create(ServiceAPI::class.java)
    }

    fun repoRequestNews() : Single<DataNews> {
        return serviceAPI.requestNews()
    }

    fun repoRequestTrendCrypto() : Single<TrendCrypto> {
        return serviceAPI.requestTrendCrypto()
    }

    fun repoRequestChartData(histo : String, fsym : String, limit : Int, aggregate : Int) : Single<DataChart> {
        return serviceAPI.requestCharData(histo , fsym, limit, aggregate)
    }
}