package com.edwinclement08;

import java.io.Serializable;
import java.time.Instant;
import org.json.simple.JSONObject;


import com.google.gson.Gson;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5120515665591704792L;
	public Object message;
	public long senderId, receiverId, timeStampMillis;
	transient static Gson gson = new Gson();

	
	JSONObject messageObject;
	

	public Message()	{
	}
	
	public Message(String message, long senderId, long receiverId)		{
		Instant instant = Instant.now();
		 
		this.message = message;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.timeStampMillis = instant.toEpochMilli();
	}
	
	public static Message recreateMessage(String message, long senderId, long receiverId, long timeStampMillis)	{
		Message messageObject = new Message();
		
		messageObject.message = message;
		messageObject.senderId = senderId;
		messageObject.receiverId = receiverId;
		messageObject.timeStampMillis = timeStampMillis;

		return messageObject;
	}
	
	public String toString()	{
		return gson.toJson(this);
	}
	
	public static Message fromString(String jsonString)	{
		if (jsonString.equals(""))	{
			Message message = new Message();
			return message;
		}
		Message message = gson.fromJson(jsonString, Message.class);		
		return message;
	}
}


