package com.example.cameraxapp.textrecognization

import android.graphics.Rect
import android.util.Log
import com.example.cameraxapp.cameraX.BaseImageAnalyzer
import com.example.cameraxapp.cameraX.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class TextRecognizeProcess(private val view: GraphicOverlay) : BaseImageAnalyzer<Text>() {

    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun stop() {
        try {
            textRecognizer.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: $e")
        }
    }

    override fun onSuccess(results: Text, graphicOverlay: GraphicOverlay, rect: Rect) {
        graphicOverlay.clear()
        for (block in results.textBlocks) {
            for (line in block.lines) {
                for (element in line.elements) {
                    val textObject = TextCounterGraphic(graphicOverlay, element, rect)
                    graphicOverlay.add(textObject)
                }
            }
        }
        graphicOverlay.postInvalidate()
    }


    override fun onFailure(e: Exception) {
        Log.w(TAG, e.message.toString())
    }

    override fun detectInImage(image: InputImage): Task<Text> {
        return textRecognizer.process(image)
    }


    companion object {
        private const val TAG = "TextRecognizeProcess"
    }


}