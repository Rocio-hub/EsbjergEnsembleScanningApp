package com.easv.esbjergensemblescanningapp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.Model.BEConcert
import com.easv.esbjergensemblescanningapp.Model.BEUser
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_concertinfo.*
import java.io.Serializable


class ConcertInfoActivity : AppCompatActivity(){
    private lateinit var user : BEUser
    private lateinit var selectedConcert : BEConcert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concertinfo)

        button_scan.setOnClickListener { v -> onClickScan() }

        var extras: Bundle = intent.extras!!

        selectedConcert = extras.getSerializable("selectedConcert") as BEConcert
        user = extras.getSerializable("user") as BEUser

        textView_title.text = selectedConcert.title
        textView_date.text = selectedConcert.Date
        textView_time.text = selectedConcert.Time
    }

    private fun onClickScan() {
        val intent = Intent(this, ScannerActivity::class.java)
        intent.putExtra("selectedConcert", selectedConcert)
        intent.putExtra("user", user as Serializable)
        startActivity(intent)
    }
}