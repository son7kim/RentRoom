package com.coolsx.dto;

import com.coolsx.constants.MConstants;
import com.parse.ParseClassName;

@ParseClassName(MConstants.kTableUser)
public class UserDTO {
	private String _objID;
	private String _userName;
	private String _email;
	
	public UserDTO(String objID, String userName, String email){
		this._objID = objID;
		this._userName = userName;
		this._email = email;
	}

	public String getObjID() {
		return this._objID;		
	}
	
	public String getUserName() {
		return this._userName;
	}
	
	public String getEmail() {
		return this._email;
	}
}
