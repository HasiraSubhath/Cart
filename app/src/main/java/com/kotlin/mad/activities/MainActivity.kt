package com.kotlin.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kotlin.mad.R

class MainActivity : AppCompatActivity() {

    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)

        btnInsertData.setOnClickListener {
            val intent = Intent(this, PaymentInsertionActivity::class.java)
            startActivity(intent)
        }

        btnFetchData.setOnClickListener {
            val intent = Intent(this, PaymentFetchingActivity::class.java)
            startActivity(intent)
        }



    }
}