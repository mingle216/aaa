package com.wisedu.amp.card.sys.cqfav.model;

public class ConfigModel {
    private int type;
    private long value;
    private boolean displayIcon;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public boolean isDisplayIcon() {
        return displayIcon;
    }

    public void setDisplayIcon(boolean displayIcon) {
        this.displayIcon = displayIcon;
    }
}
