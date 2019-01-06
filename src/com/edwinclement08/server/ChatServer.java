package com.edwinclement08.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

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
				logger.info("Shutting down server");
				try {
					socket.close();
					serverSocket.close();
				} catch (Exception e) {
					logger.info("Error Shutting down in timed manner");
				}
				System.exit(0);
			}
		}, secToRun * 1000);
		logger.info("Shutting down the server in "+secToRun +  " sec");
	}

	public static void loop(ServerSocket serverSocket, Function<Packet, Packet> callback) {
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
	          
	           

				Packet responsePacket = callback.apply(packet);
				
	            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
	            //write object to Socket
	            oos.writeObject(responsePacket);
	            //close resources
//	            ois.close();
//	            oos.close();
//	            // TODO close these
		       
				logger.info("Client::" + clientId + ":Packet sent:" + responsePacket);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Can't listen on socket");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					//socket.close();
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
			logger.info("Server Started and listening to the port ");
			timedShutdown(400);
		} catch (IOException e)	{
			logger.error("Failed to bind to port:" + port);
			e.printStackTrace();
		}
	}
	
	public ChatServer(Function<Packet, Packet> chatHandlerCallback)	{
		
		logger.info("Initializing the Server");
		serverId = new Random().nextLong();

		if (logger.isDebugEnabled()) {
			logger.debug("This is debug");
		}

		initServer();
		loop(serverSocket, chatHandlerCallback);
	}

	public static void main(String[] args) {		
		new ChatServer((packet) -> new Packet("Received a Packet from " + packet.getDeviceId(), Packet.PacketType.DEV));
	}
}