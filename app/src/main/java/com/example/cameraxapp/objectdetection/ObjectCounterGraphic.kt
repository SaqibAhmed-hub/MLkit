package com.example.cameraxapp.objectdetection


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.example.cameraxapp.cameraX.GraphicOverlay
import com.google.mlkit.vision.objects.DetectedObject

class ObjectCounterGraphic(
    overlay: GraphicOverlay,
    private var dobject: DetectedObject,
    private var imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {

    private val objectPositionPaint: Paint
    private val idPaint: Paint
    private val boxPaint: Paint
    private val textPaint: Paint

    init {
        val selectedColor = Color.RED

        objectPositionPaint = Paint()
        objectPositionPaint.color = selectedColor

        idPaint = Paint()
        idPaint.color = selectedColor

        boxPaint = Paint()
        boxPaint.color = selectedColor
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = 5.0f

        textPaint = Paint()
        textPaint.color = Color.GREEN
        textPaint.textSize = 60.0f
    }

    override fun draw(canvas: Canvas) {
        val rect = calcRect(
            imageRect.height().toFloat(),
            imageRect.width().toFloat(),
            dobject.boundingBox
        )
        canvas.drawRect(rect, boxPaint)
        val x = rect.left
        val y = rect.top

        dobject.labels.forEach {
            Log.i("Object: ", "Text = ${it.text} , Confidence = ${(it.confidence * 100).toInt()}% ")
            canvas.drawText(it.text, x, y, textPaint)
        }
    }
}