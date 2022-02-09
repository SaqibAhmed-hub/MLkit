package com.example.cameraxapp.objectdetection

import android.graphics.Rect
import android.util.Log
import com.example.cameraxapp.cameraX.BaseImageAnalyzer
import com.example.cameraxapp.cameraX.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.io.IOException

class ObjectContourProcess(private val view: GraphicOverlay): BaseImageAnalyzer<List<DetectedObject>>() {

    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableClassification()
        .enableMultipleObjects()
        .build()

    private val dectector = ObjectDetection.getClient(options)

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun stop() {
        try {
            dectector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Object Detector: $e")
        }
    }

    override fun onSuccess(results: List<DetectedObject>, graphicOverlay: GraphicOverlay, rect: Rect) {
        graphicOverlay.clear()
        results.forEach {
            val objectGraphic = ObjectCounterGraphic(graphicOverlay, it, rect)
            graphicOverlay.add(objectGraphic)
        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Object Detector failed.$e")
    }

    override fun detectInImage(image: InputImage): Task<List<DetectedObject>> {
        return dectector.process(image)
    }

    companion object {
        private const val TAG = "ObjectDetectorProcessor"
    }

}