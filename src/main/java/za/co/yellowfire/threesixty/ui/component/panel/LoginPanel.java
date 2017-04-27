package za.co.yellowfire.threesixty.ui.component.panel;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import za.co.yellowfire.threesixty.ui.component.notification.NotificationBuilder;


/**
 * @author Mark P Ashworth
 */
public class LoginPanel extends VerticalLayout {
    private static final long serialVersionUID = 1L;

    private final TextField usernameField = new TextField("Username");
    private final PasswordField passwordField = new PasswordField("Password");
    private final Button loginButton = new Button("Sign In");

    public LoginPanel(final Notification welcomeMessage, final ClickListener loginClickListener) {
        setSizeFull();
        this.loginButton.addClickListener(loginClickListener);

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

//        if (welcomeMessage != null) {
//            welcomeMessage.show(Page.getCurrent());
//        }
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        //loginPanel.addComponent(new CheckBox("Remember me", true));
        return loginPanel;
    }

    @SuppressWarnings("serial")
    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");


        usernameField.setIcon(VaadinIcons.USER);
        usernameField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        usernameField.setDescription("The users are <br />"
                + "<ul>"
                + "<li><strong>admin</strong> with password <strong>password</strong>"
                + "<li><strong>katie</strong> with password <strong>password</strong>"
                + "<li><strong>marmite</strong> with password <strong>password</strong>"
                + "<li><strong>brown</strong> with password <strong>password</strong>"
                + "<li><strong>linus</strong> with password <strong>password</strong>"
                + "<li><strong>lucy</strong> with password <strong>password</strong>"
                + "</ul>");

        passwordField.setIcon(VaadinIcons.LOCK);
        passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.focus();

        fields.addComponents(usernameField, passwordField, loginButton);
        fields.setComponentAlignment(loginButton, Alignment.BOTTOM_LEFT);
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("Three <strong>Sixty</strong>", ContentMode.HTML);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

    public String getUserName() {
        return this.usernameField.getValue();
    }

    public String getPassword() {
        return this.passwordField.getValue();
    }

    public String getPasswordAndReset() {
        String value = this.passwordField.getValue();
        this.passwordField.setValue("");
        return value;
    }

    public void enableLoginButton() {
        this.loginButton.setEnabled(true);
    }

    public void disableLoginButton() {
        this.loginButton.setEnabled(false);
    }

    public void onAuthenticationFailure(final Throwable ex) {
        this.usernameField.focus();
        this.usernameField.selectAll();
        NotificationBuilder.showNotification("Unable to authenticate", (ex != null ? ex.getMessage() : "The user name and password provided is incorrect."), 20000);
    }

    public void onUnexpectedFailure(final Throwable ex) {
        NotificationBuilder.showNotification("Unexpected error occurred", (ex != null ? ex.getMessage() : "An expected error or null pointer exception."), 20000);
    }
}



