package com.dusa.thermometer.service.util;

import static com.dusa.thermometer.service.config.ActivationConfig.CSS_LABEL_FAILED;
import static com.dusa.thermometer.service.config.ActivationConfig.CSS_LABEL_NEUTRAL;
import static com.dusa.thermometer.service.config.ActivationConfig.CSS_LABEL_SUCCESS;
import static com.dusa.thermometer.service.config.ActivationConfig.CSS_LABEL_TITLE;

import java.util.List;

import com.dusa.thermometer.service.to.TotalTo;

public class ThermometerUtil {

    public static void initializeTotals(int size, List<TotalTo> totals) {
        for (int i = 0; i < size; i++) {
            totals.add(new TotalTo());
        }
    }

    public static String generateTotalValues(TotalTo total, int labelSize) {
        String result = "";
        result += HtmlGenerator.generateCell(String.valueOf(total.getActivatedAndPlanned()),
                ((total.getActivatedAndPlanned() == total.getPlanned()) ? CSS_LABEL_SUCCESS : CSS_LABEL_FAILED))
                + HtmlGenerator.generateCell(String.valueOf(total.getActivatedAndNotPlanned()),
                CSS_LABEL_NEUTRAL)
                + HtmlGenerator.generateCell(String.valueOf(total.getPlanned()), CSS_LABEL_TITLE)
                + HtmlGenerator.generateCell(String.valueOf(Math.round((total.getPlanned() / total.getClients())
                * 100 / labelSize))
                + "%", CSS_LABEL_TITLE);
        int value = (total.getPlanned() == 0) ? 0 : Math.round(total.getActivatedAndPlanned() * 100 / 
                (total.getPlanned()));
        result += HtmlGenerator.generateCell(String.valueOf(value)
                + "%", (value == 100 || value == 0) ? CSS_LABEL_SUCCESS : CSS_LABEL_FAILED);
        return result;
    }
}
