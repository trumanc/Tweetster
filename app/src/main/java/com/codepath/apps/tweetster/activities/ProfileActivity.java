package com.codepath.apps.tweetster.activities;

import android.app.ProgressDialog;
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

import static android.content.Intent.EXTRA_USER;

public class ProfileActivity extends AppCompatActivity {
    private static TwitterClient client = TweetsterApplication.getRestClient();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = (User) getIntent().getSerializableExtra(EXTRA_USER);

        getSupportActionBar().setTitle("@" + user.getScreenName());
        populateProfileHeader(user);


        UserTimelineFragment frag = UserTimelineFragment.newInstance(user.getScreenName());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.flContainer, frag);

        ft.commit();
    }

    public static void startForUsername(final Context context, final String username) {

        final Intent i = new Intent(context, ProfileActivity.class);

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();

        client.getUserInfo(username, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (response.length() > 0) {
                    try {
                        i.putExtra(EXTRA_USER, User.fromJSON(response.getJSONObject(0)));
                        context.startActivity(i);
                    } catch (JSONException e) {
                        Log.e("ERROR", "Couldn't extract a user for profile activity.", e);
                        Toast.makeText(context, "Network call failed for some reason...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "No user with screenname: " + username, Toast.LENGTH_SHORT).show();
                }
                pd.cancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(context, "API call failed", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "Throwable: ", throwable);
                pd.cancel();
            }
        });
    }

    public static void startForDefaultUser(final Context context) {
        final Intent i = new Intent(context, ProfileActivity.class);
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();


        client.getLoggedInUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                i.putExtra(EXTRA_USER, User.fromJSON(response));
                context.startActivity(i);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(context, "API call failed", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "Throwable: ", throwable);
                pd.cancel();
            }
        });
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
