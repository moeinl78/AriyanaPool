package ir.ariyana.ariyanapool.api

import ir.ariyana.ariyanapool.data.news.DataNews
import ir.ariyana.ariyanapool.data.trend_crypto.TrendCrypto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ServiceAPI {

    @GET("v2/news/")
    @Headers(API_KEY)
    fun requestNews(
        @Query("sortOrder") sortOrder : String = "popular",
        @Query("categories") categories : String = "Trading",
        @Query("lang") lang : String = "EN") : Call<DataNews>

    @GET("top/totalvolfull/")
    @Headers(API_KEY)
    fun requestTrendCrypto(
        @Query("tsym") tsym : String = "USD",
        @Query("limit") limit : Int = 10) : Call<TrendCrypto>
}