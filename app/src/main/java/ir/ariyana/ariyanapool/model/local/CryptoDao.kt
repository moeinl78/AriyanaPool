package ir.ariyana.ariyanapool.model.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.ariyana.ariyanapool.model.data.trend_crypto.TrendCrypto

@Dao
interface CryptoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun cryptoInsert(crypto : TrendCrypto)

    @Update
    fun cryptoUpdate(crypto: TrendCrypto)

    @Delete
    fun cryptoDelete(crypto: TrendCrypto)

    @Query("SELECT * FROM crypto_trend")
    fun cryptoRead() : LiveData<TrendCrypto>
}