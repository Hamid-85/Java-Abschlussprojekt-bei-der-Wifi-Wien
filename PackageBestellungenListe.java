package daten_verwaltung;

public class PackageBestellungenListe {
	private  int packageBestellungenId;
	private Kunde kunde;
	private Packages packages;
	public PackageBestellungenListe(int packageBestellungenId, Kunde kunde, Packages packages) {
		super();
		this.packageBestellungenId = packageBestellungenId;
		this.kunde = kunde;
		this.packages = packages;
	}
	public int getPackageBestellungenId() {
		return packageBestellungenId;
	}
	public void setPackageBestellungenId(int packageBestellungenId) {
		this.packageBestellungenId = packageBestellungenId;
	}
	public Kunde getKunde() {
		return kunde;
	}
	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}
	public Packages getPackages() {
		return packages;
	}
	public void setPackages(Packages packages) {
		this.packages = packages;
	}
	
	
	
}
	