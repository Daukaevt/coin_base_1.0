package com.wixsite.mupbam1.resume.coinbase10
// https://www.youtube.com/watch?v=JXNJoxUBk7I

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.wixsite.mupbam1.resume.coinbase10.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL="https://api.coingecko.com"
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
   private val coinDataList: MutableList<CoinDataStructureItem> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseJSON()

    }

    private fun makeAdapter() {
        // Set the LayoutManager that this RecyclerView will use.
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = MyAdapter(this, coinDataList, object : onItemClickListner{
            override fun onClicked1(
                coinDataImage: String,
                coinDataName: String,
                coinDataPrice: String,
                coinDataLastUpdated: String,
                coinDataPriceChange: String,
                coinDataTotalVolume: String,
                coinDataMarketCap: String
            ) {
                with(binding){
                    includeID.llCoinItem.visibility=View.VISIBLE
                    Glide.with(this@MainActivity)
                        .load(coinDataImage)
                        .centerCrop()
                        .override(300, 200)
                        .circleCrop()
                        .into(includeID.ivCoin)
                    includeID.tvName.text=coinDataName
                    includeID.tvItemPrice.text=coinDataPrice
                    includeID.tvLastUpdated.text=coinDataLastUpdated
                    includeID.tvPriceChangePercentage24h.text=coinDataPriceChange
                    includeID.tvTotalVolume.text=coinDataTotalVolume
                    includeID.tvMarketCap.text=coinDataMarketCap


                }


            }

        })
        // adapter instance is set to the recyclerview to inflate the items.
        binding.recyclerView.adapter = itemAdapter
    }

    @SuppressLint("LongLogTag")
    fun parseJSON() {
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create Service
        val service = retrofit.create(retrofitApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            // Do the GET request and get response
            val response = service.getData()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val items = response.body()

                    if (items!= null) {
                        coinDataList.addAll(items)
                        makeAdapter()
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }
}