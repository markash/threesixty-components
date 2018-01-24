package com.github.markash.ui.component.chart.options;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.util.*;

public class Options {

    private Title title = new Title();
    private Credits credits = new Credits();
    private Set<Series> series = new HashSet<>();
    private XAxis xAxis = new XAxis();
    private YAxis yAxis = new YAxis();
    private Legend legend = new Legend();
    private Chart chart = new Chart();

    public Options() {
    }

    public Title getTitle() { return title; }
    public void setTitle(final Title title) { this.title = title; }

    public Credits getCredits() { return credits; }
    public void setCredits(final Credits credits) { this.credits = credits; }

    public Set<Series> getSeries() {
        return Collections.unmodifiableSet(series);
    }
    public void setSeries(
            final Collection<Series> series) {

        this.series.clear();
        if (series != null) {
            this.series.addAll(series);
        }
    }
    public void addSeries(
            final Series series) {

        if (series != null) {
            if (this.series.contains(series)) {
                this.series.remove(series);
            }
            this.series.add(series);
        }
    }
    public void removeSeries(
            final Series series) {

        if (series != null) {
            this.series.remove(series);
        }
    }

    public XAxis getxAxis() { return xAxis; }
    public void setxAxis(final XAxis xAxis) { this.xAxis = xAxis; }

    public YAxis getyAxis() { return yAxis; }
    public void setyAxis(final YAxis yAxis) { this.yAxis = yAxis; }

    public Legend getLegend() { return legend; }
    public void setLegend(final Legend legend) { this.legend = legend; }

    public Chart getChart() { return chart; }
    public void setChart(final Chart chart) { this.chart = chart; }

    public String build() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Jdk8Module());
            return "var options = " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            System.out.println("e = " + e);
            throw new RuntimeException("Unable to convert the object model to JSON, " + e.getMessage());
        }
    }
}
