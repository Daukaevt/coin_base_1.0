package com.wixsite.mupbam1.resume.coinbase10

import android.util.Log
import com.bumptech.glide.Glide

interface onItemClickListner {
    fun onClicked1(
        coinDataImage:String,
        coinDataName:String,
        coinDataPrice:String,
        coinDataLastUpdated:String,
        coinDataPriceChange:String,
        coinDataTotalVolume:String,
        coinDataMarketCap:String)
}