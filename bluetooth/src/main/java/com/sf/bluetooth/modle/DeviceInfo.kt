package com.sf.bluetooth.modle

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/5/8 10:20</p>
 * <p>描述: </p>
 */
class DeviceInfo {

    var address: String = ""

    var name: String = ""

    var rssi: Int = 0

    constructor()

    constructor(address: String):this(){
        this.address = address
    }

    constructor(address: String, name: String, rssi: Int):this(address) {
        this.name = name
        this.rssi = rssi
    }
}