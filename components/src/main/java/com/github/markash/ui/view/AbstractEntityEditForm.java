package com.github.markash.ui.view;

import com.vaadin.data.*;
import com.vaadin.event.EventRouter;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;
import org.springframework.data.domain.Persistable;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.Serializable;

public abstract class AbstractEntityEditForm<T extends Persistable<Serializable>> extends HorizontalLayout implements HasValue<T> {

    private TextField idField = new TextField("Id");
    private Binder<T> binder;
    //	private Map<HasValue, Registration> changeListeners = new HashMap<>();
    private boolean layoutCompleted = false;
    private T value;
    private boolean readOnly;
    private boolean requiredIndicatorVisible;
    private EventRouter eventRouter;

    /**
     * Constructs the entity edit form for the data entity and sets that the id is not editable
     * @param beanType The class of the data entity
     */
    @SuppressWarnings("unused")
    public AbstractEntityEditForm(
            final Class<T> beanType) {
        this(beanType, false);
    }

    /**
     * onstructs the entity edit form for the data entity
     * @param beanType The class of the data entity
     * @param idIsEditable Whether the id is editable
     */
    public AbstractEntityEditForm(
            final Class<T> beanType,
            final boolean idIsEditable) {

        this.binder = new Binder<>(beanType);

        if (idIsEditable) {
            this.binder.forField(idField)
                    //.withConverter(new StringToLongConverter("Unable to convert id"))
                    .asRequired("The id field is required.")
                    .bind("id");
        } else {
            this.binder.forField(idField)
                    //.withConverter(new StringToLongConverter("Unable to convert id"))
                    .bind("id");
        }
        this.binder.addStatusChangeListener(this::onBinderStatusChange);

        setSpacing(true);
        setMargin(false);
        setSizeFull();
        Responsive.makeResponsive(this);

        idField.setReadOnly(!idIsEditable);
        idField.setWidth(100.0f, Unit.PERCENTAGE);
    }

    @SuppressWarnings("unused")
    protected TextField getIdField() {
        return idField;
    }

    @SuppressWarnings("unused")
    public Binder<T> getBinder() {
        return this.binder;
    }

    public T getValue() {
        return this.value;
    }

    @Override
    public void setValue(T t) {
        this.binder.readBean(t);
        this.value = t;

        updateDependentFields();
        updateFieldConstraints();
        //registerDirtyListener();
    }

    /**
     * Validates the form and returns the status
     *
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

    public void commit() throws ValidationException {
        this.binder.writeBean(this.value);
    }

    public Registration addStatusChangeListener(final StatusChangeListener listener) {
        return getEventRouter().addListener(StatusChangeEvent.class, listener, StatusChangeListener.class.getDeclaredMethods()[0]);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<T> valueChangeListener) {
        return this.binder.addValueChangeListener(valueChangeListener);
    }

    @Override
    public void setReadOnly(final boolean fieldsReadOnly) {
        this.readOnly = fieldsReadOnly;
        this.binder.setReadOnly(fieldsReadOnly);
    }

    @Override
    public boolean isReadOnly() {
        return this.readOnly;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        this.requiredIndicatorVisible = requiredIndicatorVisible;
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return this.requiredIndicatorVisible;
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
    protected void updateDependentFields() {
    }

    /**
     * Update the field constraints to the new bound value
     */
    private void updateFieldConstraints() {
        idField.setEnabled(getValue().isNew());
    }

    public void layout() {
        if (!layoutCompleted) {
            internalLayout();
            layoutCompleted = true;
        }
    }

    protected void internalLayout() {

        addComponent(new MVerticalLayout()
                        .withSpacing(true)
                        .withMargin(false)
                        .withWidth(100.0f, Unit.PERCENTAGE)
                        .with(idField));
        addComponent(new Label(""));
    }

    private void onBinderStatusChange(final StatusChangeEvent event) {
        getEventRouter().fireEvent(event);
    }

    protected EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }
}
