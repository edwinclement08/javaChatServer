package com.edwinclement08.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.edwinclement08.Packet;
import com.edwinclement08.ServerConfig;

public class ChatServer {
	public boolean DEBUG = true;
	private static Socket socket;
	final static Logger logger = Logger.getLogger(ChatServer.class);
	public static ServerSocket serverSocket;

    static long serverId;

	/**
	 * @param secToRun
	 *            - The Amount of time(in Sec) the server will run before being
	 *            force-killed.
	 */
	public static void timedShutdown(int secToRun) {

		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Shutting down server");
				try {
					socket.close();
					serverSocket.close();
				} catch (Exception e) {
					logger.info("Error Shutting down in timed manner");
				}
				System.exit(0);
			}
		}, secToRun * 1000);
		System.out.println("Shutting down the server in 10 sec");
	}

	public static void loop(ServerSocket serverSocket) {
		logger.info("Starting the Infinite Loop");
		while (true){
			try {
				// Reading the message from the client
				socket = serverSocket.accept();
				logger.info("got a client");
				
				
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	            //convert ObjectInputStream object to String
	            Packet packet = (Packet) ois.readObject();
	            long clientId = packet.getDeviceId();
	            logger.info("Client::" + clientId + ":Packet received:" + packet);
	            //create ObjectOutputStream object
	            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
	            
				Packet responsePacket = new Packet("Return");
				responsePacket.setDeviceId(serverId);
				responsePacket.setDeviceType("server");

				
	            //write object to Socket
	            oos.writeObject(responsePacket);
	            //close resources
	            ois.close();
	            oos.close();
	            
		       
				logger.info("Client::" + clientId + ":Packet sent:" + responsePacket);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Can't listen on socket");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (Exception e) {
					logger.error("Can't close the socket");
				}
			}	
		}
		
	}

	public static void initServer() {
		int port = ServerConfig.port;
		
		try	{
			serverSocket = new ServerSocket(port);	
			System.out.println("Server Started and listening to the port ");
			timedShutdown(200);
		} catch (IOException e)	{
			logger.error("Failed to bind to port:" + port);
			e.printStackTrace();
		}

	}
	
	public ChatServer()	{
		System.out.println("Initializing the Server");
		serverId = new Random().nextLong();


		if (logger.isDebugEnabled()) {
			logger.debug("This is debug");
		}
		// logs an error message with parameter

		System.out.println("Reached Here");

		initServer();
		
		loop(serverSocket);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new ChatServer();


	}
}
//
//class ChatHandler {
//	public static Optional<Message> onMessage(Message obj) {
//		System.out.println(obj);
//
//		Optional<Message> returnMessage = Optional.ofNullable(obj);
//		return returnMessage;
//	}
//
//}