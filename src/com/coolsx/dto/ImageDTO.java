package com.coolsx.dto;

import com.coolsx.constants.MConstants;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName(MConstants.kTableImage)
public class ImageDTO extends ParseObject{

	private String _fileName;
	private byte[] _bArrData;
	
	public ImageDTO(){}
	
	public static ParseQuery<ImageDTO> getQuery(){
		return ParseQuery.getQuery(ImageDTO.class);
	}
	
	public ImageDTO (String fileName, byte[] bArrData){
		this._fileName = fileName;
		this._bArrData = bArrData;
	}
	
	public String getFileNameLocal(){
		return this._fileName;
	}
	
	public byte[] getByteArrDataLocal(){
		return this._bArrData;
	}
	
	public String getFileNameServer(){
		return getString(MConstants.kImgName);
	}
	
	public ParseFile getFile(){
		return (ParseFile)getParseFile(MConstants.kImgFile);
	}
}
