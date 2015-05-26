package com.coolsx.dto;

import java.io.Serializable;

import com.coolsx.constants.MConstants;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@SuppressWarnings("serial")
@ParseClassName(MConstants.kTablePost)
public class PostArticleDTO extends ParseObject implements Serializable {

	public static ParseQuery<PostArticleDTO> getQuery() {
		return ParseQuery.getQuery(PostArticleDTO.class);
	}

	public String getPostID() {
		return getString(MConstants.kPostID);
	}

	public String getFullName() {
		return getString(MConstants.kName);
	}

	public String getPhoneNumber() {
		return getString(MConstants.kPhoneNumber);
	}

	public String getAddress() {
		return getString(MConstants.kAddress);
	}

	public String getDescription() {
		return getString(MConstants.kDiscription);
	}

	public String getUserID() {
		return getString(MConstants.kUserIdPost);
	}

	public String getDistricID() {
		return getString(MConstants.kDistrictID);
	}

	public int getNumRoom() {
		return getInt(MConstants.kNumRoom);
	}

	public long getCostMin() {
		return getLong(MConstants.kCostMin);
	}

	public long getCostMax() {
		return getLong(MConstants.kCostMax);
	}

	public int getAreaMin() {
		return getInt(MConstants.kAreaMin);
	}

	public int getAreaMax() {
		return getInt(MConstants.kAreaMax);
	}
}
