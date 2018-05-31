package com.work.library.bluetooth.ble

import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/1/30 13:55</p>
 * <p>描述: 蓝牙连接服务类</p>
 */
class BluetoothLeService : Service() {

    var TAG = "BluetoothLeService"

    private val STATE_DISCONNECTED = 0
    private val STATE_CONNECTING = 1
    private val STATE_CONNECTED = 2

    private var mBluetoothManager: BluetoothManager? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothDeviceAddress: String? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mConnectionState = STATE_DISCONNECTED

    var callbacks = ArrayList<GattCallback>()

    private val mBinder = LocalBinder()

    private val mGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            for (callback in callbacks) {
                callback.onConnectionStateChange(gatt, status, newState)
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnectionState = STATE_CONNECTED
                Log.i(TAG, "Connected to GATT server.")
                // Attempts to discover services after successful connection.
                var flag = mBluetoothGatt?.discoverServices()
                Log.i(TAG, "Attempting to start service discovery:$flag")

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnectionState = STATE_DISCONNECTED
                Log.i(TAG, "Disconnected from GATT server.")
                close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            for (callback in callbacks) {
                callback.onServicesDiscovered(gatt, status)
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {

            for (callback in callbacks) {
                callback.onCharacteristicRead(gatt, characteristic, status)
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)

            for (callback in callbacks) {
                callback.onCharacteristicWrite(gatt, characteristic, status)
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {

            for (callback in callbacks) {
                callback.onCharacteristicChanged(gatt, characteristic)
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
    }

    fun initialize(): Boolean {
        if (mBluetoothManager == null) {
            mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.")
                return false
            }
        }

        mBluetoothAdapter = mBluetoothManager?.adapter
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.")
            return false
        }

        return true
    }

    fun writeCharacteristic(serviceUUID: String, charID: String, bytes: ByteArray): Boolean {
        val custService = mBluetoothGatt?.getService(UUID.fromString(serviceUUID))
        val characteristic = custService?.getCharacteristic(UUID.fromString(charID))
        characteristic?.value = bytes;
        return writeCharacteristic(characteristic)
    }

    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic?): Boolean {
        if (characteristic == null) {
            return false
        }
        return mBluetoothGatt?.writeCharacteristic(characteristic) ?: false
    }

    /**
     * 设置通知属性
     *
     * @param characteristic Characteristic 特征值
     * @param enabled If true, enable notification.  False otherwise.
     */
    fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic,
                                      enabled: Boolean) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        mBluetoothGatt?.setCharacteristicNotification(characteristic, enabled)

        //开启通知属性
        val properties = characteristic.properties
        if (properties or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
            //mBluetoothGatt.setCharacteristicNotification(characteristic, true);
            val descriptor = characteristic.getDescriptor(UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG))
            if (null != descriptor) {
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                mBluetoothGatt?.writeDescriptor(descriptor)
            }
        }
    }

    fun connect(address: String?): Boolean {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.")
            return false
        }

        // Previously connected device.  Try to reconnect.
//        if (mBluetoothDeviceAddress != null && address == mBluetoothDeviceAddress
//                && mBluetoothGatt != null) {
//            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.")
//            if (mBluetoothGatt!!.connect()) {
//                mConnectionState = STATE_CONNECTING
//                return true
//            } else {
//                return false
//            }
//        }

        val device = mBluetoothAdapter?.getRemoteDevice(address)
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.")
            return false
        }

        if (mBluetoothGatt != null) {
            mBluetoothGatt?.close()
            Log.d(TAG, "close a old connection.")
            mBluetoothGatt = null
        }

        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback)
        Log.d(TAG, "Trying to create a new connection.")
        mBluetoothDeviceAddress = address
        mConnectionState = STATE_CONNECTING
        return true
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * `BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)`
     * callback.
     */
    fun disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        mBluetoothGatt?.disconnect()
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        close()
        return super.onUnbind(intent)
    }

    private fun close() {
        if (mBluetoothGatt == null) {
            return
        }
        mBluetoothGatt?.close()
        mBluetoothGatt = null
    }

    inner class LocalBinder : Binder() {
        fun getService(): BluetoothLeService {
            return this@BluetoothLeService
        }
    }

    fun getGattService(uuid: UUID): BluetoothGattService? {
        return mBluetoothGatt?.getService(uuid)
    }

    fun addCallback(callback: GattCallback): Boolean {
        if (callbacks.contains(callback)) {
            return true
        }
        return callbacks.add(callback)
    }

    fun removeCallback(callback: GattCallback): Boolean {
        return callbacks.remove(callback)
    }

    fun removeAllCallback(){
        callbacks.clear()
    }
}