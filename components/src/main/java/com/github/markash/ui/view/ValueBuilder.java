package com.github.markash.ui.view;

import com.vaadin.data.PropertyDefinition;
import com.vaadin.ui.renderers.Renderer;
import org.springframework.format.Formatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class ValueBuilder<FIELD> {

    private String propertyName;
//    private ValueProvider<?, FIELD> provider;
//    private Setter<?, FIELD> setter;
    private Formatter<FIELD> formatter;
    private Renderer<?> renderer;

    ValueBuilder(
            final Class<FIELD> fieldType,
            final String propertyName) {

        this.propertyName = propertyName;
    }

    public static ValueBuilder<String> string(
            final String propertyName) {

        return property(String.class, propertyName);
    }

    public static ValueBuilder<Boolean> bool(
            final String propertyName) {

        return property(Boolean.class, propertyName);
    }

    public static ValueBuilder<Date> date(
            final String propertyName) {

        return property(Date.class, propertyName);
    }

    public static ValueBuilder<LocalDateTime> localDateTime(
            final String propertyName) {

        return property(LocalDateTime.class, propertyName);
    }

    public static ValueBuilder<LocalDate> localDate(
            final String propertyName) {

        return property(LocalDate.class, propertyName);
    }

    public static ValueBuilder<Integer> integerNumber(
            final String propertyName) {

        return property(Integer.class, propertyName);
    }

    public static ValueBuilder<Long> longNumber(
            final String propertyName) {

        return property(Long.class, propertyName);
    }

    public static ValueBuilder<Float> floatNumber(
            final String propertyName) {

        return property(Float.class, propertyName);
    }

    public static ValueBuilder<Double> doubleNumber(
            final String propertyName) {

        return property(Double.class, propertyName);
    }

    public static ValueBuilder<BigDecimal> bigDecimalNumber(
            final String propertyName) {

        return property(BigDecimal.class, propertyName);
    }

    public static ValueBuilder<BigInteger> bigIntegerNumber(
            final String propertyName) {

        return property(BigInteger.class, propertyName);
    }

    public static <FIELD> ValueBuilder<FIELD> property(
            final Class<FIELD> fieldType,
            final String propertyName) {

        return new ValueBuilder<>(fieldType, propertyName);
    }

//    public ValueBuilder<FIELD> withSetter(
//            final Setter<?, FIELD> setter) {
//
//        this.setter = setter;
//        return this;
//    }

    public ValueBuilder<FIELD> withRenderer(
            final Renderer<FIELD> renderer) {

        this.renderer = renderer;
        return this;
    }

    public ValueBuilder<FIELD> withFormatter(
            final Formatter<FIELD> formatter) {

        this.formatter = formatter;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <BEAN> ValueDefinition<BEAN, FIELD> build(
            final TableDefinition<BEAN> tableDefinition) {


        PropertyDefinition definition =
                        tableDefinition.getPropertySet()
                        .getProperty(propertyName).orElseThrow(() -> new RuntimeException("The property " + propertyName + " does not exist on bean"));


        ValueDefinition<BEAN, FIELD> def = new ValueDefinition<BEAN, FIELD>(propertyName, definition.getGetter());
//        def.withSetter(setter);
        def.withFormatter(formatter);
        return def;
    }
}
