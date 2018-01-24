package com.github.markash.ui.component.chart.options;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class YAxis implements Serializable {
    private Labels labels = new Labels();
    private Title title = new Title();
    private boolean startOnTick = false;
    private boolean endOnTick = false;
    private List<Number> tickPositions = new ArrayList<>();

    public Labels getLabels() { return labels; }
    public void setLabels(final Labels labels) { this.labels = labels; }

    public Title getTitle() { return title; }
    public void setTitle(final Title title) { this.title = title; }

    public boolean isStartOnTick() { return startOnTick; }
    public void setStartOnTick(final boolean startOnTick) { this.startOnTick = startOnTick; }

    public boolean isEndOnTick() { return endOnTick; }
    public void setEndOnTick(final boolean endOnTick) { this.endOnTick = endOnTick; }

    public List<Number> getTickPositions() { return tickPositions; }
    public void setTickPositions(final List<Number> tickPositions) { this.tickPositions = tickPositions; }
}
