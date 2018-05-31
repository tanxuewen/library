package com.work.library.bluetooth.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/1/23 10:07</p>
 * <p>描述: </p>
 */
abstract class EasyRVAdapter<T>(protected var mContext: Context, protected var mList: ArrayList<T>, vararg layoutIds: Int) : RecyclerView.Adapter<EasyRVHolder>() {
    protected var layoutIds: IntArray
    protected var mLInflater: LayoutInflater

    private val mConvertViews = SparseArray<View>()

    init {
        this.layoutIds = layoutIds
        this.mLInflater = LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EasyRVHolder {
        if (viewType < 0 || viewType > layoutIds.size) {
            throw ArrayIndexOutOfBoundsException("layoutIndex")
        }
        if (layoutIds.isEmpty()) {
            throw IllegalArgumentException("not layoutId")
        }
        val layoutId = layoutIds[viewType]
        var view = mConvertViews.get(layoutId)
        if (view == null) {
            view = mLInflater.inflate(layoutId, parent, false)
        }
        var viewHolder = if(view?.tag == null) null else view.tag as EasyRVHolder
        if (viewHolder?.layoutId != layoutId) {
            viewHolder = EasyRVHolder(mContext, layoutId, view)
            return viewHolder
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: EasyRVHolder, position: Int) {
        val item = mList[position]
        onBindData(holder, position, item)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIndex(position, mList[position])
    }

    /**
     * 指定item布局样式在layoutIds的索引。默认为第一个
     *
     * @param position
     * @param item
     * @return
     */
    fun getLayoutIndex(position: Int, item: T): Int {
        return 0
    }

    protected abstract fun onBindData(viewHolder: EasyRVHolder, position: Int, item: T)

    fun addAll(list: List<T>): Boolean {
        val result = mList.addAll(list)
        notifyDataSetChanged()
        return result
    }

    fun addAll(position: Int, list: List<T>): Boolean {
        val result = mList.addAll(position, list)
        notifyDataSetChanged()
        return result
    }

    fun add(data: T) {
        mList.add(data)
        notifyDataSetChanged()
    }

    fun add(position: Int, data: T) {
        mList.add(position, data)
        notifyDataSetChanged()
    }

    fun clearAndAdd(list: List<T>): Boolean {
        mList.clear()
        val result = mList.addAll(list)
        notifyDataSetChanged()
        return result
    }

    fun clear() {
        mList.clear()
        notifyDataSetChanged()
    }

    operator fun contains(data: T): Boolean {
        return mList.contains(data)
    }

    fun getData(index: Int): T {
        return mList[index]
    }

    fun modify(oldData: T, newData: T) {
        modify(mList.indexOf(oldData), newData)
    }

    fun modify(index: Int, newData: T) {
        mList[index] = newData
        notifyDataSetChanged()
    }

    fun remove(data: T): Boolean {
        val result = mList.remove(data)
        notifyDataSetChanged()
        return result
    }

    fun remove(index: Int) {
        mList.removeAt(index)
        notifyDataSetChanged()
    }
}