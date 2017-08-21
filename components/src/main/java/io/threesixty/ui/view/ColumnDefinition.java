package io.threesixty.ui.view;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
import com.vaadin.ui.renderers.Renderer;

import java.util.Optional;

/**
 * @param <BEAN> The type of the bean
 * @param <FIELD> The type of the field
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class ColumnDefinition<BEAN, FIELD> {
    private String property;
    private String heading;
    private boolean id;
    private boolean searchable;
    private Binder.Binding<BEAN, FIELD> binding;
    private HasValue<FIELD> editor;
    private ValueProvider<BEAN, FIELD> getter;
    private Setter<BEAN, FIELD> setter;
    private Renderer<?> renderer;
    public ColumnDefinition() { }

    public boolean isId() { return id; }
    public String getHeading() { return heading; }
    public String getProperty() { return property; }
    public boolean isSearchable() { return searchable; }
    public Optional<Renderer<?>> getRenderer() { return Optional.of(this.renderer); }

    public Binder.Binding<BEAN, FIELD> bind(final Binder<BEAN> binder) {
        if (editor != null && setter != null && getter != null) {
            return binder.bind(editor, getter, setter);
        }
        return null;
    }

    public void setProperty(final String property) { this.property = property; }
    public void setHeading(final String heading) { this.heading = heading; }
    public void setSearchable(final boolean searchable) { this.searchable = searchable; }
    public void setBinding(final Binder.Binding<BEAN, FIELD> binding) { this.binding = binding; }
    public void setGetter(final ValueProvider<BEAN, FIELD> getter) { this.getter = getter; }
    public void setSetter(final Setter<BEAN, FIELD> setter) { this.setter = setter; }

    public ColumnDefinition<BEAN, FIELD> forProperty(final String property) {
        setProperty(property);
        return this;
    }

    public ColumnDefinition<BEAN, FIELD> withHeading(final String heading) {
        setHeading(heading);
        return this;
    }

    public ColumnDefinition<BEAN, FIELD> enableTextSearch() {
        this.setSearchable(true);
        return this;
    }

    public ColumnDefinition<BEAN, FIELD> disableTextSearch() {
        this.setSearchable(false);
        return this;
    }

    public ColumnDefinition<BEAN, FIELD> getter(final ValueProvider<BEAN, FIELD> getter) {
        this.getter = getter;
        return this;
    }

    public ColumnDefinition<BEAN, FIELD> setter(final Setter<BEAN, FIELD> setter) {
        this.setSetter(setter);
        return this;
    }

    public ColumnDefinition<BEAN, FIELD> editor(final HasValue<FIELD> field) {
        this.editor = field;
        return this;
    }

    public ColumnDefinition<BEAN, FIELD> identity() {
        this.id = true;
        return this;
    }

    public ColumnDefinition<BEAN, FIELD> renderer(Renderer<?> renderer) {
        this.renderer = renderer;
        return this;
    }
}
