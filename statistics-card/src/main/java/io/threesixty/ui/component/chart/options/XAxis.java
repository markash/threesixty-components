package io.threesixty.ui.component.chart.options;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class XAxis implements Serializable {
    private Labels labels = new Labels();
    private Title title = new Title();
    private AxisType type = AxisType.linear;
    private boolean startOnTick = false;
    private boolean endOnTick = false;
    private List<Number> tickPositions = new ArrayList<>();
    private Number tickInterval = null;

    public Labels getLabels() { return labels; }
    public void setLabels(final Labels labels) { this.labels = labels; }

    public Title getTitle() { return title; }
    public void setTitle(final Title title) { this.title = title; }

    public AxisType getType() { return type; }
    public void setType(final AxisType type) { this.type = type; }

    public boolean isStartOnTick() { return startOnTick; }
    public void setStartOnTick(final boolean startOnTick) { this.startOnTick = startOnTick; }

    public boolean isEndOnTick() { return endOnTick; }
    public void setEndOnTick(final boolean endOnTick) { this.endOnTick = endOnTick; }

    public Optional<List<Number>> getTickPositions() { return Optional.ofNullable(tickPositions); }
    public void setTickPositions(final List<Number> tickPositions) { this.tickPositions = tickPositions; }

    public Optional<Number> getTickInterval() { return Optional.ofNullable(tickInterval); }
    public void setTickInterval(final Number tickInterval) { this.tickInterval = tickInterval; }

    public XAxis withHourlyInterval(final int hourInterval) {

        this.tickPositions = null;
        this.type = AxisType.datetime;
        this.tickInterval = 3600 * 1000 * hourInterval;
        this.labels.setEnabled(true);
        return this;
    }
}
