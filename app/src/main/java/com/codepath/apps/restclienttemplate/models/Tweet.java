package com.codepath.apps.restclienttemplate.models;

import android.graphics.Movie;
import android.os.Parcelable;

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
    public int favoriteCount;

    public Tweet() {
    }

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        //extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.favoriteCount=jsonObject.getInt("favorites_count");
        return tweet;
    }

    public String getBody()  {
        return body;
    }
    public String getCreatedAt()  { return createdAt; }

}
