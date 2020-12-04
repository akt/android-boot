
package chart.line;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.ak.demo.R;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.wacai.android.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.wacai.android.overscroll.adapters.IOverScrollDecoratorAdapter;

import java.util.ArrayList;
import java.util.List;

import chart.LineMarkerView;

/**
 * Chart that draws lines, surfaces, circles, ...
 *
 * @author Philipp Jahoda
 */
public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider {

    private LineDataSet lineDataSet = new LineDataSet(new ArrayList<Entry>(), "");

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new LineChartRenderer(this, mAnimator, mViewPortHandler);
        setOverScrollDecor();
    }

    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mRenderer != null && mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }


    public void setDataSet(LineDataSet lineDataSet) {
        this.lineDataSet = lineDataSet;
        initDefaultAttributes(lineDataSet);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);// add the datasets

        LineData data = new LineData(dataSets);

        setData(data);

        initDataDefaultAttribute();
        getData().notifyDataChanged();
        notifyDataSetChanged();
    }


    private void initDefaultAttributes(LineDataSet lineDataSet) {
        setTouchEnabled(true);
        setDragEnabled(true);
        setScaleEnabled(false);
        setScaleXEnabled(false);
        setScaleYEnabled(false);
        setPinchZoom(true);
        lineDataSet.setDrawFilled(true);

        LineMarkerView mv = new LineMarkerView(getContext(), R.layout.line_marker_view);
        mv.setColor(getContext().getResources().getColor(R.color.light_blue));
        mv.setChartView(this); // For bounds control
        setMarker(mv);


        //坐标基于底部
        getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        lineDataSet.setColor(getContext().getResources().getColor(R.color.light_blue));
        lineDataSet.setCircleColor(getContext().getResources().getColor(android.R.color.white));
        lineDataSet.setCircleColorHole(lineDataSet.getColor());
        lineDataSet.setAverageLineColor(getContext().getResources().getColor(R.color.grey_line));
        lineDataSet.setAverageLineLabelColor(getContext().getResources().getColor(R.color.grey_text));

        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setCircleHoleRadius(4f);
        lineDataSet.setCircleInnerRadius(2f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setValueTextSize(9f);
        setDrawGridBackground(true);
    }

    private void initDataDefaultAttribute() {
        getData().setHighlightEnabled(true);
        getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                List<Entry> entries = lineDataSet.getEntriesForXValue(value);
                if (entries != null && entries.size() > 0) {
                    for (Entry entry : entries) {
                        if (entry instanceof LineEntry && !TextUtils.isEmpty(((LineEntry) entry).getLabel())) {
                            return ((LineEntry) entry).getLabel();
                        }
                    }
                }
                return "";
            }
        });
    }


    public ILineDataSet getDataSet() {
        return lineDataSet;
    }


    private void setOverScrollDecor() {
        new HorizontalOverScrollBounceEffectDecorator(new HorizontalScrollDecorAdapter(this), 2.5f, 2.5f, -2f);
    }


    public class HorizontalScrollDecorAdapter implements IOverScrollDecoratorAdapter {

        protected final LineChart mView;

        public HorizontalScrollDecorAdapter(LineChart view) {
            mView = view;
        }

        @Override
        public View getView() {
            return mView;
        }

        @Override
        public View getInsideView() {
            return null;
        }

        @Override
        public boolean isInAbsoluteStart() {
            return !mView.canScrollHorizontally(-1);
        }

        @Override
        public boolean isInAbsoluteEnd() {
            return !mView.canScrollHorizontally(1);
        }
    }


    @Override
    public boolean canScrollHorizontally(int direction) {
        if (getViewPortHandler() != null) {
            return !getViewPortHandler().isOverBoundOnX(direction);
        } else {
            return super.canScrollHorizontally(direction);
        }
    }


}
