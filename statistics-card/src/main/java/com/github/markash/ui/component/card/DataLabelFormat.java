package com.github.markash.ui.component.card;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;

import static java.lang.Math.abs;

public enum DataLabelFormat {
    INTEGER(DecimalFormat.getIntegerInstance()),
    NUMBER(DecimalFormat.getNumberInstance()),
    PERCENTAGE(DecimalFormat.getPercentInstance()),
    CURRENCY(DecimalFormat.getCurrencyInstance());

    private final NumberFormat format;

    DataLabelFormat(
            final NumberFormat format) {
        this.format = format;
    }

    public String format(
            final Number value) {

        double counter = value.doubleValue();
        String suffix = "";

        switch (this) {
            case PERCENTAGE:
                counter = counter > 1.00 ? counter / 100 : counter;
                break;
            default:
                if (abs(counter) > 1000000) {
                    suffix = " M";
                    counter = BigDecimal.valueOf(counter).divide(BigDecimal.valueOf(1000000), 2, RoundingMode.HALF_DOWN).doubleValue();
                } else if (abs(counter) > 1000) {
                    suffix = " K";
                    counter = BigDecimal.valueOf(counter).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_DOWN).doubleValue();
                }
        }

        return this.format.format(counter) + suffix;
    }
}