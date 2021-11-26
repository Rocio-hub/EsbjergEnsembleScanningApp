package com.easv.esbjergensemblescanningapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.R

class ConcertInfoActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concertlist)

        var extras: Bundle = intent.extras!!
        var position = extras.getSerializable("concertPosition")
        //textView_other.text = position
    }
}