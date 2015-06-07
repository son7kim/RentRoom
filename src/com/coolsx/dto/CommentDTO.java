package com.coolsx.dto;

import com.coolsx.constants.MConstants;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName(MConstants.kTableComment)
public class CommentDTO extends ParseObject{
	public CommentDTO(){}
	
	public static ParseQuery<CommentDTO> getQuery(){
		return ParseQuery.getQuery(CommentDTO.class);
	}
	
	public String getContentComment(){
		return this.getString(MConstants.kContentComment);
	}
}
