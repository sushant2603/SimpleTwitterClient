package com.sushant2603.apps.basictwitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.ActiveAndroid;
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
	@Column(name = "Likes")
	private int likes;
	@Column(name = "Retweets")
	private int retweets;

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
			tweet.likes = jsonObject.getInt("favorite_count");
			tweet.retweets = jsonObject.getInt("retweet_count");
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

	public static LinkedList<Tweet> fromJSONArray(JSONArray jsonArray) {
		LinkedList<Tweet> result = new LinkedList<Tweet>();
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

	public int getRetweets() {
		return retweets;
	}

	public int getLikes() {
		return likes;
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

	public void setRetweets(int retweets) {
		this.retweets = retweets;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public static void deleteAll() {
		new Delete().from(Tweet.class).execute();
	}

	public static List<Tweet> getAll() {
		return new Select().from(Tweet.class).execute();
	}

	public static void findOrInsert(Tweet tweet) {
		List<User> result = new Select()
			.from(Tweet.class)
			.where("uid=" + Long.toString(tweet.getUid()))
			.execute();
		if (result.size() == 0) {
			User.findOrInsert(tweet.getUser());
			tweet.save();
		}
	}

	public static void InsertAll(LinkedList<Tweet> tweets) {
		ActiveAndroid.beginTransaction();
		try {
			for (Tweet tweet : tweets) {
				findOrInsert(tweet);
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
	}
}
