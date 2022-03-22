package ir.ariyana.ariyanapool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ir.ariyana.ariyanapool.adapter.AdapterChart
import ir.ariyana.ariyanapool.api.HOUR
import ir.ariyana.ariyanapool.api.ManagerAPI
import ir.ariyana.ariyanapool.data.chart.DataChart
import ir.ariyana.ariyanapool.data.trend_crypto.TrendCrypto
import ir.ariyana.ariyanapool.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCoinBinding
    private lateinit var data : TrendCrypto.Data
    private val managerAPI = ManagerAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getParcelableExtra(CRYPTO_ITEM)!!
        setSupportActionBar(binding.coinActivityToolbar.compHeaderToolbarLayout)
        supportActionBar?.title = data.coinInfo.fullName

        putStatisticsData()
        onUserInterfaceStart()
    }

    private fun putStatisticsData() {

        // statistics
        binding.coinActivityStatic.compStaticPrice.text = data.dISPLAY.uSD.oPEN24HOUR
        binding.coinActivityStatic.compStaticPricePeak.text = data.dISPLAY.uSD.hIGHDAY
        binding.coinActivityStatic.compStaticPriceMin.text = data.dISPLAY.uSD.lOWDAY
        binding.coinActivityStatic.compStaticPriceTodayChange.text = data.dISPLAY.uSD.cHANGEDAY
        binding.coinActivityStatic.compStaticMarketTransfer.text = data.dISPLAY.uSD.vOLUMEDAY
        binding.coinActivityStatic.compStaticMarketAvg.text = data.dISPLAY.uSD.lASTVOLUME
        binding.coinActivityStatic.compStaticMarketCap.text = data.dISPLAY.uSD.mKTCAP
        binding.coinActivityStatic.compStaticSupply.text = data.dISPLAY.uSD.sUPPLY
    }

    private fun onUserInterfaceStart() {

        receiveCharData()
    }

    private fun receiveCharData() {

        val coinName = data.coinInfo.name
        managerAPI.managerRequestChartData(HOUR, coinName, object : ManagerAPI.CallbackAPI<Pair<ArrayList<DataChart.Data.Data>, DataChart.Data.Data>>{

            override fun onSuccessfulRequest(data: Pair<ArrayList<DataChart.Data.Data>, DataChart.Data.Data>) {

                val adapterChart = AdapterChart(data.first, data.second.open.toString())
                binding.coinActivityChart.compChartSparkView.adapter = adapterChart
            }

            override fun onFailedRequest(error: String) {
                Toast.makeText(this@CoinActivity, error, Toast.LENGTH_SHORT).show()
            }
        })
    }
}