package com.sushant2603.apps.basictwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	private String name;
	private String screenName;
	private long uid;
	private String profileImageUrl;

	public static User fromJSON(JSONObject jsonObject) {
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.screenName = jsonObject.getString("screenName");
			user.uid =jsonObject.getLong("id");
			user.profileImageUrl = jsonObject.getString("created_at");
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
		return user;
	}

	public String getName() {
		return name;
	}

	public String getScreenName() {
		return screenName;
	}

	public long getId() {
		return uid;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}
}
