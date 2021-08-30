package daten_verwaltung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeSet;

public class Datenbank {
	private static final String DBLOCATTION = "C:\\java\\Freizeitpark";
	private static final String CONNSTRING = "jdbc:derby:" + DBLOCATTION + ";create=true";
	
	private static final String PARK_TABELLE = "Freizeitpark";
	private static final String PARK_ID = "FreizeitparkId";
	private static final String PARK_NAME = "FreizeitparkName";
	private static final String PARK_ORT = "FreizeitparkOrt";
	
	private static final String ATTRAKTION_TABELLE = "ATTRAKTION";
	private static final String ATTRAKTION_ID = "AttraktionId";
	private static final String ATTRAKTION_PARK_ID = "AttraktionParkId"; 
	private static final String ATTRAKTION_NAME = "AttraktionName";
	private static final String ATTRAKTION_THEMA = "AttraktionThema";
	private static final String ATTRAKTION_TICKET_PREIS = "AttraktionTicketPreis";
	private static final String ATTRAKTION_BILD = "AttraktionBild";
	private static final String ATTRAKTION_ANMERKUNG = "AttraktionAnmerkung";
	private static final String ATTRAKTION_DEACTIVE = "AttraktionStatus";
	private static final String ONLINETICKET_DEACTIVE = "OnlineTicketStatus";
		
	private static final String PACKAGE_TABELLE = "Package";
	private static final String PACKAGE_ID = "packageId";
	private static final String PACKAGE_PARK_ID = "PackageParkId";
	private static final String PACKAGE_NAME = "PackageName";
	private static final String PACKAGE_BILD = "PackageBild";
	private static final String PACKAGE_PREIS = "PackagePreis";
	private static final String PACKAGE_DEACTIVE = "PackageStatus";
	
	private static final String ATTRAKTIONBESTELLUNGEN_TABELLE = "AttraktionBestellungen";
	private static final String ATTRAKTIONBESTELLUNGEN_ID = "AttraktionBestellungenId";
	private static final String ATTRAKTIONBESTELLUNGEN_KUNDEN_ID  = "AttraktionBestellungenKundenId";
	private static final String ATTRAKTIONBESTELLUNGEN_ATTRAKTION_ID  = "AttraktionBestellungenAttraktionId";
	
	private static final String PACKAGESBESTELLUNGEN_TABELLE = "BestellungPackages";
	private static final String PACKAGESBESTELLUNGEN_ID = "PackageBestellungenId";
	private static final String PACKAGESBESTELLUNGEN_KUNDEN_ID  = "PackageBestellungenKundenId";
	private static final String PACKAGESBESTELLUNGEN_PACKAGE_ID  = "PackageBestellungenPackagesId";
	
	private static final String KUNDENTABLE = "Kunden";
	private static final String KUNDEN_ID = "KundeId";
	private static final String KUNDEN_VORNAME = "Vorname";
	private static final String KUNDEN_NACHNAME = "Nachname";
	private static final String KUNDEN_ADRESSE = "Adresse";
	private static final String KUNDEN_EMAIL = "Email";
	private static final String KUNDEN_BENUTZERNAME = "benutzername";
	private static final String KUNDEN_KENNWORT = "kennwort";

	/**
	 * Freizeitpark anlegen
	 * @throws SQLException
	 */
	public static void createFreizeitpark() throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNSTRING);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, PARK_TABELLE.toUpperCase(), new String[] {"TABLE"});
			if(rs.next()) {
				return;
			}
			String ct = "CREATE TABLE " + PARK_TABELLE + " (" +
					PARK_ID + " INTEGER GENERATED ALWAYS AS IDENTITY," +
                    PARK_NAME + " VARCHAR(200)," +
                    PARK_ORT + " VARCHAR(200)," +
					"PRIMARY KEY(" + PARK_ID + "))";
			stmt.executeUpdate(ct);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	public static void insertFreizeitpark(Freizeitpark f) throws SQLException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNSTRING);
			String insert = "INSERT INTO " + PARK_TABELLE + "(" + PARK_NAME + "," + PARK_ORT + 
		     ") VALUES(" +
			"?, " + 
			"?)";   
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, f.getFreizeitparkName());
			pstmt.setString(2, f.getFreizeitparkOrt());
			
			pstmt.executeUpdate();
			String identity = "SELECT IDENTITY_VAL_LOCAL() FROM " + PARK_TABELLE;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(identity);
			if(rs.next())
				f.setFreizeitparkId(rs.getInt("1"));
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					 stmt.close();
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	public static void updateFreizeitpark(Freizeitpark f) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DriverManager.getConnection(CONNSTRING);
			String insert = "UPDATE " + PARK_TABELLE + " SET " + PARK_NAME + "=?," +  PARK_ORT + "=? WHERE "+ PARK_ID + "=" 
					+ f.getFreizeitparkId();
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, f.getFreizeitparkName());
			pstmt.setString(2, f.getFreizeitparkOrt());
			pstmt.executeUpdate();
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	public static ArrayList<Freizeitpark> getFreizeitparken() throws SQLException {
		return getFreizeitparken(null);
	
	}


	public static ArrayList<Freizeitpark> getFreizeitparken(String ort) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Freizeitpark> alf = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(CONNSTRING);
			String select = "SELECT * FROM " + PARK_TABELLE;
			if(ort !=null)
				select += " WHERE " + PARK_ORT + "=?";
			stmt = conn.prepareStatement(select);
			if(ort !=null)
				stmt.setString(1, ort);
			rs = stmt.executeQuery();
			while(rs.next())
				alf.add(new Freizeitpark(rs.getInt(PARK_ID),rs.getString(PARK_NAME),rs.getString(PARK_ORT)));
			rs.close();
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				throw e;
			}
		}
		return alf;
	}
	
public static void createAttraktion() throws SQLException{
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {
		conn = DriverManager.getConnection(CONNSTRING);
		stmt = conn.createStatement();
		rs = conn.getMetaData().getTables(null, null, ATTRAKTION_TABELLE.toUpperCase(), new String[] {"TABLE"});
		if(rs.next()) {
			return;
		}
		String ct = "CREATE TABLE " + ATTRAKTION_TABELLE + " (" +
				ATTRAKTION_ID + " INTEGER GENERATED ALWAYS AS IDENTITY, " +
				ATTRAKTION_PARK_ID + " INTEGER," +
				ATTRAKTION_NAME + " VARCHAR(200)," +
                ATTRAKTION_THEMA + " VARCHAR(200)," +
				ATTRAKTION_TICKET_PREIS + " FLOAT," +
				ATTRAKTION_BILD + " VARCHAR(200)," +
				ATTRAKTION_ANMERKUNG + " VARCHAR(200)," +
				ATTRAKTION_DEACTIVE + " BOOLEAN," +
				ONLINETICKET_DEACTIVE + " BOOLEAN," +
				"PRIMARY KEY(" + ATTRAKTION_ID + ")," + 
				"FOREIGN KEY(" + ATTRAKTION_PARK_ID + ") REFERENCES " + PARK_TABELLE + "(" + PARK_ID + ")" +
				")";
		stmt.executeUpdate(ct);
	}
	catch(SQLException e) {
		throw e;
	}
	finally {
		try {
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		catch(SQLException e) {
			throw e;
		}
	}
}

public static void insertAttraktion(Attraktion attraktion) throws SQLException{
	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt = null;
	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String insert = "INSERT INTO " + ATTRAKTION_TABELLE + "(" + 
		ATTRAKTION_PARK_ID + ", " +
		ATTRAKTION_NAME + ", " +
	    ATTRAKTION_THEMA + ", " +
		ATTRAKTION_TICKET_PREIS +", "+ 
		ATTRAKTION_BILD +", " + 
		ATTRAKTION_ANMERKUNG +", " +
		ATTRAKTION_DEACTIVE +", " +
		ONLINETICKET_DEACTIVE +
		 " )VALUES(" +
		"?, " + 
		"?, " +
		"?, " +
		"?, " +
		"?, " +
		"?, " +
		"?, " +
		"?)";   
		pstmt = conn.prepareStatement(insert);
		pstmt.setInt(1, attraktion.getFreizeitpark().getFreizeitparkId());
		pstmt.setString(2, attraktion.getAttraktionName());
		pstmt.setString(3, attraktion.getAttraktionThema());
		pstmt.setDouble(4, attraktion.getAttraktionTicketPreis());
		pstmt.setString(5, attraktion.getAttraktionBild());
		pstmt.setString(6, attraktion.getAttraktionAnmerkung());
		pstmt.setBoolean(7, attraktion.getAttraktionStatus());
		pstmt.setBoolean(8, attraktion.getOnlineTicketStatus());
		pstmt.executeUpdate();
		String identity = "SELECT IDENTITY_VAL_LOCAL() FROM " + ATTRAKTION_TABELLE;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(identity);
		if(rs.next())
			attraktion.setAttraktionId(rs.getInt("1"));
	}
	catch(SQLException e) {
		throw e;
	}
	finally {
		try {
			if(stmt != null)
				 stmt.close();
			if(pstmt != null)
				pstmt.close();
			if(conn != null)
				conn.close();
		}
		catch(SQLException e) {
			throw e;
		}
	}
}

public static void updateAttraktionen(Attraktion attraktion) throws SQLException {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String insert = "UPDATE " + ATTRAKTION_TABELLE + " SET " +
		ATTRAKTION_PARK_ID + "=?," +
		ATTRAKTION_NAME + "=?," + 
		ATTRAKTION_THEMA + "=?," + 
		ATTRAKTION_TICKET_PREIS + "=?,"+
		ATTRAKTION_BILD +"=?,"+ 
		ATTRAKTION_ANMERKUNG + "=?," + 
		ATTRAKTION_DEACTIVE + "=?," + 
		ONLINETICKET_DEACTIVE + "=? WHERE "+
		ATTRAKTION_ID + "=" + attraktion.getAttraktionId();
		pstmt = conn.prepareStatement(insert);
		pstmt.setInt(1, attraktion.getFreizeitpark().getFreizeitparkId());
		pstmt.setString(2, attraktion.getAttraktionName());
		pstmt.setString(3, attraktion.getAttraktionThema());
		pstmt.setDouble(4, attraktion.getAttraktionTicketPreis());
		pstmt.setString(5, attraktion.getAttraktionBild());
		pstmt.setString(6, attraktion.getAttraktionAnmerkung());
		pstmt.setBoolean(7, attraktion.getAttraktionStatus());
		pstmt.setBoolean(8, attraktion.getOnlineTicketStatus());
		pstmt.executeUpdate();
	}
	catch(SQLException e) {
		throw e;
	}
	finally {
		try {
			if(pstmt != null)
				pstmt.close();
			if(conn != null)
				conn.close();
		}
		catch(SQLException e) {
			throw e;
		}
	}
}


public static ArrayList<Attraktion> getAttraktionen(int FreizeitparkId,Optional<Boolean> onlineTicket) throws SQLException {
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	ArrayList<Attraktion> alc = new ArrayList<>();
	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String select = "SELECT * FROM " + ATTRAKTION_TABELLE +
				" INNER JOIN " + PARK_TABELLE + " ON " + ATTRAKTION_TABELLE + "." +
				ATTRAKTION_PARK_ID + "=" + PARK_TABELLE + "." + PARK_ID ;
		String where="";
		if(FreizeitparkId != 0 )
			 where=  PARK_ID + "=? ";
		if(onlineTicket.isPresent())
		 where+= (where.length()> 0 ?  " AND " : "" )+  ONLINETICKET_DEACTIVE + "=?";
		if(where.length()> 0)
		select+=" WHERE " + where;
		stmt = conn.prepareStatement(select);
		int i=1;
		if(FreizeitparkId != 0  ) {
			stmt.setInt(1, FreizeitparkId);
			i++;
		}
		if(onlineTicket.isPresent())	
		stmt.setBoolean(i, onlineTicket.get());
		
		rs = stmt.executeQuery();
	
		while(rs.next()) {
			alc.add(new Attraktion(rs.getInt(ATTRAKTION_ID),
					new Freizeitpark(rs.getInt(PARK_ID),rs.getString(PARK_NAME),rs.getString(PARK_ORT)),
					rs.getString(ATTRAKTION_NAME),
					rs.getString(ATTRAKTION_THEMA),
					rs.getDouble(ATTRAKTION_TICKET_PREIS),
					rs.getString(ATTRAKTION_BILD),
					rs.getString(ATTRAKTION_ANMERKUNG),
					rs.getBoolean(ATTRAKTION_DEACTIVE),
					rs.getBoolean(ONLINETICKET_DEACTIVE)));
		}		
		rs.close();
	}
	catch(SQLException e) {
		throw e;
	}
	finally {
		try {
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		catch(SQLException e) {
			throw e;
		}
	}
	return alc;
}

public static void createPackage() throws SQLException{
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {
		conn = DriverManager.getConnection(CONNSTRING);
		stmt = conn.createStatement();
		rs = conn.getMetaData().getTables(null, null, PACKAGE_TABELLE.toUpperCase(), new String[] {"TABLE"});
		if(rs.next()) {
			return;
		}
		String ct = "CREATE TABLE " + PACKAGE_TABELLE + " (" +
				PACKAGE_ID + " INTEGER GENERATED ALWAYS AS IDENTITY, " +
				PACKAGE_PARK_ID + " INTEGER," +
				PACKAGE_NAME + " VARCHAR(200)," +
				PACKAGE_BILD + " VARCHAR(300)," +
                PACKAGE_PREIS + " FLOAT," +
				PACKAGE_DEACTIVE + " BOOLEAN," +
				"PRIMARY KEY(" + PACKAGE_ID + ")," + 
				"FOREIGN KEY(" + PACKAGE_PARK_ID + ") REFERENCES " + PARK_TABELLE + "(" + PARK_ID + ")" +
				")";
		stmt.executeUpdate(ct);
	}
	catch(SQLException e) {
		throw e;
	}
	finally {
		try {
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		catch(SQLException e) {
			throw e;
		}
	}
}

public static void insertPackage(Packages p) throws SQLException{
	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt = null;
	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String insert = "INSERT INTO " + PACKAGE_TABELLE + "(" + PACKAGE_PARK_ID + "," + PACKAGE_NAME + ","
			 + PACKAGE_BILD + "," + PACKAGE_PREIS + "," + PACKAGE_DEACTIVE +") VALUES(" +
		"?, " + 
		"?, " +
		"?, " +
		"?, " +
		"?)";   
		pstmt = conn.prepareStatement(insert);
		pstmt.setInt(1, p.getFreizeitpark().getFreizeitparkId());
		pstmt.setString(2, p.getPackageName());
		pstmt.setString(3, p.getPackageBild());
		pstmt.setDouble(4, p.getPackagePreis());
		pstmt.setBoolean(5, p.getPackageStatus());
		
		pstmt.executeUpdate();
		String identity = "SELECT IDENTITY_VAL_LOCAL() FROM " + PACKAGE_TABELLE;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(identity);
		if(rs.next())
			p.setPackageId(rs.getInt("1"));
	}
	catch(SQLException e) {
		throw e;
	}
	finally {
		try {
			if(stmt != null)
				 stmt.close();
			if(pstmt != null)
				pstmt.close();
			if(conn != null)
				conn.close();
		}
		catch(SQLException e) {
			throw e;
		}
	}
}

public static void updatePackages(Packages p) throws SQLException {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String insert = "UPDATE " + PACKAGE_TABELLE + " SET " + PACKAGE_PARK_ID + "=?," + PACKAGE_NAME + "=?," + 
				PACKAGE_BILD + "=?," + PACKAGE_PREIS + "=?," +  PACKAGE_DEACTIVE + "=? WHERE "+ PACKAGE_ID + "=" 
				+ p.getPackageId();
		pstmt = conn.prepareStatement(insert);
		pstmt.setInt(1, p.getFreizeitpark().getFreizeitparkId());
		pstmt.setString(2, p.getPackageName());
		pstmt.setString(3, p.getPackageBild());
		pstmt.setDouble(4, p.getPackagePreis());
		pstmt.setBoolean(5, p.getPackageStatus());
		pstmt.executeUpdate();
	}
	catch(SQLException e) {
		throw e;
	}
	finally {
		try {
			if(pstmt != null)
				pstmt.close();
			if(conn != null)
				conn.close();
		}
		catch(SQLException e) {
			throw e;
		}
	}
}


public static ArrayList<Packages> getPackages(int parkId) throws SQLException {
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	ArrayList<Packages> alc = new ArrayList<>();
	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String select = "SELECT * FROM " + PACKAGE_TABELLE +
				" INNER JOIN " + PARK_TABELLE + " ON " + PACKAGE_TABELLE + "." +
				PACKAGE_PARK_ID + "=" + PARK_TABELLE + "." + PARK_ID ;
		if(parkId != 0 )
			select+= " WHERE "+ PARK_ID + "=?" ;
		stmt = conn.prepareStatement(select);
		if(parkId != 0)
			stmt.setInt(1, parkId);
		rs = stmt.executeQuery();
		while(rs.next())
			alc.add(new Packages(rs.getInt(PACKAGE_ID),
					new Freizeitpark(rs.getInt(PARK_ID),rs.getString(PARK_NAME),rs.getString(PARK_ORT)),
					rs.getString(PACKAGE_NAME),
					rs.getString(PACKAGE_BILD),rs.getDouble(PACKAGE_PREIS),rs.getBoolean(PACKAGE_DEACTIVE)));
		rs.close();
	}
	catch(SQLException e) {
		throw e;
	}
	finally {
		try {
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		catch(SQLException e) {
			throw e;
		}
	}
	return alc;
}

public static void createAttraktionenOrders() throws SQLException {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {

		conn = DriverManager.getConnection(CONNSTRING);
		stmt = conn.createStatement();
		rs = conn.getMetaData().getTables(null, null, ATTRAKTIONBESTELLUNGEN_TABELLE.toUpperCase(), new String[] { "TABLE" });
		if (rs.next()) {
			return;
		}
		String ct = "CREATE TABLE " + ATTRAKTIONBESTELLUNGEN_TABELLE + " (" +
				ATTRAKTIONBESTELLUNGEN_ID + " INTEGER GENERATED ALWAYS AS IDENTITY, "+
				ATTRAKTIONBESTELLUNGEN_KUNDEN_ID + " BIGINT," + 
				ATTRAKTIONBESTELLUNGEN_ATTRAKTION_ID + " INTEGER," +
		 "PRIMARY KEY(" + ATTRAKTIONBESTELLUNGEN_ID + "),"+ 
		 "FOREIGN KEY(" + ATTRAKTIONBESTELLUNGEN_KUNDEN_ID +
		 ") REFERENCES " + KUNDENTABLE + "(" +
		 KUNDEN_ID + "),"+
		 "FOREIGN KEY(" + ATTRAKTIONBESTELLUNGEN_ATTRAKTION_ID + 
		 ") REFERENCES " + ATTRAKTION_TABELLE + "(" 
		 + ATTRAKTION_ID + ")" + ")";

		stmt.executeUpdate(ct);
	} catch (SQLException e) {
		throw e;
	} finally {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();

		} catch (SQLException e) {
			throw e;
		}
	}
}
public static void insertAttraktionenOrders(AttraktionBestellungenListe attraktionBestellungenListe) throws SQLException {

	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt = null;

	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String insert = "INSERT INTO " + ATTRAKTIONBESTELLUNGEN_TABELLE + " (" + ATTRAKTIONBESTELLUNGEN_KUNDEN_ID + ", " 
		+ ATTRAKTIONBESTELLUNGEN_ATTRAKTION_ID + " )VALUES("
				+ "?, " + 
				  "?)" ;
		pstmt = conn.prepareStatement(insert);
		pstmt.setLong(1, attraktionBestellungenListe.getKunde().getKundeId());
		pstmt.setInt(2, attraktionBestellungenListe.getAttraktion().getAttraktionId());
		pstmt.executeUpdate();

		String identity = "SELECT IDENTITY_VAL_LOCAL() FROM " + ATTRAKTIONBESTELLUNGEN_TABELLE;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(identity);
		if (rs.next())
			attraktionBestellungenListe.setAttraktionBestellungenId(rs.getInt("1"));

	} catch (SQLException e) {
		throw e;
	} finally {
		try {
			if (pstmt != null)
				pstmt.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();

		} catch (SQLException e) {
			throw e;
		}
	}
}
public static ArrayList<AttraktionBestellungenListe> getAttraktionenOrders(int parkId) throws SQLException {

	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	ArrayList<AttraktionBestellungenListe> alb = new ArrayList<>();

	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String select = "SELECT * FROM " + ATTRAKTIONBESTELLUNGEN_TABELLE + " INNER JOIN " + KUNDENTABLE + 
		" ON " + ATTRAKTIONBESTELLUNGEN_TABELLE + "." + ATTRAKTIONBESTELLUNGEN_KUNDEN_ID + "=" + KUNDENTABLE +
		"." + KUNDEN_ID + " INNER JOIN " + ATTRAKTION_TABELLE +  " ON " + ATTRAKTIONBESTELLUNGEN_TABELLE + 
		"." + ATTRAKTIONBESTELLUNGEN_ATTRAKTION_ID + "=" + ATTRAKTION_TABELLE + "." + ATTRAKTION_ID+ " INNER JOIN " +
				PARK_TABELLE +  " ON " + ATTRAKTION_TABELLE + "." + ATTRAKTION_PARK_ID + "=" + PARK_TABELLE + "." + PARK_ID;
		if(parkId != 0 )
			select+= " WHERE "+ ATTRAKTION_TABELLE +"."+ ATTRAKTION_PARK_ID + "=?" ;
		stmt = conn.prepareStatement(select);
		if(parkId != 0)
			stmt.setInt(1, parkId);
		rs = stmt.executeQuery();
		while (rs.next()) {
			alb.add(new AttraktionBestellungenListe(rs.getInt(ATTRAKTIONBESTELLUNGEN_ID),
					new Kunde(rs.getLong(KUNDEN_ID), rs.getString(KUNDEN_VORNAME),rs.getString(KUNDEN_NACHNAME),
							rs.getString(KUNDEN_ADRESSE),rs.getString(KUNDEN_EMAIL),
							rs.getString(KUNDEN_BENUTZERNAME),rs.getString(KUNDEN_KENNWORT)),
							new Attraktion(rs.getInt(ATTRAKTION_ID),
									new Freizeitpark(rs.getInt(PARK_ID),rs.getString(PARK_NAME),rs.getString(PARK_ORT)),
									rs.getString(ATTRAKTION_NAME),
									rs.getString(ATTRAKTION_THEMA),
									rs.getDouble(ATTRAKTION_TICKET_PREIS),
									rs.getString(ATTRAKTION_BILD),
								    rs.getString(ATTRAKTION_ANMERKUNG),
									rs.getBoolean(ATTRAKTION_DEACTIVE),
									rs.getBoolean(ONLINETICKET_DEACTIVE))));
					
		}
		rs.close();

	} catch (SQLException e) {
		throw e;
	} finally {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();

		} catch (SQLException e) {
			throw e;
		}
	}
	return alb;
}

public static void createPackagesOrders() throws SQLException {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {

		conn = DriverManager.getConnection(CONNSTRING);
		stmt = conn.createStatement();
		rs = conn.getMetaData().getTables(null, null, PACKAGESBESTELLUNGEN_TABELLE.toUpperCase(), new String[] { "TABLE" });
		if (rs.next()) {
			return;
		}
		String ct = "CREATE TABLE " + PACKAGESBESTELLUNGEN_TABELLE + " (" +
				PACKAGESBESTELLUNGEN_ID + " INTEGER GENERATED ALWAYS AS IDENTITY, "+
				PACKAGESBESTELLUNGEN_KUNDEN_ID + " BIGINT," + 
				PACKAGESBESTELLUNGEN_PACKAGE_ID + " INTEGER," +
		 "PRIMARY KEY(" + PACKAGESBESTELLUNGEN_ID + "),"+ 
		 "FOREIGN KEY(" + PACKAGESBESTELLUNGEN_KUNDEN_ID +
		 ") REFERENCES " + KUNDENTABLE + "(" +
		 KUNDEN_ID + "),"+
		 "FOREIGN KEY(" + PACKAGESBESTELLUNGEN_PACKAGE_ID + 
		 ") REFERENCES " + PACKAGE_TABELLE + "(" 
		 + PACKAGE_ID + ")" + ")";

		stmt.executeUpdate(ct);
	} catch (SQLException e) {
		throw e;
	} finally {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();

		} catch (SQLException e) {
			throw e;
		}
	}
}

public static void insertPackagesOrders(PackageBestellungenListe packageBestellungenListe) throws SQLException {

	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt = null;

	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String insert = "INSERT INTO " + PACKAGESBESTELLUNGEN_TABELLE + " (" + PACKAGESBESTELLUNGEN_KUNDEN_ID + ", " 
		+ PACKAGESBESTELLUNGEN_PACKAGE_ID + " )VALUES("
				+ "?, " + 
				  "?)" ;

		pstmt = conn.prepareStatement(insert);
		pstmt.setLong(1, packageBestellungenListe.getKunde().getKundeId());
		pstmt.setInt(2, packageBestellungenListe.getPackages().getPackageId());
		pstmt.executeUpdate();

		String identity = "SELECT IDENTITY_VAL_LOCAL() FROM " + PACKAGESBESTELLUNGEN_TABELLE;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(identity);
		if (rs.next())
			packageBestellungenListe.setPackageBestellungenId(rs.getInt("1"));

	} catch (SQLException e) {
		throw e;
	} finally {
		try {
			if (pstmt != null)
				pstmt.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();

		} catch (SQLException e) {
			throw e;
		}
	}
}
public static ArrayList<PackageBestellungenListe> getPackageBestellListe(int parkId) throws SQLException {

	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	ArrayList<PackageBestellungenListe> alb = new ArrayList<>();

	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String select = "SELECT * FROM " + PACKAGESBESTELLUNGEN_TABELLE + " INNER JOIN " + KUNDENTABLE + " ON " +
		PACKAGESBESTELLUNGEN_TABELLE + "." + PACKAGESBESTELLUNGEN_KUNDEN_ID + "=" + KUNDENTABLE + "." + 
		KUNDEN_ID + " INNER JOIN " + PACKAGE_TABELLE +  " ON " + PACKAGESBESTELLUNGEN_TABELLE +
		"." + PACKAGESBESTELLUNGEN_PACKAGE_ID + "=" + PACKAGE_TABELLE + "." + PACKAGE_ID + " INNER JOIN " +
		PARK_TABELLE +  " ON " + PACKAGE_TABELLE + "." + PACKAGE_PARK_ID + "=" + PARK_TABELLE + "." + PARK_ID;
		if(parkId != 0 )
			select+= " WHERE "+ PACKAGE_TABELLE +"."+ PACKAGE_PARK_ID + "=?" ;
		stmt = conn.prepareStatement(select);
		if(parkId != 0)
			stmt.setInt(1, parkId);
		rs = stmt.executeQuery();
		while (rs.next()) {
			alb.add(new PackageBestellungenListe(rs.getInt(PACKAGESBESTELLUNGEN_ID),
					new Kunde(rs.getLong(KUNDEN_ID), rs.getString(KUNDEN_VORNAME),rs.getString(KUNDEN_NACHNAME),
							rs.getString(KUNDEN_ADRESSE),rs.getString(KUNDEN_EMAIL),
							rs.getString(KUNDEN_BENUTZERNAME),rs.getString(KUNDEN_KENNWORT)),
					new Packages(rs.getInt(PACKAGE_ID),
							new Freizeitpark(rs.getInt(PARK_ID),rs.getString(PARK_NAME),rs.getString(PARK_ORT)),
							rs.getString(PACKAGE_NAME),
							rs.getString(PACKAGE_BILD),rs.getDouble(PACKAGE_PREIS),rs.getBoolean(PACKAGE_DEACTIVE))));
					
		}
		rs.close();

	} catch (SQLException e) {
		throw e;
	} finally {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();

		} catch (SQLException e) {
			throw e;
		}
	}
	return alb;
}

public static void createCustomers() throws SQLException {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {
		conn = DriverManager.getConnection(CONNSTRING);
		stmt = conn.createStatement();
		rs = conn.getMetaData().getTables(null, null, KUNDENTABLE.toUpperCase(), new String[] { "TABLE" });
		if (rs.next()) {
			return;
		}
		String ct = "CREATE TABLE " + KUNDENTABLE + "(" +
		KUNDEN_ID + " BIGINT GENERATED ALWAYS AS IDENTITY," +
		KUNDEN_VORNAME+ " VARCHAR(200)," + 
		KUNDEN_NACHNAME+ " VARCHAR(200)," + 
		KUNDEN_ADRESSE+ " VARCHAR(200)," + 
		KUNDEN_EMAIL+ " VARCHAR(200)," + 
		KUNDEN_BENUTZERNAME+ " VARCHAR(200)," + 
		KUNDEN_KENNWORT+ " VARCHAR(200)," + 
		"PRIMARY KEY(" + KUNDEN_ID + "))";

		stmt.executeUpdate(ct);
	} catch (SQLException e) {
		throw e;
	} finally {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();

		} catch (SQLException e) {
			throw e;
		}
	}
}
	
public static void insertCustomer(Kunde kunde) throws SQLException {

	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt = null;

	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String insert = "INSERT INTO " + KUNDENTABLE + "(" + KUNDEN_VORNAME + "," + KUNDEN_NACHNAME
				+ "," + KUNDEN_ADRESSE + "," + KUNDEN_EMAIL + "," + KUNDEN_BENUTZERNAME + ","
				+ KUNDEN_KENNWORT + ") VALUES(" + 
				"?, " + 
				"?, " + 
				"?, " + 
				"?, " + 
				"?, " + 
				"?)" ;
		pstmt = conn.prepareStatement(insert);
		pstmt.setString(1, kunde.getVorname());
		pstmt.setString(2, kunde.getNachname());
		pstmt.setString(3, kunde.getAdresse());
		pstmt.setString(4, kunde.getEmail());
		pstmt.setString(5, kunde.getBenutzername());
		pstmt.setString(6, kunde.getNachname());
		pstmt.executeUpdate();
		String identity = "SELECT IDENTITY_VAL_LOCAL() FROM " + KUNDENTABLE;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(identity);
		if (rs.next())
			kunde.setKundeId(rs.getLong("1"));

	} catch (SQLException e) {
		throw e;
	} finally {
		try {
			if (stmt != null)
				stmt.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();

		} catch (SQLException e) {
			throw e;
		}

	}
}

public static ArrayList<Kunde> getKunde() throws SQLException {
	return getKunde(null, null);
}
public static ArrayList<Kunde> getKunde(String benutzername, String kennwort) throws SQLException {

	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	ArrayList<Kunde> alk = new ArrayList<>();

	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String select = "SELECT * FROM " + KUNDENTABLE;
				if (benutzername != null) {
			select += " WHERE " + KUNDEN_BENUTZERNAME + "=? AND " + KUNDEN_KENNWORT + "=?";
		}
		stmt = conn.prepareStatement(select);
		if (benutzername != null) {
			stmt.setString(1, benutzername);
			stmt.setString(2, kennwort);
		}
		rs = stmt.executeQuery();
		while (rs.next()) {
			alk.add(new Kunde(rs.getLong(KUNDEN_ID), rs.getString(KUNDEN_VORNAME),rs.getString(KUNDEN_NACHNAME),
					rs.getString(KUNDEN_ADRESSE),rs.getString(KUNDEN_EMAIL),
					rs.getString(KUNDEN_BENUTZERNAME),rs.getString(KUNDEN_KENNWORT)));

		}

		rs.close();

	} catch (SQLException e) {
		throw e;
	} finally {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();

		} catch (SQLException e) {
			throw e;
		}
	}
	return alk;
}
public static void updateKunden(Kunde k) throws SQLException {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
		conn = DriverManager.getConnection(CONNSTRING);
		String update = "UPDATE " + KUNDENTABLE + " SET " + KUNDEN_VORNAME + "=?," + KUNDEN_NACHNAME + "=?," + 
				KUNDEN_ADRESSE + "=?," + KUNDEN_EMAIL + "=?," +  KUNDEN_BENUTZERNAME + "=?," 
				+ KUNDEN_KENNWORT + "=? WHERE "+ KUNDEN_ID + "=" 
				+ k.getKundeId();
		pstmt = conn.prepareStatement(update);
		pstmt.setString(1, k.getVorname());
		pstmt.setString(2, k.getNachname());
		pstmt.setString(3, k.getAdresse());
		pstmt.setString(4, k.getEmail());
		pstmt.setString(5, k.getBenutzername());
		pstmt.setString(6, k.getNachname());
		pstmt.executeUpdate();
	}
	catch(SQLException e) {
		throw e;
	}
	finally {
		try {
			if(pstmt != null)
				pstmt.close();
			if(conn != null)
				conn.close();
		}
		catch(SQLException e) {
			throw e;
		}
	}
}

	}

