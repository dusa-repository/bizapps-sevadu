package com.dusa.thermometer.service.transformation;

import com.dusa.thermometer.service.to.TotalTo;
import com.dusa.thermometer.service.util.HtmlGenerator;
import com.dusa.thermometer.service.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static com.dusa.thermometer.service.config.ActivationConfig.*;

abstract class HeaderData extends ThermometerData {

    protected int id;
    protected String name;
    protected String styleClass;
    protected String shortIdTag;
    protected List<ThermometerData> children = new ArrayList<ThermometerData>();

    public HeaderData(int id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String generateClientNumber(String idTag) {
        return HtmlGenerator.generateCell("<div style='text-align: center;'><a href='#' data-click='" + idTag + "' class='expand'><img id='" + idTag + "-IMG" + "' data-status='expand' src='img/expand.png' style='width:12px;height:12px'/></a></div>", "title");
    }
    
    @Override
    public String generateName(String idTag) {
        return HtmlGenerator.generateClickable(StringUtil.fillWithString(String.valueOf(id), "0", 2, false)
                + "- " + name.toUpperCase(), styleClass, idTag);
    }

    @Override
    public String generateActivationValues() {
        String result = "";
        for (TotalTo total : labelsTotals) {
            result += HtmlGenerator.generateCell(String.valueOf(total.getActivatedAndPlanned()),
                    (total.getActivatedAndPlanned() == total.getPlanned()
                            ? CSS_LABEL_SUCCESS + " " + CSS_POPUP
                            : CSS_LABEL_FAILED + " " + CSS_POPUP),
                    "data-chart='" + total.getActivatedAndPlanned() + ";" + total.getActivatedAndNotPlanned() + ";"
                            + total.getPlanned() + ";"
                            + String.valueOf((Math.round(total.getPlanned() / total.getClients())
                            * 100 / children.size())) + ";"
                            + String.valueOf((total.getPlanned() == 0) ? 0
                            : Math.round(total.getActivatedAndPlanned() * 100 / 
                                    (total.getPlanned()))) + "'");
        }
        return result;
    }

    @Override
    public List<ThermometerData> getChildren() {
        return children;
    }

    public void addChild(ThermometerData data) {
        children.add(data);
    }

    public String getId() {
        return shortIdTag + String.valueOf(id);
    }

}
