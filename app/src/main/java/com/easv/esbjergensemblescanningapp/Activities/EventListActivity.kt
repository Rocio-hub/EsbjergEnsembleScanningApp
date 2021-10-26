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
import com.easv.esbjergensemblescanningapp.Model.BEEvent
import com.easv.esbjergensemblescanningapp.Model.Events
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_eventlist.*

class EventListActivity : AppCompatActivity() {

    private lateinit var events: Events
    private lateinit var eventItems : List<BEEvent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventlist)

        events = Events()
        var eventList = events.getAllEvents()
        listView_eventItems.adapter = EventListAdapter(this, eventList.toTypedArray())
        listView_eventItems.setOnItemClickListener {
            parent,
            view,
            position,
            id -> onEventClick(parent as ListView, view, position)
        }
    }

    private fun onEventClick(parent: ListView?, v: View?, position: Int) {
        val selectedEvent = position
        val intent = Intent(this, EventInfoActivity::class.java)
        intent.putExtra("eventPosition", selectedEvent)
        startActivity(intent)

    }

    internal class EventListAdapter(context: Context, private val items: Array<BEEvent>) : ArrayAdapter<BEEvent>(context, 0, items) {
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
