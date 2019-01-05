package com.edwinclement08;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class JSONEncoder	{
    final static Logger logger = Logger.getLogger(JSONEncoder.class);

	public static String dump(JSONObject jsonObject){
		return jsonObject.toJSONString();
	}
	
	public static JSONObject load(String jsonString)	{
        JSONObject obj;
		try {
			obj = (JSONObject) (new JSONParser().parse(jsonString));
			return obj;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Failed to parse the JSONString: " + jsonString);
			return new JSONObject();
		} 
	}
} 