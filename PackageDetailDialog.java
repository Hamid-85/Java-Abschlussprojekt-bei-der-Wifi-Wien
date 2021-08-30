package Mitarbeiter;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import daten_verwaltung.Datenbank;
import daten_verwaltung.Freizeitpark;
import daten_verwaltung.FreizeitparkFX;
import daten_verwaltung.PackagesFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class PackageDetailDialog extends Dialog<ButtonType>{
	
	private ObservableList<FreizeitparkFX> olFreizeitparkFX = FXCollections.observableArrayList();
	
	public PackageDetailDialog(PackagesFX packagefx) {
		this.setTitle("Package anlegen");
	
		TableColumn<FreizeitparkFX, String> parkCol = new TableColumn<>("Wählen Sie den Park aus");
		parkCol.setCellValueFactory(new PropertyValueFactory<>("freizeitparkName"));
		parkCol.setMinWidth(500);
		TableView<FreizeitparkFX> tv = new TableView<>(olFreizeitparkFX);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.getColumns().addAll(parkCol);
		
		Label lblpkId = new Label("Package Id");
		lblpkId.setPrefWidth(150);
		Label lblPackageId = new Label(Integer.toString(packagefx.getPackageId()));
		HBox hbPackageId = new HBox(10, lblpkId, lblPackageId);

		leseFreizeitpark();
		if(packagefx.getPackageId() > 0) {
			
			tv.getSelectionModel().select(olFreizeitparkFX.stream().
					filter(ca -> ca.getFreizeitparkId()== packagefx.getModellPackage().getFreizeitpark().getFreizeitparkId()).findFirst().get());
		}
		
		Label lblPackageName = new Label("Package Name");
		lblPackageName.setPrefWidth(150);
		TextField txtPackageName = new TextField();
		txtPackageName.setPrefColumnCount(20);
		txtPackageName.setText(packagefx.getPackageName());
		HBox hbPackageName = new HBox(10, lblPackageName, txtPackageName);
		
		Label lblPackageBild = new Label("Package Bild");
		lblPackageBild.setPrefWidth(150);
		TextField txtPackageBild = new TextField();
		txtPackageBild.setPrefColumnCount(20);
		txtPackageBild.setText(packagefx.getPackageBild());
		Button btS=new Button("suchen");
		HBox hbPackageBild = new HBox(10, lblPackageBild, txtPackageBild,btS);
		btS.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			fc.setTitle("Open file");
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image file", "*.png", "*.jpg"));
			File f = fc.showOpenDialog(null);
			if(f !=null)
				txtPackageBild.setText(f.getAbsolutePath());
		});
		
		Label lblPackagePreis = new Label("Package Preis");
		lblPackagePreis.setPrefWidth(150);
		TextField txtPackagePreis = new TextField();
		txtPackagePreis.setPrefColumnCount(20);
		txtPackagePreis.setText(Double.toString(packagefx.getPackagePreis()));
		HBox hbPackagePreis = new HBox(10, lblPackagePreis, txtPackagePreis);
		
		Label lblPackageStatus = new Label("Package Status");
		lblPackageStatus.setPrefWidth(150);
		TextField txtPackageStatus = new TextField();
		txtPackageStatus.setPrefColumnCount(20);
		txtPackageStatus.setText(Boolean.toString(packagefx.getPackageStatus()));
		HBox hbPackageStatus = new HBox(10, lblPackageStatus, txtPackageStatus);
		
		
		VBox vb = new VBox(10,tv, hbPackageId,hbPackageName,hbPackageBild,hbPackagePreis,hbPackageStatus);
		this.getDialogPane().setContent(vb);
		ButtonType speichern = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(speichern, abbrechen);
		this.getDialogPane().getStylesheets().add("Dialog.css");

		Button save = (Button) this.getDialogPane().lookupButton(speichern);
		save.addEventFilter(ActionEvent.ACTION, e -> {
			if(txtPackageName.getText() == null || txtPackageName.getText().length() == 0) {
				new Alert(AlertType.ERROR, "PackageName eingeben").showAndWait();
				e.consume();
				return;
			}
			
			if(txtPackagePreis.getText() == null || txtPackagePreis.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Preis eingeben").showAndWait();
				e.consume();
				return;
			}
			try {
				
				Double.parseDouble(txtPackagePreis.getText());
			}
			catch(NumberFormatException e1) {
				new Alert(AlertType.ERROR, "Preis als Gleitkommazahl eingeben").showAndWait();
				e.consume();
			}
		});

		this.setResultConverter(new Callback<ButtonType, ButtonType>(){
			@Override
			public ButtonType call(ButtonType arg0) {
				if(arg0 == speichern) {
					packagefx.getModellPackage().setFreizeitpark(tv.getSelectionModel().getSelectedItem().getModellFreizeitpark());
					packagefx.setPackageName(txtPackageName.getText());
					packagefx.setPackageBild(txtPackageBild.getText());
					packagefx.setPackagePreis(Double.parseDouble(txtPackagePreis.getText()));
					packagefx.setPackageStatus(Boolean.parseBoolean(txtPackageStatus.getText()));
					try {
						if(packagefx.getPackageId() == 0)
							Datenbank.insertPackage(packagefx.getModellPackage());
						else
							Datenbank.updatePackages(packagefx.getModellPackage());
					} catch (SQLException e) {
						new Alert(AlertType.ERROR, e.toString()).showAndWait();
					}
				}
				return arg0;
			}
		});
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
}





