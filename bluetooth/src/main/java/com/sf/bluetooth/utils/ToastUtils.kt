package com.sf.bluetooth.utils

import android.content.Context
import android.widget.Toast

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/5/18 9:47</p>
 * <p>描述: </p>
 */
object ToastUtils {

    public fun showToast(context: Context, text:String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}