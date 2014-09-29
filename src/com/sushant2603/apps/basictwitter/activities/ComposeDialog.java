package com.sushant2603.apps.basictwitter.activities;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sushant2603.apps.basictwitter.R;
import com.sushant2603.apps.basictwitter.models.Tweet;
import com.sushant2603.apps.basictwitter.models.User;

public class ComposeDialog extends DialogFragment {

	private ImageView ivUserImage;
	private TextView tvUserName;
	private TextView tvUserHandle;
	private EditText etTweet;
	private Button btnTweet;
	private ImageButton btnCancel;
	private User user;
	public ComposeDialogListener listener;

    static ComposeDialog newInstance(User user) {
        ComposeDialog dialog = new ComposeDialog();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        dialog.setArguments(args);
        return dialog;
    }

    public interface ComposeDialogListener {
    	void onFinishComposeDialog(Tweet tweet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container);
        user = (User) getArguments().getSerializable("user");
        ivUserImage = (ImageView) view.findViewById(R.id.ivUserComposeImage);
        tvUserName = (TextView) view.findViewById(R.id.etComposeUser);
        tvUserHandle = (TextView) view.findViewById(R.id.etComposerUserHandle);
        etTweet = (EditText) view.findViewById(R.id.etComposeTweet);

		ivUserImage.setImageResource(0);
		Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivUserImage);
		tvUserName.setText(Html.fromHtml("<font color=\"#606060\" type=\"roboto\">" + user.getName() + "</font>"));
		tvUserHandle.setText(Html.fromHtml("<font color=\"#B3B3B3\">@" + user.getScreenName() + "</font>"));

        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setBackgroundColor(Color.argb(255, 0, 185, 255));
        btnTweet.setTextColor(Color.argb(255, 255, 255, 255));
        btnTweet.setGravity(android.view.Gravity.CENTER);
        btnCancel = (ImageButton) view.findViewById(R.id.btnComposeCancel);


        btnTweet.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View v) {
        		Tweet tweet = new Tweet();
                tweet.setBody(etTweet.getText().toString());
                tweet.setUser(user);
    		    listener.onFinishComposeDialog(tweet);
        		getDialog().dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    listener.onFinishComposeDialog(null);
				getDialog().dismiss();
			}
		});
    	Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);
        window.setSoftInputMode(
        		WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    @Override
    public void onStart() {
    	super.onStart();
    	// safety check
    	if (getDialog() == null) {
    		return;
    	}
    	getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}