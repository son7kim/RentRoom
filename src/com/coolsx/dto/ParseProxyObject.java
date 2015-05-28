package com.coolsx.dto;

import java.io.Serializable;
import java.util.HashMap;

import com.coolsx.constants.MConstants;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseProxyObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private HashMap<String, Object> values = new HashMap<String, Object>();
 
	public HashMap<String, Object> getValues() {
		return values;
	}
 
	public void setValues(HashMap<String, Object> values) {
		this.values = values;
	}
	
	public ParseProxyObject(ParseObject object) {
		
		// Loop the keys in the ParseObject
		for(String key : object.keySet()) {
			@SuppressWarnings("rawtypes")
			Class classType = object.get(key).getClass();
			if(classType == byte[].class || classType == String.class || 
					classType == Integer.class || classType == Boolean.class) {
				values.put(key, object.get(key));
			} else if(classType == ParseUser.class) {
				ParseProxyObject parseUserObject = new ParseProxyObject((ParseObject)object.get(key));
				values.put(key, parseUserObject);
			} else {
				// You might want to add more conditions here, for embedded ParseObject, ParseFile, etc.
			}
		}
	}
	
	public String getString(String key) {
		if(has(key)) {
			return (String) values.get(key);
		} else {
			return "";
		}
	}
	
	public String getAddress(){
		if(has(MConstants.kAddress)){
			return (String)values.get(MConstants.kAddress);
		} else{
			return "";
		}
	}
	
	public int getInt(String key) {
		if(has(key)) {
			return (Integer)values.get(key);
		} else {
			return 0;
		}
	}
	
	public Boolean getBoolean(String key) {
		if(has(key)) {
			return (Boolean)values.get(key);
		} else {
			return false;
		}
	}
	
	public byte[] getBytes(String key) {
		if(has(key)) {
			return (byte[])values.get(key);
		} else {
			return new byte[0];
		}
	}	
	
	public ParseProxyObject getParseUser(String key) {
		if(has(key)) {
			return (ParseProxyObject) values.get(key);
		} else {
			return null;
		}
	}
	
	public Boolean has(String key) {
		return values.containsKey(key);
	}
	
	public String getPostID() {
		if(has(MConstants.kPostID)){
			return (String)values.get(MConstants.kPostID);
		} else{
			return "";
		}
	}

	public String getFullName() {
		if(has(MConstants.kName)){
			return (String)values.get(MConstants.kName);
		} else{
			return "";
		}
	}

	public String getPhoneNumber() {
		if(has(MConstants.kPhoneNumber)){
			return (String)values.get(MConstants.kPhoneNumber);
		} else{
			return "";
		}
	}



	public String getDescription() {
		if(has(MConstants.kDiscription)){
			return (String)values.get(MConstants.kDiscription);
		} else{
			return "";
		}
	}

	public String getUserID() {
		if(has(MConstants.kUserIdPost)){
			return (String)values.get(MConstants.kUserIdPost);
		} else{
			return "";
		}
	}

	public String getDistricID() {
		if(has(MConstants.kDistrictID)){
			return (String)values.get(MConstants.kDistrictID);
		} else{
			return "";
		}
	}

	public int getNumRoom() {
		if(has(MConstants.kNumRoom)) {
			return (Integer)values.get(MConstants.kNumRoom);
		} else {
			return 0;
		}
	}

	public int getCostMin() {
		if(has(MConstants.kCostMin)) {
			return (Integer) values.get(MConstants.kCostMin);
		} else {
			return 0;
		}
	}

	public int getCostMax() {
		if(has(MConstants.kCostMax)) {
			return (Integer)values.get(MConstants.kCostMax);
		} else {
			return 0;
		}
	}

	public int getAreaMin() {
		if(has(MConstants.kAreaMin)) {
			return (Integer)values.get(MConstants.kAreaMin);
		} else {
			return 0;
		}
	}

	public int getAreaMax() {
		if(has(MConstants.kAreaMax)) {
			return (Integer)values.get(MConstants.kAreaMax);
		} else {
			return 0;
		}
	}
}

