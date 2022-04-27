package ir.ariyana.ariyanapool.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ir.ariyana.ariyanapool.view.adapter.AdapterCrypto
import ir.ariyana.ariyanapool.model.data.trend_crypto.TrendCrypto
import ir.ariyana.ariyanapool.databinding.ActivityMainBinding
import ir.ariyana.ariyanapool.model.data.news.DataNews
import ir.ariyana.ariyanapool.viewmodel.ViewModelMain

const val CRYPTO_ITEM = "item"

class MainActivity : AppCompatActivity(), AdapterCrypto.DataEvents {

    private lateinit var binding : ActivityMainBinding
    private lateinit var news : ArrayList<Pair<String, String>>
    private lateinit var dataCrypto : ArrayList<TrendCrypto.Data>

    private val compositeDisposable = CompositeDisposable()
    private val viewModelMain = ViewModelMain()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.componentToolbar.compHeaderToolbarLayout)
        supportActionBar?.title = "Market"
    }

    override fun onResume() {

        super.onResume()
        onUserInterfaceStart()
    }

    private fun onUserInterfaceStart() {

        // news from api
        receiveNewsData()
        receiveTrendCrypto()
    }

    // call back to receive data
    private fun receiveNewsData() {

        viewModelMain
            .requestNewsVM()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DataNews> {

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: DataNews) {
                    val dataSet : ArrayList<Pair<String, String>> = arrayListOf()

                    t.data.forEach { item ->
                        dataSet.add(Pair(item.title, item.url))
                    }
                    news = dataSet
                }

                override fun onError(e: Throwable) {
                    Log.e("throwable", e.message!!)
                }
            })
    }

    private fun receiveTrendCrypto() {

        viewModelMain
            .requestTrendVM()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<TrendCrypto> {

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: TrendCrypto) {
                    val dataList = ArrayList(t.data)
                    dataCrypto = dataList
                }

                override fun onError(e: Throwable) {
                    Log.e("throwable", e.message!!)
                }
            })
    }

    // change news onclick or show news in browser
    private fun changeNews() {

        val random = (0..49).random()
        val randomNews = news[random]
        binding.componentNews.compNewsTextView.text = randomNews.first

        binding.componentNews.compNewsFAB.setOnClickListener {
            changeNews()
        }

        binding.componentNews.compNewsTextView.setOnClickListener {
            val url = randomNews.second
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    override fun onItemClicked(item: TrendCrypto.Data) {
        val intent = Intent(this, CoinActivity::class.java)
        intent.putExtra(CRYPTO_ITEM, item)
        startActivity(intent)
    }
}