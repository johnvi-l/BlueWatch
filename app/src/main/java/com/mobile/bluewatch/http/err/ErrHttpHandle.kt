package com.mobile.bluewatch.http.err

import com.mobile.bluewatch.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ErrHttpHandle {
    companion object {

        const val NOT_SUCCESS = 0x12
    }

    fun handleErr(errCode: Int, errMsg: String) {
        GlobalScope.launch(Dispatchers.Main) {
            when (errCode) {
                NOT_SUCCESS -> doNetWorkErr(errMsg)
            }
        }
    }

    private fun doNetWorkErr(errMsg: String) {
        errMsg.toast()
    }

}