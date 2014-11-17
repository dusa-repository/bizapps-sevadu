package com.dusa.thermometer.service.transformation;

import com.dusa.thermometer.service.config.ActivationConfig;

public class SellerData extends HeaderData {

    public SellerData(int id, String name) {
        super(id, name);
        styleClass = ActivationConfig.CSS_SELLER_NAME + " " + ActivationConfig.CSS_CASCADABLE;;
        shortIdTag = ActivationConfig.SELLER_SHORT_ID;
    }

}
