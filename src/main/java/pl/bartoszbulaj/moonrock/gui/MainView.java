package pl.bartoszbulaj.moonrock.gui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.integration.DatabaseIntegration;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;
import pl.bartoszbulaj.moonrock.service.PositionManagerService;

import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Route("/")
@PageTitle("Moonrock App")
@Slf4j
public class MainView extends VerticalLayout {
	private final AppConfigurationService appConfigurationService;
	private final DatabaseIntegration databaseIntegration;
	private final PositionManagerService positionManagerService;

	private Checkbox historyAnalyzerCheckbox;
	private Checkbox emailSenderCheckbox;

	private VerticalLayout emailSenderCredentialLayout;
	private Label emailValidationError;
	private TextField emailAddressField;
	private PasswordField passwordField;

	private VerticalLayout apiKeyLayout;
	private PasswordField apiKeyNameValue;
	private PasswordField apiKeySecretValue;
	private Label apiKeysValidationError;

	private HorizontalLayout buySellLayout;
	private BigDecimalField quantityField;
	private Button buyButton;
	private Button sellButton;

	public MainView(AppConfigurationService appConfigurationService, DatabaseIntegration databaseIntegration,
			PositionManagerService positionManagerService) {
		this.appConfigurationService = appConfigurationService;
		this.databaseIntegration = databaseIntegration;
		this.positionManagerService = positionManagerService;

		addHistoryAnalyzerCheckbox();
		addEmailSenderCheckbox();

		addEmailSenderCredentialForm();
		addApiKeysForm();

		addBuySellButtons();

	}

	private void addBuySellButtons() {
		// TODO add condition for enabling if apikeys are saved in database
		buySellLayout = new HorizontalLayout();
		quantityField = new BigDecimalField();
		quantityField.setPlaceholder("Quantity");
		buyButton = new Button("Buy", click -> buyAction(quantityField.getValue()));
		buyButton.getStyle().set("color", "green");
		sellButton = new Button("Sell", click -> sellAction(quantityField.getValue()));
		sellButton.getStyle().set("color", "red");
		buySellLayout.add(sellButton, quantityField, buyButton);
		add(buySellLayout);
	}

	private boolean buyAction(BigDecimal quantity) {
		try {
			positionManagerService.buyMarket("admin", "XTBUSD", quantity);
			quantityField.clear();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean sellAction(BigDecimal quantity) {
		try {
			positionManagerService.sellMarket("admin", "XTBUSD", quantity);
			quantityField.clear();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void addApiKeysForm() {
		apiKeyLayout = new VerticalLayout();
		apiKeyNameValue = new PasswordField("Api Key Name", "api key name");
		apiKeyNameValue.setWidthFull();
		apiKeySecretValue = new PasswordField("Api Key Secret", "api key secret");
		apiKeySecretValue.setWidthFull();
		apiKeysValidationError = new Label("ApiKeys validation error");
		apiKeysValidationError.setVisible(false);
		Button saveButton = new Button("Save", this::saveApiKeys);

		apiKeyLayout.add(apiKeyNameValue, apiKeySecretValue, saveButton, apiKeysValidationError);
		apiKeyLayout.getStyle().set("border", "6px solid black");

		apiKeyLayout.setMaxWidth("50%");
		add(apiKeyLayout);
	}

	private void saveApiKeys(ClickEvent<Button> buttonClickEvent) {
		if (validateApiKeysForm()) {
			// TODO refactor argument "owner"
			boolean success = databaseIntegration.saveApiKeys("admin", apiKeyNameValue.getValue(),
					apiKeySecretValue.getValue().getBytes(StandardCharsets.UTF_8));
			if (success) {
				apiKeyLayout.setVisible(false);
				apiKeysValidationError.setVisible(false);
			} else {
				apiKeysValidationError.setVisible(true);
			}
		} else {
			apiKeysValidationError.setVisible(true);
		}
	}

	private boolean validateApiKeysForm() {
		return apiKeyNameValue.getValue().length() == 24 && apiKeySecretValue.getValue().length() == 48;
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
		Button saveEmailCredentialsButton = new Button("Save", this::saveEmailCredentials);
		emailValidationError = new Label("Email validation error");
		emailValidationError.setVisible(false);

		emailSenderCredentialLayout.setMaxWidth("50%");
		emailSenderCredentialLayout.add(emailCredentialFormTitle, emailAddressField, passwordField,
				emailValidationError, saveEmailCredentialsButton);
		emailSenderCredentialLayout.getStyle().set("border", "6px solid black");
		add(emailSenderCredentialLayout);
	}

	private void saveEmailCredentials(ClickEvent<Button> e) {
		EmailSenderDto emailSenderDto = new EmailSenderDto("admin", emailAddressField.getValue(),
				passwordField.getValue());
		try {
			InternetAddress emailAddress = new InternetAddress(emailAddressField.getValue());
			emailAddress.validate();
			databaseIntegration.saveEmailSenderCredentials(emailSenderDto);

			emailValidationError.setVisible(false);
			emailSenderCredentialLayout.setEnabled(false);
			emailSenderCheckbox.setValue(true);
			emailSenderCredentialLayout.setVisible(false);
			Notification.show("Saved");

		} catch (Exception exception) {
			emailValidationError.setVisible(true);
			exception.printStackTrace();
		}

	}

	private void addHistoryAnalyzerCheckbox() {
		historyAnalyzerCheckbox = new Checkbox("History Analyzer", event -> {
			historyAnalyzerCheckbox.setValue(event.getValue());
			appConfigurationService.setHistoryAnalyzerEnabled(event.getValue());
		});
		add(historyAnalyzerCheckbox);
		historyAnalyzerCheckbox.setValue(appConfigurationService.isHistoryAnalyzerEnabled());
	}

}
