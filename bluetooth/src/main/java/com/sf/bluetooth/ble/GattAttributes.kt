package com.sf.bluetooth.ble

import java.util.*

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/1/30 13:52</p>
 * <p>描述: </p>
 */
object GattAttributes {
    private val attributes = HashMap<String, String>()

    //Services
    val TX_POWER = "00001804-0000-1000-8000-00805f9b34fb"
    val IMMEDIATE_ALERT = "00001802-0000-1000-8000-00805f9b34fb"
    val BATTERY_SERVICE = "0000180f-0000-1000-8000-00805f9b34fb"

    //Characteristics
    var HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb"
    var CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb"
    val ALERT_LEVEL = "00002a06-0000-1000-8000-00805f9b34fb"
    val TX_POWER_LEVEL = "00002a07-0000-1000-8000-00805f9b34fb"
    val BATTERY_LEVEL = "00002a19-0000-1000-8000-00805f9b34fb"

    val CUST_SERVICE = "d973f2e0-b19e-11e2-9e96-0800200c9a66"
    val CUST_TX_DATA = "d973f2e1-b19e-11e2-9e96-0800200c9a66"
    val CUST_RX_DATA = "d973f2e2-b19e-11e2-9e96-0800200c9a66"

    //OTA Service
    val OTA_SERVICE = "8a97f7c0-8506-11e3-baa7-0800200c9a66"
    val OTA_SERVICE_LE = "669a0C20-0008-a7ba-e311-0685c0f7978a"

    val DFU_OTA_IMAGE = "122e8cc0-8508-11e3-baa7-0800200c9a66" //Read
    val DFU_OTA_NEW_IMAGE = "210f99f0-8508-11e3-baa7-0800200c9a66" //Read, WriteWithoutResponse, Write
    val DFU_OTA_NEW_IMAGE_TU_CONTENT = "2691aa80-8508-11e3-baa7-0800200c9a66" //Read, WriteWithoutResponse, Write
    val DFU_OTA_EXPECTED_IMAGE_TU_SEQNUM = "2bdc5760-8508-11e3-baa7-0800200c9a66" //Read, Notify

    init {
        // Sample Services.
        attributes["00001800-0000-1000-8000-00805f9b34fb"] = "Generic Access";
        attributes["00001801-0000-1000-8000-00805f9b34fb"] = "Generic Attribute";
        attributes[IMMEDIATE_ALERT] = "Immediate Alert";
        attributes["00001803-0000-1000-8000-00805f9b34fb"] = "Link Loss";
        attributes[TX_POWER] = "Tx Power";
        attributes["0000180a-0000-1000-8000-00805f9b34fb"] = "Device Information Service";
        attributes["0000180d-0000-1000-8000-00805f9b34fb"] = "Heart Rate Service";
        attributes[BATTERY_SERVICE] = "Battery Service";
        // Sample Characteristics.
        attributes["00002a00-0000-1000-8000-00805f9b34fb"] = "Device Name";
        attributes["00002a01-0000-1000-8000-00805f9b34fb"] = "Appearance";
        attributes["00002a02-0000-1000-8000-00805f9b34fb"] = "Peripheral Privacy Flag";
        attributes["00002a04-0000-1000-8000-00805f9b34fb"] = "Peripheral Preferred Connection Parameters";
        attributes["00002a05-0000-1000-8000-00805f9b34fb"] = "Service Changed";
        attributes[ALERT_LEVEL] = "Alert Level";
        attributes[TX_POWER_LEVEL] = "Tx Power Level";
        attributes[BATTERY_LEVEL] = "Battery Level";
        attributes["00002a23-0000-1000-8000-00805f9b34fb"] = "System ID";
        attributes["00002a24-0000-1000-8000-00805f9b34fb"] = "Model Number String";
        attributes["00002a26-0000-1000-8000-00805f9b34fb"] = "Firmware Revision String";
        attributes["00002a28-0000-1000-8000-00805f9b34fb"] = "Software Revision String";
        attributes["00002a29-0000-1000-8000-00805f9b34fb"] = "Manufacturer Name String";
        attributes["00002a37-0000-1000-8000-00805f9b34fb"] = "Heart Rate Measurement";
        attributes["00002a50-0000-1000-8000-00805f9b34fb"] = "PnP ID";
    }

    fun lookup(uuid: String, defaultName: String): String {
        val name = attributes[uuid]
        return name ?: defaultName
    }
}