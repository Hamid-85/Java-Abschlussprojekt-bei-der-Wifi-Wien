package daten_verwaltung;

import java.sql.SQLException;



public class Datenbank_main {

	public static void main(String[] args) {
		try {
			
			Datenbank.createFreizeitpark();
			
			
			System.out.println("Table created");
		} catch (SQLException e) {
			System.out.println(e);
			return;
		}

	}

}
