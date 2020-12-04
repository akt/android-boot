package com.ak.demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lab.db.ui.BookActivity
import coroutines.CoroutinesActivity
import dagger.DaggerTestActivity
import filterview.FilterActivity
import kotlinx.android.synthetic.main.activity_main.*
import kt.KtTestActivity
import rxaction.RxActivity
import ui.UIDemoActivity
import video.VideoActivity
import video.ui.CtVideo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("mm =null=",judgeSize(null).toString())
        Log.e("mm =1=",judgeSize(setOf("111")).toString())
        Log.e("mm =3=",judgeSize(setOf("111","222","333")).toString())
        to_pie_activity.setOnClickListener { x ->
            x.alpha = 0.9f
        }
    }

    private fun judgeSize(mm: Set<String>?): Boolean {
        return mm?.size != 1
    }


    fun onClick(v: View?) {
//        "http://livesec.mudu.tv/watch/aume04.m3u8?auth_key=1528776027-710214-0-83fc421f47586d60db2263a0a67cb66f"
//        http://livesec.mudu.tv/watch/hshys4.m3u8?auth_key=1528794723-425669-0-9527970d582b02cc902132c237847dcb
        //http://cloud.video.taobao.com/play/u/1748679248/p/2/e/3/t/1/50154022153.m3u8
        when (v?.id) {
            R.id.to_pie_activity -> startActivity(Intent(this, PieActivity::class.java))
            R.id.to_line_activity -> startActivity(Intent(this, LineActivity::class.java))
            R.id.to_switch_activity -> startActivity(Intent(this, SwitchActivity::class.java))
            R.id.to_video_activity -> {
                startActivity(Intent(this, VideoActivity::class.java)
                        .putExtra(VideoActivity.VIDEO_KEY, CtVideo("xxx", "http://livesec.mudu.tv/watch/hshys4.m3u8?auth_key=1528794723-425669-0-9527970d582b02cc902132c237847dcb", "")))
            }
            R.id.rx_action -> startActivity(Intent(this, RxActivity::class.java))
            R.id.filter_test -> startActivity(Intent(this, FilterActivity::class.java))
            R.id.dagger_test -> startActivity(Intent(this, DaggerTestActivity::class.java))
            R.id.kt_test -> startActivity(Intent(this, KtTestActivity::class.java))
            R.id.crash_test -> Test.crashInJava()
            R.id.db_test -> startActivity(Intent(this, BookActivity::class.java))
            R.id.ui_demo -> startActivity(Intent(this, UIDemoActivity::class.java))
            R.id.coroutine_demo -> startActivity(Intent(this, CoroutinesActivity::class.java))
        }
    }




}
