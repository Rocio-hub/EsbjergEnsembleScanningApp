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
import com.easv.esbjergensemblescanningapp.Data.IScanDao
import com.easv.esbjergensemblescanningapp.Data.ScanDao_Impl
import com.easv.esbjergensemblescanningapp.Model.BEScan
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_scanner.*

private const val CAMERA_REQUEST_CODE = 101

class ScannerActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private var scanList :  MutableList<BEScan> = mutableListOf()
    private lateinit var scanRepo: IScanDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        scanRepo = ScanDao_Impl(this)

        button_newScan.setVisibility(View.GONE)
        button_newScan.setOnClickListener { v -> onClickNewScan() }

        setUpPermissions()
        codeScanner()
    }

    private fun onClickNewScan() {
        textView_result.text = ""
        button_newScan.setVisibility(View.GONE)
        onResume()
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

                  //  textView_result.text = "Code: " + qrCode
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

    private fun checkValidQrCode(qrCode: String) {
         //si existeÇ: fail
         //si no existe: meter en db


        var e = qrCode.substring(qrCode.indexOf("event_id=") + 9)
        var e1 = e.substring(0, e.indexOf("&"))
        var eventId = e1.toInt()

        var s = qrCode.substring(qrCode.indexOf("security_code=") + 14)
        var s1 = s.substring(0, s.indexOf("&"))
        var securityCode = s1

        var newScan = BEScan(0, eventId, securityCode)
        var scanList = scanRepo.getScansByConcertId(eventId)

        var fail = false
        scanList.forEach{
            if(it.securityCode == securityCode) {
                fail = true
                methodFail()
            }
        }
        if(!fail) {
            scanRepo.insert(newScan)
            methodSuccess()
        }




    }

    private fun methodSuccess() {
        textView_result.text = "SUCCESS"
    }

    private fun methodFail() {
        textView_result.text = "Code already scanned"
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