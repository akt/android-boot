package filterview.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import com.ak.demo.R

/**
 * @author ak
 * @since 08/10/2018
 */

class TripleCheckBox : ImageView {


    companion object {
        const val EMPTY = 0
        const val FULL = 1
        const val PARTIAL = 2
    }


    var checkState:Int = 0
        set(value) {
            if (value != field) {
                field = value
                updateView()
            }
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setImageResource(R.drawable.triple_box)
        updateView()
    }


    private fun updateView(){
        setImageLevel(checkState)
    }




}