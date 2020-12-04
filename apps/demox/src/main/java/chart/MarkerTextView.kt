package chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.TextView


class MarkerTextView(context: Context, attrs: AttributeSet) : TextView(context, attrs) {

    var bgRect: RectF = RectF()
    var mPath = Path()
    var mBgPaint: Paint = Paint()
    var color: Int = Color.WHITE

    private val percentArrow: Float = 0.5f
    private val arrowLength: Int = 0

    init {
        mBgPaint.isAntiAlias = true
        mBgPaint.style = Paint.Style.FILL
    }


    override fun onDraw(canvas: Canvas) {
        mPath.reset()
        bgRect.set(0F, 0F, width.toFloat(), height.toFloat() - arrowLength * 2)
        mBgPaint.color = color
        mPath.addRoundRect(bgRect, 8f, 8f, Path.Direction.CW)
        canvas.drawPath(mPath, mBgPaint)

        /*mPath.reset()
        val pointStartX = width.toFloat() * percentArrow + arrowLength
        val pointStartY = height.toFloat() - arrowLength * 2
        mPath.moveTo(pointStartX, pointStartY)
        mPath.lineTo(width.toFloat() * percentArrow, height.toFloat())
        mPath.lineTo(width.toFloat() * percentArrow - arrowLength, height.toFloat() - arrowLength * 2)
        mPath.close()
        canvas.drawPath(mPath, mBgPaint)*/
        super.onDraw(canvas)
    }


}