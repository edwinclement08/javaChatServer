package com.edwinclement08.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import org.apache.log4j.Logger;

import com.edwinclement08.Message;
import com.edwinclement08.Packet;
import com.edwinclement08.ServerConfig;
import java.util.Optional;

public class ChatServer {
	public boolean DEBUG = true;
	private static Socket socket;
	final static Logger logger = Logger.getLogger(ChatServer.class);
	public static ServerSocket serverSocket;

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
		try {
			// Reading the message from the client
			socket = serverSocket.accept();
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String packetString = br.readLine();
			Packet packet = Packet.fromString(packetString);

			logger.info("Packet received from client is " + packet);

			// TODO Process the packet, return new packet
			Packet responsePacket = new Packet("Return");

			// Sending the response back to the client.
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(responsePacket.toString());
			logger.info("Message sent to the client is :" + responsePacket);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Can't listen on socket");
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				logger.error("Can't close the socket");
			}
		}
	}

	public static void initServer() {
		int port = ServerConfig.port;
		
		try	{
			serverSocket = new ServerSocket(port);	
			System.out.println("Server Started and listening to the port ");
			timedShutdown(20);
		} catch (IOException e)	{
			logger.error("Failed to bind to port:" + port);
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Initializing the Server");

		if (logger.isDebugEnabled()) {
			logger.debug("This is debug");
		}
		// logs an error message with parameter

		System.out.println("Reached Here");

		initServer();
		
		loop(serverSocket);


	}
}

class ChatHandler {
	public static Optional<Message> onMessage(Message obj) {
		System.out.println(obj);

		Optional<Message> returnMessage = Optional.ofNullable(obj);
		return returnMessage;
	}

}