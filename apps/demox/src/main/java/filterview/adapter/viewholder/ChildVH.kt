package filterview.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import com.ak.demo.R
import filterview.vm.Item

class ChildVH(childView: View) : ItemVH(childView) {
    private val checkbox = itemView.findViewById<AppCompatCheckBox>(R.id.check_box)
    private val text = itemView.findViewById<TextView>(R.id.text)
    override fun bind(data: Item, position: Int) {
        if (data is Item.Child) {
            checkbox.isChecked = data.checked
            text.text = data.name
            checkbox.setOnClickListener {
                emitEvent(ItemEvent.TOGGLE to data)
            }
            itemView.setOnClickListener {
                emitEvent(ItemEvent.CLICK to data)
            }
        }
    }

}