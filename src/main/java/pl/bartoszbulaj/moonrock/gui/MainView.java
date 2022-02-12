package pl.bartoszbulaj.moonrock.gui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.integration.DatabaseIntegration;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;
import pl.bartoszbulaj.moonrock.service.HistoryService;
import pl.bartoszbulaj.moonrock.service.PositionManagerService;
import pl.bartoszbulaj.moonrock.service.SchedulerService;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorChartAnalyzerService;

import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Route("/")
@PageTitle("Moonrock App")
@Slf4j
public class MainView extends VerticalLayout {

	private final AppConfigurationService appConfigurationService;
	private final DatabaseIntegration databaseIntegration;
	private final PositionManagerService positionManagerService;
	private final SchedulerService schedulerService;
	private final SimulatorChartAnalyzerService simulatorChartAnalyzerService;
	private final CandleMapper candleMapper;
	private final HistoryService historyService;

	private Checkbox historyAnalyzerCheckbox;
	private RadioButtonGroup intervalRadioButtonGroup;
	private Checkbox emailSenderCheckbox;
	private Checkbox apiKeysSavedCheckbox;

	private VerticalLayout emailSenderCredentialLayout;
	private Label emailValidationError;
	private TextField emailAddressField;
	private PasswordField passwordField;

	private VerticalLayout apiKeyLayout;
	private PasswordField apiKeyNameValue;
	private PasswordField apiKeySecretValue;
	private Label apiKeysValidationError;
	private Label savingError;

	private HorizontalLayout buySellButtonsLayout;
	private BigDecimalField quantityField;
	private Button buyButton;
	private Button sellButton;

	private HorizontalLayout simulatorLayout;
	Button showChartButton;
	ComboBox<String> cryptoPairComboBox;

	public MainView(AppConfigurationService appConfigurationService, DatabaseIntegration databaseIntegration,
			PositionManagerService positionManagerService, SchedulerService schedulerService,
			SimulatorChartAnalyzerService simulatorChartAnalyzerService, CandleMapper candleMapper,
			HistoryService historyService) {
		this.appConfigurationService = appConfigurationService;
		this.databaseIntegration = databaseIntegration;
		this.positionManagerService = positionManagerService;
		this.schedulerService = schedulerService;
		this.simulatorChartAnalyzerService = simulatorChartAnalyzerService;
		this.candleMapper = candleMapper;
		this.historyService = historyService;

		addSimulatorLayout();
		addHistoryAnalyzerCheckboxAndIntervalRadioButtons();
		addEmailSenderCheckbox();
		addApiKeysSavedCheckbox();

		addEmailSenderCredentialForm();
		addApiKeysForm();

		addBuySellButtons();

		buySellButtonsLayout.setVisible(appConfigurationService.isApiKeysSaved());
		apiKeyLayout.setVisible(!appConfigurationService.isApiKeysSaved());
	}

	private void addSimulatorLayout() {
		this.cryptoPairComboBox = new ComboBox<>();
		cryptoPairComboBox.setPlaceholder("CryptoPair");
		List<String> instrumentsList = appConfigurationService.getInstrumentsList();
		cryptoPairComboBox.setItems(instrumentsList);
		cryptoPairComboBox.setValue(instrumentsList.get(0));
		cryptoPairComboBox.setAllowCustomValue(false);
		cryptoPairComboBox.addValueChangeListener(listener -> cryptoPairValueChangeListener(listener.getValue()));

		this.showChartButton = new Button("Show Chart", click -> createChart());

		simulatorLayout = new HorizontalLayout();
		simulatorLayout.add(cryptoPairComboBox, showChartButton);
		add(simulatorLayout);
	}

	private void cryptoPairValueChangeListener(String cryptoPair) {
		appConfigurationService.setCryptoPairForSimulator(cryptoPair);
	}

	public void createChart() {
		CandlestickChartFrame candlestickChartFrame = new CandlestickChartFrame(appConfigurationService,
				simulatorChartAnalyzerService, candleMapper, historyService);
		candlestickChartFrame.showChart();
	}

	private void addBuySellButtons() {
		// TODO add condition for enabling if apikeys are saved in database
		buySellButtonsLayout = new HorizontalLayout();
		quantityField = new BigDecimalField();
		quantityField.setPlaceholder("Quantity");
		buyButton = new Button("Buy", click -> buyAction(quantityField.getValue()));
		buyButton.getStyle().set("color", "green");
		sellButton = new Button("Sell", click -> sellAction(quantityField.getValue()));
		sellButton.getStyle().set("color", "red");
		buySellButtonsLayout.add(sellButton, quantityField, buyButton);

		add(buySellButtonsLayout);
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
		savingError = new Label("Saving error");
		savingError.setVisible(false);
		Button saveButton = new Button("Save", this::saveApiKeys);

		apiKeyLayout.add(apiKeyNameValue, apiKeySecretValue, saveButton, apiKeysValidationError);
		apiKeyLayout.getStyle().set("border", "6px solid black");

		apiKeyLayout.setMaxWidth("50%");
		add(apiKeyLayout);
	}

	private void saveApiKeys(ClickEvent<Button> buttonClickEvent) {
		if (validateApiKeysForm()) {
			apiKeysValidationError.setVisible(false);
			// TODO refactor argument "owner"
			boolean saveSuccess = databaseIntegration.saveApiKeys("admin", apiKeyNameValue.getValue(),
					apiKeySecretValue.getValue().getBytes(StandardCharsets.UTF_8));
			if (saveSuccess) {
				apiKeyLayout.setVisible(false);
				apiKeysValidationError.setVisible(false);
				savingError.setVisible(false);

				apiKeysSavedCheckbox.setValue(true);
				buySellButtonsLayout.setVisible(true);
			} else {
				savingError.setVisible(true);
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

	private void addApiKeysSavedCheckbox() {
		apiKeysSavedCheckbox = new Checkbox("Api Keys Saved");
		apiKeysSavedCheckbox.setEnabled(false);
		apiKeysSavedCheckbox.setValue(appConfigurationService.isApiKeysSaved());
		add(apiKeysSavedCheckbox);
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

	private void addHistoryAnalyzerCheckboxAndIntervalRadioButtons() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		historyAnalyzerCheckbox = new Checkbox("History Analyzer", event -> {
			historyAnalyzerCheckbox.setValue(event.getValue());
			appConfigurationService.setHistoryAnalyzerEnabled(event.getValue());
		});
		historyAnalyzerCheckbox.setValue(appConfigurationService.isHistoryAnalyzerEnabled());

		intervalRadioButtonGroup = new RadioButtonGroup<>();
		intervalRadioButtonGroup.setItems("5m", "15m", "1h"); // 4h removed due to problems with calculation
		intervalRadioButtonGroup.setValue(appConfigurationService.getHistoryAnalyzerInterval());

		intervalRadioButtonGroup
				.addValueChangeListener(listener -> intervalRadioChange(listener.getValue().toString()));

		horizontalLayout.add(historyAnalyzerCheckbox, intervalRadioButtonGroup);

		add(horizontalLayout);
	}

	private void intervalRadioChange(String newInterval) {
		appConfigurationService.setHistoryAnalyzerInterval(newInterval);
		schedulerService.deleteTasks();
		schedulerService.configTasks(newInterval);
	}

}
