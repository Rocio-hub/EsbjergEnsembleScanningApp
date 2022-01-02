package com.easv.esbjergensemblescanningapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.easv.esbjergensemblescanningapp.Model.BEScan
import com.easv.esbjergensemblescanningapp.R

class StatisticsActivity : AppCompatActivity() {

    private lateinit var pieChartView : AnyChartView
    private var scanList : ArrayList<BEScan> = arrayListOf()
  //  private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        getMockScans()

        pieChartView = findViewById(R.id.pieChart)
       // pieChart = findViewById(R.id.pieChart)

        initPieChart()
      //  setDataToPieChart()

    }

    private fun getMockScans() {
        scanList = arrayListOf(
            BEScan(1, 1, 1, "a"),
            BEScan(2, 2, 2, "b"),
            BEScan(3, 3, 3, "c"),
        )
    }

    private fun initPieChart() {
        var pie : Pie = AnyChart.pie()
        var dataEntries : ArrayList<DataEntry> = arrayListOf()


        scanList.forEach(){
            var vde : ValueDataEntry = ValueDataEntry("aa", 23)
          dataEntries.add(vde)
        }
        pie.data(dataEntries)
        pieChartView.setChart(pie)





   /*     pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        //adding padding
        pieChart.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.isWordWrapEnabled = true*/
    }

  /*private fun setDataToPieChart() {

        val dataEntries = ArrayList<DataEntry>()
        dataEntries.add(PieEntry(72f, "Android"))
        dataEntries.add(PieEntry(26f, "Ios"))
        dataEntries.add(PieEntry(2f, "Other"))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#4DD0E1"))
        colors.add(Color.parseColor("#FFF176"))
        colors.add(Color.parseColor("#FF8A65"))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(15f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        //create hole in center
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        //add text in center
        pieChart.setDrawCenterText(true);
        pieChart.centerText = "Mobile OS Market share"



        pieChart.invalidate()

    }*/


}