package com.github.markash.ui.component.chart.options;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Series implements Serializable {
    private String name;
    private List<DataPoint> data;
    private Number pointStart;
    private Number pointInterval;

    public Series(
            final String name,
            final Number...data) {

        this.name = name;
        if (data != null) {
            this.data = DataPoint.convertFrom(Arrays.stream(data));
        }
    }

    public Series(
            final String name,
            final DataPoint...data) {

        this.name = name;
        if (data != null) {
            this.data = Arrays.asList(data);
        }
    }

    public Series(
            final String name,
            final Collection<DataPoint> data) {

        this.name = name;
        if (data != null) {
            this.data = new ArrayList<>(data);
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Number getPointStart() { return pointStart; }
    public void setPointStart(final Number pointStart) { this.pointStart = pointStart; }

    public Number getPointInterval() { return pointInterval; }
    public void setPointInterval(final Number pointInterval) { this.pointInterval = pointInterval; }

    public List<DataPoint> getData() {

        return Collections.unmodifiableList(this.data);
    }

    public void setData(
            final List<DataPoint> data) {

        this.data.clear();
        this.data.addAll(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return Objects.equals(name, series.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
