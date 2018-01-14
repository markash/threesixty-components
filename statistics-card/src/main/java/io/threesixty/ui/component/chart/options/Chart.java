package io.threesixty.ui.component.chart.options;

import java.io.Serializable;

public class Chart implements Serializable {
    private String backgroundColor = null;
    private int borderWidth = 0;
    private String type = "area";
    private int[] margin = new int[]{2, 0, 2, 0};
    private int width= 220;
    private int height = 80;
    private ChartStyle style = new ChartStyle();
    private boolean skipClone = true;
    private Events events = new Events();

    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(final String backgroundColor) { this.backgroundColor = backgroundColor; }

    public int getBorderWidth() { return borderWidth; }
    public void setBorderWidth(int borderWidth) { this.borderWidth = borderWidth; }

    public String getType() { return type; }
    public void setType(final String type) { this.type = type; }

    public int[] getMargin() { return margin; }
    public void setMargin(final int[] margin) { this.margin = margin; }

    public int getWidth() { return width; }
    public void setWidth(final int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public ChartStyle getStyle() { return style; }
    public void setStyle(final ChartStyle style) { this.style = style; }

    public boolean isSkipClone() { return skipClone; }
    public void setSkipClone(final boolean skipClone) { this.skipClone = skipClone; }

    public Events getEvents() { return events; }
    public void setEvents(final Events events) { this.events = events; }
}
