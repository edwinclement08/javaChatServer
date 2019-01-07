package com.edwinclement08.client;

import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;

import com.edwinclement08.Message;
import com.edwinclement08.Packet;
import com.edwinclement08.Packet.PacketType;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "ChatAgent", footer = "Copyright(c) 2019",
description = "ChatClient for the multiChat")
class ChatAgentCommandLine 	{
    @Option(names = { "-v", "--verbose" }, description = "Be verbose.")
    private boolean verbose = false;
    
    @Option(names = { "-i", "--id" }, description = "Set the client")
    Long clientId;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Display help")
    boolean usageHelpRequested;
 
    @Option(names = {"-m", "--message"}, description = "Message to Send")
    String sendMessage;
    
    @Option(names = {"-r", "--receiver"}, description = "Id for receiver")
    Long receiverId;
    
    @Option(names = {"-c", "--check"},  description = "Check For Messages")
    boolean checkMessage;
    
}


public class ChatAgent {
	final static Logger logger = Logger.getLogger(ChatAgent.class);

	public static ChatClient chatClient;
	
	public static void main(String[] args) {
		ChatAgentCommandLine arguments = CommandLine.populateCommand(new ChatAgentCommandLine(), args);
		
		if(arguments.usageHelpRequested)	{
			CommandLine.usage(arguments, System.out);
			System.exit(0);
		}
		
		chatClient = new ChatClient();
		chatClient.assignClientId(12);

		if(arguments.clientId != null)	{
		
			chatClient.assignClientId(arguments.clientId);
		}
		
		//prevent 0 as clientId
		 while(chatClient.clientId == 0) {
			 chatClient.assignClientId(new Random().nextLong());
		 }
		
		 registerAgent();

		 if(arguments.sendMessage != null)	{
			 if(arguments.receiverId == null){
				 System.out.println("Please use -r cmd option too");
			 } else {
				 sendMessage(arguments.sendMessage , arguments.receiverId);
			 }
		 } else if(arguments.checkMessage){
			 ArrayList<Message> unreadMessages =checkMessages();
			 logger.info(unreadMessages);
		 }
	}
	
	public static void sendMessage(String messageString, long receiver)	{
		Message message = new Message(messageString, chatClient.clientId, receiver);
		Packet packet = new Packet(Packet.PacketType.SEND_MESSAGE, message);
		logger.info("Sending packet:"+ packet);
		Packet resp = chatClient.sendPacket(packet);
		logger.info("Got back the acknowledgement:"+ resp);
	}
	
	public static void registerAgent()	{
		Packet packet = Packet.getRegisterPacket(chatClient.clientId);
		logger.info("Registering the client:" + packet);
		Packet resp = chatClient.sendPacket(packet);
		logger.info(resp);
	}
	
	public static ArrayList<Message> checkMessages()	{
		Packet packet = new Packet(chatClient.clientId, PacketType.CHECK_MESSAGE);
		
		Packet resp = chatClient.sendPacket(packet);
		logger.info(resp);
		
		// TODO Complete this stub
		@SuppressWarnings("unchecked")
		ArrayList<Message> unreadMessages = (ArrayList<Message>) resp.getPayload();
		
		return unreadMessages;
	}
}
