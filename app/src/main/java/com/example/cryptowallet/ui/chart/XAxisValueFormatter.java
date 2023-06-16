package com.example.cryptowallet.ui.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class XAxisValueFormatter extends ValueFormatter {

    private final List<String> labels;

    public XAxisValueFormatter(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        // Ensure the value is within the bounds of the labels list
        int index = (int) Math.min(Math.max(value, 0), labels.size() - 1);
        // Return the corresponding label
        return labels.get(index);
    }
}
