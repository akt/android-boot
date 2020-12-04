package com.ak.demo

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import chart.line.LineEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.acitivity_line.*

class LineActivity : AppCompatActivity(), OnChartGestureListener, OnChartValueSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_line)
        setData()
    }


    private fun setData() {

        val dataList = listOf(
                LineEntry("2018年1月", 0f, 3000f),
                LineEntry("2月", 1f, 800f),
                LineEntry("3月", 2f, 1000f),
                LineEntry("4月", 3f, -300f),
                LineEntry("5月", 4f, 1200f),
                LineEntry("6月", 5f, 18000f),
                LineEntry("7月", 6f, -600f),
                LineEntry("8月", 8f, 19000f),
                LineEntry("9月", 9f, 11000f),
                LineEntry("10月", 10f, 10000f),
                LineEntry("11月", 11f, 20000f),
                LineEntry("12月", 12f, -21000f),
                LineEntry("201111111年10月", 9f, 20000f)
        )

//        val dataList = mutableListOf<LineEntry>()

//        for (i in 0..36){
//            dataList.add(LineEntry("${i}月", i.toFloat(), (0..10).shuffled().last().toFloat()))

//        }

        val set1 = LineDataSet(dataList.toList(), "DataSet 1")
        set1.setHighlightValueFormatter { entry ->
            val label = (entry as LineEntry).label
            val value = entry.y
            "$label 收支差\n$value"
        }


        if (Utils.getSDKInt() >= 18) {
            val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
            set1.fillDrawable = drawable
        } else {
            set1.fillColor = Color.BLACK
        }

        chart1.minOffset = 0f
        chart1.xAxis.setLabelCount((dataList.size + 1) / 2)
        chart1.xAxis.granularity = 2.0f
        chart1.setVisibleXRange(11.0f, 11.0f)
        chart1.setDataSet(set1)
        chart1.animateX(2000)
    }

    override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChartLongPressed(me: MotionEvent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChartDoubleTapped(me: MotionEvent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {


        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNothingSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}