package Mitarbeiter;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import daten_verwaltung.Datenbank;
import daten_verwaltung.Freizeitpark;
import daten_verwaltung.FreizeitparkFX;
import daten_verwaltung.AttraktionFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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


public class AttraktionDetailDialog extends Dialog<ButtonType> {
	
	private ObservableList<FreizeitparkFX> olFreizeitparkFX = FXCollections.observableArrayList();
	
	public AttraktionDetailDialog(AttraktionFX attraktionfx) {
		this.setTitle("Attraktion anlegen");
		
		TableColumn<FreizeitparkFX, String> parkCol = new TableColumn<>("Wählen Sie den Park aus");
		parkCol.setCellValueFactory(new PropertyValueFactory<>("freizeitparkName"));
		parkCol.setMinWidth(500);
		TableView<FreizeitparkFX> tv = new TableView<>(olFreizeitparkFX);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.getColumns().addAll(parkCol);
			
		Label lblAttId = new Label("Attraktion Id");
		lblAttId.setPrefWidth(150);
		Label lblAttraktionId = new Label(Integer.toString(attraktionfx.getAttraktionId()));
		HBox hbAttraktionId = new HBox(10, lblAttId, lblAttraktionId);

		leseFreizeitpark();
		if(attraktionfx.getAttraktionId() > 0) {
			
			tv.getSelectionModel().select(olFreizeitparkFX.stream().
					filter(ca -> ca.getFreizeitparkId()== attraktionfx.getModellAtrraktion().getFreizeitpark().getFreizeitparkId()).findFirst().get());
		}
		
		Label lblAttraktionName = new Label("Attraktion");
		lblAttraktionName.setPrefWidth(150);
		TextField txtAttraktionName = new TextField();
		txtAttraktionName.setPrefColumnCount(20);
		txtAttraktionName.setText(attraktionfx.getAttraktionName());
		HBox hbAttraktionName = new HBox(10, lblAttraktionName, txtAttraktionName);
		
		Label lblAttraktionThema = new Label("Attraktion Thema");
		lblAttraktionThema.setPrefWidth(150);
		TextField txtAttraktionThema = new TextField();
		txtAttraktionThema.setPrefColumnCount(20);
		txtAttraktionThema.setText(attraktionfx.getAttraktionThema());
		HBox hbAttraktionThema = new HBox(10, lblAttraktionThema, txtAttraktionThema);
		
		Label lblPreis = new Label("Attraktion Ticket Preis");
		lblPreis.setPrefWidth(150);
		TextField txtPreis = new TextField();
		txtPreis.setPrefColumnCount(20);
		txtPreis.setText(Double.toString(attraktionfx.getAttraktionTicketPreis()));
		HBox hbPreis = new HBox(10, lblPreis, txtPreis);
		
		Label lblBild = new Label("Attraktion Bild");
		lblBild.setPrefWidth(150);
		TextField txtBild = new TextField();
		txtBild.setPrefColumnCount(20);
		txtBild.setText(attraktionfx.getAttraktionBild());
		Button btS=new Button("suchen");
		HBox hbBild = new HBox(10, lblBild, txtBild,btS);
		
		btS.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			fc.setTitle("Open file");
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image file", "*.png", "*.jpg"));
			File f = fc.showOpenDialog(null);
			if(f !=null)
			txtBild.setText(f.getAbsolutePath());
		});
		
		Label lblAnmerkung = new Label("Attraktion Anmerkung");
		lblAnmerkung.setPrefWidth(150);
		TextField txtAnmerkung = new TextField();
		txtAnmerkung.setPrefColumnCount(20);
		txtAnmerkung.setText(attraktionfx.getAttraktionAnmerkung());
		HBox hbAnmerkung = new HBox(10, lblAnmerkung, txtAnmerkung);
		
		Label lblStatus = new Label("Attraktion Status");
		lblStatus.setPrefWidth(150);
		TextField txtStatus = new TextField();
		txtStatus.setPrefColumnCount(20);
		txtStatus.setText(Boolean.toString(attraktionfx.getAttraktionStatus()));
		HBox hbStatus = new HBox(10, lblStatus, txtStatus);
		
		Label lblonlineTicketStatus = new Label("Online Ticket Status");
		lblonlineTicketStatus.setPrefWidth(150);
		TextField txtonlineTicketStatus = new TextField();
		txtonlineTicketStatus.setPrefColumnCount(20);
		txtonlineTicketStatus.setText(Boolean.toString(attraktionfx.getOnlineTicketStatus()));
		HBox hbonlineTicketStatus = new HBox(10, lblonlineTicketStatus, txtonlineTicketStatus);
		

		VBox vb = new VBox(10,tv,hbAttraktionId,hbAttraktionName,hbAttraktionThema,hbPreis,hbBild,
				hbAnmerkung,hbStatus,hbonlineTicketStatus);
		this.getDialogPane().setContent(vb);
		ButtonType speichern = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(speichern, abbrechen);
		this.getDialogPane().getStylesheets().add("Dialog.css");

		Button save = (Button) this.getDialogPane().lookupButton(speichern);
		save.addEventFilter(ActionEvent.ACTION, e -> {


			if(txtAttraktionName.getText() == null || txtAttraktionName.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Attraktion Name eingeben").showAndWait();
				e.consume();
				return;
			}
			});
	
		this.setResultConverter(new Callback<ButtonType, ButtonType>(){
			@Override
			public ButtonType call(ButtonType arg0) {
				if(arg0 == speichern) {
					attraktionfx.getModellAtrraktion().setFreizeitpark(tv.getSelectionModel().getSelectedItem().getModellFreizeitpark());
					attraktionfx.setAttraktionName(txtAttraktionName.getText());
					attraktionfx.setAttraktionThema(txtAttraktionThema.getText());
					attraktionfx.setAttraktionTicketPreis(Double.parseDouble(txtPreis.getText()));
					attraktionfx.setAttraktionBild(txtBild.getText());
					attraktionfx.setAttraktionAnmerkung(txtAnmerkung.getText());
					attraktionfx.setAttraktionStatus(Boolean.parseBoolean(txtStatus.getText()));
					attraktionfx.setOnlineTicketStatus(Boolean.parseBoolean(txtonlineTicketStatus.getText()));
					try {
						if(attraktionfx.getAttraktionId() == 0)
							Datenbank.insertAttraktion(attraktionfx.getModellAtrraktion());
						else
							Datenbank.updateAttraktionen(attraktionfx.getModellAtrraktion());
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


