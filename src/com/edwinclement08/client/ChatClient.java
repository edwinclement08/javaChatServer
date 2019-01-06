package com.edwinclement08.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import org.apache.log4j.Logger;

import com.edwinclement08.Packet;
import com.edwinclement08.ServerConfig;

import picocli.CommandLine;
import picocli.CommandLine.Option;

class ChatClientCommandLine 	{
    @Option(names = { "-v", "--verbose" }, description = "Be verbose.")
    private boolean verbose = false;
    
    @Option(names = { "-i", "--id" }, description = "Set the client")
    Long clientId;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Display help")
    boolean usageHelpRequested;
}

public class ChatClient {
    private static Socket socket;
    final static Logger logger = Logger.getLogger(ChatClient.class);
    
    long clientId;
    

    public  void initConnection()	{
    	  String host = ServerConfig.host;
          int port = ServerConfig.port;
          
          try	{
        	  InetAddress address = InetAddress.getByName(host);
              socket = new Socket(address, port);  
          } catch (Exception e){
        	  logger.error("Failed to initialize socket.");
          }
    }
    
    public  Packet sendPacket(Packet packet)	{
    	packet.setDeviceId(clientId);
		packet.setDeviceType("client");
		
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        
        String packetStringRecv = "";
        
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
		    logger.info("Sending packet to Socket Server");
			oos.writeObject(packet);
			//read the server response message
			ois = new ObjectInputStream(socket.getInputStream());
			Packet packetRecv = (Packet) ois.readObject();
			System.out.println("Return Packet: " + packetRecv);
			//close resources
			ois.close();
			oos.close();

		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Unable to set up OutputStream");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
        return Packet.fromString(packetStringRecv);

    }
    
    
    public  void closeConnection()	{
    	
        try
        {
            socket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChatClientCommandLine arguments = CommandLine.populateCommand(new ChatClientCommandLine(), args);
		System.out.println("command line client id " + arguments.clientId);
	
		if(arguments.usageHelpRequested)	{
			CommandLine.usage(arguments, System.out);
			System.exit(0);
		}
		
		ChatClient chatClient = new ChatClient();
		if(arguments.clientId != null)	{
			chatClient.assignClientId(arguments.clientId);
		}

		Packet packet = new Packet("eded", Packet.PacketType.DEV);
		chatClient.sendPacket(packet);
		chatClient.closeConnection();
	}
	
	public ChatClient() {
		clientId = new Random().nextLong();
		logger.info("Client Initializing");
		initConnection();
	}
	public void assignClientId(long clientId)	{
		this.clientId = clientId;
	}
}

 