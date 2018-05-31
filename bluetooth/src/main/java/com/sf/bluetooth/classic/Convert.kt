package com.sf.bluetooth.classic

import java.io.InputStream

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/5/17 11:39</p>
 * <p>描述: </p>
 */
interface Convert {
    fun convert(inputStream: InputStream): ByteArray
}