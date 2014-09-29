package com.sushant2603.apps.basictwitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "Body")
	private String body;
	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name = "CreatedAt")
	private String createdAt;
	@Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	private User user;

	public Tweet() {
		super();
	}

	public Tweet(int id, String body, String time, User user) {
		super();
		this.uid = id;
		this.body = body;
		this.createdAt =  time;
		this.user = user;
	}

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

	public void setBody(String body) {
		this.body = body;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setUser(User user) {
		this.user = user;
		
	}

	public static void deleteAll() {
		new Delete().from(Tweet.class).execute();
	}

	public static List<Tweet> getAll() {
		return new Select().from(Tweet.class).execute();
	}
}
