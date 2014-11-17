package com.dusa.thermometer.service.to;

public class ActivationTo {
    private boolean activated;
    private boolean objective;

    public ActivationTo(boolean activated, boolean objective) {
        this.activated = activated;
        this.objective = objective;
    }

    public boolean isActivated() {
        return activated;
    }

    public boolean isObjective() {
        return objective;
    }
}
