package filterview.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import com.ak.demo.R
import filterview.vm.Item
import filterview.widget.TripleCheckBox

class ExpandableGroupVH(groupView: View) : ItemVH(groupView) {

    private val contentView = itemView.findViewById<View>(R.id.content_view)
    private val nameView = itemView!!.findViewById<TextView>(R.id.name)
    private val summaryView = itemView!!.findViewById<TextView>(R.id.summary)
    private val checkBox = itemView.findViewById<TripleCheckBox>(R.id.check_box)
    private val expandStatus = itemView.findViewById<AppCompatCheckBox>(
        R.id.expand_status
    )

    override fun bind(data: Item, position: Int) {
        if (data is Item.ExpandableGroup) {
            nameView.text = data.name
            val originSize = data.childList.size
            val checkedChildList = data.childList.filter { it.checked }
            when {
                checkedChildList.isEmpty() -> checkBox.checkState =
                        TripleCheckBox.EMPTY
                checkedChildList.size == originSize -> checkBox.checkState =
                        TripleCheckBox.FULL
                else -> checkBox.checkState = TripleCheckBox.PARTIAL
            }
            val expandEnable =
                if (data.childList.isNotEmpty()) {
                    expandStatus.visibility = View.VISIBLE
                    expandStatus.isChecked = data.expand
                    summaryView.text = when {
                        checkedChildList.size == originSize -> "全部"
                        checkedChildList.size > 1 -> checkedChildList.joinToString(
                            postfix = "等${checkedChildList.size}个",
                            transform = Item.Child::name
                        )
                        else -> checkedChildList.joinToString(transform = Item.Child::name)
                    }
                    true
                } else {
                    expandStatus.visibility = View.GONE
                    summaryView.text = ""
                    false
                }
            checkBox.setOnClickListener {
                emitEvent(ItemEvent.TOGGLE to data)
            }
            contentView.setOnClickListener {
                if (expandEnable) {
                    emitEvent(ItemEvent.CLICK to data)
                }
            }
        }
    }
}