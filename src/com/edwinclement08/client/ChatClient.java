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
    	initConnection();
    	packet.setDeviceId(clientId);
		packet.setDeviceType("client");
		
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Packet packetRecv = new Packet();
        
		try {
			logger.debug("Debug reach here");
			logger.debug(packet);
			oos = new ObjectOutputStream(socket.getOutputStream());
		    logger.info("Sending packet to server: "+ packet);
			oos.writeObject(packet);
			//read the server response message
			ois = new ObjectInputStream(socket.getInputStream());
			packetRecv = (Packet) ois.readObject();
			logger.info("Return Packet from Server: " + packetRecv);
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
		closeConnection();
		
        return packetRecv;
        
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
		//chatClient.closeConnection();
	}
	
	public ChatClient() {
		clientId = new Random().nextLong();
//		logger.info("Client Initializing");
	}
	public void assignClientId(long clientId)	{
		this.clientId = clientId;
	}
}

 