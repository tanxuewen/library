package com.sf.bluetooth.classic

import android.util.Log
import com.sf.bluetooth.utils.HexUtils
import java.io.InputStream

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/5/17 11:50</p>
 * <p>描述: </p>
 */
class WirelessNodeConvert : Convert {

    override fun convert(inputStream: InputStream): ByteArray {
        var bytes = 0
        val buffer = ByteArray(1024)

        while (true) {
            bytes = inputStream.read(buffer)

            var real: ByteArray = ByteArray(bytes)
            System.arraycopy(buffer, 0, real, 0, bytes)
            return real
        }
    }
}