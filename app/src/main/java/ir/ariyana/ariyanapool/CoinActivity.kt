package ir.ariyana.ariyanapool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.ariyana.ariyanapool.data.trend_crypto.TrendCrypto
import ir.ariyana.ariyanapool.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCoinBinding
    private lateinit var data : TrendCrypto.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getParcelableExtra(CRYPTO_ITEM)!!
        setSupportActionBar(binding.coinActivityToolbar.compHeaderToolbarLayout)
        supportActionBar?.title = data.coinInfo.fullName

        putStatisticsData()
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
}