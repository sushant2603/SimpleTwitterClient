package com.sushant2603.apps.basictwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sushant2603.apps.basictwitter.models.Tweet;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "RZVS0D4GiD2Zjrs7kHtvIxvKJ";
	public static final String REST_CONSUMER_SECRET = "gG1JYZh5PmTJF4zprq2KBN6JPHJUWciEz34YqRUXtnVzVmJO7v";
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler, long max_id, long since_id) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		if (max_id > 0) {
			params.put("max_id", Long.toString(max_id));
		}
		if (since_id > 0) {
			params.put("since_id", Long.toString(since_id));
		}
		params.put("count", "25");
		client.get(apiUrl, params, handler);
		
	}

	//
	public void postTweet(AsyncHttpResponseHandler handler, Tweet tweet) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet.getBody());
		client.post(apiUrl, params, handler);
	}

	public void getUser(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}
}