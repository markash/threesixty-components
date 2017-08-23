package io.threesixty.ui.view;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Registration;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import io.threesixty.ui.component.panel.PanelBuilder;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public abstract class AbstractEntityEditForm<T extends Persistable<Serializable>> extends HorizontalLayout {
	private static String[] DEFAULT_NESTED_PROPERTIES = new String[] {};
	
    private TextField idField = new TextField("Id");
	private Binder<T> binder;
	private LinkedHashSet<DirtyListener> dirtyListeners = new LinkedHashSet<>();
	private Map<HasValue, Registration> changeListeners = new HashMap<>();

	private boolean layoutCompleted = false;
	
	public AbstractEntityEditForm(Class<T> beanType) {

		this.binder = new Binder<>(beanType);
		this.binder.forField(idField)
                //.withConverter(new StringToLongConverter("Unable to convert id"))
                .bind("id");

		setSpacing(true);
		setMargin(false);
		setSizeFull();
		Responsive.makeResponsive(this);
		
        idField.setWidth(100.0f, Unit.PERCENTAGE);
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
		registerDirtyListener();
	}
	
//	public void discard() {
//		this.binder.discard();
//	}
//
	/**
     * Validates the form and returns the status
     * @return the validation status
     */
	public BinderValidationStatus<T> validate() {
		return this.binder.validate();
	}
	
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
	
	protected void registerDirtyListener() {
		/* Link the changing of the id text with a form dirty since this
		 * is the only field on the form sometimes which makes it difficult
		 * for the user to know that a tab is required to enable the Save button
		 */
		if (!idField.isReadOnly()) {
			idField.addValueChangeListener(this::onTextChange);
		}

		/*
		 * Link a value change to all other fields on the form
		 */
		this.binder.getFields().forEach(this::registerFieldDirtyListener);
	}

	private void registerFieldDirtyListener(HasValue<?> field) {
	    if (this.changeListeners.containsKey(field)) {
	        this.changeListeners.remove(field);
        }
        this.changeListeners.put(field, field.addValueChangeListener(this::onValueChange));
    }

	/**
	 * Provide a hook for subclasses to update dependant fields
	 */
	protected void updateDependentFields() { }
	
	/**
	 * Update the field constraints to the new bound value
	 */
	private void updateFieldConstraints() {
		idField.setEnabled(getValue().isNew());
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
	
	protected void layout() {
		if (!layoutCompleted) {
			internalLayout();
			layoutCompleted = true;
		}
	}

	protected void internalLayout() {
		addComponent(PanelBuilder.FORM(idField));
        addComponent(new Label(""));
	}
	
	private void onValueChange(final HasValue.ValueChangeEvent<?> event) {
	    fireFormDirty(new FormDirtyEvent(event.getSource(), event.getOldValue(), event.getValue()));
	}
	
	private void onTextChange(final HasValue.ValueChangeEvent event) {
		fireFormDirty(new FormDirtyEvent(event.getSource(), event.getOldValue(), event.getValue()));
	}
		
	private void fireFormClean() {
		fireFormDirty(new FormDirtyEvent(DirtyStatus.CLEAN));
	}
	
	private void fireFormDirty(final FormDirtyEvent event) {
        if (dirtyListeners != null) {
            for (DirtyListener listener : dirtyListeners) {
                listener.onDirty(event);
            }
        }
    }
}
