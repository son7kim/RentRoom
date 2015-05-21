package com.coolsx.dto;

public class ImageDTO {

	private String _fileName;
	private byte[] _bArrData;
	
	public ImageDTO (String fileName, byte[] bArrData){
		this._fileName = fileName;
		this._bArrData = bArrData;
	}
	
	public String getFileName(){
		return this._fileName;
	}
	
	public byte[] getByteArrData(){
		return this._bArrData;
	}
}
