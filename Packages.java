package daten_verwaltung;

public class Packages {
	
	private int packageId;
	private Freizeitpark freizeitpark;
	private String packageName;
	private String packageBild;
	private double packagePreis;
	private Boolean PackageStatus;
	public Packages(int packageId, Freizeitpark freizeitpark, String packageName, String packageBild,
			double packagePreis, Boolean packageStatus) {
		super();
		this.packageId = packageId;
		this.freizeitpark = freizeitpark;
		this.packageName = packageName;
		this.packageBild = packageBild;
		this.packagePreis = packagePreis;
		PackageStatus = packageStatus;
	}
	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	public Freizeitpark getFreizeitpark() {
		return freizeitpark;
	}
	public void setFreizeitpark(Freizeitpark freizeitpark) {
		this.freizeitpark = freizeitpark;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getPackageBild() {
		return packageBild;
	}
	public void setPackageBild(String packageBild) {
		this.packageBild = packageBild;
	}
	public double getPackagePreis() {
		return packagePreis;
	}
	public void setPackagePreis(double packagePreis) {
		this.packagePreis = packagePreis;
	}
	public Boolean getPackageStatus() {
		return PackageStatus;
	}
	public void setPackageStatus(Boolean packageStatus) {
		PackageStatus = packageStatus;
	}
	
}