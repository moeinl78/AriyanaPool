package ir.ariyana.ariyanapool.api

import ir.ariyana.ariyanapool.data.news.DataNews
import ir.ariyana.ariyanapool.data.trend_crypto.TrendCrypto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ServiceAPI {

    @Headers(API_KEY)
    @GET("v2/news/")
    fun requestNews(
        @Query("sortOrder") sortOrder : String = "popular",
    ) : Call<DataNews>

    @Headers(API_KEY)
    @GET("top/totalvolfull")
    fun requestTrendCrypto(
        @Query("tsym") tsym : String = "USD",
        @Query("limit") limit : Int = 10
    ) : Call<TrendCrypto>
}