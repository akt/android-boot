package filterview

import filterview.vm.Item

/**
 * @author ak
 * @since 09/10/2018
 */
data class SelectItemListData(
    val type: Int,
    val list: List<Item>
) {
    fun getAdapterList(): List<Item> {
        return list.flatMap { it.wrapToAdapterList() }
    }
}