package com.dusa.thermometer.service.transformation;

import static com.dusa.thermometer.service.config.ActivationConfig.CSS_ZONE_NAME;
import static com.dusa.thermometer.service.config.ActivationConfig.ZONE_SHORT_ID;

import com.dusa.thermometer.service.config.ActivationConfig;

public class ZoneData extends HeaderData {

    public ZoneData(int id, String name) {
        super(id, name);
        styleClass = CSS_ZONE_NAME + " " + ActivationConfig.CSS_CASCADABLE;;
        shortIdTag = ZONE_SHORT_ID;
    }
}