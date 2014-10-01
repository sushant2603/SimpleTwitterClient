package com.sushant2603.apps.basictwitter.activities;

import com.sushant2603.apps.basictwitter.R;
import com.sushant2603.apps.basictwitter.R.layout;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ComposeActivity extends Activity {

	private ImageView ivUserImage;
	private TextView tvUserName;
	private TextView tvUserHandle;
	private EditText etTweet;
	private Button btnTweet;
	private Button btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		getActionBar().setTitle("Compose");
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 0, 185, 255)));
		
		ivUserImage = (ImageView) findViewById(R.id.ivComposeIcon);
		tvUserName = (TextView) findViewById(R.id.etComposeUser);
		tvUserHandle = (TextView) findViewById(R.id.etComposerUserHandle);
		etTweet = (EditText) findViewById(R.id.etComposeTweet);
		btnTweet = (Button) findViewById(R.id.btnTweetDown);
		btnCancel = (Button) findViewById(R.id.btnReply);
	}
}
