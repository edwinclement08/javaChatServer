package com.edwinclement08.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.edwinclement08.Packet;
import com.edwinclement08.ServerConfig;

public class ChatClient {
    private static Socket socket;
    final static Logger logger = Logger.getLogger(ChatClient.class);

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
    	String packetString = packet.toString();
    	
    	//Send the message to the server
        OutputStream os;
        OutputStreamWriter osw;
        BufferedWriter bw;
        
        InputStream is;
        InputStreamReader isr;
        BufferedReader br; 
        
        String packetStringRecv = "";
        
		try {
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
	        bw = new BufferedWriter(osw);

            bw.write(packetString);
            bw.flush();
            
            //Get the return message from the server
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            packetStringRecv = br.readLine();
            System.out.println("Message received from the server : " + packetStringRecv);
            
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Unable to set up OutputStream");
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
		logger.info("Client Initializing");
		initConnection();
		
		Packet packet = new Packet("eded");
		sendPacket(packet);
		
		closeConnection();
		
	}	
}

 