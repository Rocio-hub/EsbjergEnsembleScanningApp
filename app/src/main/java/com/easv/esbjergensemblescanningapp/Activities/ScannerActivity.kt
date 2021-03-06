 package com.easv.esbjergensemblescanningapp.Activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.easv.esbjergensemblescanningapp.Data.IScanDAO
import com.easv.esbjergensemblescanningapp.Data.ScanDAO_Impl
import com.easv.esbjergensemblescanningapp.Model.BEConcert
import com.easv.esbjergensemblescanningapp.Model.BEScan
import com.easv.esbjergensemblescanningapp.Model.BEUser
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_scanner.*

 private const val CAMERA_REQUEST_CODE = 101
 private const val ERROR_WRONG_CONCERT = 1
 private const val ERROR_ALREADY_SCANNED = 2

class ScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var scanRepo: IScanDAO
    private lateinit var user : BEUser
    private lateinit var selectedConcert : BEConcert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        var extras: Bundle = intent.extras!!
        selectedConcert = extras.getSerializable("selectedConcert") as BEConcert
        user = extras.getSerializable("user") as BEUser

        scanRepo = ScanDAO_Impl(this)

        button_newScan.setVisibility(View.GONE)
        button_newScan.setOnClickListener { v -> onClickNewScan() }

        setUpPermissions()
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    var qrCode = it.text
                    checkValidQrCode(qrCode)
                    button_newScan.setVisibility(View.VISIBLE)
                    onPause()
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera initialization error: ${it.message}")
                }
            }
        }
    }

    private fun onClickNewScan() {
        textView_result.text = ""
        button_newScan.setVisibility(View.GONE)
        onResume()
    }

    private fun checkValidQrCode(qrCode: String) {
        //Scanned Concert ID
        var c = qrCode.substring(qrCode.indexOf("event_id=") + 9)
        var c1 = c.substring(0, c.indexOf("&"))
        var concertId = c1.toInt()
        //Security Code
        var s = qrCode.substring(qrCode.indexOf("security_code=") + 14)
        var s1 = s.substring(0, s.indexOf("&"))
        var securityCode = s1

        var newScan = BEScan(0, concertId, user.id, securityCode)
        var scanList = scanRepo.getScansByConcertId(concertId)

        //selected concert id equals id of scanned code
        if(selectedConcert.id == concertId){
            //Toast.makeText(this, "Concert ID is correct", Toast.LENGTH_LONG).show()

            var alreadyScanned = false
            scanList.forEach{
                if(it.securityCode == securityCode) {
                    alreadyScanned = true
                    methodFail(ERROR_ALREADY_SCANNED)
                }
            }
            if(alreadyScanned == false) {
                scanRepo.insert(newScan)
                methodSuccess(concertId)
            }
        }
        //selected concert id different id of scanned code
        else{
            //Toast.makeText(this, "Wrong concert ID", Toast.LENGTH_LONG).show()
            methodFail(ERROR_WRONG_CONCERT)
       }
    }

    private fun methodSuccess(eventId : Int) {
        scanRepo.getScansByConcertId(eventId).forEach {
            Log.d("CONCERTLIST", it.id.toString())
        }
        imageView_mood.setImageResource(R.drawable.ic_baseline_mood_bad_24)
        textView_result.text = "New scan inserted in db"
    }

    private fun methodFail(errorCode : Int) {
        when(errorCode) {
            1 -> textView_result.text = "The scanned concert ID does not match the chosen concert"
            2 -> textView_result.text = "Code already scanned"
        }
        imageView_mood.setImageResource(R.drawable.ic_baseline_mood_bad_24)
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setUpPermissions() {
        val permission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please, add camera permission to use the scanner. Close and reopen this app to accept the permission.", Toast.LENGTH_SHORT).show()
                } else {
                    //successful
                }
            }
        }
    }
}