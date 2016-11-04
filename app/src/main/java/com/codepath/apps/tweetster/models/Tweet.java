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

import com.codepath.apps.tweetster.application.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Table(database = MyDatabase.class)
public class Tweet extends BaseModel implements Serializable {

    @Column
    private boolean isMention;

    public boolean isMention() {
        return isMention;
    }

    public void setMention(boolean mention) {
        isMention = mention;
    }

    @Column
    private String body;

    public void setUid(long uid) {
        this.uid = uid;
    }

    @PrimaryKey
    private long uid;

    @ForeignKey
    private User user;

    @Column
    private String createdAt;

    public Tweet() {
        super();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Tweet(JSONObject object) {
        super();
        try {
            body = object.getString("text");
            uid = object.getLong("id");
            user = User.fromJSON(object.getJSONObject("user"));
            createdAt = object.getString("created_at");
        } catch (JSONException e) {
            Log.e("ERROR", "Failed to parse a tweet", e);
            throw new RuntimeException(e);
        }
    }

    public Tweet(JSONObject object, boolean isMention) {
        this(object);
        this.isMention = isMention;
    }

    public static Tweet fromJSONObject(JSONObject object) {
        return new Tweet(object, false);
    }

    public static Tweet fromMentionJSONObject(JSONObject object) {
        return new Tweet(object, true);
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

    public static List<Tweet> fromMentionsJSONArray(JSONArray array) {
        List<Tweet> result = new ArrayList<>(array.length());
        for (int ind = 0; ind < array.length(); ind++) {
            try {
                result.add(fromMentionJSONObject(array.getJSONObject(ind)));
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
