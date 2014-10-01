package com.sushant2603.apps.basictwitter.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.squareup.picasso.Picasso;
import com.sushant2603.apps.basictwitter.R;
import com.sushant2603.apps.basictwitter.models.Tweet;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, android.R.layout.simple_list_item_1, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		// Only few items in the memory. Check if recycled view.
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,
					parent, false);
		}
		ImageView imgUser = (ImageView) convertView.findViewById(R.id.imgUser);
		imgUser.setImageResource(0);
		try {
			Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(imgUser);
		} catch (Exception e) {
			Log.d("Debug", e.toString());
		}

		TextView username = (TextView) convertView.findViewById(R.id.tvUsername);
		String usernameHtml = "<font size=\"1\">" + tweet.getUser().getName() + "</font>&nbsp;" +
			"<font size=\"1\"  color=\"#B3B3B3\">@" + tweet.getUser().getScreenName() + "</font>";
		username.setText(Html.fromHtml(usernameHtml));
		TextView body = (TextView) convertView.findViewById(R.id.tvTweetBody);
		body.setText(tweet.getBody());
		TextView date = (TextView) convertView.findViewById(R.id.tvDate);

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy",
				Locale.ENGLISH);
		dateFormat.setLenient(true);
		Date created = null;
		try {
			created = (Date) dateFormat.parse(tweet.getCreatedAt());
			date.setText(DateUtils.getRelativeTimeSpanString(created.getTime()));
		} catch (Exception e) {
			Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
			date.setVisibility(TextView.GONE);
		}

		TextView tvRetweets = (TextView) convertView.findViewById(R.id.tvRetweets);
		tvRetweets.setText(Integer.toString(tweet.getRetweets()));
		TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
		tvLikes.setText(Integer.toString(tweet.getLikes()));
		return convertView;
	}
}
