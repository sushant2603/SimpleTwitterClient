package com.sushant2603.apps.basictwitter.fragments;

import java.util.LinkedList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sushant2603.apps.basictwitter.EndlessScrollListener;
import com.sushant2603.apps.basictwitter.R;
import com.sushant2603.apps.basictwitter.adapters.TweetsAdapter;
import com.sushant2603.apps.basictwitter.models.Tweet;

public class TweetsListFragment extends Fragment {
	private SwipeRefreshLayout swipeContainer;
	//private TwitterClient client;
	private LinkedList<Tweet> tweets;
	private TweetsAdapter aTweets;
	private ListView lvTweets;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweets = new LinkedList<Tweet>();
		aTweets = new TweetsAdapter(getActivity(), tweets);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				populateNewItems(tweets.get(0).getUid());
				swipeContainer.setRefreshing(false);
			}
		});

		lvTweets =  (ListView) view.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline(tweets.get(tweets.size()-1).getUid()-1, 0);
			}
		});

		populateTimeline(0, 0);
		return view;
	}

	public void populateNewItems(long since_id) {
		return;
	}

	public void populateTimeline(long max_id, long since_id) {
		return;
	}

	public void addAll(LinkedList<Tweet> tweets) {
		for (int index = tweets.size()-1; index >= 0; index--) {
			this.tweets.addFirst(tweets.get(index));
		}
		aTweets.notifyDataSetChanged();
		Tweet.InsertAll(tweets);
	}

	public void addAllAtEnd(LinkedList<Tweet> tweets) {
		this.tweets.addAll(tweets);
		aTweets.notifyDataSetChanged();
		Tweet.InsertAll(tweets);
	}

	public void add(Tweet tweet) {
        this.tweets.addFirst(tweet);
		aTweets.notifyDataSetChanged();
	}

	protected Boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
}
