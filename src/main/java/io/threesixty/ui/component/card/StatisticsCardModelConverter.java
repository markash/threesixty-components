package io.threesixty.ui.component.card;

/**
 * Converts a S object to a StatisticsCardModel object
 * @author Mark P Ashworth
 */
public interface StatisticsCardModelConverter<S> {
	StatisticsCardModel convert(final S object);
}
