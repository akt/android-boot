package filterview.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.ak.demo.R
import filterview.vm.Item
import filterview.widget.TripleCheckBox

/**
 * @author ak
 * @since 10/10/2018
 */
class FlatGroupVH(groupView: View) : ItemVH(groupView) {

    private val nameView = itemView!!.findViewById<TextView>(R.id.name)
    private val checkBox = itemView.findViewById<TripleCheckBox>(R.id.check_box)

    override fun bind(data: Item, position: Int) {
        if (data is Item.FlatGroup) {
            nameView.text = data.name
            val originSize = data.childList.size
            val checkedChildList = data.childList.filter { it.checked }
            when {
                checkedChildList.isEmpty() -> checkBox.checkState = TripleCheckBox.EMPTY
                checkedChildList.size == originSize -> checkBox.checkState = TripleCheckBox.FULL
                else -> checkBox.checkState = TripleCheckBox.PARTIAL
            }
            checkBox.setOnClickListener {
                emitEvent(ItemEvent.TOGGLE to data)
            }
        }
    }
}