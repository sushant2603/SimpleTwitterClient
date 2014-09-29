package com.sushant2603.apps.basictwitter.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
//import com.example.googleimagesearch.activities.SettingsDialog;
//import com.example.googleimagesearch.activities.SettingsDialog.SettingsDialogListener;
//import com.example.googleimagesearch.models.FilterSetting;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sushant2603.apps.basictwitter.EndlessScrollListener;
import com.sushant2603.apps.basictwitter.R;
import com.sushant2603.apps.basictwitter.TwitterApplication;
import com.sushant2603.apps.basictwitter.TwitterClient;
import com.sushant2603.apps.basictwitter.activities.ComposeDialog.ComposeDialogListener;
import com.sushant2603.apps.basictwitter.adapters.TweetsAdapter;
import com.sushant2603.apps.basictwitter.models.Tweet;
import com.sushant2603.apps.basictwitter.models.User;

public class TimelineActivity extends Activity {
	private SwipeRefreshLayout swipeContainer;
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private TweetsAdapter aTweets;
	private ListView lvTweets;
	private User mainUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				populateTimeline(0);
			}
		});

		client = TwitterApplication.getRestClient();
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetsAdapter(this, tweets);

		lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline(tweets.get(tweets.size()-1).getUid());
				
			}
		});
		/*lvTweets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(TimelineActivity.this, TweetDetailsActivity.class);
				Tweet tweet = aTweets.getItem(position);
				i.putExtra("tweet", tweet);
				startActivity(i);
			}
			
		});*/

		getUserDetails();
		populateTimeline(0);
		getActionBar().setTitle("Home");
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 0, 185, 255)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	public void onComposeAction(MenuItem mi) {
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
		ComposeDialog dialog = ComposeDialog.newInstance(mainUser);
		dialog.show(ft, "compose");
		dialog.listener = new ComposeDialogListener() {
			@Override
			public void onFinishComposeDialog(Tweet tweet) {
				if (tweet != null) {
					Toast.makeText(TimelineActivity.this, tweet.getBody(), Toast.LENGTH_LONG).show();
					populateTimeline(0);
				}
			}
		};
		Toast.makeText(this, "Compose", Toast.LENGTH_SHORT).show();
	}

	public void populateTimeline(long max_id) {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				tweets.addAll(Tweet.fromJSONArray(json));
				commit();
				aTweets.notifyDataSetChanged();
				Log.d("debug", "Added Tweets # " + Integer.toString(tweets.size()));
				Toast.makeText(TimelineActivity.this, "Read Tweets", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(TimelineActivity.this, e.toString(), Toast.LENGTH_LONG).show();
				Log.d("debug", e.toString());
			}
		}, max_id);
	}

	public void getUserDetails() {
		client.getUser(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				mainUser = User.fromJSON(json);
				Toast.makeText(TimelineActivity.this, mainUser.getName(), Toast.LENGTH_LONG).show();
			}
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(TimelineActivity.this, e.toString(), Toast.LENGTH_LONG).show();
				Log.d("debug", e.toString());
			}
		});
	}

	private void commit() {
		ActiveAndroid.beginTransaction();
		//Tweet.deleteAll();
		try {
			for (int ii = 0; ii < tweets.size(); ii++) {
				tweets.get(ii).save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
	}
}
