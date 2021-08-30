package Kunde;

import java.sql.SQLException;
import java.util.ArrayList;
import daten_verwaltung.Attraktion;
import daten_verwaltung.AttraktionFX;

import daten_verwaltung.AttraktionBestellungenListe;
import daten_verwaltung.AttraktionBestellungenListeFX;
import daten_verwaltung.Datenbank;
import daten_verwaltung.KundeFX;
import daten_verwaltung.PackageBestellungenListe;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class BestellungAttraktionDetailDialog extends Dialog<ButtonType>{
	
	private ObservableList<AttraktionFX> olAttraktionFX = FXCollections.observableArrayList();
	private int summe;

	public BestellungAttraktionDetailDialog(KundeFX kundefx) {
		this.setTitle("Details der Bestellung");
		for (AttraktionFX bfx : kundefx.getBestellung()) {
//			AttraktionBestellungenListe k=new AttraktionBestellungenListe(0, kundefx.getModelKunde(),bfx.getModellAtrraktion());
			olAttraktionFX.add(bfx);
			summe += bfx.getAttraktionTicketPreis();
		}
		
		TableColumn<AttraktionFX, String> attraktionParkIdCol = new TableColumn<>("Freizeitpark Name");
		attraktionParkIdCol.setCellValueFactory(new PropertyValueFactory<>("freizeitparkName"));
		attraktionParkIdCol.setMinWidth(150);

		TableColumn<AttraktionFX, String> attraktionCol = new TableColumn<>("Attraktion");
		attraktionCol.setCellValueFactory(new PropertyValueFactory<>("attraktionName"));
		attraktionCol.setMinWidth(150);
		TableColumn<AttraktionFX, String> attraktionThemaCol = new TableColumn<>("Attraktion Thema");
		attraktionThemaCol.setCellValueFactory(new PropertyValueFactory<>("attraktionThema"));
		attraktionThemaCol.setMinWidth(150);

		TableColumn<AttraktionFX, String> attraktionTicketPreisCol = new TableColumn<>("Attraktion Ticket Preis");
		attraktionTicketPreisCol.setCellValueFactory(new PropertyValueFactory<>("attraktionTicketPreis"));
		attraktionTicketPreisCol.setMinWidth(150);
		
				TableView<AttraktionFX> tv = new TableView<>(olAttraktionFX);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.getColumns().addAll( attraktionParkIdCol, attraktionCol, attraktionThemaCol,
				attraktionTicketPreisCol);
		
		Label lbl1 = new Label("Bestellung Gesamtsumme  ");
		Label lblSum = new Label(Integer.toString(summe));
		Label lbl2 = new Label("Euro");
		Button Entfernen = new Button("Entfernen");
		HBox hb = new HBox(10, lbl1, lblSum,lbl2);
		hb.setPadding(new Insets(5));
		VBox vb = new VBox(10,tv, hb, Entfernen);
		vb.setPadding(new Insets(5));
		this.getDialogPane().setContent(vb);
		this.getDialogPane().getStylesheets().add("Dialog.css");

		tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AttraktionFX>() {

			@Override
			public void changed(ObservableValue<? extends AttraktionFX> arg0, AttraktionFX arg1, AttraktionFX arg2) {

				if (arg2 != null)
					Entfernen.setDisable(false);
				else
					Entfernen.setDisable(true);
			}

		});
		Entfernen.getStyleClass().add("fancy-button");

		Entfernen.setOnAction(e -> {
			int i = tv.getSelectionModel().getSelectedIndex();
			olAttraktionFX.remove(i);
			kundefx.getBestellung().remove(i);
			summe = 0;
			for (AttraktionFX bfx : olAttraktionFX)
				summe += bfx.getAttraktionTicketPreis();
			lblSum.setText(Integer.toString(summe));
		});

		ButtonType bezahlen = new ButtonType("Bezahlen", ButtonData.OK_DONE);
		ButtonType beenden = new ButtonType("Beenden", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(bezahlen, beenden);
		
		this.setResultConverter(new Callback<ButtonType, ButtonType>() {
			@Override
			public ButtonType call(ButtonType arg0) {

				if (arg0 == bezahlen) {
					try {
						for (AttraktionFX bfx : kundefx.getBestellung()) {
							AttraktionBestellungenListe b = new AttraktionBestellungenListe(0, kundefx.getModelKunde(), bfx.getModellAtrraktion());
							Datenbank.insertAttraktionenOrders(b);
						}
						kundefx.loeschenBestellungAttraktion();
						
						new Alert(Alert.AlertType.INFORMATION, "Vom konto werden " + summe + "Euro abgebucht")
								.showAndWait();
					} catch (SQLException e) {
						e.printStackTrace();
						new Alert(AlertType.ERROR, e.toString()).showAndWait();
						return arg0;
					}
				}
				return arg0;
			}

		});
	}

}
