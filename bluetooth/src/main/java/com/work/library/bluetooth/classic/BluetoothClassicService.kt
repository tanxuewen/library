package com.work.library.bluetooth.classic

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/5/17 10:47</p>
 * <p>描述: </p>
 */
class BluetoothClassicService {

    private val TAG = "BluetoothClassicService"

    companion object {
        //连接状态
        val STATE_CONNECTING = 1
        val STATE_CONNECTED = 2
        val STATE_DISCONNECT = 3
    }

    private var mAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var mConnectThread: ConnectThread? = null
    private var mConnectedThread: ConnectedThread? = null
    private var mState = STATE_DISCONNECT
    private var uuid = "00001101-0000-1000-8000-00805F9B34FB"
    var callbacks = ArrayList<ClassicCallback>()
    var convert: Convert? = null

    fun connect(address: String) {
        var device = mAdapter.getRemoteDevice(address)
        connect(device)
    }

    @Synchronized
    fun connect(device: BluetoothDevice) {
        Log.d(TAG, "connect to: $device")

        if (mState == STATE_CONNECTING) {
            mConnectThread?.cancel()
            mConnectThread = null
        }

        mConnectedThread?.cancel()
        mConnectedThread = null

        mConnectThread = ConnectThread(device)
        mConnectThread?.start()
        setState(STATE_CONNECTING)
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     */
    @Synchronized
    fun connected(socket: BluetoothSocket?, device: BluetoothDevice) {
        mConnectThread?.cancel()
        mConnectThread = null

        mConnectedThread?.cancel()
        mConnectedThread = null

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = ConnectedThread(socket)
        mConnectedThread?.start()

        setState(STATE_CONNECTED)
    }

    /**
     * Stop all threads
     */
    @Synchronized
    fun stop() {
        Log.d(TAG, "stop")

        mConnectThread?.cancel()
        mConnectThread = null

        mConnectedThread?.cancel()
        mConnectedThread = null

        setState(STATE_DISCONNECT)
    }

    @Synchronized
    fun setState(state: Int) {
        Log.d(TAG, "setState() $mState -> $state")
        mState = state

        for (callback in callbacks) {
            callback.onConnectionStateChange(state)
        }
    }

    fun getState(): Int {
        return mState
    }

    fun write(out: ByteArray) {
        synchronized(this) {
            if (mState != STATE_CONNECTED) return
            mConnectedThread?.write(out)
        }
    }

    private fun connectLost() {
        setState(STATE_DISCONNECT)
        stop()
    }

    //设置服务UUID
    fun setUUID(uuid: String) {
        this.uuid = uuid
    }

    fun addCallback(callback: ClassicCallback): Boolean {
        if (callbacks.contains(callback)) {
            return true
        }
        return callbacks.add(callback)
    }

    fun removeCallback(callback: ClassicCallback): Boolean {
        return callbacks.remove(callback)
    }

    fun removeAllCallback() {
        callbacks.clear()
    }

    private inner class ConnectThread(var mmDevice: BluetoothDevice) : Thread() {
        val mmSocket: BluetoothSocket?

        init {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        }

        override fun run() {
            mAdapter.cancelDiscovery()

            try {
                mmSocket?.connect()
            } catch (e: IOException) {
                cancel()
                connectLost()
                return
            }

            // Reset the ConnectThread because we're done
            synchronized(this@BluetoothClassicService) {
                mConnectThread = null
            }

            connected(mmSocket, mmDevice)
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {

            }
        }
    }

    private inner class ConnectedThread(val mmSocket: BluetoothSocket?) : Thread() {
        val mmInStream: InputStream?
        val mmOutStream: OutputStream?

        init {
            mmInStream = mmSocket?.inputStream
            mmOutStream = mmSocket?.outputStream
        }

        override fun run() {
            while (true) {
                try {
                    if (mmInStream != null) {
                        val realBuffer = convert?.convert(mmInStream)
                        for (callback in callbacks) {
                            callback.readData(realBuffer)
                        }
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "disconnected", e)
                    connectLost()
                    break
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         */
        fun write(buffer: ByteArray) {
            try {
                mmOutStream?.write(buffer)
                for (callback in callbacks){
                    callback.writeSuccess(buffer)
                }
            } catch (e: IOException) {
                Log.e(TAG, "Exception during write", e)
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of connect socket failed", e)
            }
        }
    }
}