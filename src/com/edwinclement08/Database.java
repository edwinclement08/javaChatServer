package com.edwinclement08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class Database {
	final static Logger logger = Logger.getLogger(Database.class);

	public HashMap<Long, ArrayList<Message>> messagesDB;
	public HashMap<Long, Long> chatAgentsDBReadStatus;
	private HashSet<Long> registeredClients;

	public Database() {
		messagesDB = new HashMap<Long, ArrayList<Message>>();
		chatAgentsDBReadStatus = new HashMap<Long, Long>();
		registeredClients = new HashSet<Long>();
	}

	// TODO setup a notification system.
	public boolean registerChatAgent(long clientId) {
		List<Long> idFound = registeredClients.stream().filter(id -> (id == clientId)).collect(Collectors.toList());
		if (idFound.size() == 0) {
			logger.info("Client registering for the first time");
			registeredClients.add(clientId);
			chatAgentsDBReadStatus.put(clientId, 0L);
			ArrayList<Message> messageArray = new ArrayList<Message>();
			messagesDB.put(clientId, messageArray);
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
			ArrayList<Message> messageArray;
			if (!registeredClients.contains(receiverId)) {
				logger.info("The Receiver ChatAgent is not registered");
				logger.info("Creating a message queue for receiver:" + receiverId);
				messageArray = new ArrayList<Message>();
				messageArray.add(message);
			} else {
				messageArray = messagesDB.get(receiverId);
			}
			messagesDB.put(receiverId, messageArray);
			logger.info("Added the message to the queue.");

		} else {
			logger.error("The Sender ChatAgent is not registered");
		}
	}

}
