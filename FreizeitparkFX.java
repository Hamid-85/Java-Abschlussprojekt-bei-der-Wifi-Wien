package daten_verwaltung;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FreizeitparkFX {
	private Freizeitpark modellFreizeitpark;
	private SimpleIntegerProperty freizeitparkId;
	private SimpleStringProperty freizeitparkName;
	private SimpleStringProperty freizeitparkOrt;

	public FreizeitparkFX(Freizeitpark f) {
		modellFreizeitpark = f;
		freizeitparkId = new SimpleIntegerProperty(f.getFreizeitparkId());
		freizeitparkName = new SimpleStringProperty(f.getFreizeitparkName());
		freizeitparkOrt = new SimpleStringProperty(f.getFreizeitparkOrt());
		
	}
	public Freizeitpark getModellFreizeitpark() {
		return modellFreizeitpark;
	}
	public int getFreizeitparkId() {
		return freizeitparkId.get();
	}
	public SimpleIntegerProperty freizeitparkIdProperty() {
		return freizeitparkId;
	}

	public String getFreizeitparkName() {
		return freizeitparkName.get();
	}

	public void setFreizeitparkName(String f) {
		freizeitparkName.set(f);
		modellFreizeitpark.setFreizeitparkName(f);
	}

	public SimpleStringProperty freizeitparkNameProperty() {
		return freizeitparkName;
	}
	public String getFreizeitparkOrt() {
		return freizeitparkOrt.get();
	}

	public void setFreizeitparkOrt(String o) {
		freizeitparkOrt.set(o);
		modellFreizeitpark.setFreizeitparkOrt(o);
	}

	public SimpleStringProperty freizeitparkOrtProperty() {
		return freizeitparkOrt;
	}
	}