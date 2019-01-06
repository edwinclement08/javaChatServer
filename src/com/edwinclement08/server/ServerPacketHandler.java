package com.edwinclement08.server;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.edwinclement08.Database;
import com.edwinclement08.Message;
import com.edwinclement08.Packet;
import com.edwinclement08.Packet.PacketType;


public class ServerPacketHandler {
	final static Logger logger = Logger.getLogger(ServerPacketHandler.class);
	final static Database database  = new Database();
	

	ServerPacketHandler()	{
		@SuppressWarnings("unused")
		ChatServer chatServer = new ChatServer(this::callBackHandler);	
	}
	
	public Packet callBackHandler(Packet packet){
		if(packet.getPacketType() == Packet.PacketType.SEND_MESSAGE)	{
			Message message = packet.getMessage();
			database.sendMessage(message.senderId, message.receiverId, message);
			logger.info(message);
			return new Packet("", PacketType.SEND_MESSAGE_SUCCESS);
			
		} else if (packet.getPacketType() == Packet.PacketType.REGISTER_AGENT)	{
			if(database.registerChatAgent(packet.getDeviceId()))	{
				return new Packet("", PacketType.REGISTER_AGENT_SUCCESS);
			} else {
				logger.error("Unable to register client");
				return new Packet("", PacketType.ERROR);
			}
		} else if (packet.getPacketType() == Packet.PacketType.CHECK_MESSAGE)	{
			long receiverId = (long) packet.getPayload();
			ArrayList<Message> unreadMessages = database.checkMessage(receiverId);
			Packet respPacket = new Packet(unreadMessages, PacketType.CHECK_MESSAGE_RESPONSE);
			return respPacket;
		} else {
			logger.error("Unidentified packet type");
			return new Packet("", PacketType.ERROR);
			
		}
	}
	
	public static void main(String[ ] args)	{
		@SuppressWarnings("unused")
		ServerPacketHandler serverPacketHandler = new ServerPacketHandler();
		
	}
}
