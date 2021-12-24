package com.easv.esbjergensemblescanningapp.Activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.Model.BEConcert
import com.easv.esbjergensemblescanningapp.Model.Concert
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_concertlist.*
import okhttp3.*
import okio.IOException

class ConcertListActivity : AppCompatActivity() {

    private var concert: Concert = Concert()
    private val client = OkHttpClient()
    private lateinit var concertList : List<BEConcert>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concertlist)

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
                   // val result: String = Gson().toJson(response.body!!.string())
                    if (responseBody != null) {
                        Log.d(ContentValues.TAG, "onResponse: " + responseBody)
                        println(responseBody::class.simpleName)
                    }
                }
            }
        })

















        var concertList = concert.getAllConcerts()
        listView_concertItems.adapter = ConcertListAdapter(this, concertList.toTypedArray())
        listView_concertItems.setOnItemClickListener {
            parent,
            view,
            position,
            id -> onConcertClick(parent as ListView, view, position)
        }
     //   val intent = Intent(this, GetDataActivity::class.java)
    }



    private fun onConcertClick(parent: ListView?, v: View?, position: Int) {
        val selectedConcert = position
        val intent = Intent(this, ConcertInfoActivity::class.java)
        intent.putExtra("concertPosition", selectedConcert)
        startActivity(intent)

    }

    internal class ConcertListAdapter(context: Context, private val items: Array<BEConcert>) : ArrayAdapter<BEConcert>(context, 0, items) {
        override fun getView(position: Int, v: View?, parent: ViewGroup) : View {
            var v1: View? = v
            if (v1 == null) {
                val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                        as LayoutInflater
                v1 = li.inflate(R.layout.event_items, null)
            }
            val resView: View = v1!!
            val item = items[position]
            val itemTitle = resView.findViewById<TextView>(R.id.textView_title)
            val itemDate = resView.findViewById<TextView>(R.id.textView_date)
            val itemTime = resView.findViewById<TextView>(R.id.textView_time)
            return resView
        }

    }

}
