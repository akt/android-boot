package com.ak.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import chart.PieData
import kotlinx.android.synthetic.main.acitivity_pie.*

class PieActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.acitivity_pie)
        val param1 = PieData("娱乐", 666.00,this.resources.getColor( R.color.pie_part_1))
        /*val param2 = PieData("居家", 446.00, ContextCompat.getColor(this, R.color.pie_part_2))
        val param3 = PieData("交通", 120.00, ContextCompat.getColor(this, R.color.pie_part_3))
        val param4 = PieData("餐饮", 99.00, ContextCompat.getColor(this, R.color.pie_part_4))
        val param5 = PieData("生意", 88.00, ContextCompat.getColor(this, R.color.pie_part_5))
        */
        val xlist = arrayListOf(param1)
        chartPie.setData(xlist, "")
        chartPie.invalidate()
        chartPie.setOnValueChangedListener { _, pieData, percent ->
            Log.d("????", pieData.name + "===" + percent)
        }


        val param6 = PieData("娱乐", 666.00, this.resources.getColor( R.color.pie_part_1))
        val param7 = PieData("居家", 446.00, this.resources.getColor( R.color.pie_part_2))
        val param8 = PieData("交通", 120.00, this.resources.getColor( R.color.pie_part_3))
        val param9 = PieData("餐饮", 99.00, this.resources.getColor(  R.color.pie_part_4))
        val param10 = PieData("生意", 88.00, this.resources.getColor(  R.color.pie_part_5))

        val mList = arrayListOf(param6, param7, param8, param9, param10)
        chartPie_right.setData(mList, "")
        chartPie.invalidate()
        chartPieContainer.setItemClickToScroll()
    }


}