package ir.ariyana.ariyanapool.adapter

import com.robinhood.spark.SparkAdapter
import ir.ariyana.ariyanapool.data.chart.DataChart

class AdapterChart(private val data : ArrayList<DataChart.Data.Data>, private val baseLine : String?) : SparkAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(index: Int): DataChart.Data.Data {
        return data[index]
    }

    override fun getY(index: Int): Float {
        return data[index].close.toFloat()
    }

    override fun hasBaseLine(): Boolean {
        return true
    }

    override fun getBaseLine(): Float {
        return baseLine?.toFloat() ?: super.getBaseLine()
    }
}