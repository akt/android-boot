package filterview.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import filterview.vm.Item
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author ak
 * @since 09/10/2018
 */
abstract class ItemVH(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(data: Item, position: Int) {}
    protected fun emitEvent(eventParams: Pair<ItemEvent, Item>) {
        itemEventSubject.onNext(eventParams)
    }
}

enum class ItemEvent {
    TOGGLE, CLICK
}

private val itemEventSubject: PublishSubject<Pair<ItemEvent, Item>> =
    PublishSubject.create<Pair<ItemEvent, Item>>()
val itemEvent: Observable<Pair<ItemEvent, Item>> =
    itemEventSubject

/**
 * 分割块
 * */
class DividerBlockVH(childView: View) : ItemVH(childView)
