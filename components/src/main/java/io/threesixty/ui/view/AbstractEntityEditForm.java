package io.threesixty.ui.view;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.StatusChangeListener;
import com.vaadin.data.ValidationException;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Registration;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import io.threesixty.ui.component.panel.PanelBuilder;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

public abstract class AbstractEntityEditForm<T extends Persistable<Serializable>> extends HorizontalLayout {
	private static String[] DEFAULT_NESTED_PROPERTIES = new String[] {};
	
    private TextField idField = new TextField("Id");
	private Binder<T> binder;
//	private LinkedHashSet<DirtyListener> dirtyListeners = new LinkedHashSet<>();
//	private Map<HasValue, Registration> changeListeners = new HashMap<>();
	private boolean layoutCompleted = false;
	private T value;

	public AbstractEntityEditForm(Class<T> beanType) {

		this.binder = new Binder<>(beanType);
		this.binder.forField(idField)
                //.withConverter(new StringToLongConverter("Unable to convert id"))
                .bind("id");

		setSpacing(true);
		setMargin(false);
		setSizeFull();
		Responsive.makeResponsive(this);

		idField.setReadOnly(true);
        idField.setWidth(100.0f, Unit.PERCENTAGE);
	}

	protected TextField getIdField() {
		return idField;
	}

	public Binder<T> getBinder() { return this.binder; }

	/**
	 * Returns the list of nested properties that the form group should bind to. The default
	 * is an empty array.
	 * @return An array of nested properties in Java object notation
	 */
	protected String[] getNestedProperties() { return DEFAULT_NESTED_PROPERTIES; }
	
	public T getValue() {  return this.value;  }

	/**
	 * Binds the newValue entity to the form
	 * @param newValue The entity value to bind
	 */
	public void bind(final T newValue) {
		this.binder.readBean(newValue);
        this.value = newValue;

		updateDependentFields();
		updateFieldConstraints();
		//registerDirtyListener();
	}

	/**
     * Validates the form and returns the status
     * @return the validation status
     */
	public BinderValidationStatus<T> validate() {
		return this.binder.validate();
	}
	
	public boolean isModified() { return this.binder.hasChanges(); }
	
	public boolean isValid() { return this.binder.isValid(); }

	public void commit() throws ValidationException { this.binder.writeBean(this.value); }

    public Registration addStatusChangeListener(final StatusChangeListener listener) {
	    return this.binder.addStatusChangeListener(listener);
    }

//	public void addCommitHandler(final CommitHandler commitHandler) {
//		if (commitHandler != null) {
//			this.binder.addCommitHandler(commitHandler);
//		}
//	}
	
//	public void addDirtyListener(final DirtyListener listener) {
//		if (listener != null) {
//			this.dirtyListeners.add(listener);
//		}
//	}

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
//		this.binder.getFields().forEach(this::registerFieldDirtyListener);
//	}

//	private void registerFieldDirtyListener(HasValue<?> field) {
//	    if (this.changeListeners.containsKey(field)) {
//	        this.changeListeners.remove(field);
//        }
//        this.changeListeners.put(field, field.addValueChangeListener(this::onValueChange));
//    }

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
	
//	private void onValueChange(final HasValue.ValueChangeEvent<?> event) {
//	    fireFormDirty(new FormDirtyEvent(event.getSource(), event.getOldValue(), event.getValue()));
//	}
	
//	private void onTextChange(final HasValue.ValueChangeEvent event) {
//		fireFormDirty(new FormDirtyEvent(event.getSource(), event.getOldValue(), event.getValue()));
//	}
		
//	private void fireFormClean() {
//		fireFormDirty(new FormDirtyEvent(DirtyStatus.CLEAN));
//	}
	
//	private void fireFormDirty(final FormDirtyEvent event) {
//        if (dirtyListeners != null) {
//            for (DirtyListener listener : dirtyListeners) {
//                listener.onDirty(event);
//            }
//        }
//    }
}
