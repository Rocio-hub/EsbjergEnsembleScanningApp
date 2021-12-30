package com.easv.esbjergensemblescanningapp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.Model.BEConcert
import com.easv.esbjergensemblescanningapp.Model.Concert
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_concertinfo.*


class ConcertInfoActivity : AppCompatActivity(){
    private var concert: Concert = Concert()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concertinfo)

        button_scan.setOnClickListener { v -> onClickScan() }

        var extras: Bundle = intent.extras!!
        var concertPosition = extras.getSerializable("concertPosition")
        var concertId = extras.getInt("concertId")

        var concertList = concert.getAllConcerts()
        var selectedConcert : BEConcert = concertList[concertPosition as Int]

        textView_date.setText(selectedConcert.Date)
        textView_title.setText(selectedConcert.title)
        textView_other.setText(selectedConcert.Time)
    }

    private fun onClickScan() {
        val intent = Intent(this, ScannerActivity::class.java)
        startActivity(intent)
    }
}