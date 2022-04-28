package ir.ariyana.ariyanapool.model.local

import androidx.room.*
import io.reactivex.Flowable
import ir.ariyana.ariyanapool.model.data.trend_crypto.TrendCrypto

@Dao
interface CryptoDao {

    @Insert
    fun cryptoInsert(crypto : TrendCrypto)

    @Update
    fun cryptoUpdate(crypto: TrendCrypto)

    @Delete
    fun cryptoDelete(crypto: TrendCrypto)

    @Query("SELECT * FROM crypto_trend")
    fun cryptoRead() : Flowable<TrendCrypto>
}