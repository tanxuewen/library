package com.work.library.bluetooth.activity

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
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
import java.util.*

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/5/7 19:24</p>
 * <p>描述: </p>
 */
class LeDeviceListActivity : AppCompatActivity() {

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

//        bluetoothAdapter?.bluetoothLeScanner?.startScan(callback)
    }

    var callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {

            var device = result.device;

            var address = device.address

            for (deviceInfo in deviceList) {
                if (deviceInfo.address == address) {
                    deviceList.remove(deviceInfo)
                    break
                }
            }

            var deviceInfo = DeviceInfo(device.address, device.name
                    ?: "unKnow", result.rssi)

            deviceList.add(deviceInfo)

            deviceList.sortWith(comparator)
            deviceAdapter?.notifyDataSetChanged()

        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                var device = result.device;
                var address = device.address
                for (deviceInfo in deviceList) {
                    if (deviceInfo.address == address) {
                        deviceList.remove(deviceInfo)
                        break
                    }
                }
                var deviceInfo = DeviceInfo(device.address, device.name, result.rssi)
                deviceList.add(deviceInfo)
            }
            deviceList.sortWith(comparator)
            deviceAdapter?.notifyDataSetChanged()
        }

        override fun onScanFailed(errorCode: Int) {}
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
                scanLeDevice(true)
            }
            R.id.menu_stop -> scanLeDevice(false)
        }
        return true
    }

    private fun scanLeDevice(scan: Boolean) {
        if (scan) {
            mScanning = true
            bluetoothAdapter?.bluetoothLeScanner?.startScan(callback)
            handler.postDelayed(Runnable {
                mScanning = false
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(callback)
                invalidateOptionsMenu()
            }, SCAN_PERIOD)
        } else {
            handler.removeCallbacksAndMessages(null)
            mScanning = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(callback)
        }
        invalidateOptionsMenu()
    }


    override fun onDestroy() {
        super.onDestroy()
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(callback)
    }

    var comparator = Comparator<DeviceInfo> { o1, o2 ->
        if (o1.rssi < o2.rssi) {
            1
        } else {
            -1
        }
    }
}