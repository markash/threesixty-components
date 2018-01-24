package com.github.markash.ui.component.card;

import com.github.markash.ui.component.chart.options.DataPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CounterStatisticModel implements Serializable {
	private static final long serialVersionUID = 1L;
	static DataPoint defaultValue = new DataPoint(0.00);

	private String category;
	private List<DataPoint> values = new ArrayList<>();
	private StatisticShow show = StatisticShow.Sum;
	private boolean iconVisible = true;
	private boolean varianceVisible = true;
	private boolean showOnlyStatistic = false;
    private DataLabelSettings dataLabelSettings = new DataLabelSettings();
    private DataPoint startingPoint = null;
    private Integer pointInterval = null;
    private CategoryPosition categoryPosition = CategoryPosition.TOP;

    /**
     * Constructor to create a statistics model that has only one value
     * @param category A descriptive name or label for the category for statistic
     * @param value The value of the statistic
     */
    public CounterStatisticModel(
            final String category,
            final Number value) {

        this(category, new DataPoint(value));
    }

    public CounterStatisticModel(
            final String category,
            final DataPoint...values) {
        this(category, Arrays.asList(values));
    }

	public CounterStatisticModel(
			final String category,
			final List<DataPoint> values) {

		this(category, null, values);
	}

	public CounterStatisticModel(
			final String category,
            final DataLabelSettings dataLabelSettings,
			final List<DataPoint> points) {

		this.category = category;
		if (points != null) {
		    this.values.addAll(points);
        }
		if (dataLabelSettings != null) {
            this.dataLabelSettings = dataLabelSettings;
        }
	}

	public String getCategory() { return this.category; }

    /**
     * Clears the statistics data points and sets the single data point of `value`. Use setValues(List&lt;DataPoint&gt;) to
     * set data points with multiple values.
     *
     * @param value The value of the statistic
     */
    public void setValue(
            final Number value) {

        this.values.clear();
        this.values.add(new DataPoint(value));
    }

	public Number getValue() {

	    switch(this.show) {
            case Last:
                return this.values.isEmpty() ? this.values.get(this.values.size() - 1).getY() : defaultValue.getY();
            case Sum:
                return this.values.stream().map(DataPoint::getY).mapToDouble(Number::doubleValue).sum();
            default:
                return defaultValue.getY();
	    }
	}

	public Number getVariance() {

	    return getLast().orElse(defaultValue).getY().doubleValue() - getFirst().orElse(defaultValue).getY().doubleValue();
	}

	private Optional<DataPoint> getFirst() {

    	if (!this.values.isEmpty()) {
			return Optional.ofNullable(this.values.get(this.values.size() - 1));
		}
		return Optional.empty();
	}

	private Optional<DataPoint> getLast() {

		if (!this.values.isEmpty()) {
			return Optional.ofNullable(this.values.get(0));
		}
		return Optional.empty();
	}

    public StatisticShow getShow() { return show; }
    public void setShow(final StatisticShow show) { this.show = show; }

    public boolean isIconVisible() { return iconVisible; }
    public void setIconVisible(final boolean iconVisible) { this.iconVisible = iconVisible; }

    public boolean isVarianceVisible() { return varianceVisible; }
    public void setVarianceVisible(final boolean varianceVisible) { this.varianceVisible = varianceVisible; }
    public boolean hasVariance() { return this.getVariance().doubleValue() != 0.00; }

    public boolean isShowOnlyStatistic() { return this.showOnlyStatistic; }
    public void setShowOnlyStatistic(final boolean showOnlyStatistic) { this.showOnlyStatistic = showOnlyStatistic; }

    public DataLabelSettings getDataLabelSettings() { return dataLabelSettings; }
    public void setDataLabelSettings(final DataLabelSettings dataLabelSettings) { this.dataLabelSettings = dataLabelSettings; }

    public List<DataPoint> getValues() { return values; }
    public void setValues(final List<DataPoint> values) { this.values = values; }
    public void addValues(final List<DataPoint> values) { this.values.addAll(values); }
    public void addValue(final DataPoint value) { this.values.add(value); }

    public Optional<DataPoint> getStartingPoint() { return Optional.ofNullable(startingPoint); }
    public void setStartingPoint(final DataPoint startingPoint) { this.startingPoint = startingPoint; }

    public Optional<Integer> getPointInterval() { return Optional.ofNullable(pointInterval); }
    public void setPointInterval(final Integer pointInterval) { this.pointInterval = pointInterval; }

    public CategoryPosition getCategoryPosition() { return categoryPosition; }
    public void setCategoryPosition(final CategoryPosition categoryPosition) { this.categoryPosition = categoryPosition; }

    public CounterStatisticModel withShow(
            final StatisticShow show) {

        this.setShow(show);
        return this;
    }

    public CounterStatisticModel withStartingPoint(
            final DataPoint startingPoint) {

        this.setStartingPoint(startingPoint);
        return this;
    }

    public CounterStatisticModel withPointInterval(
            final int pointInterval) {

        this.setPointInterval(pointInterval);
        return this;
    }

    public CounterStatisticModel withIconHidden() {

        this.iconVisible = false;
        return this;
    }

    public CounterStatisticModel withStatFollowedByCategory() {

        this.categoryPosition = CategoryPosition.MIDDLE;
        return this;
    }

    public CounterStatisticModel withCategoryFollowedByStat() {

        this.categoryPosition = CategoryPosition.TOP;
        return this;
    }

    public CounterStatisticModel withShowOnlyStatistic(
            final boolean value) {

        this.showOnlyStatistic = value;
        return this;
    }
}
