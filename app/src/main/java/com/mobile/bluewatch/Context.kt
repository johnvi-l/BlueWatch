package com.mobile.bluewatch

import androidx.annotation.StringRes

val @receiver:StringRes Int.stringValue: String
    get() = BaseApplication.getInstance().getString(this)

fun Int.toast() {
    stringValue?.toast()
}

fun String.toast() {
//    SmartToast.classic().showInCenter(this)
}




