package com.easv.esbjergensemblescanningapp.Activities

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.Model.Concert
import okhttp3.*
import okio.IOException

class GetDataActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var data : List<Concert>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val request = Request.Builder()
            //.url("http://publicobject.com/helloworld.txt")
            .url("https://scanningservice-easv.azurewebsites.net/api/concerts")
            .build()

        client.newCall(request).enqueue(object : Callback { //enqueue means that the http request is gonna be enqueued in the processing queue (asynchronous)
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                /*response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    println(response.body!!.string())
                }*/
                if(response.isSuccessful){
                    var responseBody : ResponseBody? = response.body
                    if (responseBody != null) {
                        Log.d(ContentValues.TAG, "onResponse: " + responseBody.string())

                    }
                }
            }
        })
    }
}