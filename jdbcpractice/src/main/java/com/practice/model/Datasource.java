package com.practice.model;

import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import java.util.ArrayList;

public class Datasource {
	public static final String DB_NAME = "";

	public static final String CONNECTION_STRING = "jdbc:oracle:thin:@sandeepsutar:1522/XE";
	public static final String TABLE_ALNBUM = "albums";
	public static final String COLUMN_ALBUM_ID = "id";
	public static final String COLUMN_ALBUM_NAME = "name";
	public static final String COLUMN_ALBUM_ARTIST = "artist";
	public static final int INDEX_ALBUM_ID = 1;
	public static final int INDEX_ALBUM_NAME = 2;
	public static final int INDEX_ALBUM_ARTIST = 3;

	public static final String TABLE_ARTISTS = "artists";
	public static final String COLUMN_ARTIST_ID = "id";
	public static final String COLUMN_ARTIST_NAME = "name";
	public static final int INDEX_ARTIST_ID = 1;
	public static final int INDEX_ARTIST_NAME = 2;

	public static final String TABLE_SONGS = "songs";
	public static final String COLUMN_SONG_ID = "id";
	public static final String COLUMN_SONG_TRACK = "track";
	public static final String COLUMN_SONG_TITLE = "title";
	public static final String COLUMN_SONG_ALBUM = "album";
	public static final int INDEX_SONG_ID = 1;
	public static final int INDEX_SONG_TRACK = 2;
	public static final int INDEX_SONG_TITLE = 3;
	public static final int INDEX_SONG_ALBUMID = 4;

	public static final String QUERY_ALBUM_NY_ARTIST_START = "";
	public static final String QUERY_ARTIST_FOR_SONG_START = "";
	public static final String QUERY_ARTIST_FOR_SONG_SORT = "";

	private Connection conn;

	public boolean open() {
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING, "scott", "tiger");
			System.out.println("Connection Opened");
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Connection Closed");
			return false;
		}
	}

	public void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public List<Artists> queryArtists() throws SQLException {
		Statement statement = null;
		ResultSet rs = null;

		List<Artists> ar = new ArrayList<Artists>();

		try {
			// open connection
			this.open();
			statement = conn.createStatement();
			rs = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS);
			while (rs.next()) {
				Artists artist = new Artists();
				artist.setId(rs.getInt(COLUMN_ARTIST_ID));
				artist.setName(rs.getString(COLUMN_ARTIST_NAME));
				ar.add(artist);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return ar;

	}

	public List<Artists> queryAlbumsForArtist() {
		List<Artists> artist = new ArrayList<Artists>();
		StringBuffer sb = new StringBuffer("SELECT ");
		sb.append(COLUMN_ALBUM_ID).append(COLUMN_ALBUM_NAME);

		return artist;

	}

	public static final int ORDER_BY_NANE = 1;
	public static final int ORDER_BY_DESC = 0;

	public List<SongArtist> queryArtistForSong(String songName, int sortOrder) {
		List<SongArtist> songArtists = new ArrayList<SongArtist>();
		StringBuilder sb = new StringBuilder();
		sb.append(songName).append("\"");

		if (sortOrder != ORDER_BY_NANE) {
			sb.append(QUERY_ARTIST_FOR_SONG_SORT);
			if (sortOrder == ORDER_BY_DESC)
				sb.append("DESC");
			else
				sb.append("ASC");
		}

		System.out.println("SQL Statement: " + sb.toString());
		try {
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sb.toString());

			while (result.next()) {
				SongArtist songArtist = new SongArtist();
				songArtist.setAlbumName(result.getString(1));
				songArtist.setArtistName(result.getString(2));
				songArtist.setTrack(result.getInt(3));
				songArtists.add(songArtist);
			}

			if (statement != null)
				statement.close();
			if (result != null)
				result.close();
		} catch (SQLException e) {

		}

		return songArtists;

	}

	public void querySongMetaData() {
		String sql = "SELECT * FROM " + TABLE_SONGS;

		try {
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			ResultSetMetaData met = result.getMetaData();
			int numColumns = met.getColumnCount();
			for (int i = 1; i <= numColumns; i++) {
				System.out.format("Column %d in the song table is names %s\n", i, met.getColumnName(i));
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
	public static final String create_artist_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " + TABLE_ARTIST_SONG_VIEW
			+ "AS SELECT " + TABLE_ARTISTS;
	public final String QUERY_VIEW_SONG_INFO = "";

	public int getCount(String table) throws SQLException {
		String sql = "SELECT COUNT(1) , min(id) AS minid FROM " + table;
		Statement statement = conn.createStatement();
		ResultSet result = statement.executeQuery(sql);

		int count = result.getInt(1);
		int min = result.getInt("minid");

		if (statement != null)
			statement.close();
		if (result != null)
			result.close();

		return count;
	}

	public List<SongArtist> querySongInfoView(String title) {
		List<SongArtist> songArtists = new ArrayList<SongArtist>();
		StringBuilder sb = new StringBuilder(QUERY_VIEW_SONG_INFO);
		sb.append("\"");
		System.out.println(sb.toString());

		try {
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sb.toString());

			while (result.next()) {
				SongArtist songArtist = new SongArtist();
				songArtist.setAlbumName(result.getString(1));
				songArtist.setArtistName(result.getString(2));
				songArtist.setTrack((result.getInt(3)));

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return songArtists;
	}
}
