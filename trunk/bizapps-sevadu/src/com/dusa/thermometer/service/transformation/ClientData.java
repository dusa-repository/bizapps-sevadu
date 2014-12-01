package com.dusa.thermometer.service.transformation;

import static com.dusa.thermometer.service.config.ActivationConfig.CSS_ACTIVE;
import static com.dusa.thermometer.service.config.ActivationConfig.CSS_ACTIVE_OBJECTIVE;
import static com.dusa.thermometer.service.config.ActivationConfig.CSS_CLIENT_CODE;
import static com.dusa.thermometer.service.config.ActivationConfig.CSS_CLIENT_NAME;
import static com.dusa.thermometer.service.config.ActivationConfig.CSS_NOT_ACTIVATE_OBJECTIVE;

import java.util.List;

import com.dusa.thermometer.service.to.ActivationTo;
import com.dusa.thermometer.service.util.HtmlGenerator;

public class ClientData extends ThermometerData {

    private String name;
    private String code;
    private List<ActivationTo> activations;
    private int clientNumber;

    public ClientData(String name, String code, List<ActivationTo> activations, int clientNumber) {
        this.name = name;
        this.code = code;
        this.activations = activations;
        this.clientNumber = clientNumber;
    }

    @Override
    public String generateClientNumber(String idTag) {
        return HtmlGenerator.generateCell(String.valueOf(clientNumber), "title");
    }
    
    @Override
    public String generateName(String id) {
        return HtmlGenerator.generateCell(code, CSS_CLIENT_CODE)
                + HtmlGenerator.generateCell(name.toUpperCase(), CSS_CLIENT_NAME);
    }

    @Override
    public String generateActivationValues() {
        String result = "";
        for (ActivationTo activation : activations) {
            if (activation.isActivated() && activation.isObjective()) {
                result += HtmlGenerator.generateCell("1", CSS_ACTIVE_OBJECTIVE);
            }
            else if (activation.isActivated()) {
                result += HtmlGenerator.generateCell("1", CSS_ACTIVE);
            }
            else if (activation.isObjective()) {
                result += HtmlGenerator.generateCell("0", CSS_NOT_ACTIVATE_OBJECTIVE);
            } else {
                result += HtmlGenerator.generateBlankCells(1, "");
            }
        }
        return result;
    }

    @Override
    public List<ThermometerData> getChildren() {
        return null;
    }

    public List<ActivationTo> getActivations() {
        return activations;
    }

    @Override
    public String getId() {
        return "";
    }
}
