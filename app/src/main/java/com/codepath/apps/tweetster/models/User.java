package com.codepath.apps.tweetster.models;

/*
{
  "name": "OAuth Dancer",
  "profile_sidebar_fill_color": "DDEEF6",
  "profile_background_tile": true,
  "profile_sidebar_border_color": "C0DEED",
  "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
  "created_at": "Wed Mar 03 19:37:35 +0000 2010",
  "location": "San Francisco, CA",
  "follow_request_sent": false,
  "id_str": "119476949",
  "is_translator": false,
  "profile_link_color": "0084B4",
  "entities": {
    "url": {
      "urls": [
        {
          "expanded_url": null,
          "url": "http://bit.ly/oauth-dancer",
          "indices": [
            0,
            26
          ],
          "display_url": null
        }
      ]
    },
    "description": null
  },
  "default_profile": false,
  "url": "http://bit.ly/oauth-dancer",
  "contributors_enabled": false,
  "favourites_count": 7,
  "utc_offset": null,
  "profile_image_url_https": "https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
  "id": 119476949,
  "listed_count": 1,
  "profile_use_background_image": true,
  "profile_text_color": "333333",
  "followers_count": 28,
  "lang": "en",
  "protected": false,
  "geo_enabled": true,
  "notifications": false,
  "description": "",
  "profile_background_color": "C0DEED",
  "verified": false,
  "time_zone": null,
  "profile_background_image_url_https": "https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
  "statuses_count": 166,
  "profile_background_image_url": "http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
  "default_profile_image": false,
  "friends_count": 14,
  "following": false,
  "show_all_inline_media": false,
  "screen_name": "oauth_dancer"
}
 */

import com.codepath.apps.tweetster.application.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Table(database = MyDatabase.class)
public class User extends BaseModel implements Serializable {
    @Column
    private String name;

    @PrimaryKey
    private long uid;

    @Column
    private String screenName;

    @Column
    private String profileImageUrl;

    @Column
    private int numFollowers;

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setNumFollowers(int numFollowers) {
        this.numFollowers = numFollowers;
    }

    public void setNumFollowing(int numFollowing) {
        this.numFollowing = numFollowing;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @Column
    private int numFollowing;

    @Column
    private String tagline;

    public User() {
        super();
    }

    public User(JSONObject object) {
        try {
            name = object.getString("name");
            uid = object.getLong("id");
            screenName = object.getString("screen_name");
            profileImageUrl = object.getString("profile_image_url");
            tagline = object.getString("description");
            numFollowers = object.getInt("followers_count");
            numFollowing = object.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static User fromJSON(JSONObject object) {
        return new User(object);
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public int getNumFollowing() {
        return numFollowing;
    }

    public String getTagline() {
        return tagline;
    }
}
