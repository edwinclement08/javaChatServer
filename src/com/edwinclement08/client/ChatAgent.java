package com.edwinclement08.client;

import org.apache.log4j.Logger;

import com.edwinclement08.Message;
import com.edwinclement08.Packet;

import picocli.CommandLine;
import picocli.CommandLine.Option;

class ChatAgentCommandLine 	{
    @Option(names = { "-v", "--verbose" }, description = "Be verbose.")
    private boolean verbose = false;
    
    @Option(names = { "-i", "--id" }, description = "Set the client")
    Long clientId;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Display help")
    boolean usageHelpRequested;
}

public class ChatAgent {
	final static Logger logger = Logger.getLogger(ChatAgent.class);

	public static ChatClient chatClient;
	
	public static void main(String[] args) {
		ChatAgentCommandLine arguments = CommandLine.populateCommand(new ChatAgentCommandLine(), args);
		System.out.println("command line client id " + arguments.clientId);
	
		if(arguments.usageHelpRequested)	{
			CommandLine.usage(arguments, System.out);
			System.exit(0);
		}
		
		chatClient = new ChatClient();
		if(arguments.clientId != null)	{
			
			chatClient.assignClientId(arguments.clientId);
		}
		
		
		chatClient.assignClientId(12);		
		registerAgent();
		sendMessage("Testing the Chat Agent", 24);
	}
	
	
	public static void sendMessage(String messageString, long receiver)	{
		Message message = new Message(messageString, chatClient.clientId, receiver);
		Packet packet = new Packet(Packet.PacketType.SEND_MESSAGE, message);
		logger.info(packet);
		chatClient.sendPacket(packet);
	}
	
	public static void registerAgent()	{
		Packet packet = Packet.getRegisterPacket(chatClient.clientId);
		chatClient.sendPacket(packet);
	}
}
