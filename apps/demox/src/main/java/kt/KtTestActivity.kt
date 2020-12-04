package kt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ak.demo.R
import kotlinx.android.synthetic.main.activity_kt_test.*
import kt.coroutines.main

class KtTestActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kt_test)
        action_click.setOnClickListener {
//            main()
            testWhen()
        }
    }

    private fun testWhen(){
        var x:String? = null
        when(x){
            "" -> Toast.makeText(this, "?", Toast.LENGTH_SHORT).show()
            null -> Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "haha", Toast.LENGTH_SHORT).show()
        }

    }

}