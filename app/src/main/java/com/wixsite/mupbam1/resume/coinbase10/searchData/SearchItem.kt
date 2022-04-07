package com.wixsite.mupbam1.resume.coinbase10.searchData

data class SearchItem(
    val categories: List<Category>,
    val coins: List<Coin>,
    val exchanges: List<Exchange>,
    val icos: List<Any>,
    val nfts: List<Nft>
)