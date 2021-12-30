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
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_concertlist.*
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONTokener
import java.lang.Thread.sleep

class ConcertListActivity : AppCompatActivity() {

  //  private var concert: Concert = Concert()
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concertlist)

      //  concert = Concert()
    //    var concertList = concert.getAllConcerts()


        listView_concertItem.adapter = ListAdapter(this, getDataFromAzure().toTypedArray())
    /*    listView_concertItems.setOnItemClickListener {
            parent,
            view,
            position,
            id -> onConcertClick(parent as ListView, view, position)
        }*/
    }

    private fun getDataFromAzure() : MutableList<BEConcert> {
        var allConcerts : MutableList<BEConcert> = mutableListOf()
        val request = Request.Builder()
            .url("https://scanningservice-easv.azurewebsites.net/api/concerts")
            .build()

        client.newCall(request).enqueue(object : Callback { //enqueue means that the http request is gonna be enqueued in the processing queue (asynchronous)
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {

                if(response.isSuccessful){
                    var responseBody : String? = response.body?.string()
                    if (responseBody != null) {
                        Log.d(ContentValues.TAG, "onResponse: " + responseBody)

                        val jsonArray = JSONTokener(responseBody).nextValue() as JSONArray
                        for (i in 0 until jsonArray.length()) {
                            var newConcert = BEConcert (0, "", "", "")

                            val id = jsonArray.getJSONObject(i).getString("id")
                            val title = jsonArray.getJSONObject(i).getString("title")
                            val date = jsonArray.getJSONObject(i).getString("start_date").split("T")[0]
                            val time = jsonArray.getJSONObject(i).getString("start_date").split("T")[1]


                        //    Log.d("AAAAAAAAAA", "Concert " + i)
                            newConcert.id = id.toInt()
                          //  Log.d("AAAAAAAAAA", newConcert.id.toString())
                            newConcert.title = title
                         //   Log.d("AAAAAAAAAA", newConcert.title)
                            newConcert.Date = date
                          //  Log.d("AAAAAAAAAA", newConcert.Date)
                            newConcert.Time = time
                         //   Log.d("AAAAAAAAAA", newConcert.Time)

                            allConcerts.add(newConcert)

                        }

                    }
                }
            }

        })
        sleep(10000)
return allConcerts
    }

    private fun onConcertClick(parent: ListView?, v: View?, position: Int) {
        val concertPosition = position
        val intent = Intent(this, ConcertInfoActivity::class.java)
       // var concertList = concert.getAllConcerts()
 //       var selectedConcert : BEConcert = concertList[position as Int]

        intent.putExtra("concertPosition", concertPosition)
      //  intent.putExtra("concertId", selectedConcert.id)
        //intent.putExtra("concertId", concertId)
        startActivity(intent)
    }

    internal class ListAdapter(context: Context, private val items: Array<BEConcert>) : ArrayAdapter<BEConcert>(context, 0, items) {

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
            itemTitle.text = "HOLA"
            val itemDate = resView.findViewById<TextView>(R.id.textView_date)
            itemDate.text = item.Date
            val itemTime = resView.findViewById<TextView>(R.id.textView_time)
            itemTime.text = item.Time

            return resView
        }
    }

}
