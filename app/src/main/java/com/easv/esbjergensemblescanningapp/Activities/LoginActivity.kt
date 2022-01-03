package com.easv.esbjergensemblescanningapp.Activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.Model.BEConcert
import com.easv.esbjergensemblescanningapp.Model.User
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONTokener
import java.io.Serializable

class  LoginActivity: AppCompatActivity() {
    private lateinit var users: User
    private var client = OkHttpClient()
    var allConcerts : MutableList<BEConcert> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getConcertsFromAzure()

        users = User()

        textView_error.setVisibility(View.GONE)

        button_ok.setOnClickListener { v -> onClickOk() }
    }

    private fun onClickOk() {
        val code = editText_code.text.toString()
        if(validateCode(code)){
            textView_error.setVisibility(View.INVISIBLE)
            val intent = Intent(this, ConcertListActivity::class.java)
            intent.putExtra("allConcerts", allConcerts as Serializable)
            startActivity(intent)
        }
    }

    private fun getConcertsFromAzure(){
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

                            newConcert.id = id.toInt()
                            newConcert.title = title
                            newConcert.Date = date
                            newConcert.Time = time
                            allConcerts.add(newConcert)
                        }
                    }
                }
            }
        })
    }

    //Check if the input in form is valid
    private fun validateCode(code: String): Boolean {
        //Case code empty
        if (editText_code.text.toString() == "") {
            textView_error.text = "Code cannot be empty."
            textView_error.setVisibility(View.VISIBLE)
            return false
        }
        //Case code not in db
        if((!users.checkUserExists(code))){
            textView_error.text = "Wrong code. Please, try again."
            textView_error.setVisibility(View.VISIBLE)
            return false
        }
        //Case correct code
        return true
    }

}
