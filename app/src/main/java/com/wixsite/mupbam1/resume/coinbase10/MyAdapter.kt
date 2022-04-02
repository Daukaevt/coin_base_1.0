package com.wixsite.mupbam1.resume.coinbase10

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wixsite.mupbam1.resume.coinbase10.databinding.ActivityMainBinding

class MyAdapter(
    val cntxt: Context,
    private val coinDataList: MutableList<CoinDataStructureItem>?,
    private val onClickListener: onItemClickListner
) : RecyclerView
.Adapter<MyAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val poster: ImageView = itemView.findViewById<View>(R.id.ivImage) as ImageView
        val title: TextView = itemView.findViewById<View>(R.id.tvID) as TextView
        val content: TextView = itemView.findViewById<View>(R.id.tvPrice) as TextView
        val itemLayout: LinearLayout = itemView.findViewById<View>(R.id.itemLayout) as LinearLayout
        val coin1 = itemView.findViewById<View>(R.id.llCoinItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var context=parent.context
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val radius = 50
        val margin = 20
        val coinDataImage = coinDataList?.get(position)?.image
            if (coinDataImage!=null){

                Glide.with(cntxt)
                    .load(coinDataImage)
                    .centerCrop()
                    .override(300, 200)
                    .circleCrop()
                    .into(holder.poster)

            val coinDataName = coinDataList?.get(position)?.name
            holder.title.text= coinDataName.toString()

            val coinDataPrice= coinDataList?.get(position)?.current_price
            holder.content.text= coinDataPrice?.toFloat().toString()

                val coinDataLastUpdated=coinDataList?.get(position)?.last_updated
                val coinDataPriceChange=coinDataList?.get(position)?.price_change_percentage_24h
                val coinDataTotalVolume=coinDataList?.get(position)?.total_volume
                val coinDataMarketCap=coinDataList?.get(position)?.market_cap

                holder.itemLayout.setOnClickListener {
                    onClickListener.onClicked1(
                        coinDataImage.toString()
                        ,coinDataName.toString()
                        ,coinDataPrice.toString()
                        ,coinDataLastUpdated.toString()
                        ,coinDataPriceChange.toString()
                        ,coinDataTotalVolume.toString()
                        ,coinDataMarketCap.toString())
                }
        }
    }

    override fun getItemCount(): Int {
        return coinDataList!!.size
    }

}