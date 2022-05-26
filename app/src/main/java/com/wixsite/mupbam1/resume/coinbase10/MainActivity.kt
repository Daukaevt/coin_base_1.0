package com.wixsite.mupbam1.resume.coinbase10

// https://www.youtube.com/watch?v=JXNJoxUBk7I
// https://www.youtube.com/watch?v=-41e7nYnhj8

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.bumptech.glide.Glide
import com.wixsite.mupbam1.resume.coinbase10.databinding.ActivityMainBinding
import com.wixsite.mupbam1.resume.coinbase10.searchData.Coin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


const val BASE_URL="https://api.coingecko.com"
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
   private val coinDataList: MutableList<CoinDataStructureItem> = mutableListOf()
   private val coinSearchList: MutableList<CoinDataStructureItem> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    var network = isNetworkAvailable(this)
    if (network==false){

        Toast.makeText(this, getString(R.string.noNetwork), Toast.LENGTH_SHORT).show()
    }else{
        parseJSON()
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    // Network chek
    val connectivityManager = context.getApplicationContext()
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem=menu!!.findItem(R.id.search)
        if (menuItem!=null){

            val searchView=menuItem.actionView as SearchView
            val editText=searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.hint="Search..."

            searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()){
                        coinSearchList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        coinDataList.forEach {
                            if (it.name.toLowerCase(Locale.getDefault()).contains(search)){
                                coinSearchList.add(it)
                            }
                        }
                        binding.recyclerView.adapter!!.notifyDataSetChanged()
                    }else{
                        coinSearchList.clear()
                        coinSearchList.addAll(coinDataList)
                        binding.recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun makeAdapter() {
        // Set the LayoutManager that this RecyclerView will use.
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = MyAdapter(this, coinSearchList, object : onItemClickListner{
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
                    includeID.llCoinItem.setOnClickListener {
                        includeID.llCoinItem.visibility=View.GONE
                       // recyclerView.getLayoutManager()?.scrollToPosition(10)

                        val smoothScroller: SmoothScroller =
                            object : LinearSmoothScroller(this@MainActivity) {
                                override fun getVerticalSnapPreference(): Int {
                                    return SNAP_TO_START
                                }
                            }
                        smoothScroller.setTargetPosition(55)
                        recyclerView.layoutManager?.startSmoothScroll(smoothScroller)

                    }
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

        // Create Service for Global Search from API        "/api/v3/search?query=bitcoin" gonna be realised
        val service = retrofit.create(retrofitApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {

            val responseSearch = service.getSearch()


            withContext(Dispatchers.Main) {
                val itemsSearch = responseSearch.body()

                if (responseSearch.isSuccessful) {
                    val itemsSearch = responseSearch.body()?.coins

                    if (itemsSearch != null) {
                        //coinList.addAll(itemsSearch)
                        Log.d("MyLog", "itemsSearch-${itemsSearch.size}")
                        //makeAdapter()
                    }
                } else {
                    Log.d("MyLog", responseSearch.code().toString())
                }
            }
        }

        //Create Service     API       "/api/v3/coins/markets?vs_currency=usd"


        CoroutineScope(Dispatchers.IO).launch {

            // Do the GET request and get response
            val response = service.getData()



            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val items = response.body()

                    if (items != null) {

                        coinDataList.addAll(items)
                        coinSearchList.addAll(coinDataList)
                        //coinSearch()
                        makeAdapter()
                    } else {
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
            }
        }
    }
}