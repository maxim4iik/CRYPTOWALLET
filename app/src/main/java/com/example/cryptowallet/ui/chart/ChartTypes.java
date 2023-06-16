package com.example.cryptowallet.ui.chart;

public enum ChartTypes {
    MINUTE(60),
    HOUR(24),
    DAY(7);

    private final int limit;

    ChartTypes(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
}
