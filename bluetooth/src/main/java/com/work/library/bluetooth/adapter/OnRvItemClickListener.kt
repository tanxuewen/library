package com.work.library.bluetooth.adapter

import android.view.View

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/1/24 11:59</p>
 * <p>描述: </p>
 */
abstract class OnRvItemClickListener<T> {
    open fun onItemClick(view: View, position: Int, data: T){}
    open fun onItemLongClick(view: View, position: Int, data: T){}
}