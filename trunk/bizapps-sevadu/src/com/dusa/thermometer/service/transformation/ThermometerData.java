package com.dusa.thermometer.service.transformation;

import com.dusa.thermometer.service.to.TotalTo;
import com.dusa.thermometer.service.util.ThermometerUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class ThermometerData {

    protected List<TotalTo> labelsTotals;
    protected TotalTo finalTotals;

    public String generateHTML(String id) {
        return generateClientNumber(id) + generateName(id)
                + generateActivationValues() + generateTotalValues() + "\n";
    }

    public abstract String generateClientNumber(String id);
    public abstract String generateName(String id);
    public abstract String generateActivationValues();

    public String generateTotalValues() {
        return ThermometerUtil.generateTotalValues(finalTotals, labelsTotals.size());
    }

    public abstract List<ThermometerData> getChildren();
    public abstract String getId();

    public TotalTo getFinalTotals() {
        if (finalTotals == null) {
            finalTotals = new TotalTo();
        }
        return finalTotals;
    }

    public List<TotalTo> getLabelsTotals() {
        if (labelsTotals == null) {
            labelsTotals = new ArrayList<TotalTo>();
        }
        return labelsTotals;
    }

    public void setLabelsTotals(List<TotalTo> labelsTotals) {
        this.labelsTotals = labelsTotals;
    }

    public void setFinalTotals(TotalTo finalTotals) {
        this.finalTotals = finalTotals;
    }
}
