package com.sushant2603.apps.basictwitter.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	private String body;
	private long uid;
	private String createdAt;
	private User user;

	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid =jsonObject.getLong("id");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Tweet> result = new ArrayList<Tweet>();
		for (int index = 0; index < jsonArray.length(); index++) {
			JSONObject tweetJson = null; 
			try {
				tweetJson = jsonArray.getJSONObject(index);
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			Tweet tweet = fromJSON(tweetJson);
			if (tweet != null) {
				result.add(tweet);
			}
		}
		return result;
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}
	
	@Override
	public String toString() {
		return "Tweet";
	}
}
