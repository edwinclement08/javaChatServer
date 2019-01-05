package com.edwinclement08;

import com.google.gson.Gson;


public class Packet	{
	transient static Gson gson = new Gson();
	String value;
		
	public Packet()	{
	}
	public Packet(String val )	{
		value = val; 
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


