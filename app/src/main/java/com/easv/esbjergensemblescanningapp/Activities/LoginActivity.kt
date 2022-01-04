package com.easv.esbjergensemblescanningapp.Activities

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.Data.IUserDAO
import com.easv.esbjergensemblescanningapp.Data.UserDAO_Impl
import com.easv.esbjergensemblescanningapp.Model.BEConcert
import com.easv.esbjergensemblescanningapp.Model.BEUser
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONTokener
import java.io.Serializable

class  LoginActivity: AppCompatActivity() {
    private lateinit var userRepo: IUserDAO
    private var client = OkHttpClient()
    var allConcerts : MutableList<BEConcert> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getConcertsFromAzure()
        userRepo = UserDAO_Impl(this)

        textView_error.setVisibility(View.INVISIBLE)

        button_ok.setOnClickListener { v -> onClickOk() }

        var wmbPreference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        var isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        val editor = wmbPreference.edit()
        if (isFirstRun){
            getUsersFromAzure()
            editor.putBoolean("FIRSTRUN", false);
            editor.apply();
        }
    }

    private fun getUsersFromAzure() {
        val request = Request.Builder()
                .url("https://scanningsystem-easv.azurewebsites.net/api/user")
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
                            var newUser = BEUser(0, 0, "", "")

                            val id = jsonArray.getJSONObject(i).getInt("id")
                            val code = jsonArray.getJSONObject(i).getInt("code")
                            val firstName = jsonArray.getJSONObject(i).getString("firstName")
                            val lastName = jsonArray.getJSONObject(i).getString("lastName")

                            newUser.id = id
                            newUser.code = code
                            newUser.firstName = firstName
                            newUser.lastName = lastName
                            userRepo.insert(newUser)
                        }
                    }
                }
            }
        })
    }

    private fun onClickOk() {
        val code = editText_code.text.toString()
        if(validateCode(code)!=null){
            textView_error.setVisibility(View.INVISIBLE)
            val intent = Intent(this, ConcertListActivity::class.java)
            intent.putExtra("allConcerts", allConcerts as Serializable)
            intent.putExtra("user", validateCode(code) as Serializable)
            startActivity(intent)
        }
    }

    private fun getConcertsFromAzure(){
        val request = Request.Builder()
                .url("https://scanningsystem-easv.azurewebsites.net/api/concerts")
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
                            var newConcert = BEConcert(0, "", "", "")

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
    private fun validateCode(codeString: String): BEUser? {
        var code = 0
        try{
             code = codeString.toInt()
        }
        catch (e: Exception){
            //Case code empty
            if (editText_code.text.toString() == "") {
                textView_error.text = "Code cannot be empty."
                textView_error.setVisibility(View.VISIBLE)
                return null
            }
            //Case code with letters or symbols
            else{
                textView_error.text = "Wrong code. Please, try again."
                textView_error.setVisibility(View.VISIBLE)
                return null
            }
        }
        var userLogged = userRepo.login(code)

        //Case code not in db
        if(userLogged==null){
            textView_error.text = "Wrong code. Please, try again."
            textView_error.setVisibility(View.VISIBLE)
            return null
        }
        //Case correct code
        return userLogged
    }

}
