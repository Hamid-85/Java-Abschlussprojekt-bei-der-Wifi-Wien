package daten_verwaltung;

public class Freizeitpark {
	private int freizeitparkId;
	private String freizeitparkName;
	private String freizeitparkOrt;
	public Freizeitpark(int freizeitparkId, String freizeitparkName, String freizeitparkOrt) {
		super();
		this.freizeitparkId = freizeitparkId;
		this.freizeitparkName = freizeitparkName;
		this.freizeitparkOrt = freizeitparkOrt;
	}
	public int getFreizeitparkId() {
		return freizeitparkId;
	}
	public void setFreizeitparkId(int freizeitparkId) {
		this.freizeitparkId = freizeitparkId;
	}
	public String getFreizeitparkName() {
		return freizeitparkName;
	}
	public void setFreizeitparkName(String freizeitparkName) {
		this.freizeitparkName = freizeitparkName;
	}
	public String getFreizeitparkOrt() {
		return freizeitparkOrt;
	}
	public void setFreizeitparkOrt(String freizeitparkOrt) {
		this.freizeitparkOrt = freizeitparkOrt;
	}
	


}
