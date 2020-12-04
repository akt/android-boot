package coroutines

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ak.demo.R
import com.lab.request.JSLApi
import kotlinx.android.synthetic.main.activity_coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by ak on 2020/6/10.
 */
class CoroutinesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines)
        postBtn.setOnClickListener {
            postRequest()
        }
    }

    private fun postRequest() {
        GlobalScope.launch(Dispatchers.IO) {

            val imageUrl =
                URL("https://www.baidu.com/img/PCfb_5bf082d29588c07f842ccde3f97243ea.png")

            val httpConnection = imageUrl.openConnection() as HttpURLConnection
            httpConnection.doInput = true
            httpConnection.connect()

            val inputStream = httpConnection.inputStream
            val bitmapImage = BitmapFactory.decodeStream(inputStream)

            launch(Dispatchers.Main) {
                imageView.setImageBitmap(bitmapImage)
            }
        }


        GlobalScope.launch {
            try {
//                val searchWeatherResponse =
//                    retrofitService.getCurrentPrice()
                val response =
                    JSLApi.retrofitService.getCbList(System.currentTimeMillis().toString())

                Log.e(log.TAG, response.toString())
                launch(Dispatchers.Main) {
                    textView.text = response.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
