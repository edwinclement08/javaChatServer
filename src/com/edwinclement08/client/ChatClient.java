package com.edwinclement08.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.edwinclement08.Packet;
import com.edwinclement08.ServerConfig;

import java.util.Random;


public class ChatClient {
    private static Socket socket;
    final static Logger logger = Logger.getLogger(ChatClient.class);
    
    static long clientId;
    

    public static void initConnection()	{
    	  String host = ServerConfig.host;
          int port = ServerConfig.port;
          
          try	{
        	  InetAddress address = InetAddress.getByName(host);
              socket = new Socket(address, port);  
          } catch (Exception e){
        	  logger.error("Failed to initialize socket.");
          }
    }
    
    public static Packet sendPacket(Packet packet)	{
       
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
    
    
    public static void closeConnection()	{
    	
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
		
		new ChatClient();
	}	
	
	public ChatClient() {
		// TODO Auto-generated constructor stub
		clientId = new Random().nextLong();
		logger.info("Client Initializing");
		initConnection();
		
		Packet packet = new Packet("eded");
		packet.setDeviceId(clientId);
		packet.setDeviceType("client");
		sendPacket(packet);
		
		closeConnection();
		
	}
}

 