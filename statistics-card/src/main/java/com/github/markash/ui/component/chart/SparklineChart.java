package com.github.markash.ui.component.chart;

import com.github.markash.ui.component.chart.options.Options;
import com.vaadin.annotations.JavaScript;

@JavaScript({
        "vaadin://addons/highchart/jquery-3.2.1.min.js",
        "vaadin://addons/highchart/highcharts.js",
        "vaadin://addons/highchart/highcharts-connector.js",
        "highcharts-connector.js"
})
public class SparklineChart extends AbstractHighChart {
    private static final long serialVersionUID = 1L;

    private Options options = new Options();

    public Options getOptions() { return options; }

    public void refresh() {
        super.setHcjs(this.options.build());
    }
}
