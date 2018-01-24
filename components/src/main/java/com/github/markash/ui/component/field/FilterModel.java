package io.threesixty.ui.component.field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String header;
    private final String property;
    private final String value;
    private final List<String> availableOptions = new ArrayList<>();

    public FilterModel(
            final String header,
            final String property) {

        this(header, property, null, null);
    }

    public FilterModel(
            final String header,
            final String property,
            final String value) {

        this(header, property, null, value);
    }

    public FilterModel(
            final String header,
            final String property,
            final Collection<String> options) {

        this(header, property, options, null);
    }

    private FilterModel(
            final String header,
            final String property,
            final Collection<String> options,
            final String value) {

        this.header = header;
        this.property = property;
        this.value = value;
        if (options != null) {
            this.availableOptions.addAll(options);
        }
    }

    public String getHeader() { return header; }
    public String getProperty() { return property; }
    public String getValue() { return value; }
    public Collection<String> getAvailableOptions() { return availableOptions; }
    public boolean hasAvailableOptions() { return this.availableOptions != null && this.availableOptions.size() > 0; }

    public FilterModel withValue(
            final String value) {

        return new FilterModel(header, property, availableOptions, value);
    }

    @Override
    public String toString() {
        return getHeader();
    }
}