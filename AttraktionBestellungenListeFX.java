package daten_verwaltung;

import daten_verwaltung.Attraktion;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class AttraktionBestellungenListeFX {
	private AttraktionBestellungenListe modellBestellung;
	private SimpleIntegerProperty attraktionBestellungenId;
	private SimpleLongProperty kundenId;
	private SimpleStringProperty attraktionName;
	public AttraktionBestellungenListeFX(AttraktionBestellungenListe b) {
		modellBestellung = b;
		attraktionBestellungenId = new SimpleIntegerProperty(b.getAttraktionBestellungenId());
		kundenId = new SimpleLongProperty(b.getKunde().getKundeId());
		attraktionName = new SimpleStringProperty(b.getAttraktion().getAttraktionName());
		
	}

	public AttraktionBestellungenListe getModellBestellung() {
		return modellBestellung;
	}
	

	public int getAttraktionBestellungenId() {
		return attraktionBestellungenId.get();
	}


	public SimpleIntegerProperty attraktionBestellungenIdProperty() {
		return attraktionBestellungenId;
	}
	public long getKundenId() {
		return kundenId.get();
	}

	public void setKundenId(long v) {
		kundenId.set(v);
		
	}

	public SimpleLongProperty kundenIdProperty() {
		return kundenId;
	}

	public String getAttraktionName() {
		return attraktionName.get();
	}

	public void setAttraktionName(String v) {
		attraktionName.set(v);
		
	}

	public SimpleStringProperty attraktionNameProperty() {
		return attraktionName;
	}

	
}

