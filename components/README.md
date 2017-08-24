ThreeSixty Components
=====================

Version 0.2.1
-------------

This version introduces breaking changes to accommodate for Vaadin 8.0.

Properties
----------

| Name                        | Default                | Description                                                   |
|-----------------------------|------------------------|---------------------------------------------------------------|
|threesixty.application.title | Application            | The title of the application used in the browser and the menu header |
|threesixty.application.logo  | VaadinIcons.SPECIALIST | The logo that should be used for the menu logo area. The default is to use the VaadinIcons icon but can be any HTML value.


Table Definition
----------------

The table definition is used to define the columns in a Table Search View.

```java
/* Define a definition */
TableDefinition definition = new TableDefinition();

/* Add a text column that will be searched by the FilterTextField */
definition.withColumn(String.class)
    .forProperty("employeeCode")
    .withHeading("Name")
    .enableTextSearch();

/* Add a BigDecimal column that will not be searched but is editable */
List<BigDecimal> options = LongStream.range(1, 600).boxed().map(BigDecimal::valueOf).collect(Collectors.toList());
definition.withColumn(BigDecimal.class)
   .forProperty("january")
   .withHeading("Jan")
   .disableTextSearch()
   .editor(new ComboBox<>(null, options))
   .getter(YearForecast::getJanuary)
   .setter(YearForecast::setJanuary);

definition.withColumn(BigDecimal.class)
   .forProperty("february")
   .withHeading("Feb")
   .disableTextSearch()
   .editor(new ComboBox<>(null, options))
   .getter(YearForecast::getFebruary)
   .setter(YearForecast::setFebruary);
```