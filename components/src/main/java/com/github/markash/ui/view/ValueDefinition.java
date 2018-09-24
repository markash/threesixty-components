package com.github.markash.ui.view;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
import com.vaadin.ui.renderers.Renderer;
import org.springframework.format.Formatter;

import java.util.Locale;
import java.util.Optional;

/**
 *
 * @param <B>
 * @param <F> The type of the field
 */
public class ValueDefinition<B, F> {

    private final String propertyName;
//    private final ColumnDefinition<B, F> columnDefinition;
    private ValueProvider<B, ?> provider;
    private Setter<B, F> setter;
    private HasValue<F> editor;
    private Formatter<F> formatter;
    private Renderer<?> renderer;

    ValueDefinition(
//            final ColumnDefinition<B, F> columnDefinition,
            final String propertyName,
            final ValueProvider<B, ?> provider) {

        this.propertyName = propertyName;
//        this.columnDefinition = columnDefinition;
        this.provider = provider;
    }

    public ValueProvider<B, ?> getProvider() { return provider; }
    public String getPropertyName() { return propertyName; }

    public ValueDefinition withProvider(
            final ValueProvider<B, F> provider) {

        this.provider = provider;
        return this;
    }

    public ValueDefinition withSetter(
            final Setter<B, F> setter) {

        this.setter = setter;
        return this;
    }

    public ValueDefinition withFormatter(
            final Formatter<F> formatter) {

        this.formatter = formatter;
        return this;
    }

    public ValueDefinition withRenderer(
            final Renderer<F> renderer) {

        this.renderer = renderer;
        return this;
    }

    public ValueDefinition withEditor(
            final HasValue<F> field) {

        this.editor = field;
        return this;
    }
//        @SuppressWarnings("unused")
//        public ValueDefinition<F> renderer(
//                final Renderer<?> renderer) {
//
//            this.renderer = renderer;
//            return this;
//        }
    /**
     * Reads the value from bean using the value provider
     * @param bean The bean to read the value from
     * @return The value
     */
    @SuppressWarnings("unused")
    public Object read(
            final B bean) {

        return Optional.ofNullable(this.provider)
                .map(provider -> provider.apply(bean))
                .orElse(null);
    }
    /**
     * Renders the value from the bean using the value provider of the display using the defined renderer
     * @param bean The bean to read the value from
     * @return The rendered value
     */
    @SuppressWarnings("unchecked")
    public String render(
            final B bean) {

        return Optional.ofNullable(this.provider)
                .map(provider -> provider.apply(bean))
                .map(value -> Optional.ofNullable(formatter)
                        .orElse(new DefaultStringFormatter())
                        .print((F)value, Locale.getDefault()))
                .orElse(null);
    }
    //        private Converter<F, Object> createConverter(
//                final Class<?> getterType) {
//
//            return Converter.from(fieldValue -> getterType.cast(fieldValue),
//                    propertyValue -> (F) propertyValue, exception -> {
//                        throw new RuntimeException(exception);
//                    });
//        }
    @SuppressWarnings("unchecked")
    public Binder.Binding<B, F> bind(
            final Binder binder) {

        if (editor != null && setter != null && provider != null) {
            return binder.bind(editor, provider, setter);
        }
        return null;
    }
//    public ColumnDefinition<B, F> end() {
//        return this.columnDefinition;
//    }

    private class DefaultStringFormatter implements Formatter<F> {
        @Override
        public F parse(
                final String text,
                final Locale locale) {
            return null;
        }

        @Override
        public String print(final F object, final Locale locale) {
            return object != null ? object.toString() : "null";
        }
    }
}
