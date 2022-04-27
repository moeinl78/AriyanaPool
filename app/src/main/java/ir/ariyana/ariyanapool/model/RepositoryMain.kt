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

    fun repoRequestChartData(period : String, fsym : String) : Single<DataChart> {

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

        return serviceAPI.requestCharData(histo, fsym, limit, aggregate)
    }
}