package io.threesixty.ui.view;

import com.vaadin.data.BinderValidationStatus;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import io.threesixty.ui.component.BlankSupplier;
import io.threesixty.ui.component.EntityPersistFunction;
import io.threesixty.ui.component.EntitySupplier;
import io.threesixty.ui.component.button.ButtonBuilder;
import io.threesixty.ui.component.button.HeaderButtons;
import io.threesixty.ui.component.notification.NotificationBuilder;
import io.threesixty.ui.event.EntityPersistEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@SuppressWarnings("unused")
public abstract class AbstractEntityEditView<T extends Persistable<Serializable>> extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;
	          
    private Button saveButton = ButtonBuilder.SAVE(this::onSave);
	private Button resetButton = ButtonBuilder.RESET(this::onReset);
	private Button createButton = ButtonBuilder.NEW(this::onCreate);	
	
    private Button[] buttons = new Button[] {saveButton, resetButton, createButton};
    private final AbstractEntityEditForm<T> form;
    private transient EntityPersistFunction<T> entityPersistFunction;
    private transient EntitySupplier<T, Serializable> entitySupplier;
    private transient BlankSupplier<T> blankSupplier;
    
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
		this.form.addDirtyListener(this::onDirty);
		this.createButton.setEnabled(enableCreation);
		this.entitySupplier = entitySupplier;
		this.blankSupplier = blankSupplier;
		this.entityPersistFunction = entityPersistFunction;
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
    protected Component getHeaderButtons() {
        return new HeaderButtons(buttons);
 	}
    
	@Override
    public void enter(final ViewChangeEvent event) {
		String[] parameters = event.getParameters().split("/");
		if (parameters.length > 0) {
			form.bind(entitySupplier.get(parameters[0]).orElse(blankSupplier.blank()));
		}
//		build();
		onClean();
    }

    /**
     * Saves the entity using the EntityPersistFunction and binds the result to the form
     * @param event The click event
     */
    @SuppressWarnings("unused")
	private void onSave(final ClickEvent event) {

        //Validate the field group
        BinderValidationStatus<T> status = form.validate();
        if (status.isOk()) {
            //Persist the outcome
            T result = entityPersistFunction.apply(form.getValue());
            //Bind the form to the result
            form.bind(result);
            //Notify the user of the outcome
            NotificationBuilder.showNotification(
                    "Update",
                    result.getId() + " updated successfully.",
                    2000);
            //Notify the system of the outcome
            publishOnEventBus(new EntityPersistEvent<T>(this, result));
            //Set the button status
            onClean();
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
	}
	
	protected void add(ClickEvent event) {
//		if (form.isModified()) {
//			ConfirmDialog.show(
//					UI.getCurrent(), 
//					"Confirmation", 
//					"Would you like to discard you changes?",
//					"Yes",
//					"No",
//			        new ConfirmDialog.Listener() {
//			            public void onClose(ConfirmDialog dialog) {
//			                if (dialog.isConfirmed()) {
//			                    //Discard the field group
//			                	form.discard();
//			                    //DashboardEventBus.post(new ProfileUpdatedEvent());
//			                    //Set a new data source
//			                	form.bindToEmpty();
//			                	onClean();
//			                }
//			            }
//			        });
//		} else {
//			form.discard();
//			form.bindToEmpty();
//			onClean();
//		}
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
//			                    form.discard();
//			                    //DashboardEventBus.post(new ProfileUpdatedEvent());
//			                    onClean();
//			                }
//			            }
//			        });
//		} catch (Exception e) {
//            Notification.show("Error while deleting outcome", Type.ERROR_MESSAGE);
//        }
	}
	
	protected void onReset(ClickEvent event) {
//		if (form.isModified()) {
//			ConfirmDialog.show(
//					UI.getCurrent(), 
//					"Confirmation", 
//					"Would you like to discard you changes?",
//					"Yes",
//					"No",
//			        new ConfirmDialog.Listener() {
//			            public void onClose(ConfirmDialog dialog) {
//			                if (dialog.isConfirmed()) {
//			                	form.discard();
//			                	onClean();
//			                }
//			            }
//			        });
//		} else {
//			form.discard();
//			onClean();
//		}
	}
	
	protected void onCreate(ClickEvent event) {
	}
	
	protected void onDirty(final DirtyEvent event) {
		this.saveButton.setEnabled(event.getStatus() == DirtyStatus.DIRTY);
		this.resetButton.setEnabled(event.getStatus() == DirtyStatus.DIRTY);
	}
	
	protected void onClean() {
		this.saveButton.setEnabled(false);
		this.resetButton.setEnabled(false);
	}

    /**
     * Used by the implementing class to notify the system via an event bus
     */
	protected void publishOnEventBus(final ApplicationEvent event) {

    }

//	protected User getCurrentUser() {
//		return ((MainUI) UI.getCurrent()).getCurrentUser();
//	}

	protected AbstractEntityEditForm<T> getForm() {
		return this.form;
	}
	
	protected Button getSaveButton() { return this.saveButton; }
	protected Button getResetButton() { return this.resetButton; }
	protected Button getCreateButton() { return this.createButton; }
}

