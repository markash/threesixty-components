package za.co.yellowfire.threesixty.ui.component;

import java.io.Serializable;
import java.util.List;

import org.vaadin.viritin.LazyList.CountProvider;
import org.vaadin.viritin.v7.SortableLazyList.SortablePagingProvider;

@SuppressWarnings("serial")
public class EntitiesSupplierPagingProvider<T extends Serializable> implements SortablePagingProvider<T>, CountProvider {

	private transient EntitiesSupplier<T> supplier;
	private transient EntitiesSorter<T> sorter;
	private List<T> rows;
	
	public EntitiesSupplierPagingProvider(final EntitiesSupplier<T> supplier, final EntitiesSorter<T> sorter) {
		this.supplier = supplier;
		this.sorter = sorter;
	}

	@Override
	public int size() {
		return getRows().size();
	}

	@Override
	public List<T> findEntities(int firstRow, boolean sortAscending, String property) {
		return this.sorter.sort(getRows(), sortAscending, property);
	}
	
	protected List<T> getRows() {
		if (this.rows == null) {
			this.rows = supplier.get();
		}
		return this.rows;
	}
}
