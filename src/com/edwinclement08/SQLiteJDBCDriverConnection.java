package com.edwinclement08;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

class SQLiteJDBCDriverConnection {
	final static Logger logger = Logger.getLogger(SQLiteJDBCDriverConnection.class);

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

	public Connection getConnection() {
		if (conn == null) {
			connect();
		}
		return conn;
	}

	public void closeConnection() {
		if (conn != null) {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				Database.logger.error(ex.getMessage());
			}
		}
	}

	// messagesDB long, Messages()[String]
	// readStatus long ,integer
	public void initSetup() {
		String sql_create_messagesDB = "CREATE TABLE IF NOT EXISTS messagesDB (\n" + "	serial_order INTEGER PRIMARY KEY AUTOINCREMENT, clientid INTEGER,\n"
				+ "	message text NOT NULL\n" + ");";

		String sql_create_readStatus = "CREATE TABLE IF NOT EXISTS readStatus (\n" + "	clientid INTEGER PRIMARY KEY,\n"
				+ "	unread_messages INTEGER NOT NULL\n" + ");";

		try {
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			// create a new table
			stmt.execute(sql_create_messagesDB);
			stmt.execute(sql_create_readStatus);
			logger.info("Tables exist or have been created");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	// TODO add return success values

	public void insertToMessagesDB(long clientId, Message message) {
		String insertMessage = "INSERT INTO messagesDB(clientid,message) VALUES(?,?)";
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(insertMessage);
			pstmt.setLong(1, clientId);
			pstmt.setString(2, message.toString());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}

		int unreadStatus = getUnreadStatus(clientId);
		logger.info(unreadStatus + " is the number of unread messages");;
		updateUnreadStatus(clientId, unreadStatus + 1);
	}

	public int getUnreadStatus(long clientId) {
		String getReadStatus = "SELECT clientid, unread_messages FROM readStatus";
		int unreadMessages = 0;

		try {
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getReadStatus);

			// loop through the result set
			int count = 0;
			while (rs.next()) {
				count++;
				unreadMessages = rs.getInt("unread_messages");
				System.out.println(rs.getInt("clientid") + "\t" + rs.getInt("unread_messages") + "\n");
			}
			if (count > 1) {
				logger.error("Multiple Entries of client in unreadstatus");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		if(unreadMessages ==0)	{
			createEntryForUnreadStatus(clientId);
		}
		return unreadMessages;
	}
	public void createEntryForUnreadStatus(long clientId)	{
		
		String sql_createEntryForUnreadStatus = "INSERT INTO readStatus(clientid,unread_messages) VALUES(?,?)";
		 
        try {
        	Connection conn = getConnection();
        	PreparedStatement pstmt = conn.prepareStatement(sql_createEntryForUnreadStatus);
            pstmt.setLong(1, clientId);
            pstmt.setInt(2, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
	}

	public void updateUnreadStatus(long clientId, int unreadCount) {
		String sql_updateUnreadstatus = "UPDATE readStatus SET unread_messages = ?  " + "WHERE clientid = ?";

		try {
			Connection conn = getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql_updateUnreadstatus);

			// set the corresponding param
			pstmt.setInt(1, unreadCount);
			pstmt.setLong(2, clientId);
			// update
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	// messagesDB(clientid,message) VALUES(?,?)";
	public ArrayList<Message> checkMessages(long clientId)	{
		ArrayList<Message> unreadMessageArray = new ArrayList<Message>();
		String sql_checkMessages = "SELECT clientid, message FROM messagesDB ORDER BY serial_order DESC LIMIT ?";

		int unreadMessagesCount = getUnreadStatus(clientId);

		try {
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql_checkMessages);
			pstmt.setInt(1, unreadMessagesCount);
			
			ResultSet rs = pstmt.executeQuery();

			// loop through the result set
			int count = 0;
			while (rs.next()) {
				count++;
				String messageString = rs.getString("message");
				logger.info(messageString);
				Message m = Message.fromString(messageString);
				unreadMessageArray.add(m);
			}
				logger.info("Number of unread messages:" + count);
				if(count == unreadMessagesCount)	{
					logger.info(("all messages read, consistent"));
					updateUnreadStatus(clientId, 0);
				} else {
					logger.error("Somehow inconsistent unreadmessage count in messageDB(" + count +
							") and unreadStatusDB(" + unreadMessagesCount +")");
				}
		} catch (SQLException e) {
			logger.info(e.getMessage());
		}
		return unreadMessageArray;
		
	}

}