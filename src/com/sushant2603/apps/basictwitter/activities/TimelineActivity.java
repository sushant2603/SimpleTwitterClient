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
import android.text.Html;
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
	private TwitterClient client;
	private User mainUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActiveAndroid.initialize(this);
		setContentView(R.layout.activity_timeline);
		setupTabs();
		client = TwitterApplication.getRestClient();
		getUserDetails();
		getActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + "Home" + "</font>"));
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
		ComposeDialog dialog = ComposeDialog.newInstance(mainUser, "", 0);
		dialog.show(ft, "compose");
		dialog.listener = new ComposeDialogListener() {
			@Override
			public void onFinishComposeDialog(Tweet tweet, long replyId) {
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
					}, tweet, 0);
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
	    String username = (String) view.getTag(R.id.TAG_USER);
	    Long replyId = (Long) view.getTag(R.id.TWEET_ID);
		ComposeDialog dialog = ComposeDialog.newInstance(mainUser, username, replyId);
		dialog.show(ft, "compose");
		dialog.listener = new ComposeDialogListener() {
			@Override
			public void onFinishComposeDialog(Tweet tweet, long replyId) {
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
					}, tweet, replyId);
				}
			}
		};
	}

	public void onRetweet(View view) {
		Long replyId = (Long) view.getTag();
		client.postReTweet(new JsonHttpResponseHandler() {
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
		}, replyId);
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
}
