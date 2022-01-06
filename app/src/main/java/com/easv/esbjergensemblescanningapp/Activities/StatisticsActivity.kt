package com.easv.esbjergensemblescanningapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.easv.esbjergensemblescanningapp.Model.BEConcert
import com.easv.esbjergensemblescanningapp.Model.BEScan
import com.easv.esbjergensemblescanningapp.Model.BEUser
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_statistics.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class StatisticsActivity : AppCompatActivity() {
    private lateinit var pieChartView : AnyChartView
    private var scanList : ArrayList<BEScan> = arrayListOf()
    private lateinit var user : BEUser
    private lateinit var allScans : MutableList<BEScan>
    private lateinit var allConcerts : MutableList<BEConcert>
    private var totalScans: Int = 0
    private var userScans: Int = 0
    private var userScanPercentage: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        pieChartView = findViewById(R.id.pieChart)

        var extras: Bundle = intent.extras!!
        allScans = extras.getSerializable("allScans") as MutableList<BEScan>
        user = extras.getSerializable("user") as BEUser
        allConcerts = extras.getSerializable("allConcerts") as MutableList<BEConcert>

        textView_user.text = user.firstName.toUpperCase()+" "+user.lastName.toUpperCase()

        getMockScans()
        initPieChart()
        initStats()
    }

    private fun getMockScans() {
        scanList = arrayListOf(
                BEScan(1, 3726, 1, "d5UKu5"),
                BEScan(2, 3726, 2, "2svnDn"),
                BEScan(3, 3726, 1, "Pgq2Tf"),
                BEScan(4, 3726, 2, "3MApGz"),
                BEScan(5, 3733, 1, "ZX7eB2"),
                BEScan(6, 3733, 2, "9VeBnx"),
                BEScan(7, 3733, 1, "Gxcr6m"),
                BEScan(8, 3733, 2, "3aEW6F"),
                BEScan(9, 3733, 1, "Qent9A"),
                BEScan(10, 3733, 1, "pd3H97"),
                BEScan(11, 3733, 2, "4QtFAT"),
                BEScan(12, 3744, 1, "vgVY54"),
                BEScan(13, 3744, 1, "ryr8G9"),
                BEScan(14, 3753, 2, "7gB6Fx"),
                BEScan(15, 3753, 1, "g2e9Lg"),
                BEScan(16, 3744, 1, "f4XsNG"),
                BEScan(17, 3744, 2, "CDN7tc"),
                BEScan(18, 3744, 1, "4rtLdq"),
                BEScan(19, 3764, 1, "34TdaX"),
                BEScan(20, 3764, 2, "JJhR4E"),
                BEScan(21, 3764, 1, "E7BEmu"),
        )
    }

    private fun initPieChart() {
        var pie : Pie = AnyChart.pie()
        var dataEntries : ArrayList<DataEntry> = arrayListOf()
        val byConcertId = scanList.groupBy { it.concertId }

        byConcertId.forEach(){
            for(item in allConcerts) {
                if (item.id == it.key){
                    dataEntries.add(ValueDataEntry(item.title, it.value.size))
                }
            }
        }
        pie.data(dataEntries)
        pieChartView.setChart(pie)
    }

    private fun initStats() {
        totalScans = scanList.size
        userScans = 0
        scanList.forEach() {
            if (it.userId == user.id) {
                userScans++
            }
        }

        val df = DecimalFormat("#.#", DecimalFormatSymbols(Locale.ENGLISH)).apply {
            roundingMode = RoundingMode.HALF_UP
        }

        userScanPercentage = df.format((userScans.toDouble() * 100 / totalScans)).toDouble()

        textView_totalScans.text = "Total Scans: " + totalScans.toString()
        textView_userScans.text = "Scans made by you: " + userScans.toString()
        textView_userScanPercent.text = "Scans made by you (%): " + userScanPercentage.toString() + "%"
    }
}