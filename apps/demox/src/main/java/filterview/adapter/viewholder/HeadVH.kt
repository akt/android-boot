package filterview.adapter.viewholder

import android.widget.TextView
import filterview.vm.Item

class HeadVH(textView: TextView) : ItemVH(textView) {
    override fun bind(data: Item, position: Int) {
        if (data is Item.Header) {
            (itemView as TextView).text = data.name
        }
    }
}