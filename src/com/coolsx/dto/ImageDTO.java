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
	private boolean _isDeleteAccepted;
	private boolean _isFileLocal;
	
	public ImageDTO(){}
	
	public static ParseQuery<ImageDTO> getQuery(){
		return ParseQuery.getQuery(ImageDTO.class);
	}
	
	public ImageDTO (String fileName, byte[] bArrData, boolean isLocal){
		this._fileName = fileName;
		this._bArrData = bArrData;
		this._isFileLocal = isLocal;
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
	
	public String getImageId(){
		return getString(MConstants.kImageId);
	}
	
	public void setIsDeleteAccepted(boolean isDeleteAccepted){
		this._isDeleteAccepted = isDeleteAccepted;
	}
	
	public boolean getIsDeleteAccepted(){
		return _isDeleteAccepted;
	}
	
	public void setIsFileLocal(boolean isFileLocal){
		this._isFileLocal = isFileLocal;
	}
	
	public boolean getIsFileLocal(){
		return _isFileLocal;
	}
}
