package com.easv.esbjergensemblescanningapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_scanner.*

class ScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        button_newScan.setOnClickListener { v -> onClickNewScan() }
    }

    private fun onClickNewScan() {
        TODO("Not yet implemented")
    }
}