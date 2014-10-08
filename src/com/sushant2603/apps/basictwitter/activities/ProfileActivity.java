package com.sushant2603.apps.basictwitter.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import com.sushant2603.apps.basictwitter.R;
import com.sushant2603.apps.basictwitter.fragments.UserTimelineFragment;
import com.sushant2603.apps.basictwitter.models.User;

public class ProfileActivity extends FragmentActivity {

	private RelativeLayout rlInfo;
	private User user;
	private Target backgroundImageTarget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		user = (User) getIntent().getSerializableExtra("user");
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 0, 185, 255)));
		getActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + "@" + user.getScreenName() + "</font>"));
		loadProfileInfo();
	}
	
	public void loadProfileInfo() {
		/*TwitterApplication.getRestClient().getUser(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject jsonObject) {
				User user = User.fromJSON(jsonObject);
				getActionBar().setTitle("@" + user.getScreenName());
				populateProfileHeader();
			}
		});*/
		populateProfileHeader();
	}
	
	public void populateProfileHeader() {
		TextView tvName = (TextView) findViewById(R.id.tvProfileUser);
		TextView tvScreenName = (TextView) findViewById(R.id.tvProfileTag);
		Button tvFollowers = (Button) findViewById(R.id.tvProfileFollowers);
		Button tvFollowing = (Button) findViewById(R.id.tvProfileFollowing);
		Button tvTweets = (Button) findViewById(R.id.tvProfileTweets);

		tvFollowers.setText(
			Html.fromHtml("<b>&nbsp;&nbsp;" + Integer.toString(user.getNFollowers()) + "</b><br>" +
				"<font color=\"#AAAAAA\">&nbsp;&nbsp; FOLLOWERS</font>"));
		tvTweets.setText(
			Html.fromHtml("<b>&nbsp;&nbsp;" + Integer.toString(user.getNTweets()) + "</b><br>" +
					"<font color=\"#AAAAAA\">&nbsp;&nbsp;TWEETS</font>"));
		tvFollowing.setText(
			Html.fromHtml("<b>&nbsp;&nbsp;" + Integer.toString(user.getNFavorites()) + "</b><br>" +
				"<font color=\"#AAAAAA\">&nbsp;&nbsp; FOLLOWING</font>"));

		ImageView ivUserImage = (ImageView) findViewById(R.id.imageView1);
		rlInfo = (RelativeLayout) findViewById(R.id.userInfo);
		backgroundImageTarget = new Target() {
			@Override
			public void onPrepareLoad(Drawable arg0) {}
			@Override
			public void onBitmapLoaded(Bitmap bitmap, LoadedFrom arg1) {
				rlInfo.setBackground(new BitmapDrawable(null, bitmap));
			}
			@Override
			public void onBitmapFailed(Drawable arg0) {}
		};

		Picasso.with(this).load(user.getProfileBackgroundImageUrl()).resize(200,200).centerCrop()
			.into(backgroundImageTarget);
		tvName.setText(user.getName());
		tvScreenName.setText("@" + user.getScreenName());
		Picasso.with(this).load(user.getProfileImageUrl()).into(ivUserImage);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UserTimelineFragment fragment = new UserTimelineFragment();
		fragment.setUser(user);
		ft.replace(R.id.profileTweetsTimeline, fragment);
		ft.commit();
	}
}
