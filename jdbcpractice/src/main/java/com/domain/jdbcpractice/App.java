package com.domain.jdbcpractice;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.practice.model.Artists;
import com.practice.model.Datasource;

import java.sql.ResultSet;
import java.sql.DriverManager;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws SQLException {
		Datasource d = new Datasource();		
		
		
		try {
			List<Artists> artists = d.queryArtists();
			if(artists == null)
			{
				System.out.println("No Artists");
				System.out.println("No Artists");
				return;
			}			
			d.querySongMetaData();
			for(Artists artist: artists)
			{
				System.out.println("ID" + artist.getId() +" ,     Name "+  artist.getName());
			}
			
		}catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		
		
		d.close();
		
		
		
		
		
		
		
		/*
		System.out.println("Hello World!");
		Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@sandeepsutar:1522/XE", "scott", "tiger");
		System.out.println("here");
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery("SELECT * FROM emp");

		while (result.next()) {
			System.out.println(result.getString("ename") + "\n");
			System.out.println(result.getString("job"));

		}

		
		insertAuthor(statement,1,"Denis","Richie");
		insertAuthor(statement,2,"Shashi","Tharur");
		insertAuthor(statement,3,"Raghuram","Rajan");
		insertAuthor(statement,4,"Manmohan","Sighn");
		insertAuthor(statement,5,"Robert","Frost");
		
		connection.commit();
		statement.close();
		connection.close();
		System.out.println("Completed");*/
		
	}

	private static void insertAuthor(Statement stmt, int id, String fname, String lName) {
		try {
			String str = "INSERT INTO AUTHOR VALUES(" + id + ",'"  + fname + "','" + lName+"')";
			System.out.println(str);
			stmt.execute(str);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			System.out.println(e.getMessage());
			System.out.println(e.getMessage());
			
		}

	}
}
