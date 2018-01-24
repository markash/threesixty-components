package com.github.markash.ui.component.card;

import com.vaadin.icons.VaadinIcons;

import java.io.Serializable;

public class StatisticsCardModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final static String FIELD_STATISTIC = "statistic";
	public final static String FIELD_STATISTIC_LABEL = "statisticLabel";
	public final static String FIELD_STATISTIC_INFO = "statisticInfoAsHtml";
	public final static String FIELD_STATISTIC_ICON = "statisticIconAsHtml";
	
	private String statistic = "";
	private String statisticLabel = "";
	private String statisticInfo = "";
	private String styleName = "";
	private VaadinIcons statisticIcon = VaadinIcons.COMPRESS_SQUARE;
	
	public StatisticsCardModel() { }
	
	public StatisticsCardModel(String statistic, String statisticLabel, String statisticInfo, VaadinIcons statisticIcon, String styleName) {
		this.statistic = statistic;
		this.statisticLabel = statisticLabel;
		this.statisticInfo = statisticInfo;
		this.statisticIcon = statisticIcon;
		this.styleName = styleName;
	}
	public String getStatistic() { return statistic; }
	public String getStatisticLabel() { return statisticLabel; }
	public String getStatisticInfo() { return statisticInfo; }
	public String getStyleName() { return styleName; }
	
	public String getStatisticInfoAsHtml() { return VaadinIcons.INFO_CIRCLE.getHtml() + "  " + getStatisticInfo(); }
	public String getStatisticIconAsHtml() { return statisticIcon != null ? statisticIcon.getHtml() : null; }
	public VaadinIcons getStatisticIcon() { return statisticIcon; }
	public void setStyleName(String styleName) { this.styleName = styleName; }
	
	public void setStatistic(final String value) { this.statistic = value; }
	public void setStatisticLabel(final String value) { this.statisticLabel = value; }
	public void setStatisticInfo(final String value) { this.statisticInfo = value; }
	public void setStatisticIcon(final VaadinIcons statisticIcon) { this.statisticIcon = statisticIcon; }
	
	public <S> void updateWith(StatisticsCardModelConverter<S> converter, S object) {
		if (converter == null) {
			throw new IllegalArgumentException("The converter object cannot be null");
		}
		
		updateWith(converter.convert(object));
	}
	
	public void updateWith(final StatisticsCardModel model) {
		if (model != null) {
			if (model.getStatistic() != null)
				this.statistic =  model.getStatistic();
			
			if (model.getStatisticLabel() != null)
				this.statisticLabel =  model.getStatisticLabel();
			
			if (model.getStatisticInfo() != null)
				this.statisticInfo =  model.getStatisticInfo();
			
			if (model.getStatisticIcon() != null)
				this.statisticIcon =  model.getStatisticIcon();
		}
	}
}
