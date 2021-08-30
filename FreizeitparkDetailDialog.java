package Mitarbeiter;

import java.sql.SQLException;

import daten_verwaltung.Datenbank;
import daten_verwaltung.FreizeitparkFX;
import daten_verwaltung.PackagesFX;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class FreizeitparkDetailDialog extends Dialog<ButtonType>{
	
	public FreizeitparkDetailDialog(FreizeitparkFX freizeitparkFx) {
		this.setTitle("neuer Freizeitpark anlegen");
		
		Label lblfrId = new Label("Freizeitpark Id");
		lblfrId.setPrefWidth(150);
		Label lblparkId = new Label(Integer.toString(freizeitparkFx.getFreizeitparkId()));
		HBox hbFreizeitparkId = new HBox(10, lblfrId, lblparkId);
		
		Label lblFreizeitparkName = new Label("Freizeitpark Name");
		lblFreizeitparkName.setPrefWidth(150);
		TextField txtFreizeitparkName = new TextField();
		txtFreizeitparkName.setPrefColumnCount(20);
		txtFreizeitparkName.setText(freizeitparkFx.getFreizeitparkName());
		HBox hbFreizeitparkName = new HBox(10, lblFreizeitparkName, txtFreizeitparkName);
		
		Label lblFreizeitparkOrt = new Label("Freizeitpark Ort");
		lblFreizeitparkOrt.setPrefWidth(150);
		TextField txtFreizeitparkOrt = new TextField();
		txtFreizeitparkOrt.setPrefColumnCount(20);
		txtFreizeitparkOrt.setText(freizeitparkFx.getFreizeitparkOrt());
		HBox hbFreizeitparkOrt = new HBox(10, lblFreizeitparkOrt, txtFreizeitparkOrt);
		
		VBox vb = new VBox(10, hbFreizeitparkId, hbFreizeitparkName, hbFreizeitparkOrt);
		this.getDialogPane().setContent(vb);
		ButtonType speichern = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(speichern, abbrechen);
		this.getDialogPane().getStylesheets().add("Dialog.css");

		Button save = (Button) this.getDialogPane().lookupButton(speichern);
		save.addEventFilter(ActionEvent.ACTION, e -> {
			if(txtFreizeitparkName.getText() == null || txtFreizeitparkName.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Freizeitpark Name eingeben").showAndWait();
				e.consume();
				return;
			}
			if(txtFreizeitparkOrt.getText() == null || txtFreizeitparkOrt.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Freizeitpark Ort eingeben").showAndWait();
				e.consume();
				return;
			}
			
		});

		this.setResultConverter(new Callback<ButtonType, ButtonType>(){
			@Override
			public ButtonType call(ButtonType arg0) {
				if(arg0 == speichern) {
					freizeitparkFx.setFreizeitparkName(txtFreizeitparkName.getText());
					freizeitparkFx.setFreizeitparkOrt(txtFreizeitparkOrt.getText());
					
					try {
						if(freizeitparkFx.getFreizeitparkId() == 0)
							Datenbank.insertFreizeitpark(freizeitparkFx.getModellFreizeitpark());
						else
							Datenbank.updateFreizeitpark(freizeitparkFx.getModellFreizeitpark());
					} catch (SQLException e) {
						new Alert(AlertType.ERROR, e.toString()).showAndWait();
					}
				}
				return arg0;
			}
		});
	}

}
