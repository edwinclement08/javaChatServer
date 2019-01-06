package com.edwinclement08.server;

import org.apache.log4j.Logger;

import com.edwinclement08.Database;
import com.edwinclement08.Message;
import com.edwinclement08.Packet;


public class ServerPacketHandler {
	final static Logger logger = Logger.getLogger(ServerPacketHandler.class);
	final static Database database  = new Database();
	

	ServerPacketHandler()	{
		ChatServer chatServer = new ChatServer(this::callBackHandler);	
	}
	
	public Packet callBackHandler(Packet packet){
		Packet output = new Packet();
		if(packet.getPacketType() == Packet.PacketType.SEND_MESSAGE)	{
			Message message = packet.getMessage();
			database.sendMessage(message.senderId, message.receiverId, message);
			logger.info(message);
		} else if (packet.getPacketType() == Packet.PacketType.REGISTER_AGENT)	{
			database.registerChatAgent(packet.getDeviceId());
		}
		return output;
	}
	
	public static void main(String[ ] args)	{
		ServerPacketHandler serverPacketHandler = new ServerPacketHandler();
		
	}
}
