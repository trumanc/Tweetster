package com.codepath.apps.tweetster.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.tweetster.TwitterClient;
import com.codepath.apps.tweetster.application.TweetsterApplication;
import com.codepath.apps.tweetster.models.Tweet;
import com.codepath.apps.tweetster.models.Tweet_Table;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.id;

public class MentionsTimelineFragment extends TweetListFragment {
    private TwitterClient client;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, container, savedInstanceState);

        client = TweetsterApplication.getRestClient();


        List<Tweet> cachedTweets = new Select()
                .from(Tweet.class)
                .where(Tweet_Table.isMention.eq(true)) // This could break if we allowed the user to sign out of the app. Good thing we don't :)
                .orderBy(Tweet_Table.uid, false)
                .queryList();
        addAll(cachedTweets);
        refreshing = true;

        return v;
    }

    @Override
    protected void loadTweetsSinceId(Long id) {
        adapter.showLoaderBar(true);
        client.getMentionsTimeline(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromMentionsJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject errorResponse) {
                adapter.showLoaderBar(false);
                swipeContainer.setRefreshing(false);
                Log.e("ERROR", errorResponse != null ? errorResponse.toString() : "", error);
            }
        });
    }

}
