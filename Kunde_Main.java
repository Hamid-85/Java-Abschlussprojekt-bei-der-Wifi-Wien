package Kunde;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import java.net.URI;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import Mitarbeiter.AttraktionDetailDialog;
import Mitarbeiter.PackageDetailDialog;
import daten_verwaltung.Attraktion;
import daten_verwaltung.AttraktionFX;
import daten_verwaltung.AttraktionBestellungenListe;
import daten_verwaltung.AttraktionBestellungenListeFX;
import daten_verwaltung.Datenbank;
import daten_verwaltung.Freizeitpark;
import daten_verwaltung.FreizeitparkFX;
import daten_verwaltung.Kunde;
import daten_verwaltung.KundeFX;
import daten_verwaltung.PackageBestellungenListe;
import daten_verwaltung.Packages;
import daten_verwaltung.PackagesFX;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;


public class Kunde_Main extends Application {

	private ObservableList<AttraktionFX> olAttraktionFX = FXCollections.observableArrayList();
	private ObservableList<PackagesFX> olPackagesFX = FXCollections.observableArrayList();	
	private Kunde kunde;
	private KundeFX kundeFx;
	Stage window;
	Scene LoginScene,Hauptscene, sceneAttraktionenListe, sceneOnlineTicket, scenePackages;
    boolean displayed;
    
       
	@Override
	public void start(Stage primaryStage) {
		
		window=primaryStage;
		
		Accordion accOrt = new Accordion();
	
		try {

			ArrayList<Freizeitpark> al=Datenbank.getFreizeitparken();
			Map<String,List<Freizeitpark>>ort=al.stream().collect(Collectors.groupingBy(Freizeitpark::getFreizeitparkOrt));
		
			for(String key:ort.keySet()) {
				accOrt.getPanes().add(creatTitlePane(key,ort.get(key)));
			}	
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	
		VBox vbAccOrt=new VBox(accOrt);
	
		accOrt.setPrefSize(850, 100);
		accOrt.getStyleClass().add("fancy-accordion");
		Hauptscene = new Scene(vbAccOrt,800,600);
		Hauptscene.getStylesheets().add("Hauptmenu.css");
		
		/*
		 * LoginBereich
		 */
		Text txt=new Text("welcome");
		Font font=new Font("Serif",30);
		txt.setFont(font);
		txt.setFill(Color.DARKORANGE);
		
		Label lbluser = new Label("Benutzername");
		TextField txtuser = new TextField();
		txtuser.setPromptText("Email");

		Label lblpass = new Label("Kennwort");
		PasswordField passwordfield=new PasswordField();

         Button neue = new Button("neue Kunde");
         neue.getStyleClass().add("fancy-button");
         
		Button login = new Button("Login");
		login.getStyleClass().add("fancy-button");
		login.setOnAction(e -> {
			creatTitlePane("", new ArrayList < Freizeitpark >());
			window.setScene(Hauptscene);
		});
		login.addEventFilter(ActionEvent.ACTION, e -> {
			if (txtuser.getText() == null || txtuser.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Benutzername eingeben").showAndWait();
				e.consume();
				return;
			}
			if (passwordfield.getText() == null || passwordfield.getText().length() == 0 )
			{
				new Alert(AlertType.ERROR, "Kennwort eingeben").showAndWait();
				e.consume();
				return;
			}
			try {
				ArrayList<Kunde> al=Datenbank.getKunde(txtuser.getText(),passwordfield.getText());
				if(al.size()==0  ) {
					new Alert(AlertType.ERROR, "Benutzername oder Kennwort falsch").showAndWait();
					e.consume();
					return;
				}
				kunde=al.get(0);
				kundeFx=new KundeFX(kunde);
			} catch (SQLException e1) {
				new Alert(AlertType.ERROR, e1.toString()).showAndWait();
				e.consume();
				return;				
			}
		});

		GridPane gpLogin = new GridPane();
		gpLogin.setPadding(new Insets(10));
		gpLogin.setHgap(10);
		gpLogin.setVgap(10);
		gpLogin.add(txt, 0, 0,2,1);
		gpLogin.add(lbluser, 0, 1);
		gpLogin.add(txtuser, 1, 1,2,1);
		gpLogin.add(lblpass, 0, 2);
		gpLogin.add(passwordfield, 1, 2,2,1);
		gpLogin.add(login, 1, 3);
		gpLogin.add(neue, 2,3);
		gpLogin.setAlignment(Pos.TOP_LEFT);
		
		/*
		 * nach dem einklick auf neuButton im LoginBereich wird ein Fenster erscheint. Kunden können sich da anmelden.
		 * Nach dem einklick auf Speichern-Button wird die Kunden daten im Datenbank gespeichert.
		 */
		Label lblname = new Label("Vorname * ");
		TextField txtname = new TextField();

		Label lblNachname = new Label("Nachname * ");
		TextField txtNachname = new TextField();
		txtNachname.setPrefColumnCount(20);

		Label lblAdresse = new Label("Adresse ");
		TextField txtAdresse = new TextField();
		
		Label lblEmail = new Label("Email * ");
		TextField txtEmail = new TextField();
		Button speichern = new Button("speichern");
		speichern.getStyleClass().add("fancy-button");
		
		speichern.setOnAction(e -> {
			Alert alert =new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("Benutzername ist Ihre Email und Kennwort ist Ihre Nachname");
			ImageView alertIcon=new ImageView("beestaetigen.png");
			alertIcon.setFitHeight(48);
			alertIcon.setFitWidth(48);
			alert.getDialogPane().setGraphic(alertIcon);
			alert.show();

			try {
				Datenbank.insertCustomer(new Kunde(0, txtname.getText(), txtNachname.getText(), txtAdresse.getText(),
						txtEmail.getText(), txtEmail.getText(), txtNachname.getText()));
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		
		});
				speichern.addEventFilter(ActionEvent.ACTION, e -> {
					if (txtname.getText() == null || txtname.getText().length() == 0) {
						new Alert(AlertType.ERROR, "Vorname eingeben").showAndWait();
						e.consume();
						return;
					}
					
					if (txtNachname.getText() == null || txtNachname.getText().length() == 0) {
						new Alert(AlertType.ERROR, "Nachname eingeben").showAndWait();
						e.consume();
						return;
					}

					if (txtEmail.getText() == null || txtEmail.getText().length() == 0) {
						new Alert(AlertType.ERROR, "Email eingeben").showAndWait();
						e.consume();
						return;
					}
					
				});

		GridPane gpAnmeldung = new GridPane();
		gpAnmeldung.setHgap(10);
		gpAnmeldung.setVgap(8);
		gpAnmeldung.add(lblname, 0, 2);
		gpAnmeldung.add(txtname, 1, 2, 2, 1);
		gpAnmeldung.add(lblNachname, 0, 3);
		gpAnmeldung.add(txtNachname, 1, 3, 2, 1);
		gpAnmeldung.add(lblAdresse, 0, 4);
		gpAnmeldung.add(txtAdresse, 1, 4, 2, 1);
		gpAnmeldung.add(lblEmail, 0, 5);
		gpAnmeldung.add(txtEmail, 1, 5, 2, 1);
		gpAnmeldung.add(speichern, 1, 6);
		gpAnmeldung.setPadding(new Insets(10));
		
		displayed = false;
		Rectangle clip = new Rectangle(0,300);	
		BorderPane sliderShowAnmeldung = new BorderPane(); 
		sliderShowAnmeldung.setBottom(gpAnmeldung);
		sliderShowAnmeldung.setClip(clip);
		sliderShowAnmeldung.setStyle("-fx-background-color: transparent");
		neue.setOnAction(event -> {
		  if (displayed)
		  {
		      Timeline tm = new Timeline();
		      KeyValue kv1 = new KeyValue(sliderShowAnmeldung.translateXProperty(), -100);
		      KeyValue kv2 = new KeyValue(sliderShowAnmeldung.prefWidthProperty(), 0);
		      KeyValue kv3 = new KeyValue(clip.widthProperty(), 0);
		      KeyFrame kf = new KeyFrame(Duration.millis(200), kv1,kv2,kv3);
		      tm.getKeyFrames().addAll(kf);
		      tm.play();
		      displayed = false;
		  }
		  else
		  {
		      Timeline tm = new Timeline();
		      KeyValue kv1 = new KeyValue(sliderShowAnmeldung.translateXProperty(), 0);
		      KeyValue kv2 = new KeyValue(sliderShowAnmeldung.prefWidthProperty(), 600);
		      KeyValue kv3 = new KeyValue(clip.widthProperty(), 400);
		      KeyFrame kf = new KeyFrame(Duration.millis(200), kv1, kv2, kv3);
		      tm.getKeyFrames().addAll(kf);
		      tm.play();
		      displayed = true;
		  }
		});
		
		VBox vbLoginUndAnmeldung=new VBox(10,gpLogin,sliderShowAnmeldung);

		LoginScene = new Scene(vbLoginUndAnmeldung);
		
		LoginScene.getStylesheets().add("LoginBereich.css");

        primaryStage.setScene(LoginScene);
		primaryStage.setTitle("Freizeitparks in Österreich");
		Image primaryStageicon=new Image(getClass().getResourceAsStream("Austria.png"));
		primaryStage.getIcons().add(primaryStageicon);
		primaryStage.setMaximized(true);
		window.setWidth(primaryStage.getWidth());
		window.setHeight(primaryStage.getHeight());
		primaryStage.show();
	}
	
	/*
	 * es werden für jede Bundeslander eine TitlePane angelegt, Jede TitlePane beinhaltet drei Button, AttraktionListe,
	 * OnlineTicket, Packages. Bundeländer TitlePane liegen im HauptScene.
	 */
	private TitledPane creatTitlePane(String ort,List<Freizeitpark> l) {

		GridPane gp_Park=new GridPane();
		gp_Park.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));
		gp_Park.setHgap(100);
		gp_Park.setVgap(20);
		
		int i=0;
		for(Freizeitpark f:l) {
			Label lblFreizetparkName = new Label("" + f.getFreizeitparkName());
			lblFreizetparkName.setFont(new Font("Arial", 18));

			Button btAttraktionen = new Button("Attraktion Liste " );
			btAttraktionen.setPrefWidth(300);
			btAttraktionen.getStyleClass().add("fancy-button");
			
			btAttraktionen.setOnAction(e -> {
				AttraktionenListeScene(f.getFreizeitparkId());
			window.setScene(sceneAttraktionenListe);
			
			});
			Button btOnlineTicket = new Button("Online Ticket");
			btOnlineTicket.setPrefWidth(150);
			btOnlineTicket.getStyleClass().add("fancy-button");
			
			btOnlineTicket.setOnAction(e -> {
				OnlineTicketScene(f.getFreizeitparkId());
				window.setScene(sceneOnlineTicket);
				});
			Button btPackages = new Button("Unsere Packages");
			btPackages.setPrefWidth(150);
			btPackages.getStyleClass().add("fancy-button");
			
			btPackages.setOnAction(e -> {
				PackageScene(f.getFreizeitparkId());
				window.setScene(scenePackages);
				});
			
			gp_Park.add(lblFreizetparkName, 1, i);
			gp_Park.add(btAttraktionen, 2, i);
			gp_Park.add(btOnlineTicket, 3, i);
			gp_Park.add(btPackages, 4, i);
			i++;
		}

		gp_Park.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		return new TitledPane(ort,gp_Park);
	}

/*
 * Übersicht aller Attraktionen des Parks. 
 */
	private void AttraktionenListeScene(int FreizeitparkId) {
		
	TableColumn<AttraktionFX, String> attraktionCol = new TableColumn<>("Attraktion");
	attraktionCol.setCellValueFactory(new PropertyValueFactory<>("attraktionName"));
	attraktionCol.setMinWidth(150);
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
	
	attraktionBildCol.setCellFactory(new Callback<TableColumn<AttraktionFX, String>, TableCell<AttraktionFX, String>>() {
		@Override
		public TableCell<AttraktionFX, String> call(TableColumn<AttraktionFX, String> arg0) {
			return new CoverCellImplementationAttraktion();
		}
	});
	
	leseAttraktion(FreizeitparkId,Optional.empty());
	TableView<AttraktionFX> tvAttraktion = new TableView<>(olAttraktionFX);
	tvAttraktion.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	
	tvAttraktion.getColumns().addAll(attraktionCol, attraktionThemaCol,
			attraktionTicketPreisCol, attraktionBildCol, attraktionAnmerkungCol);

	Button btzurück = new Button("zurück zum Hauptmenü");
	btzurück.getStyleClass().add("fancy-button");
	btzurück.setPadding(new Insets(5));
	btzurück.setAlignment(Pos.BOTTOM_RIGHT);
	btzurück.setOnAction(e -> window.setScene(Hauptscene));
	btzurück.setAlignment(Pos.BOTTOM_RIGHT);
	GridPane gp=new GridPane();
	gp.add(btzurück, 0, 0);
	gp.setAlignment(Pos.BOTTOM_RIGHT);
	
	VBox vb = new VBox(10,tvAttraktion,gp);
	vb.setPadding(new Insets(10));
	sceneAttraktionenListe = new Scene(vb);
	sceneAttraktionenListe.getStylesheets().add("AttraktionListe.css");
	}
	
	/*
	 * Übersicht aller Attraktionen des Parks, die OnlineTicket möglichkeit haben. Hier kann man Attraktionen auswählen ,
	 *  im Warenkorb hinzufügen und bezahlen.
	 */
	private void OnlineTicketScene(int FreizeitparkId) {

		TableColumn<AttraktionFX, String> attraktionCol = new TableColumn<>("Attraktion");
		attraktionCol.setCellValueFactory(new PropertyValueFactory<>("attraktionName"));
		attraktionCol.setMinWidth(150);
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
		
		attraktionBildCol.setCellFactory(new Callback<TableColumn<AttraktionFX, String>, TableCell<AttraktionFX, String>>() {
			@Override
			public TableCell<AttraktionFX, String> call(TableColumn<AttraktionFX, String> arg0) {
				return new CoverCellImplementationAttraktion();
			}
		});
		
		leseAttraktion(FreizeitparkId,Optional.of(Boolean.TRUE));
		TableView<AttraktionFX> tvOnlineTicket = new TableView<>(olAttraktionFX);
		tvOnlineTicket.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		tvOnlineTicket.getColumns().addAll( attraktionCol, attraktionThemaCol,
				attraktionTicketPreisCol, attraktionBildCol,attraktionAnmerkungCol);

		Button btWarenkorb = new Button("In den Warenkorb");
		btWarenkorb.getStyleClass().add("fancy-button");
		btWarenkorb.getStyleClass().add("warenkorb-button");
		btWarenkorb.setPadding(new Insets(5));
		btWarenkorb.setDisable(true);

		Button btKasse = new Button("zur Kasse");
		btKasse.getStyleClass().add("fancy-button");
		btKasse.setPadding(new Insets(5));
		btKasse.setDisable(true);

		tvOnlineTicket.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AttraktionFX>() {

			@Override
			public void changed(ObservableValue<? extends AttraktionFX> arg0, AttraktionFX arg1, AttraktionFX arg2) {

				if (kunde != null)
					btWarenkorb.setDisable(false);
			}
		});
		
		btWarenkorb.setOnAction(e -> {
			for (AttraktionFX b : tvOnlineTicket.getSelectionModel().getSelectedItems())
				kundeFx.getBestellung().add(b);
			btKasse.setDisable(false);
		});

		btKasse.setOnAction(e -> {
			new BestellungAttraktionDetailDialog(kundeFx).showAndWait();
		});
		
		Button btzurück = new Button("zurück zum Hauptmenü");
		btzurück.getStyleClass().add("fancy-button");
		btzurück.getStyleClass().add("back-button");
		btzurück.setPadding(new Insets(5));
		btzurück.setAlignment(Pos.BOTTOM_RIGHT);
		btzurück.setOnAction(e -> window.setScene(Hauptscene));
		GridPane gp=new GridPane();
		gp.setPadding(new Insets(10));
		gp.setHgap(10);
		gp.setVgap(10);
		gp.add(btWarenkorb, 0, 0);
		gp.add(btKasse, 0, 1);
		gp.add(btzurück, 0, 2);
		gp.setAlignment(Pos.BOTTOM_RIGHT);
		
		VBox vbOnlineTicket = new VBox(10,tvOnlineTicket,gp);
		vbOnlineTicket.setPadding(new Insets(10));
	
		StackPane st_OnlineTicket = new StackPane(vbOnlineTicket);
		sceneOnlineTicket = new Scene(st_OnlineTicket, 1000, 600);
		sceneOnlineTicket.getStylesheets().add("onlineTicket.css");
		
	}
	/*
	 * Übersicht aller Packages des Parks. Hier kann man Packages auswählen ,
	 *  im Warenkorb hinzufügen und bezahlen.
	 */
    private void PackageScene(int FreizeitparkId) {

		TableColumn<PackagesFX, String> packageNameCol = new TableColumn<>("Package Name");
		packageNameCol.setCellValueFactory(new PropertyValueFactory<>("packageName"));
		packageNameCol.setMinWidth(150);
		TableColumn<PackagesFX, String> packageBildCol = new TableColumn<>("Package Bild");
		packageBildCol.setCellValueFactory(new PropertyValueFactory<>("packageBild"));
		packageBildCol.setMinWidth(150);
		TableColumn<PackagesFX, Double> packagePreisCol = new TableColumn<>("Package Preis");
		packagePreisCol.setCellValueFactory(new PropertyValueFactory<>("packagePreis"));
		packagePreisCol.setMinWidth(150);
		
		Button btWarenkorb = new Button("In den warenkorb");
		btWarenkorb.getStyleClass().add("fancy-button");
		btWarenkorb.getStyleClass().add("warenkorb-button");
		btWarenkorb.setPadding(new Insets(5));
		btWarenkorb.setDisable(true);

		Button btKasse = new Button("zur Kasse");
		btKasse.getStyleClass().add("fancy-button");
		btKasse.setPadding(new Insets(5));
		btKasse.setDisable(true);

		packageBildCol.setCellFactory(new Callback<TableColumn<PackagesFX, String>, TableCell<PackagesFX, String>>() {
			@Override
			public TableCell<PackagesFX, String> call(TableColumn<PackagesFX, String> arg0) {
				return new CoverCellImplementationPackage();
				
			}
		});

		lesePackages(FreizeitparkId);
		TableView<PackagesFX> tv = new TableView<>(olPackagesFX);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.getColumns().addAll( packageNameCol, packageBildCol, packagePreisCol);
		
		tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PackagesFX>() {
			@Override
			public void changed(ObservableValue<? extends PackagesFX> arg0, PackagesFX arg1, PackagesFX arg2) {

				if (kunde != null)
					btWarenkorb.setDisable(false);
			}
		});

		btWarenkorb.setOnAction(e -> {
			for (PackagesFX b : tv.getSelectionModel().getSelectedItems())
				kundeFx.getPackages_bestellung().add(b);
			btKasse.setDisable(false);

		});

		btKasse.setOnAction(e -> {
			new BestellungPackageDetailDialog(kundeFx).showAndWait();

		});

    	Button btzurück = new Button("zurück zum Hauptmenü");
    	btzurück.getStyleClass().add("fancy-button");
    	btzurück.getStyleClass().add("back-button");
    	btzurück.setAlignment(Pos.BOTTOM_RIGHT);
		btzurück.setPadding(new Insets(5));
		btzurück.setOnAction(e -> window.setScene(Hauptscene));
		
		GridPane gp=new GridPane();
		gp.setPadding(new Insets(10));
		gp.setHgap(10);
		gp.setVgap(10);
		gp.add(btWarenkorb, 0, 0);
		gp.add(btKasse, 0, 1);
		gp.add(btzurück, 0, 2);
		gp.setAlignment(Pos.BOTTOM_RIGHT);
		VBox vb = new VBox(10,tv,gp);
		vb.setPadding(new Insets(10));
		
		StackPane st_Packages = new StackPane(vb);
		scenePackages = new Scene(st_Packages, 1000, 600);
		scenePackages.getStylesheets().add("packages.css");
		
	}

	private void leseAttraktion(int FreizeitparkId,Optional<Boolean> onlineTicket) {
	try {
		ArrayList<Attraktion> lc = Datenbank.getAttraktionen(FreizeitparkId,onlineTicket);
		olAttraktionFX=FXCollections.observableArrayList();
		for (Attraktion c : lc)
			olAttraktionFX.add(new AttraktionFX(c));
	} catch (SQLException e) {
		new Alert(AlertType.ERROR, e.toString()).showAndWait();
	}
	}

	private void lesePackages(int parkId) {
		try {
			ArrayList<Packages> lc = Datenbank.getPackages(parkId);
			olPackagesFX=FXCollections.observableArrayList();
			for (Packages c : lc)
				olPackagesFX.add(new PackagesFX(c));
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
}

	public static void main(String[] args) {
		launch(args);
	}
}
class CoverCellImplementationAttraktion extends TableCell<AttraktionFX, String> {
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

