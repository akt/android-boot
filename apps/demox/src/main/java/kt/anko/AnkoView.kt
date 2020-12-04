package kt.anko

import android.content.Context
import android.widget.TextView
import org.jetbrains.anko.textColor

fun test(context: Context){
    val textView = TextView(context).apply {
        text = "heihei"
        hint = "haha"
        textColor = android.R.color.black
    }
}