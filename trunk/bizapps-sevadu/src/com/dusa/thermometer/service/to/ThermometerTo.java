package com.dusa.thermometer.service.to;

import com.dusa.thermometer.service.transformation.ThermometerData;

import java.util.List;

public class ThermometerTo {

    private List<ThermometerData> data;
    private List<HeaderTo> labels;
    private List<TotalTo> totals;

    public ThermometerTo(){}

    public ThermometerTo(List<ThermometerData> data, List<HeaderTo> labels, List<TotalTo> totals) {
        this.data = data;
        this.labels = labels;
        this.totals = totals;
    }

    public List<ThermometerData> getData() {
        return data;
    }

    public void setData(List<ThermometerData> data) {
        this.data = data;
    }

    public List<HeaderTo> getLabels() {
        return labels;
    }

    public void setLabels(List<HeaderTo> labels) {
        this.labels = labels;
    }

    public List<TotalTo> getTotals() {
        return totals;
    }

    public void setTotals(List<TotalTo> totals) {
        this.totals = totals;
    }
}
