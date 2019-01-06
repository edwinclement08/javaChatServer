package com.edwinclement08;

import java.io.Serializable;

import com.google.gson.Gson;

public class Packet implements Serializable	{
	public enum PacketType	{
		DEV, 					// Default 
		ERROR,					// Error 						
		SEND_MESSAGE, 			// sending message(Client to Server)
		SEND_MESSAGE_SUCCESS,	// successful response to client sending message(Server to client)
		
		CHECK_MESSAGE, 			// checking message(Client to Server)
		CHECK_MESSAGE_RESPONSE,	// Response to checking message(Server to client), check the Value
		
		REGISTER_AGENT,			// register client(client to server)
		REGISTER_AGENT_SUCCESS;		// successful response to client register(Server to client)
	};

	private static final long serialVersionUID = 1506783218794229333L;
	transient static Gson gson = new Gson();
	Object payload;


	PacketType packetType = PacketType.DEV;
	long deviceId;
	String deviceType;
	Message message;
	
	public Message getMessage()	{
		return message;
	}

	public Packet()	{
	}
	
	public Packet(Object payload, PacketType packetType )	{
		this.payload = payload; 
		this.packetType = packetType;
	}
	
	// TODO standardize and use the pay load object
	public Packet(PacketType packetType, Object parameters){
		if (packetType == PacketType.SEND_MESSAGE){
			message = (Message) parameters;
			this.packetType = packetType;
		}
	}
	
	public static Packet getRegisterPacket(long clientId)	{
		Packet packet = new Packet();
		packet.setPacketType(Packet.PacketType.REGISTER_AGENT);
		packet.setDeviceId(clientId);
		packet.setDeviceType("client");
		return packet;
	}
	
	// for higher level
	public PacketType getPacketType() {
		return packetType;
	}
	public void setPacketType(PacketType packetType) {
		this.packetType = packetType;
	}
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}
		
	
	// for low level packet interfaces
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	public void setDeviceType(String deviceType)	{
		this.deviceType = deviceType;
	}
	
	
	// legacy
	public String toString()	{
		return gson.toJson(this);
	}
	
	public static Packet fromString(String packetString)	{
		if (packetString.equals(""))	{
			Packet packet = new Packet();
			return packet;
		}
		Packet packet = gson.fromJson(packetString, Packet.class);		
		return packet;
	}
}


