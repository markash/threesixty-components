package io.threesixty.ui.component.field;

public class FilterModel {
    private String header;
    private String property;
    private String value;

    public FilterModel(
            final String header,
            final String property,
            final String value) {

        this.header = header;
        this.property = property;
        this.value = value;
    }

    public String getHeader() { return header; }
    public String getProperty() { return property; }
    public String getValue() { return value; }
}