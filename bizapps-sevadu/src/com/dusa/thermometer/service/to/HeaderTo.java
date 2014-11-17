package com.dusa.thermometer.service.to;

public class HeaderTo {

    private String name;
    private boolean vertical;
    private String style;

    public HeaderTo(String name, boolean vertical) {
        this.name = name;
        this.vertical = vertical;
    }

    public HeaderTo(String name, boolean vertical, String style) {
        this.name = name;
        this.vertical = vertical;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}