
package daten_verwaltung;


public class Attraktion {
	
	private int attraktionId;
	private Freizeitpark freizeitpark;
	private String attraktionName;
	private String attraktionThema;
	private double attraktionTicketPreis;
	private String attraktionBild;
	private String attraktionAnmerkung;
	private Boolean attraktionStatus;
	private Boolean onlineTicketStatus;
	public Attraktion(int attraktionId, Freizeitpark freizeitpark, String attraktionName, String attraktionThema,
			double attraktionTicketPreis, String attraktionBild, String attraktionAnmerkung,
			Boolean attraktionStatus, Boolean onlineTicketStatus) {
		super();
		this.attraktionId = attraktionId;
		this.freizeitpark = freizeitpark;
		this.attraktionName = attraktionName;
		this.attraktionThema = attraktionThema;
		this.attraktionTicketPreis = attraktionTicketPreis;
		this.attraktionBild = attraktionBild;
		this.attraktionAnmerkung = attraktionAnmerkung;
		this.attraktionStatus = attraktionStatus;
		this.onlineTicketStatus = onlineTicketStatus;
	}
	public int getAttraktionId() {
		return attraktionId;
	}
	public void setAttraktionId(int attraktionId) {
		this.attraktionId = attraktionId;
	}
	public Freizeitpark getFreizeitpark() {
		return freizeitpark;
	}
	public void setFreizeitpark(Freizeitpark freizeitpark) {
		this.freizeitpark = freizeitpark;
	}
	public String getAttraktionName() {
		return attraktionName;
	}
	public void setAttraktionName(String attraktionName) {
		this.attraktionName = attraktionName;
	}
	public String getAttraktionThema() {
		return attraktionThema;
	}
	public void setAttraktionThema(String attraktionThema) {
		this.attraktionThema = attraktionThema;
	}
	public double getAttraktionTicketPreis() {
		return attraktionTicketPreis;
	}
	public void setAttraktionTicketPreis(double attraktionTicketPreis) {
		this.attraktionTicketPreis = attraktionTicketPreis;
	}
	public String getAttraktionBild() {
		return attraktionBild;
	}
	public void setAttraktionBild(String attraktionBild) {
		this.attraktionBild = attraktionBild;
	}
	public String getAttraktionAnmerkung() {
		return attraktionAnmerkung;
	}
	public void setAttraktionAnmerkung(String attraktionAnmerkung) {
		this.attraktionAnmerkung = attraktionAnmerkung;
	}
	public Boolean getAttraktionStatus() {
		return attraktionStatus;
	}
	public void setAttraktionStatus(Boolean attraktionStatus) {
		this.attraktionStatus = attraktionStatus;
	}
	public Boolean getOnlineTicketStatus() {
		return onlineTicketStatus;
	}
	public void setOnlineTicketStatus(Boolean onlineTicketStatus) {
		this.onlineTicketStatus = onlineTicketStatus;
	}
	
}
	
	