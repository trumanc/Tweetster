package com.codepath.apps.tweetster.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweetster.R;
import com.codepath.apps.tweetster.TwitterClient;
import com.codepath.apps.tweetster.application.TweetsterApplication;
import com.codepath.apps.tweetster.fragments.UserTimelineFragment;
import com.codepath.apps.tweetster.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    private TwitterClient client;
    private User user;

    private static final String EXTRA_SCREEN_NAME = "extra_screen_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TweetsterApplication.getRestClient();

        final String screenName = getIntent().getStringExtra(EXTRA_SCREEN_NAME);

        if (screenName != null) {
            client.getUserInfo(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if (response.length() > 0) {
                        try {
                            user = User.fromJSON(response.getJSONObject(0));
                        } catch (JSONException e) {
                            Log.e("ERROR", "Couldn't extract a user for profile activity.", e);
                            finish();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "No user with screenname: " + screenName, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    populateProfileHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(ProfileActivity.this, "API call failed", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", "Throwable: ", throwable);
                    finish();
                }
            });
        } else {

            client.getLoggedInUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    populateProfileHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(ProfileActivity.this, "API call failed", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", "Throwable: ", throwable);
                    finish();
                }
            });
        }

        UserTimelineFragment frag = UserTimelineFragment.newInstance(screenName);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.flContainer, frag);

        ft.commit();
    }

    public static Intent forUsername(Context context, String username) {
        Intent i = new Intent(context, ProfileActivity.class);
        i.putExtra(EXTRA_SCREEN_NAME, username);
        return i;
    }

    public static Intent forDefaultUser(Context context) {
        Intent i = new Intent(context, ProfileActivity.class);
        return i;
    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(Integer.toString(user.getNumFollowers()) + " Followers");
        tvFollowing.setText(Integer.toString(user.getNumFollowing()) + " Following");

        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }
}
