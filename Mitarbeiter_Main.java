package Mitarbeiter;

import java.io.File;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import daten_verwaltung.Datenbank;
import daten_verwaltung.FreizeitparkFX;
import daten_verwaltung.Kunde;
import daten_verwaltung.KundeFX;
import daten_verwaltung.PackageBestellungenListe;
import daten_verwaltung.PackageBestellungenListeFX;
import daten_verwaltung.Freizeitpark;
import daten_verwaltung.Packages;
import daten_verwaltung.PackagesFX;
import daten_verwaltung.Attraktion;
import daten_verwaltung.AttraktionBestellungenListe;
import daten_verwaltung.AttraktionFX;
import daten_verwaltung.AttraktionBestellungenListeFX;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Mitarbeiter_Main extends Application {
	private ObservableList<AttraktionFX> olAttraktionFX = FXCollections.observableArrayList();
	private ObservableList<PackagesFX> olPackageFX = FXCollections.observableArrayList();
	private ObservableList<FreizeitparkFX> olFreizeitparkFX = FXCollections.observableArrayList();
	private ObservableList<KundeFX> olKundeFX = FXCollections.observableArrayList();
	private ObservableList<PackageBestellungenListeFX> olPackageBestellListeFX = FXCollections.observableArrayList();
	private ObservableList<AttraktionBestellungenListeFX> olBestellungfx = FXCollections.observableArrayList();
Stage window;
	@Override
	public void start(Stage primaryStage) {
		window=primaryStage;
		
		try {
			Datenbank.createFreizeitpark();
			Datenbank.createAttraktion();
			Datenbank.createPackage();
			Datenbank.createCustomers();
			Datenbank.createAttraktionenOrders();
			Datenbank.createPackagesOrders();
			

		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
		Accordion acc = new Accordion(Freizeitpark(),Attraktion(), Package(),Kunden(),Attraktion_Bestellung(),Package_Bestellung());
	
		Scene scene=new Scene(acc);
		primaryStage.setScene(scene);
		scene.getStylesheets().add("Mitarbeiter.css");
		primaryStage.setTitle("Datenbank Mitarbeiter");
		Image primaryStageicon=new Image(getClass().getResourceAsStream("Austria.png"));
		primaryStage.getIcons().add(primaryStageicon);
		primaryStage.setMaximized(true);
		window.setWidth(primaryStage.getWidth());
		window.setHeight(primaryStage.getHeight());
		primaryStage.show();
		
	}
	/*
	 * Freizeitpark anlegen bsw.bearbeiten
	 */
	private TitledPane Freizeitpark() {
		TableColumn<FreizeitparkFX, Integer> FreizeitparkIdCol = new TableColumn<>("Freizeitpark Id");
		FreizeitparkIdCol.setCellValueFactory(new PropertyValueFactory<>("freizeitparkId"));
		FreizeitparkIdCol.setMinWidth(100);
		TableColumn<FreizeitparkFX, String> freizeitparkNameCol = new TableColumn<>("Freizeitpark Name");
		freizeitparkNameCol.setCellValueFactory(new PropertyValueFactory<>("freizeitparkName"));
		freizeitparkNameCol.setMinWidth(200);
		TableColumn<FreizeitparkFX, String> freizeitparkOrtCol = new TableColumn<>("Freizeitpark Ort");
		freizeitparkOrtCol.setCellValueFactory(new PropertyValueFactory<>("freizeitparkOrt"));
		freizeitparkOrtCol.setMinWidth(200);
		TableView<FreizeitparkFX> tv = new TableView<>(olFreizeitparkFX);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.getColumns().addAll(FreizeitparkIdCol, freizeitparkNameCol, freizeitparkOrtCol);
		Button neu = new Button("Neuer Freizeitpark");
		neu.getStyleClass().add("fancy-button");
		Button bearbeiten = new Button("bearbeiten");
		bearbeiten.getStyleClass().add("fancy-button");
		bearbeiten.setDisable(true);
		HBox hb = new HBox(10, neu, bearbeiten);
		hb.setPadding(new Insets(5));

		leseFreizeitpark();
		tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FreizeitparkFX>() {
			@Override
			public void changed(ObservableValue<? extends FreizeitparkFX> arg0, FreizeitparkFX arg1, FreizeitparkFX arg2) {

				if (arg2 != null) {
					bearbeiten.setDisable(false);
				} else {
					bearbeiten.setDisable(true);
				}
			}
		});
		neu.setOnAction(e -> {
			FreizeitparkFX freizeitparkFX = new FreizeitparkFX(new Freizeitpark(0, "", ""));
			Optional<ButtonType> r = new FreizeitparkDetailDialog(freizeitparkFX).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				leseFreizeitpark();
		});
		bearbeiten.setOnAction(e -> {
			Optional<ButtonType> r = new FreizeitparkDetailDialog(tv.getSelectionModel().getSelectedItem()).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				leseFreizeitpark();
		});

		VBox vb = new VBox(10, tv, hb);
		vb.setPadding(new Insets(5));
		return new TitledPane("Freizeitpark", vb);
		
	}
	/*
	 * Attraktion anlegen bsw. bearbeiten
	 */
	private TitledPane Attraktion() {
		TableColumn<AttraktionFX, Integer> attraktionIdCol = new TableColumn<>("Attraktion Id");
		attraktionIdCol.setCellValueFactory(new PropertyValueFactory<>("attraktionId"));
		attraktionIdCol.setMinWidth(30);
		TableColumn<AttraktionFX, String> attraktionParkIdCol = new TableColumn<>("Freizeitpark Name");
		attraktionParkIdCol.setCellValueFactory(new PropertyValueFactory<>("freizeitparkName"));
		attraktionParkIdCol.setMinWidth(150);

		TableColumn<AttraktionFX, String> attraktionCol = new TableColumn<>("Attraktion");
		attraktionCol.setCellValueFactory(new PropertyValueFactory<>("attraktionName"));
		attraktionCol.setMinWidth(130);
		TableColumn<AttraktionFX, String> attraktionThemaCol = new TableColumn<>("Attraktion Thema");
		attraktionThemaCol.setCellValueFactory(new PropertyValueFactory<>("attraktionThema"));
		attraktionThemaCol.setMinWidth(150);

		TableColumn<AttraktionFX, String> attraktionTicketPreisCol = new TableColumn<>("Attraktion Ticket Preis");
		attraktionTicketPreisCol.setCellValueFactory(new PropertyValueFactory<>("attraktionTicketPreis"));
		attraktionTicketPreisCol.setMinWidth(150);
		TableColumn<AttraktionFX, String> attraktionBildCol = new TableColumn<>("Attraktion Bild");
		attraktionBildCol.setCellValueFactory(new PropertyValueFactory<>("attraktionBild"));
		attraktionBildCol.setMinWidth(150);

		TableColumn<AttraktionFX, String> attraktionAnmerkungCol = new TableColumn<>("Attraktion Anmerkung");
		attraktionAnmerkungCol.setCellValueFactory(new PropertyValueFactory<>("attraktionAnmerkung"));
		attraktionAnmerkungCol.setMinWidth(150);
		TableColumn<AttraktionFX, Boolean> attraktionStatusCol = new TableColumn<>("Attraktion Status");
		attraktionStatusCol.setCellValueFactory(new PropertyValueFactory<>("attraktionStatus"));
		attraktionStatusCol.setMinWidth(130);
		TableColumn<AttraktionFX, Boolean> onlineTicketStatusCol = new TableColumn<>("Online Ticket Status");
		onlineTicketStatusCol.setCellValueFactory(new PropertyValueFactory<>("onlineTicketStatus"));
		onlineTicketStatusCol.setMinWidth(150);

				attraktionBildCol.setCellFactory(new Callback<TableColumn<AttraktionFX, String>, TableCell<AttraktionFX, String>>() {
			@Override
			public TableCell<AttraktionFX, String> call(TableColumn<AttraktionFX, String> arg0) {
				return new CoverCellImplementation();
			}
		});
		TableView<AttraktionFX> tv = new TableView<>(olAttraktionFX);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.getColumns().addAll(attraktionIdCol, attraktionParkIdCol, attraktionCol, attraktionThemaCol,
				attraktionTicketPreisCol, attraktionBildCol,attraktionAnmerkungCol,attraktionStatusCol
				,onlineTicketStatusCol);
		Button neu = new Button("Neue Attraktion");
		neu.getStyleClass().add("fancy-button");
		Button bearbeiten = new Button("bearbeiten");
		bearbeiten.getStyleClass().add("fancy-button");
		bearbeiten.setDisable(true);

		HBox hb = new HBox(10, neu, bearbeiten);
		hb.setPadding(new Insets(5));
		
		leseAttraktion(0,Optional.empty());
		tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AttraktionFX>() {
			@Override
			public void changed(ObservableValue<? extends AttraktionFX> arg0, AttraktionFX arg1, AttraktionFX arg2) {

				if (arg2 != null) {
					bearbeiten.setDisable(false);
				} else {
					bearbeiten.setDisable(true);
				}
			}
		});
		neu.setOnAction(e -> {
			AttraktionFX attraktionFX = new AttraktionFX(new Attraktion(0,new Freizeitpark (0,"",""),
					"", "", 0, "", "",true,true));
			Optional<ButtonType> r = new AttraktionDetailDialog(attraktionFX).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)			
			leseAttraktion(0,Optional.empty());
		});
		bearbeiten.setOnAction(e -> {
			Optional<ButtonType> r = new AttraktionDetailDialog(tv.getSelectionModel().getSelectedItem()).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
			leseAttraktion(0,Optional.empty());
		});

		VBox vb = new VBox(10, tv, hb);
		vb.setPadding(new Insets(5));
		return new TitledPane("Attraktion", vb);
	}
	/*
	 * Packages anlegen bsw. bearbeiten
	 */

	private TitledPane Package() {
		TableColumn<PackagesFX, Integer> packageIdCol = new TableColumn<>("PackageId");
		packageIdCol.setCellValueFactory(new PropertyValueFactory<>("packageId"));
		packageIdCol.setMinWidth(30);
		TableColumn<PackagesFX, Integer> packageParkIdCol = new TableColumn<>("Freizeitpark Name");
		packageParkIdCol.setCellValueFactory(new PropertyValueFactory<>("freizeitparkName"));
		packageParkIdCol.setMinWidth(150);

		TableColumn<PackagesFX, String> packageNameCol = new TableColumn<>("Package Name");
		packageNameCol.setCellValueFactory(new PropertyValueFactory<>("packageName"));
		packageNameCol.setMinWidth(150);
		TableColumn<PackagesFX, String> packageBildCol = new TableColumn<>("Package Bild");
		packageBildCol.setCellValueFactory(new PropertyValueFactory<>("packageBild"));
		packageBildCol.setMinWidth(150);
		TableColumn<PackagesFX, Double> packagePreisCol = new TableColumn<>("Package Preis");
		packagePreisCol.setCellValueFactory(new PropertyValueFactory<>("packagePreis"));
		packagePreisCol.setMinWidth(150);
		TableColumn<PackagesFX, Boolean> packageStatusCol = new TableColumn<>("Package Status");
		packageStatusCol.setCellValueFactory(new PropertyValueFactory<>("packageStatus"));
		packageStatusCol.setMinWidth(150);

		packageBildCol.setCellFactory(new Callback<TableColumn<PackagesFX, String>, TableCell<PackagesFX, String>>() {
			@Override
			public TableCell<PackagesFX, String> call(TableColumn<PackagesFX, String> arg0) {
				return new CoverCellImplementationPackage();
			}
		});

		TableView<PackagesFX> tv = new TableView<>(olPackageFX);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.getColumns().addAll(packageIdCol, packageParkIdCol, packageNameCol, packageBildCol, packagePreisCol,packageStatusCol);
		Button neu = new Button("Neues Package");
		neu.getStyleClass().add("fancy-button");
		Button bearbeiten = new Button("bearbeiten");
		bearbeiten.getStyleClass().add("fancy-button");
		bearbeiten.setDisable(true);

		HBox hb = new HBox(10, neu, bearbeiten);
		hb.setPadding(new Insets(5));

		lesePackage(0);
		tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PackagesFX>() {
			@Override
			public void changed(ObservableValue<? extends PackagesFX> arg0, PackagesFX arg1, PackagesFX arg2) {

				if (arg2 != null) {
					bearbeiten.setDisable(false);
				} else {
					bearbeiten.setDisable(true);
				}
			}
		});
		neu.setOnAction(e -> {
			PackagesFX packageFX = new PackagesFX(new Packages(0,new Freizeitpark(0,"","") ,
					"", "", 0,true));
			Optional<ButtonType> r = new PackageDetailDialog(packageFX).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				lesePackage(0);
		});
		bearbeiten.setOnAction(e -> {
			Optional<ButtonType> r = new PackageDetailDialog(tv.getSelectionModel().getSelectedItem()).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				lesePackage(0);
		});

		VBox vb = new VBox(10, tv, hb);
		vb.setPadding(new Insets(5));
		return new TitledPane("Packages", vb);
	}
	/*
	 *Mitarbeiter können Kundendaten ansehen bsw. Bearbeiten(Nach
	*der Registrierung in der Kundenoberfläche werden die Kundendaten in der Datenbank
	*gespeichert. Mitarbeiter haben Zugriff auf Daten).
	 */
	private TitledPane Kunden() {
		TableColumn<KundeFX, Integer> kundeIdCol = new TableColumn<>("Kunde Id");
		kundeIdCol.setCellValueFactory(new PropertyValueFactory<>("kundeId"));
		kundeIdCol.setMinWidth(80);
		
		TableColumn<KundeFX, String> vornameCol = new TableColumn<>("Vorname");
		vornameCol.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		vornameCol.setMinWidth(150);
		TableColumn<KundeFX, String> nachnameCol = new TableColumn<>("Nachname");
		nachnameCol.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		nachnameCol.setMinWidth(150);
		TableColumn<KundeFX, String> adresseCol = new TableColumn<>("Adresse");
		adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
		adresseCol.setMinWidth(150);
		TableColumn<KundeFX, String> emailCol = new TableColumn<>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		emailCol.setMinWidth(150);
		TableColumn<KundeFX, String> benutzernameCol = new TableColumn<>("Benutzername");
		benutzernameCol.setCellValueFactory(new PropertyValueFactory<>("benutzername"));
		benutzernameCol.setMinWidth(150);
		TableColumn<KundeFX, String> kennwortCol = new TableColumn<>("kennwort");
		kennwortCol.setCellValueFactory(new PropertyValueFactory<>("kennwort"));
		kennwortCol.setMinWidth(150);
		
		TableView<KundeFX> tv = new TableView<>(olKundeFX);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.getColumns().addAll(kundeIdCol,vornameCol, nachnameCol,adresseCol,emailCol,benutzernameCol,kennwortCol);
		Button bearbeiten = new Button("bearbeiten");
		bearbeiten.getStyleClass().add("fancy-button");
		
		leseKunde();
		tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KundeFX>() {
			@Override
			public void changed(ObservableValue<? extends KundeFX> arg0, KundeFX arg1, KundeFX arg2) {

				if (arg2 != null) {
					bearbeiten.setDisable(false);
					
				} else {
					bearbeiten.setDisable(true);

				}	

			}
					
		});
		bearbeiten.setOnAction(e -> {
			Optional<ButtonType> r = new KundenDetailDialog(tv.getSelectionModel().getSelectedItem()).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				lesePackage(0);
		});
		
		VBox vb = new VBox(10, tv,bearbeiten);
		vb.setPadding(new Insets(5));
		return new TitledPane("Kunden Liste", vb);
	}
	/*
	*Mitarbeiter können Liste der gebuchten Attraktionen ansehen(Nach
		*	erfolgter Buchung der Attraktionen in der Kundenoberfläche werden die Daten in der
		*	Datenbank gespeichert. Mitarbeiter haben Zugriff auf Daten).
		* */
		private TitledPane Attraktion_Bestellung() {
		TableColumn<AttraktionBestellungenListeFX, Integer> BestellungIdCol = new TableColumn<>("BestellungId");
		BestellungIdCol.setCellValueFactory(new PropertyValueFactory<>("attraktionBestellungenId"));
		BestellungIdCol.setMinWidth(80);
		TableColumn<AttraktionBestellungenListeFX, Integer> kundenIdCol = new TableColumn<>("Kunden Id");
		kundenIdCol.setCellValueFactory(new PropertyValueFactory<>("kundenId"));
		kundenIdCol.setMinWidth(80);
		TableColumn<AttraktionBestellungenListeFX, String> AttraktionNameCol = new TableColumn<>("Attraktion Name");
		AttraktionNameCol.setCellValueFactory(new PropertyValueFactory<>("attraktionName"));
		AttraktionNameCol.setMinWidth(150);
		
		leseAttraktionBestellListe(0);
		TableView<AttraktionBestellungenListeFX> tv = new TableView<>(olBestellungfx);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.getColumns().addAll(BestellungIdCol,kundenIdCol, AttraktionNameCol);
		VBox vb = new VBox(10, tv);
		vb.setPadding(new Insets(5));
		return new TitledPane("Liste der gebuchten Attraktionen", vb);
	}
		/*
		 *Mitarbeiter können Liste der gebuchten Packages ansehen( Nach
        *erfolgter Buchung der Packages in der Kundenoberfläche werden die Daten in der
        *Datenbank gespeichert. Mitarbeiter haben Zugriff auf Daten).
		 */
	private TitledPane Package_Bestellung() {
		TableColumn<PackageBestellungenListeFX, Integer> BestellungIdCol = new TableColumn<>("BestellungId");
		BestellungIdCol.setCellValueFactory(new PropertyValueFactory<>("packageBestellungenId"));
		BestellungIdCol.setMinWidth(80);
		TableColumn<PackageBestellungenListeFX, Integer> kundenIdCol = new TableColumn<>("Kunden Id");
		kundenIdCol.setCellValueFactory(new PropertyValueFactory<>("kundenId"));
		kundenIdCol.setMinWidth(80);
		TableColumn<PackageBestellungenListeFX, String> AttraktionNameCol = new TableColumn<>("Package Name");
		AttraktionNameCol.setCellValueFactory(new PropertyValueFactory<>("packageName"));
		AttraktionNameCol.setMinWidth(150);
		lesePackageBestellListe(0);
		TableView<PackageBestellungenListeFX> tv = new TableView<>(olPackageBestellListeFX);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.getColumns().addAll(BestellungIdCol,kundenIdCol, AttraktionNameCol);
		
		VBox vb = new VBox(10, tv);
		vb.setPadding(new Insets(5));
		return new TitledPane("Liste der gebuchten Packages", vb);
	}
	
	private void leseFreizeitpark() {
		try {
			ArrayList<Freizeitpark> lc = Datenbank.getFreizeitparken();
			olFreizeitparkFX.clear();
			for (Freizeitpark c : lc)
				olFreizeitparkFX.add(new FreizeitparkFX(c));
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	}

	private void leseAttraktion(int FreizeitparkId,Optional<Boolean> onlineTicket) {
		try {
			ArrayList<Attraktion> lc = Datenbank.getAttraktionen(FreizeitparkId, onlineTicket);
			olAttraktionFX.clear();
			for (Attraktion c : lc)
				olAttraktionFX.add(new AttraktionFX(c));
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
		}
	private void lesePackage(int parkld) {
		try {
			ArrayList<Packages> lc = Datenbank.getPackages(parkld);
			olPackageFX.clear();
			for (Packages p : lc)
				olPackageFX.add(new PackagesFX(p));
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	}

	private void leseAttraktionBestellListe(int parkId) {
		try {
			ArrayList<AttraktionBestellungenListe> lc = Datenbank.getAttraktionenOrders(parkId);
			olBestellungfx.clear();
			for(AttraktionBestellungenListe r : lc)
				olBestellungfx.add(new AttraktionBestellungenListeFX(r));
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}		
	}
	private void leseKunde() {
		try {
			ArrayList<Kunde> lc = Datenbank.getKunde();
			olKundeFX.clear();
			for (Kunde m : lc)
				olKundeFX.add(new KundeFX(m));

		} catch (SQLException e) {

			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	}
	private void lesePackageBestellListe(int parkId) {
		try {
			ArrayList<PackageBestellungenListe> lc = Datenbank.getPackageBestellListe(parkId);
			olPackageBestellListeFX.clear();
			for (PackageBestellungenListe k : lc)
				olPackageBestellListeFX.add(new PackageBestellungenListeFX(k));

		} catch (SQLException e) {

			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}

class CoverCellImplementation extends TableCell<AttraktionFX, String> {
	@Override
	protected void updateItem(String bild, boolean empty) {
		super.updateItem(bild, empty);
		if (!empty && bild.length() > 0) {
			this.setPrefSize(150, 100);
			ImageView imageView = new ImageView(Paths.get(bild).toUri().toString());
			imageView.setFitHeight(this.getPrefHeight());
			imageView.setFitWidth(this.getPrefWidth());
			imageView.setPreserveRatio(true);
			this.setGraphic(imageView);
		} else {
			this.setText("");
			this.setPrefSize(100, 10);
			this.setGraphic(null);
		}
	}
}

class CoverCellImplementationPackage extends TableCell<PackagesFX, String> {
	@Override
	protected void updateItem(String bild, boolean empty) {
		super.updateItem(bild, empty);
		if (!empty && bild.length() > 0) {
			this.setPrefSize(150, 100);
			ImageView imageView = new ImageView(Paths.get(bild).toUri().toString());
			imageView.setFitHeight(this.getPrefHeight());
			imageView.setFitWidth(this.getPrefWidth());
			imageView.setPreserveRatio(true);
			this.setGraphic(imageView);
		} else {
			this.setText("");
			this.setPrefSize(100, 10);
			this.setGraphic(null);
		}
	}
}