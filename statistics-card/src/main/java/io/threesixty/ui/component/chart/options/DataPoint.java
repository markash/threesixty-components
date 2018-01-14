package io.threesixty.ui.component.chart.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vaadin.shared.ui.colorpicker.Color;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataPoint implements Serializable {
    private Number y;
    private Number x;
    private String name;
    private Color color;

    public DataPoint(
            final Number y) {

        this.y = y;
    }

    public DataPoint(
            final Number y,
            final LocalDateTime x) {

        this.y = y;
        this.x = x.toEpochSecond(ZoneOffset.UTC) * 1000;
    }

    public Number getY() { return y; }
    public void setY(final Number y) { this.y = y; }

    public Number getX() { return x; }
    public void setX(final Number x) { this.x = x; }

    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }

    public Color getColor() { return color; }
    public void setColor(final Color color) { this.color = color; }

    public static List<DataPoint> convertFrom(
            final Stream<Number> data) {

        return data
                .map(DataPoint::new)
                .collect(Collectors.toList());
    }
}
