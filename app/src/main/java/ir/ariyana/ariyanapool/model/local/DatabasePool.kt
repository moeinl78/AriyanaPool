package ir.ariyana.ariyanapool.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.ariyana.ariyanapool.model.data.trend_crypto.TrendCrypto

@Database(entities = [TrendCrypto::class], version = 1)
abstract class DatabasePool : RoomDatabase() {

    abstract var cryptoDao : CryptoDao

    companion object {

        @Volatile
        private var database : DatabasePool ? = null

        fun createDataBase(context : Context) : DatabasePool {

            if(database == null) {
                database = Room.databaseBuilder(
                    context.applicationContext,
                    DatabasePool::class.java,
                    "database.db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return database!!
        }
    }
}