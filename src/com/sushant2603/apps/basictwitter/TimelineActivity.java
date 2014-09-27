package com.sushant2603.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sushant2603.apps.basictwitter.models.Tweet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new ArrayAdapter<Tweet>(this,android.R.layout.simple_list_item_1, tweets);
		lvTweets.setAdapter(aTweets);
	}

	public void  populateTimeline() {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				aTweets.addAll(Tweet.fromJSONArray(json));
				aTweets.notifyDataSetChanged();
			}
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
			}
		});
	}
}
