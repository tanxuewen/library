package com.work.library.bluetooth.ble

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/5/15 16:10</p>
 * <p>描述: </p>
 */
abstract class GattCallback {

    open fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
    }

    open fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
    }

    open fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
    }

    open fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
    }

    open fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
    }

    open fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
    }

    open fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
    }
}