package ir.ariyana.ariyanapool

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ir.ariyana.ariyanapool.adapter.AdapterCrypto
import ir.ariyana.ariyanapool.api.ManagerAPI
import ir.ariyana.ariyanapool.data.trend_crypto.TrendCrypto
import ir.ariyana.ariyanapool.databinding.ActivityCoinBinding
import ir.ariyana.ariyanapool.databinding.ActivityMainBinding

const val CRYPTO_ITEM = "item"

class MainActivity : AppCompatActivity(), AdapterCrypto.DataEvents {

    private lateinit var binding : ActivityMainBinding
    private val managerAPI = ManagerAPI()
    private lateinit var news : ArrayList<Pair<String, String>>
    private lateinit var dataCrypto : ArrayList<TrendCrypto.Data>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.componentToolbar.compHeaderToolbarLayout)
        supportActionBar?.title = "Market"

        binding.mainActivitySwipeRefreshLayout.setOnRefreshListener {

            onUserInterfaceStart()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.mainActivitySwipeRefreshLayout.isRefreshing = false
            }, 2000)
        }
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

        managerAPI.managerRequestNews(object : ManagerAPI.CallbackAPI<ArrayList<Pair<String, String>>>{

            override fun onSuccessfulRequest(data: ArrayList<Pair<String, String>>) {
                news = data
                changeNews()
            }

            override fun onFailedRequest(error: String) {
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun receiveTrendCrypto() {

        managerAPI.managerRequestTrendCrypto(object : ManagerAPI.CallbackAPI<ArrayList<TrendCrypto.Data>> {

            override fun onSuccessfulRequest(data: ArrayList<TrendCrypto.Data>) {

                dataCrypto = data
                val adapter = AdapterCrypto(dataCrypto, this@MainActivity)
                binding.componentRecycler.compCryptoRecyclerView.adapter = adapter
                binding.componentRecycler.compCryptoRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            override fun onFailedRequest(error: String) {
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                Log.v("error", error)
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