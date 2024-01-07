package com.awd.feature.toggle.model;

public enum Toggle {
    ON(true), OFF(false);
    private final boolean toggleOn;

    Toggle(boolean toggleOn) {
        this.toggleOn = toggleOn;
    }

    public boolean isToggleOn() {
        return toggleOn;
    }
}
