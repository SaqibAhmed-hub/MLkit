package com.example.cameraxapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        makeFullScreen()
        text_btn.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("act","text")
            startActivity(intent)
        }
        face_btn.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("act","face")
            startActivity(intent)
        }
        object_btn.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("act","object")
            startActivity(intent)
        }

    }
    private fun makeFullScreen() {
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}