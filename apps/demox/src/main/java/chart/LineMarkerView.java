
package chart;

import android.content.Context;

import com.ak.demo.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IHighlightValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import chart.line.LineEntry;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class LineMarkerView extends MarkerView {

    private MarkerTextView tvContent;

    public LineMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (MarkerTextView) findViewById(R.id.tvContent);
        tvContent.setColor(getResources().getColor(R.color.pie_part_6));
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        tvContent.setColor(color);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight, IHighlightValueFormatter highlightValueFormatter) {

        if (e instanceof LineEntry) {
            LineEntry lineEntry = (LineEntry) e;
            tvContent.setText(highlightValueFormatter.getFormattedValue(lineEntry));
        }
        super.refreshContent(e, highlight, highlightValueFormatter);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
