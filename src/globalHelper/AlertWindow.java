package globalHelper;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

public class AlertWindow {
	private static AlertWindow alert = null;
	private static boolean bool = false;

	private AlertWindow() {

	}
	
	public boolean isBool() {
		return bool;
	}

	

	public static AlertWindow getInstance() {
		if (alert == null)
			alert = new AlertWindow();
		return alert;
	}

	public void showAlert(Alert.AlertType alertType, String contentText, String headerText, String title) {
		
		Alert alert = new Alert(alertType);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setContentText(contentText);
		alert.setHeaderText(headerText);
		alert.setTitle(title);

		if (alertType == Alert.AlertType.CONFIRMATION) {
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				bool = true;
			} else {
				bool = false;
			}
		}else {
			alert.show();
		}
	}
		

}
