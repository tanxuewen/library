package com.work.library.bluetooth.classic

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/5/17 11:29</p>
 * <p>描述: </p>
 */
abstract class ClassicCallback {
    /**
     * 断开连接
     */
    open fun onConnectionStateChange(status: Int) {

    }

    /**
     * 接收数据
     */
    open fun readData(data: ByteArray?) {

    }

    /**
     * 发送数据成功
     */
    open fun writeSuccess(writeBuf: ByteArray?) {

    }
}