package com.github.markash.ui.component.field;

import java.util.EventObject;
public class FilterChangeEvent extends EventObject {

    private final FilterAction action;
    private final FilterModel filter;

    private FilterChangeEvent(
            final Object source,
            final FilterAction action,
            final FilterModel filter) {

        super(source);
        this.action = action;
        this.filter = filter;
    }

    public static FilterChangeEvent ADD (
            final Object source,
            final FilterModel filter) {

        return new FilterChangeEvent(
                source,
                FilterAction.ADD,
                filter
        );
    }

    public static FilterChangeEvent CLEAR (
            final Object source,
            final FilterModel filter) {

        return new FilterChangeEvent(
                source,
                FilterAction.CLEAR,
                filter
        );
    }

    public static FilterChangeEvent CLEAR_ALL (
            final Object source) {

        return new FilterChangeEvent(
                source,
                FilterAction.CLEAR_ALL,
                new FilterModel(null, null)
        );
    }

    public FilterAction getAction() { return this.action; }
    public FilterModel getFilter() { return this.filter; }

    public enum FilterAction {
        ADD,
        CLEAR,
        CLEAR_ALL
    }
}
