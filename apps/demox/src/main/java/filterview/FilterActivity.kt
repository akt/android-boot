package filterview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.demo.R
import filterview.adapter.ItemAdapter
import filterview.vm.Item
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.acitivity_animation.*

/**
 * @author ak
 * @since 04/09/2018
 */
class FilterActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val adapter = ItemAdapter(this)

        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FilterActivity)
            this.adapter = adapter
        }

        val selectItemListData = Item.Builder().addAll(
            listOf(
                Item.DividerBlock(),
                Item.CheckItem(0, "全部", false),
                Item.Header(0, "收入"),
                Item.ExpandableGroup(
                    1, "餐饮", true, listOf(
                        Item.Child(
                            2, "早餐", false
                        ),
                        Item.Child(
                            3, "午餐", true
                        ),
                        Item.Child(
                            4, "晚餐", false
                        )
                    )
                ),

                Item.FlatGroup(
                    5, "餐饮", listOf(
                        Item.Child(
                            2, "早餐", false
                        ),
                        Item.Child(
                            3, "午餐", true
                        ),
                        Item.Child(
                            4, "晚餐", false
                        )
                    )
                ),
                Item.Header(0, "支出"),

                Item.ExpandableGroup(
                    6, "餐饮", true, listOf(
                        Item.Child(
                            2, "早餐", false
                        ),
                        Item.Child(
                            3, "午餐", true
                        ),
                        Item.Child(
                            4, "晚餐", false
                        )
                    )
                )
            )
        ).build()

        adapter.setData(selectItemListData)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}