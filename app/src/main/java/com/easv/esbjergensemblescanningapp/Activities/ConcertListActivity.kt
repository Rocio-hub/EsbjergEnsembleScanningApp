package com.easv.esbjergensemblescanningapp.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.Model.BEConcert
import com.easv.esbjergensemblescanningapp.Model.BEUser
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_concertlist.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.Serializable

class ConcertListActivity : AppCompatActivity() {
    private var allConcerts : MutableList<BEConcert> = mutableListOf()
    private lateinit var user : BEUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concertlist)

        var extras: Bundle = intent.extras!!
        allConcerts = extras.getSerializable("allConcerts") as ArrayList<BEConcert>
        user = extras.getSerializable("user") as BEUser

        toolBar.title = "ESBJERG ENSEMBLE"
        setSupportActionBar(toolBar)

        listView_concertItem.adapter = ListAdapter(this, allConcerts.toTypedArray())
        listView_concertItem.setOnItemClickListener {
            parent,
            view,
            position,
            id -> onConcertClick(parent as ListView, view, position)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, StatisticsActivity::class.java)
        intent.putExtra("user", user as Serializable)
        when (item.itemId) {
            R.id.nav_stats ->
            //Toast.makeText(this, "menu item works", Toast.LENGTH_LONG).show()
            startActivity(intent)
        }
        return true
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
            val year = item.Date.dropLast(6)
            val month1 = item.Date.dropLast(3)
            val month2 = month1.drop(5)
            val day = item.Date.drop(8)
            itemDate.text = day+"/"+month2+"/"+year
            val itemTime = resView.findViewById<TextView>(R.id.textView_time)
            itemTime.text = item.Time.dropLast(3)

            return resView
        }
    }
}
