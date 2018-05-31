package com.work.library.bluetooth.adapter

import android.content.Context
import android.view.View
import com.sf.bluetooth.R
import com.work.library.bluetooth.modle.DeviceInfo

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/1/22 20:13</p>
 * <p>描述: </p>
 */
class DeviceAdapter(context: Context, list: ArrayList<DeviceInfo>) : EasyRVAdapter<DeviceInfo>(context, list, R.layout.item_device) {

    var listener: OnRvItemClickListener<DeviceInfo>? = null

    override fun onBindData(viewHolder: EasyRVHolder, position: Int, item: DeviceInfo) {
//
        viewHolder.setText(R.id.item_deviceName, item.name ?: "unKnow")
        viewHolder.setText(R.id.item_macAddress, item.address)
        viewHolder.setText(R.id.item_rssi, "${item.rssi}DBm")

        viewHolder.setOnItemViewClickListener(View.OnClickListener {
            listener?.onItemClick(viewHolder.itemView, position, item)
        })
    }

}