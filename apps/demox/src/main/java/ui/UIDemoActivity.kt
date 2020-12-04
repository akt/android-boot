package ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ak.demo.R
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_ui_demo.*
import ui.widget.DrawableText

class UIDemoActivity: AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_demo)
        val end = DrawableText("BTC")

        tvDisplay.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null)
        compositeDisposable.add(edtText.textChanges().subscribe {
            tvDisplay.text = it
        })

        vibrateCustom.setOnClickListener {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(50);
        }

        vibrateNormal.setOnClickListener {
            var vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator;
            vibrator.vibrate(100);
        }

    }


}
