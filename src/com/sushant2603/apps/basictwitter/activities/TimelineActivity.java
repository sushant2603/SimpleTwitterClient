package com.sushant2603.apps.basictwitter.activities;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
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
	private LinkedList<Tweet> tweets;
	private TweetsAdapter aTweets;
	private ListView lvTweets;
	private User mainUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActiveAndroid.initialize(this);
		setContentView(R.layout.activity_timeline);
		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				populateNewItems(tweets.get(0).getUid());
			}
		});

		client = TwitterApplication.getRestClient();
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new LinkedList<Tweet>();
		aTweets = new TweetsAdapter(this, tweets);

		lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline(tweets.get(tweets.size()-1).getUid(), 0);
			}
		});
		getUserDetails();
		populateTimeline(0, 0);
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
					client.postTweet(new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject json) {
							tweets.addFirst(Tweet.fromJSON(json));
							aTweets.notifyDataSetChanged();
						}
						@Override
						public void onFailure(Throwable e, String s) {
							Log.d("debug", e.toString());
						}
					}, tweet);
				}
			}
		};
	}

	public void populateNewItems(long since_id) {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				for (Tweet tweet : Tweet.fromJSONArray(json)) {
					tweets.addFirst(tweet);
				}
				commit();
				aTweets.notifyDataSetChanged();
				swipeContainer.setRefreshing(false);
			}
			@Override
			public void onFailure(Throwable e, String s) {
				swipeContainer.setRefreshing(false);
				Log.d("debug", e.toString());
			}
		}, 0, since_id);
	}


	public void populateTimeline(long max_id, long since_id) {
		/*if (!isNetworkAvailable()) {
			Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
			tweets.clear();
			tweets.addAll(Tweet.getAll());
			aTweets.notifyDataSetChanged();
			return;
		}

		if (max_id == 0 && since_id == 0) {
			tweets.clear();
		}*/

		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				tweets.addAll(Tweet.fromJSONArray(json));
				commit();
				aTweets.notifyDataSetChanged();
			}
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(TimelineActivity.this, e.toString(), Toast.LENGTH_LONG).show();
				Log.d("debug", e.toString());
			}
		}, max_id, since_id);
	}

	public void getUserDetails() {
		if (mainUser == null) {
			client.getUser(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					mainUser = User.fromJSON(json);
				}
				@Override
				public void onFailure(Throwable e, String s) {
					Log.d("debug", e.toString());
				}
			});
		}
	}

	private void commit() {
		ActiveAndroid.beginTransaction();
		Tweet.deleteAll();
		int total = tweets.size() > 25 ? 25 : tweets.size();
		try {
			for (int ii = 0; ii < total; ii++) {
				tweets.get(ii).getUser().save();
				tweets.get(ii).save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
	}

	private Boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
}
