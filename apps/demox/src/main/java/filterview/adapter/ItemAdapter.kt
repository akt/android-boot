package filterview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ak.demo.R
import filterview.SelectItemListData
import filterview.adapter.viewholder.*
import filterview.vm.Item
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ak
 */

class ItemAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEAD = 0
        private const val TYPE_EXPAND_GROUP = 1
        private const val TYPE_FLAT_GROUP = 2
        private const val TYPE_CHILD = 3
        private const val TYPE_DIVIDER_BLOCK = 4
        private const val TYPE_CHECK_ITEM = 5
    }

    private val dataList: ArrayList<Item> = ArrayList()
    private var data = SelectItemListData(0, listOf())


    private val compositeDisposable = CompositeDisposable()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
         itemEvent.subscribe {
            val event = it.first
            val data = it.second
            when (data) {
                is Item.ExpandableGroup -> {
                    if (it.first == ItemEvent.CLICK) {
                        data.expand = !data.expand
                        refreshData()
                    } else if (event == ItemEvent.TOGGLE) {
                        val allCheck = data.childList.all { child -> child.checked }
                        if (allCheck) {
                            data.childList.forEach { child -> child.checked = false }
                        } else {
                            data.childList.forEach { child -> child.checked = true }
                        }
                        refreshData()
                    }
                }
                is Item.Child -> {
                   data.checked = !data.checked
                    refreshData()
                }
                is Item.FlatGroup ->{
                    val allCheck = data.childList.all { child -> child.checked }
                    if (allCheck) {
                        data.childList.forEach { child -> child.checked = false }
                    } else {
                        data.childList.forEach { child -> child.checked = true }
                    }
                    refreshData()
                }
                is Item.CheckItem ->{
                    data.checked = !data.checked
                    refreshData()
                }
            }


        }.let {
             compositeDisposable.add(it)
         }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.clear()
    }

    fun setData(newData: SelectItemListData) {
        this.data = newData
        refreshData()
    }

    private fun refreshData() {
        this.dataList.clear()
        this.dataList.addAll(data.getAdapterList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEAD ->
                HeadVH(
                    inflateView(
                        context,
                        R.layout.item_select_value_header,
                        parent
                    ) as TextView
                )
            TYPE_EXPAND_GROUP -> ExpandableGroupVH(
                inflateView(
                    context,
                    R.layout.item_select_value_expandable_group,
                    parent
                )
            )
            TYPE_CHILD -> ChildVH(
                inflateView(
                    context,
                    R.layout.item_select_value_child,
                    parent
                )
            )
            TYPE_FLAT_GROUP -> FlatGroupVH(
                inflateView(
                    context, R.layout.item_select_value_flat_group,
                    parent
                )
            )
            TYPE_DIVIDER_BLOCK -> DividerBlockVH(
                inflateView(
                    context, R.layout.item_select_value_divider_block,
                    parent
                )
            )
            TYPE_CHECK_ITEM -> CheckedItemVH(inflateView(
                context, R.layout.item_select_value_item,
                parent
            ))
            else -> throw IllegalStateException("Unknown type")
        }
    }

    override fun getItemCount() = dataList.size


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val selectItemVM = dataList[position]
        (viewHolder as ItemVH).bind(selectItemVM, position)
    }

    private fun inflateView(context: Context, resId: Int, parent: ViewGroup) =
        LayoutInflater.from(context).inflate(resId, parent, false)


    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataList[position]
        return when (item) {
            is Item.ExpandableGroup -> TYPE_EXPAND_GROUP
            is Item.Child -> TYPE_CHILD
            is Item.Header -> TYPE_HEAD
            is Item.FlatGroup -> TYPE_FLAT_GROUP
            is Item.DividerBlock -> TYPE_DIVIDER_BLOCK
            is Item.CheckItem -> TYPE_CHECK_ITEM
        }
    }
}

