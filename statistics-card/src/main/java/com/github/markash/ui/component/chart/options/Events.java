package com.github.markash.ui.component.chart.options;

import java.io.Serializable;

public class Events implements Serializable {
    private String click =
            "function (event) {\n" +
            "\t\tconnector.onClick(event.xAxis[0].value, event.yAxis[0].value);\n" +
            "}\n";

    public String getClick() { return click; }
    public void setClick(final String click) { this.click = click; }
}
