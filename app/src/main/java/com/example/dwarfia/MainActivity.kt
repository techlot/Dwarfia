package com.example.dwarfia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


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
