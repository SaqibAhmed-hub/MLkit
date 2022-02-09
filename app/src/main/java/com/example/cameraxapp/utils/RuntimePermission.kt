package com.example.cameraxapp.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


internal object RuntimePermission {

    fun checkPermissionGranted(context: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity, permission: String, REQUEST_CODE: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_CODE)
    }

    fun requestPermissions(activity: Activity, permission: Array<String>, REQUEST_CODE: Int) {
        ActivityCompat.requestPermissions(activity, permission, REQUEST_CODE)
    }

    fun onRequestPermissionResult(grantResult: IntArray,listener: RPListener){
        if (grantResult.size > 0){
            for (grantresults in grantResult){
                if (grantresults == PackageManager.PERMISSION_GRANTED){
                    listener.onPermissionGranted()
                }else{
                    listener.onPermissionDenied()
                }
            }
        }

    }

}