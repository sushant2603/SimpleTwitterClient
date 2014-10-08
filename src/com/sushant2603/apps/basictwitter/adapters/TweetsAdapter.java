package com.sushant2603.apps.basictwitter.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.sushant2603.apps.basictwitter.R;
import com.sushant2603.apps.basictwitter.activities.ProfileActivity;
import com.sushant2603.apps.basictwitter.models.Tweet;
import com.sushant2603.apps.basictwitter.models.User;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

	private Context context;

    private static class ViewHolder {
    	ImageView imgUser;
    	TextView username;
    	TextView date;
    	TextView tvRetweets;
    	TextView tvLikes;
    	TextView body;
    	ImageView imgTweet;
    	ImageButton btnReply;
	}

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, android.R.layout.simple_list_item_1, tweets);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,
					parent, false);
			viewHolder =new ViewHolder();
	    	viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.imgUser);
	    	viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
	    	viewHolder.date = (TextView) convertView.findViewById(R.id.tvDate);
	    	viewHolder.tvRetweets = (TextView) convertView.findViewById(R.id.tvRetweets);
	    	viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
	    	viewHolder.imgTweet = (ImageView) convertView.findViewById(R.id.imgTweet);
	    	viewHolder.btnReply = (ImageButton) convertView.findViewById(R.id.btnReply);
	    	viewHolder.body = (TextView) convertView.findViewById(R.id.tvTweetBody);
	    	convertView.setTag(viewHolder);
		} else {
		    viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.imgUser.setImageResource(0);
		try {
			Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.imgUser);
		} catch (Exception e) {
			Log.d("Debug", e.toString());
		}

		viewHolder.imgUser.setTag(R.id.TAG_USER, tweet.getUser());
		viewHolder.imgUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, ProfileActivity.class);
				i.putExtra("user", (User) v.getTag(R.id.TAG_USER));
				context.startActivity(i);
			}
		});

		String usernameHtml = "<font size=\"1\">" + tweet.getUser().getName() + "</font>&nbsp;" +
			"<font size=\"1\"  color=\"#B3B3B3\">@" + tweet.getUser().getScreenName() + "</font>";
		viewHolder.username.setText(Html.fromHtml(usernameHtml));
		viewHolder.body.setText(tweet.getBody());

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy",
				Locale.ENGLISH);
		dateFormat.setLenient(true);
		Date created = null;
		try {
			created = (Date) dateFormat.parse(tweet.getCreatedAt());
			viewHolder.date.setText(DateUtils.getRelativeTimeSpanString(created.getTime()));
		} catch (Exception e) {
			Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
			viewHolder.date.setVisibility(TextView.GONE);
		}

		viewHolder.tvRetweets.setText(Integer.toString(tweet.getRetweets()));
		viewHolder.tvRetweets.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
			}
			
		});
		
		viewHolder.tvLikes.setText(Integer.toString(tweet.getLikes()));
		viewHolder.tvRetweets.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
			}
			
		});

		viewHolder.imgTweet = (ImageView) convertView.findViewById(R.id.imgTweet);
		if (tweet.getMediaUrl() == null || tweet.getMediaUrl().isEmpty()) {
			viewHolder.imgTweet.setVisibility(ImageView.GONE);
		} else {
			try {
				Picasso.with(getContext()).load(tweet.getMediaUrl()).into(viewHolder.imgTweet);
			} catch (Exception e) {
				Log.d("Debug", e.toString());
			}
		}

		viewHolder.btnReply = (ImageButton) convertView.findViewById(R.id.btnReply);
		viewHolder.btnReply.setTag(R.id.TAG_USER, tweet.getUser().getScreenName());
		viewHolder.btnReply.setTag(R.id.TWEET_ID, tweet.getUid());
		return convertView;
	}
}
