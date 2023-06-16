package com.example.cryptowallet.ui.chart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class ChartViewModel extends ViewModel {
    public String currType;
    public ChartTypes chartType;
    private MutableLiveData<List<Entry>> chartData;
    private MutableLiveData<List<String>> labelsData;

    public ChartViewModel() {
        chartData = new MutableLiveData<>();
        labelsData = new MutableLiveData<>();
        currType = "BTC";
        chartType = ChartTypes.MINUTE;
    }

    public LiveData<List<Entry>> getChartData() {
        return chartData;
    }

    public void setChartData(List<Entry> entries) {
        chartData.setValue(entries);
    }

    public LiveData<List<String>> getLabels() {
        return labelsData;
    }

    public void setLabelsData(List<String> labels) {
        labelsData.setValue(labels);
    }
}