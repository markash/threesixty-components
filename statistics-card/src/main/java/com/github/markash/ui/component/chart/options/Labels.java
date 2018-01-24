package com.github.markash.ui.component.chart.options;

import java.io.Serializable;

public class Labels implements Serializable {
    private boolean enabled = false;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(final boolean enabled) { this.enabled = enabled; }
}
