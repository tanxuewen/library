package com.work.library.bluetooth.activity

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.sf.bluetooth.R
import com.work.library.bluetooth.adapter.DeviceAdapter
import com.work.library.bluetooth.adapter.OnRvItemClickListener
import com.work.library.bluetooth.modle.DeviceInfo
import kotlinx.android.synthetic.main.activity_devicelist.*

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/5/7 19:24</p>
 * <p>描述: </p>
 */
class ClassicDeviceListActivity : AppCompatActivity() {

    var bluetoothAdapter: BluetoothAdapter? = null

    var deviceAdapter: DeviceAdapter? = null
    var deviceList: ArrayList<DeviceInfo> = ArrayList()

    var handler = Handler()
    val SCAN_PERIOD = 20 * 1000L;

    var mScanning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devicelist)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        deviceAdapter = DeviceAdapter(this, deviceList)
        deviceAdapter?.listener = object : OnRvItemClickListener<DeviceInfo>() {
            override fun onItemClick(view: View, position: Int, data: DeviceInfo) {
                var resultData = Intent();
                resultData.putExtra("result", data.address)
                setResult(Activity.RESULT_OK, resultData)
                finish()
            }
        }

        device_rcv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        device_rcv.itemAnimator = DefaultItemAnimator()
        device_rcv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        device_rcv.adapter = deviceAdapter

        var filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(mReceiver, filter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scan, menu);
        if (!mScanning) {
            menu?.findItem(R.id.menu_stop)?.isVisible = false;
            menu?.findItem(R.id.menu_scan)?.isVisible = true;
            menu?.findItem(R.id.menu_refresh)?.actionView = null;
        } else {
            menu?.findItem(R.id.menu_stop)?.isVisible = true;
            menu?.findItem(R.id.menu_scan)?.isVisible = false;
            menu?.findItem(R.id.menu_refresh)?.setActionView(R.layout.actionbar_indeterminate_progress);
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_scan -> {
                deviceList.clear()
                deviceAdapter?.notifyDataSetChanged()
                doDiscovery(true)
            }
            R.id.menu_stop -> doDiscovery(false)
        }
        return true
    }


    private fun doDiscovery(scan: Boolean) {

        if (scan) {
            if (bluetoothAdapter?.isDiscovering == true) {
                bluetoothAdapter?.cancelDiscovery()
            }
            mScanning = scan
            bluetoothAdapter?.startDiscovery()

            handler.postDelayed({
                mScanning = false
                bluetoothAdapter?.cancelDiscovery()
                invalidateOptionsMenu()
            }, SCAN_PERIOD)
        } else {
            handler.removeCallbacksAndMessages(null)
            bluetoothAdapter?.cancelDiscovery()
            mScanning = scan
        }

        invalidateOptionsMenu()
    }


    override fun onDestroy() {
        super.onDestroy()
        bluetoothAdapter?.cancelDiscovery()
        unregisterReceiver(mReceiver)
    }

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                var info = DeviceInfo(device.address, device.name
                        ?: "unKnow", 0)
                deviceList.add(info)
                deviceAdapter?.notifyDataSetChanged()

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {

            }
        }
    }
}