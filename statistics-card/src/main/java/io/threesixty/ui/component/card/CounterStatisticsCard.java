package io.threesixty.ui.component.card;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import io.threesixty.ui.component.chart.SparklineChart;
import io.threesixty.ui.component.chart.options.Axis;
import io.threesixty.ui.component.chart.options.DataPoint;
import io.threesixty.ui.component.chart.options.Series;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CounterStatisticsCard extends CustomField<CounterStatisticModel> {
    private static final long serialVersionUID = 1L;

    private static final String STYLE = "card";
    private static final String STYLE_CONTAINER = STYLE + "-container";
    private static final String STYLE_TITLE = STYLE + "-title";
    private static final String STYLE_TITLE_LEFT = STYLE_TITLE + "-left";
    private static final String STYLE_TITLE_CENTER = STYLE_TITLE + "-center";
    private static final String STYLE_CATEGORY = STYLE + "-category";
    private static final String STYLE_FOOTER = STYLE + "-footer";
    private static final String STYLE_LINK = STYLE + "-link";

    private MLabel imageField;
    private MLabel textField;
    private MLabel categoryLabel;
    private MButton button;
    private SparklineChart chart = new SparklineChart();

    private final DataLabelSupplier dataLabelSupplier;

    //private CounterStatisticModel statistic;
    private DataLabelSettings dataLabelSettings = new DataLabelSettings();

    public CounterStatisticsCard() {
        this(null, null, null);
    }

    public CounterStatisticsCard(
            final VaadinIcons icon,
            final String viewName) {

        this(icon, null, viewName);
    }

	public CounterStatisticsCard(
			final VaadinIcons icon,
			final CounterStatisticModel statistic,
			final String viewName) {
		super();

		this.dataLabelSupplier = new DataLabelSupplier(statistic, this.dataLabelSettings);

        this.imageField =
                new MLabel(icon != null ? icon.getHtml() : "")
                        .withContentMode(ContentMode.HTML);

        this.textField =
                new MLabel()
                        .withContentMode(ContentMode.HTML);
        this.categoryLabel =
                new MLabel()
                        .withWidth(100, Unit.PERCENTAGE)
                        .withContentMode(ContentMode.HTML);

        if (viewName != null) {
            this.button =
                    new MButton(VaadinIcons.ARROW_CIRCLE_RIGHT_O)
                            .withPrimaryStyleName(STYLE_LINK);
            this.button.addClickListener(event -> UI.getCurrent().getNavigator().navigateTo(viewName));
        }
        setPrimaryStyleName(STYLE);
		setSizeUndefined();
        refreshComponent();
	}

    @Override
    protected Component initContent() {

        return buildContent();
    }

    @Override
    protected void doSetValue(
            final CounterStatisticModel value) {

        this.dataLabelSupplier.statistic = value;
        refreshComponent();
    }

    protected void refreshComponent() {
        CounterStatisticModel model = this.dataLabelSupplier.statistic;

        this.imageField.setVisible(model.isIconVisible());

        if (CategoryPosition.TOP == model.getCategoryPosition()) {
            this.categoryLabel.setValue(this.dataLabelSupplier.get());
            this.categoryLabel.setPrimaryStyleName(this.imageField.isVisible() ? STYLE_TITLE_LEFT : STYLE_TITLE_CENTER);
            this.textField.setValue(model.getCategory());
            this.textField.withPrimaryStyleName(STYLE_CATEGORY);
        } else {
            this.textField.setValue(this.dataLabelSupplier.get());
            this.textField.setPrimaryStyleName(this.imageField.isVisible() ? STYLE_TITLE_LEFT : STYLE_TITLE_CENTER);
            this.categoryLabel.setValue(model.getCategory());
            this.categoryLabel.withPrimaryStyleName(STYLE_CATEGORY);
        }

        Series series = new Series(model.getCategory(), model.getValues());
        model.getStartingPoint().map(DataPoint::getY).ifPresent(series::setPointStart);
        model.getPointInterval().ifPresent(series::setPointInterval);

        this.chart.getOptions().addSeries(series);
        this.chart.refresh();
    }

    /**
     * Returns the current value of this object.
     * <p>
     * <i>Implementation note:</i> the implementing class should document
     * whether null values may be returned or not.
     *
     * @return the current value
     */
    @Override
    public CounterStatisticModel getValue() {
        return this.dataLabelSupplier.statistic;
    }

    public DataLabelSettings getDataLabelSettings() { return dataLabelSettings; }

    public SparklineChart getChart() { return chart; }

    public CounterStatisticsCard withHourlyInterval(
            final Axis axis,
            final int interval) {

        switch (axis) {
            case X:
                getChart().getOptions().getxAxis().withHourlyInterval(interval);
                break;
        }

        return this;
    }

	private CssLayout buildContent() {
		final MCssLayout content = new MCssLayout();

		final MCssLayout container = new MCssLayout()
                .withPrimaryStyleName(STYLE_CONTAINER)
                .withWidth(100, Unit.PERCENTAGE)
                .withComponents(
                        new MHorizontalLayout(imageField, textField)
                                .withPrimaryStyleName(STYLE_TITLE),
                        categoryLabel, chart);

        final MCssLayout footer = new MCssLayout()
                .withPrimaryStyleName(STYLE_FOOTER);
                //.withComponents(button);

        return content.withComponents(container, footer);
	}

	private static class DataLabelSupplier implements Supplier<String> {

	    private final DataLabelSettings dataLabelSettings;
	    private CounterStatisticModel statistic;

        DataLabelSupplier(
                final CounterStatisticModel statistic) {

            this(statistic, new DataLabelSettings());
        }

	    DataLabelSupplier(
	            final CounterStatisticModel statistic,
                final DataLabelSettings dataLabelSettings) {

	        this.statistic = statistic;
	        this.dataLabelSettings = dataLabelSettings;
        }

        @Override
        public String get() {
            return "&nbsp;&nbsp;<strong>" +
                    getDataLabelPrefix() +
                    getDataLabelText() +
                    "</strong> " +
                    getVarianceText();
        }

        private String getDataLabelPrefix() {
            return (dataLabelSettings.hasDisplayUnit() ? dataLabelSettings.getDisplayUnit() + " "  : "");
        }

        private String getDataLabelText() {
            Number data =
                    Optional.ofNullable(this.statistic)
                            .orElseGet(CounterStatisticsCard::EMPTY)
                            .getValue();
            return this.dataLabelSettings.getFormat().format(data);
        }

        private String getVarianceText() {
            Number variance = this.statistic.getVariance();

            String text = "";
            if (this.statistic.isVarianceVisible() && this.statistic.hasVariance()) {
                text = "<span class='" + getVarianceClass(variance) + "'>(" +
                        this.dataLabelSettings.getFormat().format(variance) +
                        ")</span>";
            }
            return text;
        }

        private String getVarianceClass(final Number variance) {
            List<String> classes = new ArrayList<>();
            classes.add("card-variance");

            if (variance.doubleValue() > 0) {
                classes.add("positive");
            } else if (variance.doubleValue() < 0) {
                classes.add("negative");
            }

            return classes.stream().collect(Collectors.joining(" "));
        }
    }

    private static CounterStatisticModel EMPTY() {

        return new CounterStatisticModel("Unknown", CounterStatisticModel.defaultValue);
    }
}
