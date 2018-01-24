package com.github.markash.ui.component.card;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Currency;
import java.util.Locale;

/**
 * Specifies the data label settings
 */
public class DataLabelSettings implements Serializable {
    private String displayUnit;
    private int decimalPlaces;
    private DataLabelFormat format;

    public DataLabelSettings() {
        this("", 2, DataLabelFormat.NUMBER);
    }

    public DataLabelSettings(
            final String displayUnit,
            final int decimalPlaces,
            final DataLabelFormat format) {

        this.displayUnit = displayUnit;
        this.decimalPlaces = decimalPlaces;
        this.format = format;
    }

    public boolean hasDisplayUnit() { return StringUtils.isNotBlank(this.displayUnit); }
    public String getDisplayUnit() { return StringUtils.isBlank(displayUnit) ? "" : displayUnit; }
    public void setDisplayUnit(final String displayUnit) { this.displayUnit = displayUnit; }

    public int getDecimalPlaces() { return decimalPlaces; }
    public void setDecimalPlaces(final int decimalPlaces) { this.decimalPlaces = decimalPlaces; }

    public DataLabelFormat getFormat() { return format; }
    public void setFormat(final DataLabelFormat format) { this.format = format; }

    public static DataLabelSettings INTEGER() {

        return new DataLabelSettings(
                "",
                0,
                DataLabelFormat.INTEGER);
    }

    public static DataLabelSettings CURRENCY() {

        return CURRENCY(Currency.getInstance(Locale.getDefault()));
    }

    public static DataLabelSettings CURRENCY(
            final Currency currency) {

        return new DataLabelSettings(
                currency.getSymbol(),
                currency.getDefaultFractionDigits(),
                DataLabelFormat.CURRENCY
        );
    }
}
