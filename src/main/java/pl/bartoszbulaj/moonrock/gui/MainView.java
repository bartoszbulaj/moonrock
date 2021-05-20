package pl.bartoszbulaj.moonrock.gui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import pl.bartoszbulaj.moonrock.controller.AppConfigurationController;
import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;

@Route
public class MainView extends VerticalLayout {

	@Autowired
	private AppConfigurationController appConfigurationController;

	private VerticalLayout emailSenderCredentialLayout;
	private Label emailCredentialFormTitle;
	private TextField emailAddressField;
	private PasswordField passwordField;
	private Button saveButton;

	public MainView() {
		emailSenderCredentialLayout = new VerticalLayout();
		emailCredentialFormTitle = new Label("Email Sender Credentials");
		emailAddressField = new TextField("Email address", "email address");
		passwordField = new PasswordField("Password", "password");
		saveButton = new Button("Save", this::saveEmailCredentials);
		saveButton.setWidth(passwordField.getWidth());
		emailSenderCredentialLayout.add(emailCredentialFormTitle, emailAddressField, passwordField, saveButton);

		add(emailSenderCredentialLayout);
	}

	private void saveEmailCredentials(ClickEvent<Button> e) {
		EmailSenderDto emailSenderDto = new EmailSenderDto("admin", emailAddressField.getValue(),
				passwordField.getValue());
		appConfigurationController.addEmailSenderCredentials(emailSenderDto);
		Notification.show("Saved");
		emailSenderCredentialLayout.setEnabled(false);
	}
}
