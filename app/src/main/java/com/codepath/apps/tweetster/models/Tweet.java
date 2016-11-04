package com.codepath.apps.tweetster.models;
/* A single tweet looks like this:
{
    "coordinates": null,
    "truncated": false,
    "created_at": "Tue Aug 28 21:16:23 +0000 2012",
    "favorited": false,
    "id_str": "240558470661799936",
    "in_reply_to_user_id_str": null,
    "entities": {
      "urls": [

      ],
      "hashtags": [

      ],
      "user_mentions": [

      ]
    },
    "text": "just another test",
    "contributors": null,
    "id": 240558470661799936,
    "retweet_count": 0,
    "in_reply_to_status_id_str": null,
    "geo": null,
    "retweeted": false,
    "in_reply_to_user_id": null,
    "place": null,
    "source": "OAuth Dancer Reborn",
    "user":      *****************              <---- moved to its own model >,
    "in_reply_to_screen_name": null,
    "in_reply_to_status_id": null
  }
 */

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Tweet implements Serializable {

    private String body;
    private long uid;
    private User user;
    private String createdAt;

    public static Tweet fromJSONObject(JSONObject object) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = object.getString("text");
            tweet.uid = object.getLong("id");
            tweet.user = User.fromJSON(object.getJSONObject("user"));
            tweet.createdAt = object.getString("created_at");
        } catch (JSONException e) {
            Log.e("ERROR", "Failed to parse a tweet", e);
            throw new RuntimeException(e);
        }

        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray array) {
        List<Tweet> result = new ArrayList<>(array.length());
        for (int ind = 0; ind < array.length(); ind++) {
            try {
                result.add(fromJSONObject(array.getJSONObject(ind)));
            } catch (JSONException e) {
                Log.e("ERROR", "Failed to extract a tweet from array", e);
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getRelativeTimeAgo() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            Log.e("ERROR", "Failed to parse created_at for timestamp", e);
            return "??";
        }

        return relativeDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
