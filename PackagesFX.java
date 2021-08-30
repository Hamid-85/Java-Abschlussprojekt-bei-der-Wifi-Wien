package daten_verwaltung;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PackagesFX {
	
	private Packages modellPackage;
	private SimpleIntegerProperty packageId;
	private SimpleStringProperty freizeitparkName;
	private SimpleStringProperty packageName;
	private SimpleStringProperty packageBild;
	private SimpleDoubleProperty packagePreis;
	private SimpleBooleanProperty packageStatus;
	public PackagesFX(Packages p) {
		modellPackage = p;
		packageId = new SimpleIntegerProperty(p.getPackageId());
		freizeitparkName = new SimpleStringProperty(p.getFreizeitpark().getFreizeitparkName());
		packageName = new SimpleStringProperty(p.getPackageName());
		packageBild = new SimpleStringProperty(p.getPackageBild());
		packagePreis = new SimpleDoubleProperty(p.getPackagePreis());
		packageStatus=new SimpleBooleanProperty(p.getPackageStatus());
		
	}
	public Packages getModellPackage() {
		return modellPackage;
	}

	public int getPackageId() {
		return packageId.get();
	}
	public String getFreizeitparkName() {
		return freizeitparkName.get();
	}

	public void setFreizeitparkName(String f) {
		freizeitparkName.set(f);
	
	}

	public SimpleStringProperty freizeitparkNameProperty() {
		return freizeitparkName;
	}

	public SimpleIntegerProperty packageIdProperty() {
		return packageId;
	}

	public String getPackageName() {
		return packageName.get();
	}

	public void setPackageName(String v) {
		packageName.set(v);
		modellPackage.setPackageName(v);
	}

	public SimpleStringProperty packageNameProperty() {
		return packageName;
	}
	public String getPackageBild() {
		return packageBild.get();
	}

	public void setPackageBild(String v) {
		packageBild.set(v);
		modellPackage.setPackageBild(v);
	}

	public SimpleStringProperty packageBildProperty() {
		return packageBild;
	}
	public double getPackagePreis() {
		return packagePreis.get();
	}

	public void setPackagePreis(double v) {
		packagePreis.set(v);
		modellPackage.setPackagePreis(v);
	}

	public SimpleDoubleProperty packagePreisProperty() {
		return packagePreis;
	}

	public Boolean getPackageStatus() {
		return packageStatus.get();
	}

	public void setPackageStatus(Boolean v) {
		packageStatus.set(v);
		modellPackage.setPackageStatus(v);
	}

	public SimpleBooleanProperty packageStatusProperty() {
		return packageStatus;
	}
}

