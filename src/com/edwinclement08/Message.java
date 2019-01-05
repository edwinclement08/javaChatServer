package com.edwinclement08;

import java.time.Instant;
import org.json.simple.JSONObject;


public class Message	{
	JSONObject messageObject;
	
	public JSONObject getMessageObject() {
		return messageObject;
	}

	public void setMessageObject(JSONObject messageObject) {
		this.messageObject = messageObject;
	}

	Message()	{
	}
	
	@SuppressWarnings("unchecked")
	Message(String message, long senderId, long receiverId)		{
		Instant instant = Instant.now();
		long timeStampMillis = instant.toEpochMilli();
		 
		messageObject = new JSONObject();
		messageObject.put("message", message);
		messageObject.put("senderId", senderId);
		messageObject.put("receiverId", receiverId);
		messageObject.put("timeStampMillis", timeStampMillis);
	}
	
	@SuppressWarnings("unchecked")
	public static Message recreateMessage(String message, long senderId, long receiverId, long timeStampMillis)	{
		Message m = new Message();
		JSONObject messageObject = new JSONObject();
		messageObject.put("message", message);
		messageObject.put("senderId", senderId);
		messageObject.put("receiverId", receiverId);
		messageObject.put("timeStampMillis", timeStampMillis);
		m.setMessageObject(messageObject);
		return m;
	}
	
	public String toString()	{
		return JSONEncoder.dump(messageObject);
	}
	
	public static Message fromString(String jsonString)	{
		JSONObject jo = JSONEncoder.load(jsonString);
		String message = (String) jo.get("message"); 
		long senderId = (long) jo.get("senderId"); 
		long receiverId = (long) jo.get("receiverId"); 
		long timeStampMillis = (long) jo.get("timeStampMillis"); 
		
		Message tempMessage = Message.recreateMessage(message, senderId, receiverId, timeStampMillis );
		
		return tempMessage;
	}
}


