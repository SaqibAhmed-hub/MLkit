package com.example.cameraxapp

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import com.example.cameraxapp.cameraX.CameraManager
import com.example.cameraxapp.utils.RPListener
import com.example.cameraxapp.utils.RuntimePermission
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private lateinit var previous: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeFullScreen()
        createCameraManager()
        checkPermission()
        onClick()
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            this,
            camera_x,
            this,
            graphic_overley
        )
    }

    private fun onClick() {
        flip_iv.setOnClickListener {
            cameraManager.changeCameraSelector()
        }
        takePhoto_btn.setOnClickListener {
            cameraManager.capturePhoto()
        }
    }

    private fun makeFullScreen() {
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun checkPermission() {
        hasPermissionGranted = RuntimePermission.checkPermissionGranted(this, REQUIRED_PERMISSION)
        if (hasPermissionGranted) {
            previous = intent.getStringExtra("act")!!
            when (previous) {
                "text" -> cameraManager.getValue(1)
                "face" -> cameraManager.getValue(2)
                "object" -> cameraManager.getValue(3)
                else -> cameraManager.getValue(1)
            }
            cameraManager.startCamera()
        } else {
            RuntimePermission.requestPermission(this, REQUIRED_PERMISSION, CAMERA_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_CODE) {
            RuntimePermission.onRequestPermissionResult(grantResults, object : RPListener {
                override fun onPermissionGranted() {
                    previous = intent.getStringExtra("act")!!
                    println(previous)
                    when (previous) {
                        "text" -> cameraManager.getValue(1)
                        "face" -> cameraManager.getValue(2)
                        "object" -> cameraManager.getValue(3)
                        else -> cameraManager.getValue(1)
                    }
                    cameraManager.startCamera()
                }

                override fun onPermissionDenied() {
                    Toast.makeText(
                        this@MainActivity,
                        "Please grant the permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }
    }

    companion object {
        private const val CAMERA_CODE = 101
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
        private var hasPermissionGranted = false
    }

}
