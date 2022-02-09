package com.example.cameraxapp.textrecognization

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.cameraxapp.cameraX.GraphicOverlay
import com.google.mlkit.vision.text.Text

class TextCounterGraphic(
    graphicOverlay: GraphicOverlay,
    private var text: Text.Element,
    private var rect: Rect
) : GraphicOverlay.Graphic(graphicOverlay) {

    private val boxPaint: Paint
    private val textPaint: Paint

    init {

        boxPaint = Paint()
        boxPaint.color = Color.RED
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = 5.0f

        textPaint = Paint()
        textPaint.textSize = 60.0f
        textPaint.color = Color.BLUE

    }

    override fun draw(canvas: Canvas) {

        val textrect = calcRect(
            rect.height().toFloat(),
            rect.width().toFloat(),
            text.boundingBox!!
        )
        canvas.drawRect(textrect, boxPaint)
        val x = textrect.left
        val y = textrect.top
        canvas.drawText(text.text,x,y,textPaint)
    }

}
