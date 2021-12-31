package com.easv.esbjergensemblescanningapp.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

class ConcertListActivity : AppCompatActivity() {

  //  private var concert: Concert = Concert()
  var allConcerts : MutableList<BEConcert> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concertlist)
        var extras: Bundle = intent.extras!!
        allConcerts = extras.getSerializable("allConcerts") as ArrayList<BEConcert>
      //  concert = Concert()
    //    var concertList = concert.getAllConcerts()


        listView_concertItem.adapter = ListAdapter(this, allConcerts.toTypedArray())
    /*    listView_concertItems.setOnItemClickListener {
            parent,
            view,
            position,
            id -> onConcertClick(parent as ListView, view, position)
        }*/
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
            itemTitle.text = item.title
            val itemDate = resView.findViewById<TextView>(R.id.textView_date)
            itemDate.text = item.Date
            val itemTime = resView.findViewById<TextView>(R.id.textView_time)
            itemTime.text = item.Time

            return resView
        }
    }

}
