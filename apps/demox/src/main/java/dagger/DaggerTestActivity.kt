package dagger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ak.demo.R
import kotlinx.android.synthetic.main.activity_dagger_test.*

class DaggerTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dagger_test)

        sample_1.setOnClickListener {
            val car = dagger.sample1.Car()
            car.engine.run()
        }

        sample_2.setOnClickListener {
            val car = dagger.sample2.Car()
            car.engine.run()
        }

        sample_3.setOnClickListener {
            val car = dagger.sample3.Car()
            car.engineA.run()
            car.engineB.run()
        }

        sample_4.setOnClickListener {
            val car = dagger.sampled.Car()
            car.engineA.run()
            car.engineB.run()
        }
    }

}
