package com.module.network.util

import android.widget.Toast
import com.module.network.NetworkLib.Companion.mContext

/**
 * @author: Xiao Bo
 * @date: 26/10/2020
 */
fun toast(msg: String) {
    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
}

fun toastLong(msg: String){
    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
}

