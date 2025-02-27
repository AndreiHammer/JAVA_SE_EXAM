package eu.ase.exam.db;

import eu.ase.exam.Animal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Database {
	private static Connection c;
	
	public static void setConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    }
	
	public static void createTable() throws SQLException {
		String sqlDrop = "DROP TABLE IF EXISTS ANIMALS";
		String sqlCreate = "CREATE TABLE ANIMALS"
				+"(ID INT PRIMARY KEY, NAME TEXT, NOLEGS INT, WEIGHT REAL)";
		Statement stmt = c.createStatement();
		stmt.executeUpdate(sqlDrop);
		stmt.executeUpdate(sqlCreate);
		stmt.close();
		c.commit();
	}
	
	public static void insertData(List<Animal> list) throws SQLException {
		String insert = "INSERT INTO ANIMALS (ID,NAME,NOLEGS,WEIGHT) VALUES (?,?,?,?)";
		PreparedStatement ps = c.prepareStatement(insert);
		
		int i =0;
		for(Animal a : list) {
			i++;
			ps.setInt(1, i);
			ps.setString(2, a.getName());
			ps.setInt(3, a.getNoLegs());
			ps.setFloat(4, a.getWeight());
			
			ps.executeUpdate();
		}
		
		ps.close();
		c.commit();
	}
	
	public static String selectData() throws SQLException {
		String select = "SELECT * FROM ANIMALS";
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(select);
		StringBuilder sb = new StringBuilder();
		while(rs.next()) {
			sb.append(String.format("\n\r %d : %s : %d : %.2f", rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getFloat(4)));
		}
		rs.close();
		stmt.close();
		return sb.toString();
	}
	
	public static void closeConnection() {
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
