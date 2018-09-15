package com.github.markash.ui.view;

import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.ValidationException;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.component.button.ButtonBuilder;
import com.github.markash.ui.component.field.Toolbar;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.event.EnterEntityEditViewEvent;
import com.github.markash.ui.event.EntityPersistEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.domain.Persistable;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.label.MLabel;

import java.io.Serializable;
import java.util.Optional;

@SuppressWarnings("unused")
public abstract class AbstractEntityEditView<T extends Persistable<Serializable>> extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;
	private static final String NEW_ENTITY_ID = "new-entity";

    private Button saveButton = ButtonBuilder.SAVE(this::onSave);
	private Button resetButton = ButtonBuilder.RESET(this::onReset);
	private Button createButton = ButtonBuilder.NEW(this::onCreate);	

    private final AbstractEntityEditForm<T> form;
    private transient EntityPersistFunction<T> entityPersistFunction;
    private transient EntitySupplier<T, Serializable> entitySupplier;
    private transient BlankSupplier<T> blankSupplier;
    /* The name of the current view when the enter() is triggered */
    private transient String viewName = null;
    /* The id of the entity for the current view when entered() is triggered */
    private transient String entityId = null;

    public AbstractEntityEditView(
    		final String viewCaption,
    		final AbstractEntityEditForm<T> form,
    		final EntitySupplier<T, Serializable> entitySupplier,
    		final BlankSupplier<T> blankSupplier,
    		final EntityPersistFunction<T> entityPersistFunction) {

        this(viewCaption, form, true, entitySupplier, blankSupplier, entityPersistFunction);
    }
    
    public AbstractEntityEditView( 
    		final String viewCaption,
    		final AbstractEntityEditForm<T> form,
    		final boolean enableCreation,
    		final EntitySupplier<T, Serializable> entitySupplier,
    		final BlankSupplier<T> blankSupplier,
    		final EntityPersistFunction<T> entityPersistFunction) {

        super(viewCaption);
		
		this.form = form;
		this.createButton.setEnabled(enableCreation);
		this.entitySupplier = entitySupplier;
		this.blankSupplier = blankSupplier;
		this.entityPersistFunction = entityPersistFunction;

        this.form.addStatusChangeListener(event -> {
            saveButton.setEnabled(this.form.isModified() && this.form.isValid());
            resetButton.setEnabled(this.form.isModified());
        });


        getToolbar().add(saveButton, Toolbar.ToolbarSection.ACTION);
        getToolbar().add(resetButton, Toolbar.ToolbarSection.ACTION);
        getToolbar().add(createButton, Toolbar.ToolbarSection.ACTION);
        getToolbar().add(
                new MLabel("The fields marked with <span class='v-required-field-indicator'>*</span> are required")
                        .withContentMode(ContentMode.HTML),
                Toolbar.ToolbarSection.GUTTER);
	}

	@Override
	protected Component buildContent() {
		
    	VerticalLayout root = new VerticalLayout();
    	root.setSpacing(true);
        root.setMargin(false);
        Responsive.makeResponsive(root);
        
        form.layout();
        root.addComponent(form); 
		return root;
	}

	@Override
    public void enter(
            final ViewChangeEvent event) {

        String[] parameters = event.getParameters().split("/");
		this.viewName = event.getViewName();

		onEnter(
		        parameters.length > 0 ? parameters[0] : null,
                parameters);
    }

    @SuppressWarnings("unchecked")
    private void onEnter(
            final String entityId,
            final String[] parameters) {

        this.entityId = Optional.ofNullable(entityId).orElse(NEW_ENTITY_ID);

        T entity;
        if (NEW_ENTITY_ID.equals(this.entityId)) {
            entity = blankSupplier.blank();
        } else {
            entity = entitySupplier.get(this.entityId).orElse(blankSupplier.blank());
        }

		form.setValue(entity);
        publishOnEventBus(new EnterEntityEditViewEvent(this.viewName, entity));
        onEnter(entity, parameters);
	}

    /**
     * Called to inform sub-classes of the entity and the parameters when entered.
     *
     * By default does nothing.
     *
     * @param entity The entity
     * @param parameters The parameters of the view
     */
	public void onEnter(
	        final T entity,
            final String[] parameters) {

    }

    /**
     * Saves the entity using the EntityPersistFunction and binds the result to the form
     * @param event The click event
     */
    @SuppressWarnings("unused")
	private void onSave(
			final ClickEvent event) {

        try {
            //Validate the field group
            BinderValidationStatus<T> status = form.validate();
            if (status.isOk()) {
                //Persist the outcome
                form.commit();
                T result = entityPersistFunction.apply(form.getValue());
                //Bind the form to the result
                form.setValue(result);
                //Notify the user of the outcome
                NotificationBuilder.showNotification(
                        "Update",
                        successfulPersistNotification(result),
                        2000);
                //Notify the system of the outcome
                publishOnEventBus(EntityPersistEvent.build(this, result));
            } else {
                StringBuilder errors = new StringBuilder();
                status.getFieldValidationErrors()
                        .stream()
                        .map(m -> m.getMessage().orElse(""))
                        .forEach(errors::append);

                NotificationBuilder.showNotification(
                        "Validation",
                        errors.toString(),
                        2000);
            }
        } catch (ValidationException e) {
            Notification.show("Validation error count: " + e.getValidationErrors().size());
        }
	}

	/**
	 * Provide the notification for the entity that was successfully persisted
	 * @param entity The entity that was persisted
	 * @return The notification message
	 */
	protected String successfulPersistNotification(
			final T entity) {

    	return entity.getId() + " updated successfully";
	}

	protected void add(
	        final ClickEvent event) {

		if (form.isModified()) {
			ConfirmDialog.show(
					UI.getCurrent(),
					"Confirmation",
					"Would you like to discard you changes?",
					"Yes",
					"No",
                    (ConfirmDialog.Listener) dialog -> {
                        if (dialog.isConfirmed()) {
                            //Load and enter a new entity id
                            onEnter(NEW_ENTITY_ID, new String[0]);
                        }
                    });
		} else {
            onEnter(NEW_ENTITY_ID, new String[0]);
		}
	}
	
	protected void delete(ClickEvent event) {
//		try {
//			ConfirmDialog.show(
//					UI.getCurrent(),
//					"Confirmation",
//					"Are you sure you would like to delete this rating question?",
//					"Yes",
//					"No",
//			        new ConfirmDialog.Listener() {
//			            public void onClose(ConfirmDialog dialog) {
//			                if (dialog.isConfirmed()) {
//			                	//Delete the entity
//			                    getService().delete(form.getValue(), getCurrentUser());
//			                    //Notify the user of the outcome
//			                    NotificationBuilder.showNotification("Update", "Rating question updated successfully", 2000);
//			                    //Discard the field group
//			                    onEnter(NEW_ENTITY_ID);
//			                }
//			            }
//			        });
//		} catch (Exception e) {
//            Notification.show("Error while deleting outcome", Notification.Type.ERROR_MESSAGE);
//        }
	}
	
	private void onReset(ClickEvent event) {
		if (form.isModified()) {
			ConfirmDialog.show(
					UI.getCurrent(),
					"Confirmation",
					"Would you like to discard you changes?",
					"Yes",
					"No",
					(ConfirmDialog.Listener) dialog -> {
                        if (dialog.isConfirmed()) {
                            onEnter(this.entityId, new String[0]);
                        }
                    });
		} else {
			onEnter(this.entityId, new String[0]);
		}
	}
	
	private void onCreate(ClickEvent event) {
        UI.getCurrent().getNavigator().navigateTo(encodeNavigationState(this.viewName, NEW_ENTITY_ID));
	}

    /**
     * Used by the implementing class to notify the system via an event bus
	 * @param event The event to publish
     */
	protected void publishOnEventBus(final ApplicationEvent event) {

    }

	protected AbstractEntityEditForm<T> getForm() {
		return this.form;
	}
	
	protected Button getSaveButton() { return this.saveButton; }
	protected Button getResetButton() { return this.resetButton; }
	protected Button getCreateButton() { return this.createButton; }

    /**
     * Encodes the navigation state for the view and id for the entity views
     * @param viewName The view name
     * @param id The id of the entity
     * @return The navigation state
     */
    private static String encodeNavigationState(final String viewName, final String id) {
	    return viewName + (StringUtils.isBlank(id) ? "" : "/" + id);
	}
}

