package ir.ariyana.ariyanapool.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ir.ariyana.ariyanapool.R
import ir.ariyana.ariyanapool.view.adapter.AdapterChart
import ir.ariyana.ariyanapool.model.api.*
import ir.ariyana.ariyanapool.model.data.chart.DataChart
import ir.ariyana.ariyanapool.model.data.trend_crypto.TrendCrypto
import ir.ariyana.ariyanapool.databinding.ActivityCoinBinding
import ir.ariyana.ariyanapool.viewmodel.ViewModelMain

class CoinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCoinBinding
    private lateinit var data : TrendCrypto.Data
    private lateinit var disposable : Disposable
    private val viewModelMain = ViewModelMain()

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
        receiveChartData(period, coinName)

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
            receiveChartData(period, coinName)
        }
    }

    private fun receiveChartData(period : String, coinName : String) {

        viewModelMain
            .requestChartVM(period, coinName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DataChart> {

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onSuccess(t: DataChart) {
                    val dataOne = t.data.data
                    val dataTwo = t.data.data.maxByOrNull { it.close.toFloat() }
                    val data = Pair(ArrayList(dataOne), dataTwo!!)
                    val adapterChart = AdapterChart(data.first, data.second.open.toString())
                    binding.coinActivityChart.compChartSparkView.adapter = adapterChart
                }

                override fun onError(e: Throwable) {
                    Log.e("throwableChart", e.message!!)
                }
            })
    }
}