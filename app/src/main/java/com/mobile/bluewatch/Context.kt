package com.mobile.bluewatch

import android.widget.Toast
import androidx.annotation.StringRes

val @receiver:StringRes Int.stringValue: String
    get() = BaseApplication.getInstance().getString(this)

fun Int.toast() {
    stringValue?.toast()
}

fun String.toast() {
    Toast.makeText(BaseApplication.getInstance(), this, Toast.LENGTH_SHORT).show()
//    SmartToast.classic().showInCenter(this)
}


fun ByteArray.toHexWithSpaces(): String {
    return joinToString(" ") { "%02x".format(it) }
}
fun ByteArray.to16Hex(): String {
    return map { it.toChar().toString() }.joinToString(" ")
}

fun ByteArray.byteArrayToHexString(): String {
    val hexChars = CharArray(size * 2)

    for (i in indices) {
        val v = i.toInt() and 0xFF
        hexChars[i * 2] = "0123456789ABCDEF"[v shr 4]
        hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
    }

    return String(hexChars)
}


