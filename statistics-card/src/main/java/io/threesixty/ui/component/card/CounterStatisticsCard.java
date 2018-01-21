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
    private static final String STYLE_TITLE_LARGE = STYLE_TITLE + "-large";
    private static final String STYLE_CATEGORY = STYLE + "-category";
    private static final String STYLE_FOOTER = STYLE + "-footer";
    private static final String STYLE_LINK = STYLE + "-link";

    //private final MLabel imageField;
    private final MLabel textField;
    private final MLabel categoryLabel;
    private MButton button;
    private final SparklineChart chart = new SparklineChart();

    private final DataLabelSupplier dataLabelSupplier;
    private final DataLabelSettings dataLabelSettings = new DataLabelSettings();
    private final CategoryLabelSupplier categoryLabelSupplier;

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
        this.categoryLabelSupplier = new CategoryLabelSupplier(statistic, icon);

        //this.imageField =
        //        new MLabel(icon != null ? icon.getHtml() : "")
        //                .withContentMode(ContentMode.HTML);

        this.textField =
                new MLabel()
                        .withWidth(100, Unit.PERCENTAGE)
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
        refresh();
	}

    @Override
    protected Component initContent() {

        final MCssLayout container = new MCssLayout()
                .withPrimaryStyleName(STYLE_CONTAINER)
                .withWidth(100, Unit.PERCENTAGE)
                .withComponents(
                        new MHorizontalLayout(/*imageField,*/ textField)
                                .withWidth(100, Unit.PERCENTAGE)
                                .withPrimaryStyleName(STYLE_TITLE),
                        categoryLabel,
                        chart);

        final MCssLayout footer = new MCssLayout()
                .withPrimaryStyleName(STYLE_FOOTER);
        //.withComponents(button);

        return new MCssLayout()
                .withWidth(100, Unit.PERCENTAGE)
                .withComponents(container, footer);
    }

    @Override
    protected void doSetValue(
            final CounterStatisticModel value) {

        this.dataLabelSupplier.statistic = value;
        this.categoryLabelSupplier.statistic = value;
        refresh();
    }

    /**
     * Refresh the statistics card after providing updated data.
     */
    public void refresh() {
        CounterStatisticModel model = this.dataLabelSupplier.statistic;

        if (model != null) {
            //this.imageField.setVisible(model.isIconVisible());

            if (CategoryPosition.TOP == model.getCategoryPosition()) {
                this.categoryLabel.setValue(this.dataLabelSupplier.get());
                this.categoryLabel.setPrimaryStyleName(/*this.imageField.isVisible() ? STYLE_TITLE_LEFT :*/ STYLE_TITLE_CENTER);
                this.textField.setValue(this.categoryLabelSupplier.get());
                this.textField.withPrimaryStyleName(STYLE_CATEGORY);
            } else {
                this.textField.setValue(this.dataLabelSupplier.get());
                this.textField.setPrimaryStyleName(/*this.imageField.isVisible() ? STYLE_TITLE_LEFT :*/ STYLE_TITLE_CENTER);
                this.categoryLabel.setValue(this.categoryLabelSupplier.get());
                this.categoryLabel.withPrimaryStyleName(STYLE_CATEGORY);
            }

            if (model.isShowOnlyStatistic()) {
                this.chart.setVisible(false);
                this.categoryLabel.addStyleName(STYLE_TITLE_LARGE);
            } else {
                this.chart.setVisible(true);
                this.categoryLabel.removeStyleName(STYLE_TITLE_LARGE);
            }

            Series series = new Series(model.getCategory(), model.getValues());
            model.getStartingPoint().map(DataPoint::getY).ifPresent(series::setPointStart);
            model.getPointInterval().ifPresent(series::setPointInterval);

            this.chart.getOptions().addSeries(series);
            this.chart.refresh();
        }
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

    private static class CategoryLabelSupplier implements Supplier<String> {

        private CounterStatisticModel statistic;
        private VaadinIcons icon;

        CategoryLabelSupplier(
                final CounterStatisticModel statistic,
                final VaadinIcons icon) {

            this.statistic = statistic;
            this.icon = icon;
        }

        @Override
        public String get() {
            return (this.statistic.isIconVisible() ?
                        Optional.ofNullable(this.icon)
                            .map(icon -> icon.getHtml() + "&nbsp;&nbsp;")
                            .orElse("")  :
                        "") +
                    statistic.getCategory();
        }
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
            return "<strong>" +
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
