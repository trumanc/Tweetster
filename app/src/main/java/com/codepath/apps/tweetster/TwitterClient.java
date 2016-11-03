package com.codepath.apps.tweetster;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.*;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = BuildConfig.TWITTER_API_KEY;
	public static final String REST_CONSUMER_SECRET = BuildConfig.TWITTER_API_SECRET;
	public static final String REST_CALLBACK_URL = "oauth://trumantweetster"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(Long id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", 25);

        if (id != null) {
            params.put("max_id", id - 1);
        }

        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(Long id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", 25);

        if (id != null) {
            params.put("max_id", id - 1);
        }

        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(Long id, String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (id != null) {
            params.put("max_id", id - 1);
        }
        params.put("screen_name", screenName);

        getClient().get(apiUrl, params, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, null, handler);
    }
}
