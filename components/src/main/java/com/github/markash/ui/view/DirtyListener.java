package com.github.markash.ui.view;

import java.io.Serializable;

/**
 * A listener to determine if the form is dirty (i.e. a field has changed) or
 * clean (i.e. the fields are persisted). The dirty / clean state of the form usual determines
 * whether functionality like Save, Reset, Delete are enabled.
 */
public interface DirtyListener extends Serializable {
    void onDirty(DirtyEvent event);
}