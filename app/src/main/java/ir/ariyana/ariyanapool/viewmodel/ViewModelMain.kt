package ir.ariyana.ariyanapool.viewmodel

import io.reactivex.Single
import ir.ariyana.ariyanapool.model.RepositoryMain
import ir.ariyana.ariyanapool.model.api.*
import ir.ariyana.ariyanapool.model.data.chart.DataChart
import ir.ariyana.ariyanapool.model.data.news.DataNews
import ir.ariyana.ariyanapool.model.data.trend_crypto.TrendCrypto

class ViewModelMain {

    private val repositoryMain = RepositoryMain()

    fun requestNewsVM() : Single<DataNews> {
        return repositoryMain.repoRequestNews()
    }

    fun requestTrendVM() : Single<TrendCrypto> {
        return repositoryMain.repoRequestTrendCrypto()
    }

    fun requestChartVM(period : String, fsym : String) : Single<DataChart> {
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
        return repositoryMain.repoRequestChartData(histo, fsym, limit, aggregate)
    }

}