package com.easv.esbjergensemblescanningapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.R

class EventInfoActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventlist)

        var extras: Bundle = intent.extras!!
        var position = extras.getSerializable("eventPosition")
        //textView_other.text = position
    }
}