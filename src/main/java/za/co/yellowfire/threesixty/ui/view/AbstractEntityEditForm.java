package za.co.yellowfire.threesixty.ui.view;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.server.Responsive;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import org.springframework.data.domain.Persistable;
import za.co.yellowfire.threesixty.ui.component.panel.PanelBuilder;

import java.io.Serializable;
import java.util.LinkedHashSet;

public abstract class AbstractEntityEditForm<T extends Persistable<Long>> extends HorizontalLayout {
	private static String[] DEFAULT_NESTED_PROPERTIES = new String[] {};
	
	@PropertyId("id")
    protected TextField idField = new TextField("Id");

	private Binder<T> binder;
	private LinkedHashSet<DirtyListener> dirtyListeners = new LinkedHashSet<>();
	
	private boolean layoutCompleted = false;
	
	public AbstractEntityEditForm(Class<T> beanType) {

		this.binder = new Binder<>(beanType);
		this.binder.forField(idField)
                .withConverter(new StringToLongConverter("Unable to convert id"))
                .bind("id");

		setSpacing(true);
		setMargin(false);
		setSizeFull();
		Responsive.makeResponsive(this);
		
        idField.setWidth(100.0f, Unit.PERCENTAGE);
        //idField.setNullRepresentation("");
	}

	public Binder<T> getBinder() { return this.binder; }

	/**
	 * Returns the list of nested properties that the form group should bind to. The default
	 * is an empty array.
	 * @return An array of nested properties in Java object notation
	 */
	protected String[] getNestedProperties() { return DEFAULT_NESTED_PROPERTIES; }
	
	public T getValue() {  return this.binder.getBean();  }
	
	public void bindToEmpty() {
		bind(null);
	}
	
	public void bind(final T newValue) {
		this.binder.setBean(newValue);

		updateDependentFields();
		updateFieldConstraints();
		//registerDirtyListener();
	}
	
//	public void discard() {
//		this.binder.discard();
//	}
//
//	public void commit() throws CommitException {
//		this.binder.commit();
//	}
	
	public boolean isModified() {
		return this.binder.hasChanges();
	}
	
	public boolean isValid() {
		return this.binder.isValid();
	}
	
//	public void addCommitHandler(final CommitHandler commitHandler) {
//		if (commitHandler != null) {
//			this.binder.addCommitHandler(commitHandler);
//		}
//	}
	
	public void addDirtyListener(final DirtyListener listener) {
		if (listener != null) {
			this.dirtyListeners.add(listener);
		}
	}
	
	public void removeDirtyListener(final DirtyListener listener) {
		if (listener != null) {
			this.dirtyListeners.remove(listener);
		}
	}
	
//	protected void registerDirtyListener() {
//		/* Link the changing of the id text with a form dirty since this
//		 * is the only field on the form sometimes which makes it difficult
//		 * for the user to know that a tab is required to enable the Save button
//		 */
//		if (!idField.isReadOnly()) {
//			idField.addValueChangeListener(this::onTextChange);
//		}
//
//		/*
//		 * Link a value change to all other fields on the form
//		 */
//		for(Field<?> field : this.binder.getFields()) {
//			field.removeValueChangeListener(this::onValueChange);
//			field.addValueChangeListener(this::onValueChange);
//		}
//	}
	
	/**
	 * Provide a hoot for subclasses to update dependant fields
	 */
	protected void updateDependentFields() { }
	
	/**
	 * Update the field constraints to the new bound value
	 */
	protected void updateFieldConstraints() {
		idField.setEnabled(getValue().isNew());
		
//		for(Field<?> field : fieldGroup.getFields()) {
//			field.removeValueChangeListener(this::onValueChange);
//			field.addValueChangeListener(this::onValueChange);
//		}
	}
		
	protected abstract T buildEmpty();
	
	/**
	 * Provides the form the capability of enriching the entity with data that is not part of the entity read from
	 * the data source.
	 * 
	 * @param entity The entity
	 * @return The enriched entity
	 */
	protected T buildEntity(T entity) {
		return entity;
	}
	
	public void layout() {
		if (!layoutCompleted) {
			internalLayout();
			layoutCompleted = true;
		}
	}
	
	protected void internalLayout() {
		addComponent(PanelBuilder.FORM(idField));
        addComponent(new Label(""));
	}
	
	protected void onValueChange(final ValueChangeEvent event) {
		if (isModified()) {
			fireFormDirty(new FormDirtyEvent(event.getProperty()));
		}
	}
	
	protected void onTextChange(final HasValue.ValueChangeEvent event) {
		fireFormDirty(new FormDirtyEvent());
	}
		
	protected void fireFormClean() {
		fireFormDirty(new FormDirtyEvent(DirtyStatus.CLEAN));
	}
	
	protected void fireFormDirty(final FormDirtyEvent event) {
		if (dirtyListeners != null) {
			for (DirtyListener listener : dirtyListeners) {
				listener.onDirty(event);
			}
		}
	}

	/**
	 * A listener to determine if the form is dirty (i.e. a field has changed) or 
	 * clean (i.e. the fields are persisted). The dirty / clean state of the form usual determines
	 * whether functionality like Save, Reset, Delete are enabled.
     */
    public interface DirtyListener extends Serializable {
        public void onDirty(DirtyEvent event);
    }
    
    public static enum DirtyStatus {
    	DIRTY,
    	CLEAN
    }
    
    public interface DirtyEvent extends Serializable {
        public Property<?> getProperty();
        public DirtyStatus getStatus(); 
        public boolean isRecalculationRequired();
    }
    
    public static class FormDirtyEvent implements DirtyEvent {
    	private final Property<?> property;
    	private final DirtyStatus status;
    	private final boolean recalculationRequired;
    	
    	public FormDirtyEvent() {
    		this(null, false);
    	}
    	public FormDirtyEvent(final DirtyStatus status) {
    		this(null, false, status);
    	}
    	public FormDirtyEvent(Property<?> property) {
    		this(property, false);
    	}
    	public FormDirtyEvent(final Property<?> property, final boolean recalculationRequired) {
    		this(property, recalculationRequired, DirtyStatus.DIRTY);
    	}
    	public FormDirtyEvent(final Property<?> property, final boolean recalculationRequired, final DirtyStatus status) {
    		this.property = property;
    		this.status = status;
    		this.recalculationRequired = recalculationRequired;
    	}
    	
    	public Property<?> getProperty() { return this.property; }
    	public DirtyStatus getStatus() { return this.status; }
    	public boolean isRecalculationRequired() { return this.recalculationRequired; }
    }
}
