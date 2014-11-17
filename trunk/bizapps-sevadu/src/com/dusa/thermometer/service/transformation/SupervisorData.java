package com.dusa.thermometer.service.transformation;

import com.dusa.thermometer.service.config.ActivationConfig;

public class SupervisorData extends HeaderData {

    public SupervisorData(int id, String name) {
        super(id, name);
        styleClass = ActivationConfig.CSS_SUPERVISOR_NAME + " " + ActivationConfig.CSS_CASCADABLE;
        shortIdTag = ActivationConfig.SUPERVISOR_SHORT_ID;
    }

}