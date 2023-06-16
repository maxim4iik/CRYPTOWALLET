package com.example.cryptowallet.ui.chart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.cryptowallet.API.CryptoCompareApi;
import com.example.cryptowallet.MyApplication;
import com.example.cryptowallet.R;
import com.example.cryptowallet.databinding.FragmentChartBinding;
import com.example.cryptowallet.ui.home.HomeViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartFragment extends Fragment {
    private FragmentChartBinding binding;
    private ChartViewModel chartViewModel;
    private LineChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChartBinding.inflate(inflater);
        Spinner spinnerCurr = binding.spinnerCurr;
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(requireActivity(),
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurr.setAdapter(adapter1);

        Spinner spinnerTime = binding.spinnerTime;
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(requireActivity(),
                R.array.time_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter2);
        chart = binding.lineChart;
        chartViewModel = new ViewModelProvider(this).get(ChartViewModel.class);

        chartViewModel.getChartData().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                updateChart(entries, chartViewModel.getLabels().getValue());
            }
        });
        binding.spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        chartViewModel.chartType = ChartTypes.MINUTE;
                        break;
                    case 1:
                        chartViewModel.chartType = ChartTypes.HOUR;
                        break;
                    case 2:
                        chartViewModel.chartType = ChartTypes.DAY;
                        break;
                }
                fetchCryptoPriceHistory(chartViewModel.currType, chartViewModel.chartType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                fetchCryptoPriceHistory("BTC", ChartTypes.MINUTE);

            }
        });

        binding.spinnerCurr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chartViewModel.currType = spinnerCurr.getSelectedItem().toString();
                fetchCryptoPriceHistory(chartViewModel.currType, chartViewModel.chartType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                fetchCryptoPriceHistory("BTC", ChartTypes.MINUTE);

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void updateChart(List<Entry> entries, List<String> xLabels) {
        LineDataSet dataSet = new LineDataSet(entries, "Crypto Prices");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter(xLabels));

        chart.invalidate();
    }


    private void fetchCryptoPriceHistory(String symbol, ChartTypes type) {
        CryptoCompareApi cryptoCompareApi = MyApplication.getCryptoCompareApi();
        String apiKey = "314ec75597aca4f95c33ccdfc88d5a7493f0b97d89d1cc995745ca4e89f9a32f";
        Call<JsonObject> call;
        switch (type) {
            case MINUTE:
                call = cryptoCompareApi.getHistoricalMinutePrice(symbol, "USD", type.getLimit(), apiKey);
                break;
            case HOUR:
                call = cryptoCompareApi.getHistoricalHourPrice(symbol, "USD", type.getLimit(), apiKey);
                break;
            case DAY:
                call = cryptoCompareApi.getHistoricalDayPrice(symbol, "USD", type.getLimit(), apiKey);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        List<Entry> entries = new ArrayList<>();
                        List<String> labels = new ArrayList<>();

                        if (jsonObject.has("Data")) {
                            JsonArray data = jsonObject.getAsJsonObject("Data").getAsJsonArray("Data");
                            Log.d("resp", jsonObject.toString());
                            Log.d("resp", data.toString());
                            int i = 0;
                            for (JsonElement element : data) {
                                i+=1;
                                JsonObject item = element.getAsJsonObject();
                                long timestamp = item.getAsJsonPrimitive("time").getAsLong();
                                double closingPrice = item.getAsJsonPrimitive("close").getAsDouble();
                                entries.add(new Entry((float) i, (float) closingPrice));
                                labels.add(formatTimestamp(timestamp, type));
                            }

                        }

                        Log.d("Entry", entries.toString());
                        chartViewModel.setLabelsData(labels);
                        chartViewModel.setChartData(entries);
                    }
                } else {
                    // Handle error response
                    // ...
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle network or API call failure
                // ...
            }
        });
    }

    public String formatTimestamp(long timestamp, ChartTypes chartType) {
        SimpleDateFormat dateFormat;

        TimeZone timeZone = TimeZone.getTimeZone("Europe/Kiev");

        if (chartType == ChartTypes.MINUTE) {
            Date date = new Date(timestamp * 1000L);
            dateFormat = new SimpleDateFormat("HH:mm");
            dateFormat.setTimeZone(timeZone);
            return dateFormat.format(date);
        } else if (chartType == ChartTypes.HOUR) {
            int hours = (int) (timestamp / 3600);
            int day = hours / 24;
            int hour = hours % 24;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(timeZone);
            calendar.set(Calendar.DAY_OF_WEEK, day + 1);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, 0);
            dateFormat = new SimpleDateFormat("E HH:mm");
            dateFormat.setTimeZone(timeZone);
            return dateFormat.format(calendar.getTime());
        } else if (chartType == ChartTypes.DAY) {
            dateFormat = new SimpleDateFormat("yyyy:MM:dd");
            dateFormat.setTimeZone(timeZone);
            return dateFormat.format(new Date(timestamp * 1000L));
        }

        return "";
    }

    private Date getDate(int value1, int value2) {
        Date date = new Date();
        date.setDate(value1);
        date.setHours(value2);
        return date;
    }
}