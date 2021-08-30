package Mitarbeiter;

import java.sql.SQLException;

import daten_verwaltung.AttraktionFX;
import daten_verwaltung.Datenbank;
import daten_verwaltung.FreizeitparkFX;
import daten_verwaltung.KundeFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class KundenDetailDialog extends Dialog<ButtonType>{

	
	public KundenDetailDialog(KundeFX kundefx) {
		this.setTitle("Kunden bearbeiten");
		
		Label lblVorname = new Label("Vorname");
		lblVorname.setPrefWidth(150);
		TextField txtVorname = new TextField();
		txtVorname.setPrefColumnCount(20);
		txtVorname.setText(kundefx.getVorname());
		HBox hbVorname = new HBox(10, lblVorname, txtVorname);
		
		Label lblNachname = new Label("Nachname");
		lblNachname.setPrefWidth(150);
		TextField txtNachname = new TextField();
		txtNachname.setPrefColumnCount(20);
		txtNachname.setText(kundefx.getNachname());
		HBox hbNachname = new HBox(10, lblNachname, txtNachname);
		
		Label lblAdresse = new Label("Adresse");
		lblAdresse.setPrefWidth(150);
		TextField txtAdresse = new TextField();
		txtAdresse.setPrefColumnCount(20);
		txtAdresse.setText(kundefx.getAdresse());
		HBox hbAdresse = new HBox(10, lblAdresse, txtAdresse);
		
		Label lblEmail = new Label("Email");
		lblEmail.setPrefWidth(150);
		TextField txtEmail = new TextField();
		txtEmail.setPrefColumnCount(20);
		txtEmail.setText(kundefx.getEmail());
		HBox hbEmail = new HBox(10, lblEmail, txtEmail);
		
		Label lblBenutzername = new Label("Benutzername");
		lblBenutzername.setPrefWidth(150);
		TextField txtBenutzername = new TextField();
		txtBenutzername.setPrefColumnCount(20);
		txtBenutzername.setText(kundefx.getEmail());
		HBox hbBenutzername = new HBox(10, lblBenutzername, txtBenutzername);
		
		Label lblkennwort = new Label("kennwort");
		lblkennwort.setPrefWidth(150);
		TextField txtkennwort = new TextField();
		txtkennwort.setPrefColumnCount(20);
		txtkennwort.setText(kundefx.getNachname());
		HBox hbkennwort = new HBox(10, lblkennwort, txtkennwort);
		
		VBox vb = new VBox(10,hbVorname,hbNachname,hbAdresse,hbEmail,hbBenutzername,hbkennwort);
		this.getDialogPane().setContent(vb);
		ButtonType speichern = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(speichern, abbrechen);
		this.getDialogPane().getStylesheets().add("Dialog.css");

		Button save = (Button) this.getDialogPane().lookupButton(speichern);
		save.addEventFilter(ActionEvent.ACTION, e -> {
			
		});
	
		this.setResultConverter(new Callback<ButtonType, ButtonType>(){
			@Override
			public ButtonType call(ButtonType arg0) {
				if(arg0 == speichern) {
					kundefx.setVorname(txtVorname.getText());
					kundefx.setNachname(txtNachname.getText());
					kundefx.setAdresse(txtAdresse.getText());
					kundefx.setEmail(txtEmail.getText());
					kundefx.setBenutzername(txtBenutzername.getText());
					kundefx.setKennwort(txtkennwort.getText());
					try {
						if(kundefx.getKundeId() == 0)
							Datenbank.insertCustomer(kundefx.getModelKunde());
						else
							Datenbank.updateKunden(kundefx.getModelKunde());
					} catch (SQLException e) {
						new Alert(AlertType.ERROR, e.toString()).showAndWait();
					}
				}
				return arg0;
			}
		});
	}

}

