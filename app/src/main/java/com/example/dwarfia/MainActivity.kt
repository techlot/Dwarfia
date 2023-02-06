package com.example.dwarfia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fun getUserId(): String {
            return intent.getStringExtra("user_id").toString()
        }
    }

    fun getUserId(): String {
        return intent.getStringExtra("user_id").toString()
    }
}
