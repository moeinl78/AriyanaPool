package ir.ariyana.ariyanapool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.ariyana.ariyanapool.api.BASE_URL
import ir.ariyana.ariyanapool.data.trend_crypto.TrendCrypto
import ir.ariyana.ariyanapool.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_coin)

        val intent = Intent()
        val data = intent.getParcelableExtra<TrendCrypto.Data>(CRYPTO_ITEM)

        if (data != null) {
            putData(data)
        }
    }

    private fun putData(data : TrendCrypto.Data) {

        // statistics
        binding.coinActivityStatic.compStaticPrice.text = data.dISPLAY.uSD.pRICE
        binding.coinActivityStatic.compStaticPricePeak.text = data.dISPLAY.uSD.hIGHDAY
        binding.coinActivityStatic.compStaticPriceMin.text = data.dISPLAY.uSD.lOWDAY
        binding.coinActivityStatic.compStaticPriceTodayChange.text = data.dISPLAY.uSD.cHANGEDAY
        binding.coinActivityStatic.compStaticMarketTransfer.text = data.dISPLAY.uSD.vOLUMEDAY
        binding.coinActivityStatic.compStaticMarketAvg.text = data.dISPLAY.uSD.lASTVOLUME
        binding.coinActivityStatic.compStaticMarketCap.text = data.dISPLAY.uSD.mKTCAP
        binding.coinActivityStatic.compStaticSupply.text = data.dISPLAY.uSD.sUPPLY

        // about
        binding.coinActivityAbout.compAboutWebsite.text = BASE_URL + data.coinInfo.url
        binding.coinActivityAbout.compAboutTwitter.text
        binding.coinActivityAbout.compAboutReddit.text
        binding.coinActivityAbout.compAboutGithub.text
    }
}