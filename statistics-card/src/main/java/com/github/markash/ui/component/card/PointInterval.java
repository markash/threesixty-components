package com.github.markash.ui.component.card;

public enum PointInterval {
    HOURLY(24 * 3600 * 1000);

    private int value;

    PointInterval(
            final int value) {

        this.value = value;
    }

    public int getValue() { return value; }
}
