package ir.ariyana.ariyanapool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.ariyana.ariyanapool.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {

    lateinit var binding : ActivityCoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_coin)
    }
}