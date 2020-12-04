
package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.Entry;

/**
 * @author shangxiu
 */
public class DefaultHighlightValueFormatter implements IHighlightValueFormatter {

    @Override
    public String getFormattedValue(Entry entry) {
        return String.valueOf(entry.getY());
    }
}
