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
	public HashMap<Long, Integer> chatAgentsDBReadStatus;
	private HashSet<Long> registeredClients;

	public Database() {
		logger.error("Inited");
		messagesDB = new HashMap<Long, ArrayList<Message>>();
		chatAgentsDBReadStatus = new HashMap<Long, Integer>();
		registeredClients = new HashSet<Long>();
	}

	// TODO setup a notification system.
	public boolean registerChatAgent(long clientId) {
		List<Long> idFound = registeredClients.stream().filter(id -> (id == clientId)).collect(Collectors.toList());
		if (idFound.size() == 0) {
			logger.info("Client registering for the first time");
			registeredClients.add(clientId);
			chatAgentsDBReadStatus.put(clientId, 0);
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
			
			if(messagesDB.containsKey(receiverId))	{
				messageArray = messagesDB.get(receiverId);
			} else {
				messageArray = new ArrayList<Message>();
			}
			
			// add the message to the queue
			messageArray.add(message);

			// updating the unread count
			if(!chatAgentsDBReadStatus.containsKey(receiverId))	{
				chatAgentsDBReadStatus.put(receiverId, 0);
			}
			int numberOfMessagesUnread =  chatAgentsDBReadStatus.get(receiverId);
			chatAgentsDBReadStatus.put(receiverId, numberOfMessagesUnread+1);

			
			messagesDB.put(receiverId, messageArray);
			logger.info("Added the message to the queue.");
			logger.info(messagesDB.get(receiverId));

		} else {
			logger.error("The Sender ChatAgent is not registered");
		}
	}
	
	public ArrayList<Message> checkMessage(long senderId){
		ArrayList<Message> unreadMessages = new ArrayList<Message>();

		if(messagesDB.containsKey(senderId))	{
			int numberOfMessagesUnread = (int) chatAgentsDBReadStatus.get(senderId);
			
			ArrayList<Message> messages = messagesDB.get(senderId);
			
			int totalLength = messages.size();
			logger.info(messages);
			logger.info(totalLength);
			logger.info(numberOfMessagesUnread);

			// create a list view with only unread messages
			if(numberOfMessagesUnread > 0)	{
				unreadMessages.addAll(messages.subList(totalLength-1-numberOfMessagesUnread, totalLength));
			}

			logger.info(unreadMessages);					
		} else {
			logger.info("Sender "+ senderId + " has no message queue");
		}
		return unreadMessages;

		
	}

}
