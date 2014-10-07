package com.sushant2603.apps.basictwitter.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.activeandroid.ActiveAndroid;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sushant2603.apps.basictwitter.R;
import com.sushant2603.apps.basictwitter.TwitterApplication;
import com.sushant2603.apps.basictwitter.TwitterClient;
import com.sushant2603.apps.basictwitter.activities.ComposeDialog.ComposeDialogListener;
import com.sushant2603.apps.basictwitter.fragments.HomeTimelineFragment;
import com.sushant2603.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.sushant2603.apps.basictwitter.fragments.TweetsListFragment;
import com.sushant2603.apps.basictwitter.listeners.FragmentTabListener;
import com.sushant2603.apps.basictwitter.models.Tweet;
import com.sushant2603.apps.basictwitter.models.User;

public class TimelineActivity extends FragmentActivity {
	//private SwipeRefreshLayout swipeContainer;
	private TwitterClient client;
	//private LinkedList<Tweet> tweets;
	//private TweetsAdapter aTweets;
	//private TweetsListFragment fragmentTweetsList;
	//private ListView lvTweets;
	private User mainUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActiveAndroid.initialize(this);
		setContentView(R.layout.activity_timeline);
		setupTabs();
		/*swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				populateNewItems(tweets.get(0).getUid());
			}
		});*/

		client = TwitterApplication.getRestClient();
		//fragmentTweetsList = (TweetsListFragment) 
		//		getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
		//lvTweets = (ListView) findViewById(R.id.lvTweets);
		//tweets = new LinkedList<Tweet>();
		//aTweets = new TweetsAdapter(this, tweets);

		/*lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline(tweets.get(tweets.size()-1).getUid(), 0);
			}
		});*/
		getUserDetails();
		//populateTimeline(0, 0);
		getActionBar().setTitle("Home");
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 0, 185, 255)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "first",
								HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "second",
								MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
	}

	public void onComposeAction(MenuItem mi) {
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
		ComposeDialog dialog = ComposeDialog.newInstance(mainUser, "");
		dialog.show(ft, "compose");
		dialog.listener = new ComposeDialogListener() {
			@Override
			public void onFinishComposeDialog(Tweet tweet) {
				if (tweet != null) {
					client.postTweet(new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject json) {
							Tweet tweet = Tweet.fromJSON(json);
					        TweetsListFragment fragment = (TweetsListFragment) 
					        	getSupportFragmentManager().findFragmentById(R.id.flContainer);
							fragment.add(tweet);
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

	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("user", mainUser);
		startActivity(i);
	}
	
	public void onReply(View view) {
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    String username = (String) view.getTag();
		ComposeDialog dialog = ComposeDialog.newInstance(mainUser, username);
		dialog.show(ft, "compose");
		dialog.listener = new ComposeDialogListener() {
			@Override
			public void onFinishComposeDialog(Tweet tweet) {
				if (tweet != null) {
					client.postTweet(new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject json) {
							Tweet tweet = Tweet.fromJSON(json);
					        TweetsListFragment fragment = (TweetsListFragment) 
					        	getSupportFragmentManager().findFragmentById(R.id.flContainer);
							fragment.add(tweet);
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

	/*public void populateNewItems(long since_id) {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				for (Tweet tweet : Tweet.fromJSONArray(json)) {
					tweets.addFirst(tweet);
					fragmentTweetsList.addAll(tweets);
				}
				commit();
				//aTweets.notifyDataSetChanged();
				//swipeContainer.setRefreshing(false);
			}
			@Override
			public void onFailure(Throwable e, String s) {
				//swipeContainer.setRefreshing(false);
				//Log.d("debug", e.toString());
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

		/*client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				tweets.addAll(Tweet.fromJSONArray(json));
				//commit();
			//	aTweets.notifyDataSetChanged();
			}
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(TimelineActivity.this, e.toString(), Toast.LENGTH_LONG).show();
				Log.d("debug", e.toString());
			}
		}, max_id, since_id);
	}*/

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

	/*private Boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}*/
}
