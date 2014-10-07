package com.sushant2603.apps.basictwitter.models;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "User")
public class User extends Model implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "Name")
	private String name;
	@Column(name = "screenName")
	private String screenName;
	@Column(name = "uid", unique = true)
	private long uid;
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	@Column(name = "profileBackgroundImageUrl")
	private String profileBackgroundImageUrl;
	@Column(name = "tagline")
	private String tagline;
	@Column(name = "nTweets")
	private int nTweets;
	@Column(name = "nFollowers")
	private int nFollowers;
	@Column(name = "nFavorites")
	private int nFavorites;

	public User() {
		super();
	}

	public static User fromJSON(JSONObject jsonObject) {
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.screenName = jsonObject.getString("screen_name");
			user.uid = jsonObject.getLong("id");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			user.tagline = jsonObject.getString("description");
			user.profileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
			user.nFollowers = jsonObject.getInt("followers_count");
			user.nTweets = jsonObject.getInt("statuses_count");
			user.nFavorites = jsonObject.getInt("friends_count");
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

	public long getUId() {
		return uid;
	}

	public int getNTweets() {
		return nTweets;
	}

	public int getNFollowers() {
		return nFollowers;
	}

	public int getNFavorites() {
		return nFavorites;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}

	public String getTagline() {
		return tagline;
	}

	public static void findOrInsert(User user) {
		List<User> result = new Select()
			.from(User.class)
			.where("uid=" + Long.toString(user.getUId()))
			.execute();
		if (result.size() == 0) {
			user.save();
		}
	}
}
