package com.wixsite.mupbam1.resume.coinbase10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Splash_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}