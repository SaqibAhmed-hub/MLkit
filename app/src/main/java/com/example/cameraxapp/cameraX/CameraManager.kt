package com.example.cameraxapp.cameraX

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.cameraxapp.facedetection.FaceContourProcess
import com.example.cameraxapp.objectdetection.ObjectContourProcess
import com.example.cameraxapp.textrecognization.TextRecognizeProcess
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
    private val context: Context,
    private val finderView: PreviewView,
    private val lifecycleOwner: LifecycleOwner,
    private val graphicOverlay: GraphicOverlay
) {
    private var preview: Preview? = null
    private var camera: Camera? = null
    private var selectionvalue: Int = 0
    private var cameraSelector = CameraSelector.LENS_FACING_BACK
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var imageCapture: ImageCapture? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()


    fun getValue(value: Int) {
        selectionvalue = value
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder()
                .build()
            imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, selectAnalyzer())
                }
            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(cameraSelector)
                .build()

            setConfig(cameraProvider, cameraSelector)

        }, ContextCompat.getMainExecutor(context))
    }

    private fun setConfig(cameraProvider: ProcessCameraProvider?, cameraSelector: CameraSelector) {
        try {
            cameraProvider?.unbindAll()
            camera = cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalyzer
            )
            preview?.setSurfaceProvider(
                finderView.surfaceProvider
            )
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
    }

    private fun selectAnalyzer(): ImageAnalysis.Analyzer {
        return when(selectionvalue){
            1 -> TextRecognizeProcess(graphicOverlay)
            2 -> FaceContourProcess(graphicOverlay)
            3 -> ObjectContourProcess(graphicOverlay)
            else -> TextRecognizeProcess(graphicOverlay)
        }
    }

    fun changeCameraSelector() {
        cameraProvider?.unbindAll()
        cameraSelector =
            if (cameraSelector == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
        graphicOverlay.toggleSelector()
        startCamera()
    }

    fun capturePhoto() {
        imageCapture = imageCapture ?: return
        val photoFile = File(
            context.externalMediaDirs[0],
            SimpleDateFormat(DATEFORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )


        val outputoption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture!!.takePicture(
            outputoption,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uri = Uri.fromFile(photoFile)
                    val msg = "photo is store in location: $uri"
                    Log.i("Image", msg)

                }

                override fun onError(exception: ImageCaptureException) {
                    Log.i("Error", "The Error $exception")
                }
            })
    }

    companion object {
        private const val DATEFORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }


}