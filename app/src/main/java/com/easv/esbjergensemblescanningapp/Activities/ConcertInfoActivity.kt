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
    private var userId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concertinfo)

        button_scan.setOnClickListener { v -> onClickScan() }

        var extras: Bundle = intent.extras!!
      //  var concertPosition = extras.getSerializable("concertPosition")
        var selectedConcert = extras.getSerializable("selectedConcert") as BEConcert
        userId = extras.getInt("userId")
        //var concertId = extras.getInt("concertId")

     //   var concertList = concert.getAllConcerts()
    //    var selectedConcert : BEConcert = concertList[concertPosition as Int]

        textView_title.text = selectedConcert.title
        textView_date.text = selectedConcert.Date
        textView_other.text = selectedConcert.Time

    }

    private fun onClickScan() {
        val intent = Intent(this, ScannerActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
}