package com.edwinclement08.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.edwinclement08.ServerConfig;
 
public class ChatServer {
	public boolean DEBUG = true;
    private static Socket socket;
    final static Logger logger = Logger.getLogger(ChatServer.class);
    public static ServerSocket serverSocket; 
    
    /**
     * @param secToRun - The Amount of time(in Sec) the server will run before being force-killed.
     */
    public static void timedShutdown(int secToRun)	{
    	
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask(){ 
			@Override
			public void run() {
				// TODO Auto-generated method stub
        		System.out.println("Shutting down server");
        		try {
        			socket.close();
        			serverSocket.close();
        		} catch (Exception e)	{
        	    	logger.info("Error Shutting down in timed manner");
        		}
        		System.exit(0);
			}},  secToRun*1000);
        System.out.println("Shutting down the server in 10 sec");
    }
    
    public static void initServer()	{
    	int port = ServerConfig.port;
		try
        {
			serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port ");
            
            timedShutdown(20);
            
            //Server is running always. This is done using this while(true) loop
            while(true)
            {
                //Reading the message from the client
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String number = br.readLine();
                System.out.println("Message received from client is "+number);
 
                //Multiplying the number by 2 and forming the return message
                String returnMessage;
                try
                {
                    int numberInIntFormat = Integer.parseInt(number);
                    int returnValue = numberInIntFormat*2;
                    returnMessage = String.valueOf(returnValue) + "\n";
                }
                catch(NumberFormatException e)
                {
                    //Input was not a number. Sending proper message back to client.
                    returnMessage = "Please send a proper number\n";
                }
 
                //Sending the response back to the client.
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(returnMessage);
                System.out.println("Message sent to the client is "+returnMessage);
                bw.flush();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(Exception e){}
        }
    	
    }
        

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
    	int port = ServerConfig.port;
		System.out.println("Initializing the Server");
		
    	if(logger.isDebugEnabled()){
    	    logger.debug("This is debug");
    	}
    	//logs an error message with parameter
    	logger.error("This is error : " + "231");
    	System.out.println("Reached Here");
    	
    	initServer();

	}

}

@FunctionalInterface
interface CallBackOnMessage {
	public void accept();
		
	
}

class tempImplementation	{
	
	
}