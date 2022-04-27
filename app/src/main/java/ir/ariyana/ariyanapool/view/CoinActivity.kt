package ir.ariyana.ariyanapool.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import ir.ariyana.ariyanapool.R
import ir.ariyana.ariyanapool.view.adapter.AdapterChart
import ir.ariyana.ariyanapool.model.api.*
import ir.ariyana.ariyanapool.model.data.chart.DataChart
import ir.ariyana.ariyanapool.model.data.trend_crypto.TrendCrypto
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

        modifyCharData()
    }

    private fun modifyCharData() {

        val coinName = data.coinInfo.name
        var period = HOUR
        binding.coinActivityChart.compChartCurrentPrice.text = data.dISPLAY.uSD.pRICE
        binding.coinActivityChart.compChartStatusDifference.text = data.dISPLAY.uSD.cHANGEDAY
        binding.coinActivityChart.compChartStatusPercentage.text = data.dISPLAY.uSD.cHANGEPCTDAY + " %"
        receiveCharData(period, coinName)

        val diff = data.rAW.uSD.cHANGE24HOUR
        if (diff > 0) {
            binding.coinActivityChart.compChartStatusDifference.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            binding.coinActivityChart.compChartStatusPercentage.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            binding.coinActivityChart.compChartStatusIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        }
        else {
            binding.coinActivityChart.compChartStatusDifference.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            binding.coinActivityChart.compChartStatusPercentage.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            binding.coinActivityChart.compChartStatusIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        }

        binding.coinActivityChart.compChartSparkView.setScrubListener {
            if(it == null) {
                binding.coinActivityChart.compChartCurrentPrice.text = data.dISPLAY.uSD.pRICE
            }
            else {
                binding.coinActivityChart.compChartCurrentPrice.text = "$ " + (it as DataChart.Data.Data).close.toString()
            }
        }

        // check any changes for the radio group
        binding.coinActivityChart.compchartRadioGroup.setOnCheckedChangeListener { _, id ->
            period = when(id) {
                R.id.compChartRadioHalfDay -> {
                    HOUR
                }

                R.id.compChartRadioOneDay -> {
                    HOURS24
                }

                R.id.compChartRadioOneWeek -> {
                    WEEK
                }

                R.id.compChartRadioOneMonth -> {
                    MONTH
                }

                R.id.compChartRadioThreeMonth -> {
                    MONTH3
                }

                R.id.compChartRadioOneYear -> {
                    YEAR
                }

                R.id.compChartRadioAll -> {
                    ALL
                }
                else -> {
                    HOUR
                }
            }
            receiveCharData(period, coinName)
        }
    }

    private fun receiveCharData(period : String, coinName : String) {

        // instantiate api call for chart data
        managerAPI.managerRequestChartData(period, coinName, object : ManagerAPI.CallbackAPI<Pair<ArrayList<DataChart.Data.Data>, DataChart.Data.Data>>{

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