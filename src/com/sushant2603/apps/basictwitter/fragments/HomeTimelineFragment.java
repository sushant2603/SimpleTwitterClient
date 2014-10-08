package com.sushant2603.apps.basictwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sushant2603.apps.basictwitter.TwitterApplication;
import com.sushant2603.apps.basictwitter.TwitterClient;
import com.sushant2603.apps.basictwitter.models.Tweet;

public class HomeTimelineFragment extends TweetsListFragment {

	private TwitterClient client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
	}

	@Override
	public void populateNewItems(long since_id) {
		//if (!isNetworkAvailable()) {
			//addAll((LinkedList<Tweet>) Tweet.getAll());
			//return;
		//}
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				addAll(Tweet.fromJSONArray(json));
			}
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
			}
		}, 0, since_id);
	}

	@Override
	public void populateTimeline(long max_id, long since_id) {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				addAllAtEnd(Tweet.fromJSONArray(json));
			}
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
			}
		}, max_id, since_id);
	}
}
