package com.edwinclement08;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class Database {
	final static Logger logger = Logger.getLogger(Database.class);

	static Database _instance;
	static SQLiteJDBCDriverConnection sqlite;

	private HashSet<Long> registeredClients;



	private Database() {

		registeredClients = new HashSet<Long>();
		
		sqlite = new SQLiteJDBCDriverConnection();
		sqlite.getConnection();
		sqlite.initSetup();		// create the objects

	}

	public static Database getInstance() {
		if (_instance == null) {
			_instance = new Database();
		}
		return _instance;
	}

	// TODO setup a notification system. to tell clients to check messages
	public boolean registerChatAgent(long clientId) {
		List<Long> idFound = registeredClients.stream().filter(id -> (id == clientId)).collect(Collectors.toList());
		if (idFound.size() == 0) {
			logger.info("Client registering for the first time");
			registeredClients.add(clientId);
			return true;
		} else if (idFound.size() == 1) {
			// Else the client has appeared at least once
			logger.info("Client has appeared bfr.");
			return true;
		} else {
			return false;
		}
	}

	public void sendMessage(long senderId, long receiverId, Message message) {
	
		if (registeredClients.contains(senderId)) {
			sqlite.insertToMessagesDB(receiverId, message);
			logger.info("Added the message to the database.");
		} else {
			logger.error("The Sender ChatAgent is not registered");
		}
	}

	public ArrayList<Message> checkMessage(long senderId) {

		return sqlite.checkMessages(senderId);

	}

}
