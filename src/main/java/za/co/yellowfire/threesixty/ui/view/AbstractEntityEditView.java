package za.co.yellowfire.threesixty.ui.view;

import org.springframework.data.domain.Persistable;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.ui.component.BlankSupplier;
import za.co.yellowfire.threesixty.ui.component.EntityConsumer;
import za.co.yellowfire.threesixty.ui.component.EntitySupplier;
import za.co.yellowfire.threesixty.ui.component.button.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.button.HeaderButtons;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm.DirtyEvent;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm.DirtyStatus;

public abstract class AbstractEntityEditView<T extends Persistable<Long>> extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;
	          
    private Button saveButton = ButtonBuilder.SAVE(this::onSave);
	private Button resetButton = ButtonBuilder.RESET(this::onReset);
	private Button createButton = ButtonBuilder.NEW(this::onCreate);	
	
    private Button[] buttons = new Button[] {saveButton, resetButton, createButton};
    private final AbstractEntityEditForm<T> form;
    private transient EntityConsumer<T> entityConsumer;
    private transient EntitySupplier<T, Long> entitySupplier;
    private transient BlankSupplier<T> blankSupplier;
    
    public AbstractEntityEditView(
    		final String viewCaption,
    		final AbstractEntityEditForm<T> form,
    		final EntitySupplier<T, Long> entitySupplier,
    		final BlankSupplier<T> blankSupplier,
    		final EntityConsumer<T> entityConsumer) {
    	this(viewCaption, form, true, entitySupplier, blankSupplier, entityConsumer);
    }
    
    public AbstractEntityEditView( 
    		final String viewCaption,
    		final AbstractEntityEditForm<T> form,
    		final boolean enableCreation,
    		final EntitySupplier<T, Long> entitySupplier,
    		final BlankSupplier<T> blankSupplier,
    		final EntityConsumer<T> entityConsumer) {
		super(viewCaption);
		
		this.form = form;
		this.form.addDirtyListener(this::onDirty);
		this.createButton.setEnabled(enableCreation);
		this.entitySupplier = entitySupplier;
		this.blankSupplier = blankSupplier;
		this.entityConsumer = entityConsumer;
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
			form.bind(entitySupplier.get(Long.parseLong(parameters[0])).orElse(blankSupplier.blank()));
		}
//		build();
		onClean();
    }
	
	protected void onSave(ClickEvent event) {
//		try {
//			//Validate the field group
//	        form.commit();
//	        //Persist the outcome
//	        T result = getService().save(form.getValue(), getCurrentUser());
//	        //Bind the form to the result
//	        form.bind(result);
//	        //Notify the user of the outcome
//	        NotificationBuilder.showNotification("Update", result.getId() + " updated successfully.", 2000);
//	        //DashboardEventBus.post(new ProfileUpdatedEvent());
//	        onClean();
//		} catch (CommitException exception) {
//            Notification.show("Error while updating : " + exception.getMessage(), Type.ERROR_MESSAGE);
//        }
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

