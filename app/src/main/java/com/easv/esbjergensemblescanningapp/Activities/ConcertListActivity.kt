package com.easv.esbjergensemblescanningapp.Activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.Data.IScanDAO
import com.easv.esbjergensemblescanningapp.Data.ScanDAO_Impl
import com.easv.esbjergensemblescanningapp.Model.BEConcert
import com.easv.esbjergensemblescanningapp.Model.BEScan
import com.easv.esbjergensemblescanningapp.Model.BEUser
import com.easv.esbjergensemblescanningapp.Model.ScanDTO
import com.easv.esbjergensemblescanningapp.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_concertlist.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONTokener
import java.io.IOException
import java.io.Serializable

class ConcertListActivity : AppCompatActivity() {
    private var allConcerts : MutableList<BEConcert> = mutableListOf()
    private lateinit var user : BEUser
    private lateinit var scanRepo: IScanDAO
    private var client = OkHttpClient()
    private var allScans : MutableList<BEScan> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concertlist)

        var extras: Bundle = intent.extras!!
        allConcerts = extras.getSerializable("allConcerts") as ArrayList<BEConcert>
        user = extras.getSerializable("user") as BEUser

        scanRepo = ScanDAO_Impl(this)
        toolBar.title = "EE SCANNING SERVICE"
        setSupportActionBar(toolBar)

        listView_concertItem.adapter = ListAdapter(this, allConcerts.toTypedArray())
        listView_concertItem.setOnItemClickListener {
            parent,
            view,
            position,
            id -> onConcertClick(parent as ListView, view, position)
        }
         allScans = getScansInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, StatisticsActivity::class.java)
        intent.putExtra("allScans", allScans as Serializable)
        intent.putExtra("user", user as Serializable)
        intent.putExtra("allConcerts", allConcerts as Serializable)
        when (item.itemId) {
            R.id.nav_stats -> {
                //Toast.makeText(this, "menu item works", Toast.LENGTH_LONG).show()
                sendScansToDb()
                startActivity(intent)
            }
        }
        return true
    }

    private fun getScansInfo() : MutableList<BEScan> {
        val request = Request.Builder()
                .url("https://scanningsystem-easv.azurewebsites.net/api/scans")
                .build()

        client.newCall(request).enqueue(object : Callback { //enqueue means that the http request is gonna be enqueued in the processing queue (asynchronous)
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var responseBody: String? = response.body?.string()
                    if (responseBody != null) {
                        Log.d(ContentValues.TAG, "onResponse: " + responseBody)

                        val jsonArray = JSONTokener(responseBody).nextValue() as JSONArray
                        for (i in 0 until jsonArray.length()) {
                            var newScan = BEScan(0, 0, 0, "")

                            val id = jsonArray.getJSONObject(i).getInt("id")
                            val concertId = jsonArray.getJSONObject(i).getInt("concertId")
                            val userId = jsonArray.getJSONObject(i).getInt("userId")
                            val securityCode = jsonArray.getJSONObject(i).getString("securityCode")

                            newScan.id = id
                            newScan.concertId = concertId
                            newScan.userId = userId
                            newScan.securityCode = securityCode
                            allScans.add(newScan)
                        }
                    }
                }
            }
        })
        return allScans
    }

    private fun sendScansToDb() {
        val gson = Gson()
        var scanDTOList : MutableList<ScanDTO> = mutableListOf()

        for (item in scanRepo.getScansByConcertId(123)){
            var scanDTO = ScanDTO(0,0,"")
            scanDTO.concertId = item.concertId
            scanDTO.securityCode = item.securityCode
            scanDTO.userId = item.userId

            scanDTOList.add(scanDTO)
        }

        val scanDTOJson = gson.toJson(scanDTOList)
        val okHttpClient = OkHttpClient()
        val postBody = scanDTOJson.toRequestBody()
        val post = Request.Builder()
                .url("https://scanningsystem-easv.azurewebsites.net/api/scans")
                .post(postBody)
                .build()
        okHttpClient.newCall(post).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR", "Cannot make POST request")
            }
            override fun onResponse(call: Call, response: Response) {
                Log.i("OK", "POST request successful")
                Log.d("AAA", scanDTOJson)
            }
        })
    }

    private fun onConcertClick(parent: ListView?, v: View?, position: Int) {
        var selectedConcert  = allConcerts[position]

        val intent = Intent(this, ConcertInfoActivity::class.java)
        intent.putExtra("selectedConcert", selectedConcert)
        intent.putExtra("user", user as Serializable)
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
            itemTitle.text = item.title
            val itemDate = resView.findViewById<TextView>(R.id.textView_date)
            val year = item.date.dropLast(6)
            val month1 = item.date.dropLast(3)
            val month2 = month1.drop(5)
            val day = item.date.drop(8)
            itemDate.text = day+"/"+month2+"/"+year
            val itemTime = resView.findViewById<TextView>(R.id.textView_time)
            itemTime.text = item.time.dropLast(3)

            return resView
        }
    }
}
