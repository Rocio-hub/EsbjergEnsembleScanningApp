package com.easv.esbjergensemblescanningapp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.Model.BEConcert
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_concertinfo.*


class ConcertInfoActivity : AppCompatActivity(){
    private var userId : Int = 0
    private lateinit var selectedConcert : BEConcert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concertinfo)

        button_scan.setOnClickListener { v -> onClickScan() }

        var extras: Bundle = intent.extras!!

        selectedConcert = extras.getSerializable("selectedConcert") as BEConcert
        userId = extras.getInt("userId")

        textView_title.text = selectedConcert.title
        textView_date.text = selectedConcert.Date
        textView_time.text = selectedConcert.Time
    }

    private fun onClickScan() {
        val intent = Intent(this, ScannerActivity::class.java)
        intent.putExtra("selectedConcert", selectedConcert)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
}