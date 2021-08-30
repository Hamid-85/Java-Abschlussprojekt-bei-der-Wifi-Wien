package daten_verwaltung;

import java.time.LocalDate;

public class Kunde {
	private long kundeId;
	private String vorname;
	private String nachname;
	private String adresse;
	private String email;
	private String benutzername;
	private String kennwort;
	public Kunde(long kundeId, String vorname, String nachname, String adresse, String email,
			String benutzername, String kennwort) {
		super();
		this.kundeId = kundeId;
		this.vorname = vorname;
		this.nachname = nachname;
		this.adresse = adresse;
		this.email = email;
		this.benutzername = benutzername;
		this.kennwort = kennwort;
	}
	public long getKundeId() {
		return kundeId;
	}
	public void setKundeId(long kundeId) {
		this.kundeId = kundeId;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBenutzername() {
		return benutzername;
	}
	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}
	public String getKennwort() {
		return kennwort;
	}
	public void setKennwort(String kennwort) {
		this.kennwort = kennwort;
	}
	
}

	