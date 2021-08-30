package daten_verwaltung;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class PackageBestellungenListeFX {
	private PackageBestellungenListe modellPackageBestellListe;
	private SimpleIntegerProperty packageBestellungenId;
	private SimpleLongProperty kundenId;
	private SimpleStringProperty packageName;
	public PackageBestellungenListeFX(PackageBestellungenListe b) {
		modellPackageBestellListe = b;
		packageBestellungenId = new SimpleIntegerProperty(b.getPackageBestellungenId());
		kundenId = new SimpleLongProperty(b.getKunde().getKundeId());
		packageName = new SimpleStringProperty(b.getPackages().getPackageName());
		
	}

	public PackageBestellungenListe getModellPackageBestellListe() {
		return modellPackageBestellListe;
	}
	

	public int getPackageBestellungenId() {
		return packageBestellungenId.get();
	}


	public SimpleIntegerProperty packageBestellungenIdProperty() {
		return packageBestellungenId;
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

	public String getPackageName() {
		return packageName.get();
	}

	public void setPackageName(String v) {
		packageName.set(v);
		
	}

	public SimpleStringProperty packageNameProperty() {
		return packageName;
	}

	
}

