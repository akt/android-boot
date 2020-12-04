package ui.widget

import android.graphics.*
import android.graphics.drawable.Drawable



/**
 * Created by ak on 2020-02-10.
 */
class DrawableText(private val text: String) : Drawable() {

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.setColorFilter(colorFilter)
    }


    private val paint: Paint

//     var opacity: Int =  PixelFormat.TRANSLUCENT

    init {

        this.paint = Paint()
        paint.setColor(Color.BLUE)
        paint.setTextSize(66f)
        paint.setAntiAlias(true)
        paint.setFakeBoldText(true)
        paint.setShadowLayer(6f, 0f, 0f, Color.BLACK)
        paint.setStyle(Paint.Style.FILL)
        paint.setTextAlign(Paint.Align.LEFT)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawText(text, 0f, 0f, paint)
        setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    override fun setAlpha(alpha: Int) {
        paint.setAlpha(alpha)
    }

}
