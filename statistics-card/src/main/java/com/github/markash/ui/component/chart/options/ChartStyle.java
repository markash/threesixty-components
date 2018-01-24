package com.github.markash.ui.component.chart.options;

import java.io.Serializable;

public class ChartStyle implements Serializable {
    private String overflow = "visible";

    public String getOverflow() { return overflow; }
    public void setOverflow(final String overflow) { this.overflow = overflow; }
}
