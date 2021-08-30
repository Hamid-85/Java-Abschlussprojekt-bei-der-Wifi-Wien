package daten_verwaltung;

import java.util.ArrayList;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;



public class AttraktionFX {
	private Attraktion modellAtrraktion;
	private SimpleIntegerProperty attraktionId;
	private SimpleStringProperty freizeitparkName;
	private SimpleStringProperty attraktionName;
	private SimpleStringProperty attraktionThema;
	private SimpleDoubleProperty attraktionTicketPreis;
	private SimpleStringProperty attraktionBild;
	private SimpleStringProperty attraktionAnmerkung;
	private SimpleBooleanProperty attraktionStatus;
	private SimpleBooleanProperty onlineTicketStatus;
	

	public AttraktionFX(Attraktion p) {
		modellAtrraktion = p;
		attraktionId = new SimpleIntegerProperty(p.getAttraktionId());
		freizeitparkName = new SimpleStringProperty(p.getFreizeitpark().getFreizeitparkName());
		attraktionName = new SimpleStringProperty(p.getAttraktionName());
		attraktionThema = new SimpleStringProperty(p.getAttraktionThema());
		attraktionTicketPreis = new SimpleDoubleProperty(p.getAttraktionTicketPreis());
		attraktionBild = new SimpleStringProperty(p.getAttraktionBild());
		attraktionAnmerkung = new SimpleStringProperty(p.getAttraktionAnmerkung());
		attraktionStatus=new SimpleBooleanProperty(p.getAttraktionStatus());
		onlineTicketStatus=new SimpleBooleanProperty(p.getOnlineTicketStatus());
	}

	public Attraktion getModellAtrraktion() {
		return modellAtrraktion;
	}
	

	public int getAttraktionId() {
		return attraktionId.get();
	}

	public SimpleIntegerProperty attraktionIdProperty() {
		return attraktionId;
	}

	public String getFreizeitparkName() {
		return freizeitparkName.get();
	}

	public void setFreizeitparkName(String v) {
		freizeitparkName.set(v);
		
	}

	public SimpleStringProperty freizeitparkNameProperty() {
		return freizeitparkName;
	}
	public String getAttraktionName() {
		return attraktionName.get();
	}

	public void setAttraktionName(String v) {
		attraktionName.set(v);
		modellAtrraktion.setAttraktionName(v);
	}

	public SimpleStringProperty attraktionNameProperty() {
		return attraktionName;
	}

	public String getAttraktionThema() {
		return attraktionThema.get();
	}

	public void setAttraktionThema(String v) {
		attraktionThema.set(v);
		modellAtrraktion.setAttraktionThema(v);
	}

	public SimpleStringProperty attraktionThemaProperty() {
		return attraktionThema;
	}

	

	public double getAttraktionTicketPreis() {
		return attraktionTicketPreis.get();
	}

	public void setAttraktionTicketPreis(double v) {
		attraktionTicketPreis.set(v);
		modellAtrraktion.setAttraktionTicketPreis(v);
	}

	public SimpleDoubleProperty attraktionTicketPreisProperty() {
		return attraktionTicketPreis;
	}

	public String getAttraktionBild() {
		return attraktionBild.get();
	}

	public void setAttraktionBild(String v) {
		attraktionBild.set(v);
		modellAtrraktion.setAttraktionBild(v);
	}

	public SimpleStringProperty attraktionBildProperty() {
		return attraktionBild;
	}

	public String getAttraktionAnmerkung() {
		return attraktionAnmerkung.get();
	}

	public void setAttraktionAnmerkung(String v) {
		attraktionAnmerkung.set(v);
		modellAtrraktion.setAttraktionAnmerkung(v);
	}

	public SimpleStringProperty attraktionAnmerkungProperty() {
		return attraktionAnmerkung;
	}
	public Boolean getAttraktionStatus() {
		return attraktionStatus.get();
	}

	public void setAttraktionStatus(Boolean v) {
		attraktionStatus.set(v);
		modellAtrraktion.setAttraktionStatus(v);
	}

	public SimpleBooleanProperty attraktionStatusProperty() {
		return attraktionStatus;
	}
	public Boolean getOnlineTicketStatus() {
		return onlineTicketStatus.get();
	}

	public void setOnlineTicketStatus(Boolean v) {
		onlineTicketStatus.set(v);
		modellAtrraktion.setOnlineTicketStatus(v);
	}

	public SimpleBooleanProperty onlineTicketStatusProperty() {
		return onlineTicketStatus;
	}
}
