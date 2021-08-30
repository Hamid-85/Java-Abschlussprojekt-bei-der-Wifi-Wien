package daten_verwaltung;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class KundeFX {

	private Kunde modelKunde;
	private SimpleLongProperty kundeId;
	private SimpleStringProperty vorname;
	private SimpleStringProperty nachname;
	private SimpleStringProperty adresse;
	private SimpleStringProperty email;
	private SimpleStringProperty benutzername;
	private SimpleStringProperty kennwort;
	private ArrayList<AttraktionFX> bestellung_Attraktion = new ArrayList<>();
	private ArrayList<PackagesFX> Packages_bestellung = new ArrayList<>();

	public KundeFX(Kunde k) {
		modelKunde = k;
		kundeId = new SimpleLongProperty(k.getKundeId());
		vorname = new SimpleStringProperty(k.getVorname());
		nachname = new SimpleStringProperty(k.getNachname());
		adresse = new SimpleStringProperty(k.getAdresse());
		email = new SimpleStringProperty(k.getEmail());
		benutzername = new SimpleStringProperty(k.getBenutzername());
		kennwort = new SimpleStringProperty(k.getKennwort());

	}

	public Kunde getModelKunde() {
		return modelKunde;
	}

	public ArrayList<AttraktionFX> getBestellung() {
		return bestellung_Attraktion;
	}
	public ArrayList<PackagesFX> getPackages_bestellung() {
		return Packages_bestellung;
	}
	public void loeschenBestellungAttraktion() {
		bestellung_Attraktion.clear();
	  }
	  public void loeschenBestellungPackages() {
		  Packages_bestellung.clear();
	  }

	public long getKundeId() {
		return kundeId.get();
	}

	public SimpleLongProperty kundeIdProperty() {
		return kundeId;
	}

	public String getVorname() {
		return vorname.get();
	}
	public void setVorname(String k) {
		vorname.set(k);
		modelKunde.setVorname(k);
	}

	public SimpleStringProperty vornameProperty() {
		return vorname;
	}
	public String getNachname() {
		return nachname.get();
	}
	public void setNachname(String k) {
		nachname.set(k);
		modelKunde.setNachname(k);
	}

	public SimpleStringProperty nachnameProperty() {
		return nachname;
	}
	public String getAdresse() {
		return adresse.get();
	}
	public void setAdresse(String k) {
		adresse.set(k);
		modelKunde.setAdresse(k);
	}

	public SimpleStringProperty adresseProperty() {
		return adresse;
	}
	public String getEmail() {
		return email.get();
	}
	public void setEmail(String k) {
		email.set(k);
		modelKunde.setEmail(k);
	}

	public SimpleStringProperty emailProperty() {
		return email;
	}

	public String getBenutzername() {
		return benutzername.get();
	}
	public void setBenutzername(String b) {
		benutzername.set(b);
		modelKunde.setBenutzername(b);
	}

	public SimpleStringProperty benutzernameProperty() {
		return benutzername;
	}


	public String getKennwort() {
		return kennwort.get();
	}
	public void setKennwort(String k) {
		kennwort.set(k);
		modelKunde.setKennwort(k);
	}

	public SimpleStringProperty kennwortProperty() {
		return kennwort;
	}

	

}
