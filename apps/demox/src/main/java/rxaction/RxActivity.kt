package rxaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ak.demo.R
import com.example.bizinterface.ModuleManager
import com.example.bizinterface.user.IUserBizModule
import kotlinx.android.synthetic.main.activity_rx_action.*
import java.util.*

/**
 * @author ak
 * @since 04/09/2018
 */
class RxActivity : AppCompatActivity(){

    private var mworkLate = getWorkLate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_action)

        work_late_click.setOnClickListener {
            work_late_click.text = mworkLate.id
            getWorkLate()
            ModuleManager.getModule(IUserBizModule::class.java).login(this)
        }

        switch_map_action.setOnClickListener { switchMapSample() }
        flat_map_action.setOnClickListener { flatMapSample() }
        concat_map_action.setOnClickListener { concatMapSample() }
        merge_action.setOnClickListener { mergeSample() }
        concat_action.setOnClickListener { concatSample() }
        zip_action.setOnClickListener { zipSample() }
    }

    data class WorkLate(
        val id: String,
        val text:String
    )

    fun getWorkLate():WorkLate{
        return WorkLate(Random().nextInt().toString(), "222")
    }



}
