package com.codepath.apps.restclienttemplate.models;

import android.graphics.Movie;
import android.os.Parcelable;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {
    //list out attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public Integer favoriteCount;
    public boolean isFavorited;
    public Integer retweetCount;
    public boolean isRetweeted;

    public Tweet() {
    }

    public void toggleFavorite(JsonHttpResponseHandler handler) {
        favoriteCount += !isFavorited ? 1 : -1;
    }

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        //extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.isRetweeted = jsonObject.getBoolean("retweeted");
        tweet.isFavorited = jsonObject.getBoolean("favorited");
        Long retweetLong = jsonObject.getLong("retweet_count");
        tweet.retweetCount = retweetLong.intValue();
        Long favoriteCount = jsonObject.getLong("favorite_count");
        tweet.favoriteCount = favoriteCount.intValue();

        return tweet;
    }

    public String getBody()  {
        return body;
    }
    public String getCreatedAt()  { return createdAt; }
    public  String getUser() { return user.screenName; }
    public  Integer getFavoriteCount() { return favoriteCount; }
    public  Integer getRetweetCount() { return retweetCount; }

}
