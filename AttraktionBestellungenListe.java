package daten_verwaltung;

import java.util.ArrayList;

public class AttraktionBestellungenListe {
	private  int attraktionBestellungenId;
	private Kunde kunde;
	private Attraktion attraktion;
	public AttraktionBestellungenListe(int attraktionBestellungenId, Kunde kunde, Attraktion attraktion) {
		super();
		this.attraktionBestellungenId = attraktionBestellungenId;
		this.kunde = kunde;
		this.attraktion = attraktion;
	}
	public int getAttraktionBestellungenId() {
		return attraktionBestellungenId;
	}
	public void setAttraktionBestellungenId(int attraktionBestellungenId) {
		this.attraktionBestellungenId = attraktionBestellungenId;
	}
	public Kunde getKunde() {
		return kunde;
	}
	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}
	public Attraktion getAttraktion() {
		return attraktion;
	}
	public void setAttraktion(Attraktion attraktion) {
		this.attraktion = attraktion;
	}
	
}
	