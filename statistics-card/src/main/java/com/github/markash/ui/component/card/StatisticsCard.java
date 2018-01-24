package com.github.markash.ui.component.card;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
public class StatisticsCard extends CustomField<StatisticsCardModel> {
	
	public final static String STYLE_WARNING = "stat-warning";
	public final static String STYLE_INFO = "stat-info";
	public final static String STYLE_ERROR = "stat-error";
	public final static String STYLE_SUCCESS = "stat-success";
	
	private Label statText = new Label();
	private Label statLabel = new Label();
	private Label statInfo = new Label("", ContentMode.HTML);
	private Label statPic = new Label("", ContentMode.HTML);
	
	private String styleName = null;
    private StatisticsCardModel value;

	public StatisticsCard() {
		this("0", "Some interesting event", "0% up from last week", VaadinIcons.COMPRESS_SQUARE);
	}
	
	public StatisticsCard(
			final String statistic,
			final String label,
			final String info,
			final VaadinIcons icon) {
		this(statistic, label, info, icon, null);
	}
	
	public StatisticsCard(
			final String statistic,
			final String label,
			final String info,
			final VaadinIcons icon,
			final String styleName) {

	    this.value = new StatisticsCardModel();
		this.value.setStatistic(statistic);
		this.value.setStatisticLabel(label);
		this.value.setStatisticInfo(info);
		this.value.setStatisticIcon(icon);
		this.styleName = styleName;
		this.addValueChangeListener(this::onChange);
	}

	public String getStatistic() { return this.getValue().getStatistic(); }
	public String getStatisticLabel() { return getValue().getStatisticLabel(); }	
	public String getStatisticInfo() { return getValue().getStatisticInfoAsHtml(); }
	public String getStatisticIcon() { return getValue().getStatisticIconAsHtml(); }
	
	public void setStatistic(String statistic) { 
		this.getValue().setStatistic(statistic); 
		updateStatistic();
	}
	
	public void setStatisticLabel(String statisticLabel) { 
		this.getValue().setStatisticLabel(statisticLabel);
		updateStatisticLabel();
	}
	
	public void setStatisticInfo(String statisticInfo) { 
		this.getValue().setStatisticInfo(statisticInfo); 
		updateStatisticInfo();
	}
	
	public void setStatisticIcon(VaadinIcons statisticIcon) {
		this.getValue().setStatisticIcon(statisticIcon);
		updateStatisticPic();
	}

	protected void updateStatistic() {
		if (this.statText != null) {
			this.statText.setValue(getStatistic());
		}
	}
	
	protected void updateStatisticLabel() {
		if (this.statLabel != null) {
			this.statLabel.setValue(getStatisticLabel());
		}
	}
	
	protected void updateStatisticInfo() {
		if (this.statInfo != null) {
			this.statInfo.setValue(getStatisticInfo());
		}
	}
	
	protected void updateStatisticPic() {
		if (this.statPic != null) {
			this.statPic.setValue(getStatisticIcon());
		}
	}
	
	@Override
	protected Component initContent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setStyleName("stats-panel");
		
		if (StringUtils.isNotBlank(styleName)) {
			layout.addStyleName(styleName);
		}
		
		statText = new Label(getStatistic());
		statText.setSizeUndefined();
		statText.setStyleName("stats-text");
		statText.addStyleName(ValoTheme.LABEL_H1);

		statLabel = new Label(getStatisticLabel());
		statLabel.setSizeUndefined();
		statLabel.setStyleName("stats-label");
		statLabel.addStyleName(ValoTheme.LABEL_H4);
		
		statInfo = new Label(getStatisticInfo(), ContentMode.HTML);
		statInfo.setStyleName("stats-info");
		
		statPic = new Label(getStatisticIcon(), ContentMode.HTML);
		statPic.setStyleName("stats-icon");

		layout.addComponents(statText, statLabel, statInfo, statPic);
		return layout;
	}

    @Override
    protected void doSetValue(final StatisticsCardModel value) {
        /* Update the internal value */
        super.setValue(value, true);
    }

    @Override
    public StatisticsCardModel getValue() {
        return this.value;
    }

    private void onChange(final ValueChangeEvent<StatisticsCardModel> event) {
		updateStatistic();
		updateStatisticLabel();
		updateStatisticInfo();
		updateStatisticPic();
	}
}
