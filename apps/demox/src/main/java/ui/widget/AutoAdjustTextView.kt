package ui.widget

import android.content.Context
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.TextView
import com.ak.demo.R

/**
 * Created by ak on 2020-02-10.
 */
class AutoAdjustTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {


    private var minTextSize: Float = 0f
    private var maxTextSize: Float = 0f

    private var mPaint:TextPaint? = null


    init {
        minTextSize = resources.getDimension(R.dimen.TextView_MinSize)
        maxTextSize = this.textSize
    }

    private fun refitText(text: String, textWidth: Int) {
        if (textWidth <= 0 || text.isEmpty() || maxTextSize <= minTextSize) return
        if (mPaint == null) {
            mPaint = TextPaint(this.paint)
        }
        var nFieldWidth = maxWidth - compoundPaddingLeft - compoundPaddingRight
        if (isPaddingOffsetRequired) {
            nFieldWidth -= ((shadowRadius + shadowDx) * 2).toInt()
        }

        var dSize = maxTextSize
        while (dSize >= minTextSize) {
            mPaint!!.textSize = dSize
            val dW = mPaint!!.measureText(text)
            if (dW <= nFieldWidth) {
                break
            }
            dSize = dSize.minus(1f)
        }

        textSize = dSize / resources.displayMetrics.scaledDensity
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw) {
            refitText(text.toString(), w)
        }

    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        refitText(text?.toString() ?: "", width)
    }
}