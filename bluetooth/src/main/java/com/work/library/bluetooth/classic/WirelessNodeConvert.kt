package com.work.library.bluetooth.classic

import java.io.InputStream
import android.R.attr.data



/**
 * <p>作者：01140782</p>
 * <p>日期：2018/5/17 11:50</p>
 * <p>描述: </p>
 */
class WirelessNodeConvert : Convert {

    override fun convert(inputStream: InputStream): ByteArray {
        var bytes = 0
        var buffer = ByteArray(1024)

        while (true) {
            var sof = inputStream.read();
            if(sof.equals(0xF2)){
                var adr = inputStream.read();
                var len_h = inputStream.read();
                var len_l = inputStream.read();
                var length = (len_h and 0xFF shl 8) + (len_l and 0xFF)
                buffer = ByteArray(length + 6)

                buffer[0] = sof.toByte()
                buffer[1] = adr.toByte()
                buffer[2] = len_h.toByte()
                buffer[3] = len_l.toByte()

                var count = 4;

                while (count < length + 6) {
                    count += inputStream.read(buffer, count, length + 6 - count)
                }

//                bytes = inputStream.read(buffer)
//
//                var real: ByteArray = ByteArray(bytes)
//                System.arraycopy(buffer, 0, real, 0, bytes)
                return buffer

            }

        }
    }
}