package Kunde;

import java.sql.SQLException;

import daten_verwaltung.AttraktionFX;
import daten_verwaltung.Datenbank;
import daten_verwaltung.KundeFX;
import daten_verwaltung.PackageBestellungenListe;
import daten_verwaltung.PackageBestellungenListeFX;
import daten_verwaltung.PackagesFX;
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


public class BestellungPackageDetailDialog extends Dialog<ButtonType>{
	
private ObservableList<PackagesFX> olPackagesFX = FXCollections.observableArrayList();

private int summe;

public BestellungPackageDetailDialog(KundeFX kundefx) {
	this.setTitle("Packages Bestellung Liste");
	for (PackagesFX bfx : kundefx.getPackages_bestellung()) {
//		PackageBestellungenListe k=new PackageBestellungenListe(0, kundefx.getModelKunde(),bfx.getModellPackage());
		olPackagesFX.add(bfx);
		summe += bfx.getPackagePreis();
	}

	TableColumn<PackagesFX, Integer> packageParkIdCol = new TableColumn<>("Freizeitpark Name");
	packageParkIdCol.setCellValueFactory(new PropertyValueFactory<>("freizeitparkName"));
	packageParkIdCol.setMinWidth(150);

	TableColumn<PackagesFX, String> packageNameCol = new TableColumn<>("Package Name");
	packageNameCol.setCellValueFactory(new PropertyValueFactory<>("packageName"));
	packageNameCol.setMinWidth(150);
	
	TableColumn<PackagesFX, Double> packagePreisCol = new TableColumn<>("Package Preis");
	packagePreisCol.setCellValueFactory(new PropertyValueFactory<>("packagePreis"));
	packagePreisCol.setMinWidth(150);
	
	
	TableView<PackagesFX> tv = new TableView<>(olPackagesFX);
	tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	tv.getColumns().addAll( packageParkIdCol, packageNameCol, packagePreisCol);
	
	Label lbl1 = new Label("Ihre Bestellung Gesamtsumme  ");
	Label lblSum = new Label(Integer.toString(summe));
	Label lbl2 = new Label("Euro");
	Button entfernen = new Button("entfernen");
	entfernen.getStyleClass().add("fancy-button");
	HBox hb = new HBox(10, lbl1, lblSum,lbl2);
	hb.setPadding(new Insets(5));
	
	VBox vb = new VBox(10,tv, hb, entfernen);
	vb.setPadding(new Insets(5));
	this.getDialogPane().setContent(vb);
	
	tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PackagesFX>() {
		@Override
		public void changed(ObservableValue<? extends PackagesFX> arg0, PackagesFX arg1, PackagesFX arg2) {

			if (arg2 != null)
				entfernen.setDisable(false);
			else
				entfernen.setDisable(true);
		}
	});
	entfernen.setOnAction(e -> {
		int i = tv.getSelectionModel().getSelectedIndex();
		olPackagesFX.remove(i);
		kundefx.getBestellung().remove(i);
		summe = 0;
		for (PackagesFX bfx : olPackagesFX)
			summe += bfx.getPackagePreis();
		lblSum.setText(Integer.toString(summe));
	});

	ButtonType bezahlen = new ButtonType("bezahlen", ButtonData.OK_DONE);
	ButtonType beenden = new ButtonType("Beenden", ButtonData.CANCEL_CLOSE);
	this.getDialogPane().getButtonTypes().addAll(bezahlen, beenden);
	this.getDialogPane().getStylesheets().add("Dialog.css");

	this.setResultConverter(new Callback<ButtonType, ButtonType>() {
		@Override
		public ButtonType call(ButtonType arg0) {

			if (arg0 == bezahlen) {
				try {
					
					for (PackagesFX bfx : kundefx.getPackages_bestellung()) {
						PackageBestellungenListe b = new PackageBestellungenListe(0, kundefx.getModelKunde(),bfx.getModellPackage());
						Datenbank.insertPackagesOrders(b);
					}
					kundefx.loeschenBestellungPackages();

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

