package com.edwinclement08;

import java.io.Serializable;

import com.google.gson.Gson;

public class Packet implements Serializable	{
	public enum PacketType	{
		DEV, SEND_MESSAGE, CHECK_MESSAGE, REGISTER_AGENT;
	};

	private static final long serialVersionUID = 1506783218794229333L;
	transient static Gson gson = new Gson();
	String value;
	PacketType packetType = PacketType.DEV;
	long deviceId;
	String deviceType;
	Message message;
	
	public Message getMessage()	{
		return message;
	}

	public Packet()	{
	}
	public Packet(String val, PacketType packetType )	{
		value = val; 
		this.packetType = packetType;
	}
	
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
	
	public PacketType getPacketType() {
		return packetType;
	}
	public void setPacketType(PacketType packetType) {
		this.packetType = packetType;
	}
		
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	
	public void setDeviceType(String deviceType)	{
		this.deviceType = deviceType;
	}
	
	
	
	public void setValue(String val)	{
		value = val;
	}
	
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


