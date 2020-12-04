package filterview.vm

import filterview.SelectItemListData

/**
 * 选择器对象
 * @author ak
 * @since 08/10/2018
 */
sealed class Item {

    open fun wrapToAdapterList(): List<filterview.vm.Item> {
        return listOf(this)
    }

    class DividerBlock : Item()

    data class CheckItem(val id: Long, val name: String, var checked: Boolean) : Item()

    data class FlatGroup(
        val id: Long,
        val name: String,
        val childList: List<Child>
    ) : Item(){
        override fun wrapToAdapterList(): List<filterview.vm.Item> {
            val list = mutableListOf<filterview.vm.Item>(this)
            list.addAll(childList)
            return list
        }
    }

    data class ExpandableGroup(
        val id: Long,
        val name: String,
        var expand: Boolean,
        val childList: List<Child>
    ) : Item(){
        override fun wrapToAdapterList(): List<filterview.vm.Item> {
            val list = mutableListOf<filterview.vm.Item>(this)
            if (expand){
                list.addAll(childList)
            }
            return list
        }
    }

    data class Child(val id: Long, val name: String, var checked: Boolean) : Item()

    data class Header(val id: Long, val name: String) : Item()


    class Builder(
        private val selectItemList: MutableList<filterview.vm.Item> = mutableListOf(),
        private var type: Int = 0
    ) {

        fun setType(type: Int):Builder{
            this.type = type
            return this
        }

        fun addItem(selectItem: filterview.vm.Item): Builder {
            selectItemList.add(selectItem)
            return this
        }

        fun addAll(collection: Collection<filterview.vm.Item>):Builder{
            selectItemList.addAll(collection)
            return this
        }

        fun build(): SelectItemListData {
            return SelectItemListData(type, selectItemList)
        }

    }

}


