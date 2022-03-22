package ir.ariyana.ariyanapool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.ariyana.ariyanapool.api.BASE_URL_IMAGE
import ir.ariyana.ariyanapool.data.trend_crypto.TrendCrypto
import ir.ariyana.ariyanapool.databinding.ItemRecyclerCryptoBinding

class AdapterCrypto(private val data : ArrayList<TrendCrypto.Data>, private val dataEvents : DataEvents) : RecyclerView.Adapter<AdapterCrypto.ViewHolder>() {

    inner class ViewHolder(private val binding : ItemRecyclerCryptoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(position: Int) {

            binding.itemCryptoName.text = data[position].coinInfo.fullName
            binding.itemCryptoType.text = data[position].coinInfo.name
            binding.itemCryptoBase.text = data[position].coinInfo.algorithm
            binding.itemCryptoPrice.text = data[position].dISPLAY.uSD.pRICE
            binding.itemCryptoMarketValue.text = data[position].dISPLAY.uSD.mKTCAP
            Glide
                .with(binding.root.context)
                .load(BASE_URL_IMAGE + data[position].coinInfo.imageUrl)
                .into(binding.itemCryptoCircleImage)

            itemView.setOnClickListener {
                dataEvents.onItemClicked(data[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerCryptoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface DataEvents {

        fun onItemClicked(item : TrendCrypto.Data)
    }
}