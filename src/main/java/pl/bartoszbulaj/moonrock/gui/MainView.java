package pl.bartoszbulaj.moonrock.gui;

import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.controller.AppConfigurationController;
import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;

@Route
@PageTitle("Hello World")
@Slf4j
public class MainView extends VerticalLayout {

	@Autowired
	private AppConfigurationController appConfigurationController;

	private final AppConfigurationService appConfigurationService;

	private VerticalLayout emailSenderCredentialLayout;
	private Label emailValidationError;
	private TextField emailAddressField;
	private PasswordField passwordField;
	private Checkbox historyAnalyzerCheckbox;
	private Checkbox emailSenderCheckbox;

	public MainView(AppConfigurationService appConfigurationService) {
		this.appConfigurationService = appConfigurationService;

		addHistoryAnalyzerCheckbox();
		addEmailSenderCheckbox();

		addEmailSenderCredentialForm();
		addApiKeysForm();
	}

	private void addApiKeysForm() {
		VerticalLayout apiKeyLayout = new VerticalLayout();
		PasswordField apiKeyNameValue = new PasswordField("Api Key Name", "api key name");
		apiKeyNameValue.setWidthFull();
		PasswordField apiKeySecretValue = new PasswordField("Api Key Secret", "api key secret");
		apiKeySecretValue.setWidthFull();
		Button saveButton = new Button("Save", this::saveApiKeys);

		apiKeyLayout.add(apiKeyNameValue, apiKeySecretValue, saveButton);
		apiKeyLayout.getStyle().set("border", "6px solid black");

		apiKeyLayout.setMaxWidth("50%");
		add(apiKeyLayout);
	}

	private void saveApiKeys(ClickEvent<Button> buttonClickEvent) {
		// TODO call controller to save apiKeys
	}

	private void addEmailSenderCheckbox() {
		emailSenderCheckbox = new Checkbox("Email Sender Credentials");
		emailSenderCheckbox.setEnabled(false);
		emailSenderCheckbox.setValue(appConfigurationService.isAnyEmailSender());
		add(emailSenderCheckbox);
	}

	private void addEmailSenderCredentialForm() {
		emailSenderCredentialLayout = new VerticalLayout();

		Label emailCredentialFormTitle = new Label("Email Sender Credentials");
		emailAddressField = new TextField("Email address", "email address");
		passwordField = new PasswordField("Password", "password");
		Button saveButton = new Button("Save", this::saveEmailCredentials);
		emailValidationError = new Label("Email validation error");
		emailValidationError.setVisible(false);
		saveButton.setWidth(passwordField.getWidth());

		emailSenderCredentialLayout.setMaxWidth("50%");
		emailSenderCredentialLayout.add(emailCredentialFormTitle, emailAddressField, passwordField,
				emailValidationError, saveButton);
		emailSenderCredentialLayout.getStyle().set("border", "6px solid black");
		add(emailSenderCredentialLayout);
	}

	private void addHistoryAnalyzerCheckbox() {
		historyAnalyzerCheckbox = new Checkbox("History Analyzer", event -> {
			historyAnalyzerCheckbox.setValue(event.getValue());
			appConfigurationService.setHistoryAnalyzerEnabled(event.getValue());
		});
		add(historyAnalyzerCheckbox);
		historyAnalyzerCheckbox.setValue(appConfigurationService.isHistoryAnalyzerEnabled());
	}

	private void saveEmailCredentials(ClickEvent<Button> e) {
		EmailSenderDto emailSenderDto = new EmailSenderDto("admin", emailAddressField.getValue(),
				passwordField.getValue());
		try {
			InternetAddress emailAddress = new InternetAddress(emailAddressField.getValue());
			emailAddress.validate();
			appConfigurationController.addEmailSenderCredentials(emailSenderDto);

			emailValidationError.setVisible(false);
			emailSenderCredentialLayout.setEnabled(false);
			emailSenderCheckbox.setValue(true);
			Notification.show("Saved");

		} catch (Exception exception) {
			emailValidationError.setVisible(true);
			exception.printStackTrace();
		}

	}
}
