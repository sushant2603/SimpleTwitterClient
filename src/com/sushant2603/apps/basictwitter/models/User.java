package com.sushant2603.apps.basictwitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "User")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "Name")
	private String name;
	@Column(name = "screenName")
	private String screenName;
	@Column(name = "uid", unique = true)
	private long uid;
	@Column(name = "profileImageUrl")
	private String profileImageUrl;

	public User() {
		super();
	}

	public static User fromJSON(JSONObject jsonObject) {
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.screenName = jsonObject.getString("screen_name");
			user.uid =jsonObject.getLong("id");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
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
