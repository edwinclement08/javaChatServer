package com.edwinclement08;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class SQLiteJDBCDriverConnection {
	private Connection conn = null;

	private void connect() {
		try {
			String url = "jdbc:sqlite:./database.db";
			conn = DriverManager.getConnection(url);
			Database.logger.info("Connection to SQLite has been established.");
		} catch (SQLException e) {
			Database.logger.fatal(e.getMessage());
		} 
	}
	public Connection getConnection()	{
		if(conn == null){
			connect();
		}
		return conn;	
	}
	
	public void closeConnection()	{
		if(conn != null)	{
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				Database.logger.error(ex.getMessage());
			}
		}
	}
}