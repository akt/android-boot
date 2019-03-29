package com.aboot

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lab.db.ui.BookActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun onClick(v: View) {
        when (v.id) {
            R.id.to_db_activity -> startActivity(Intent(this, BookActivity::class.java))
        }
    }
}
