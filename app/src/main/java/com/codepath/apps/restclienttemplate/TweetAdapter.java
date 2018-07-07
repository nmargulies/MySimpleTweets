package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    List<Tweet> mTweets;
    Context context;

    public TweetAdapter(List<Tweet> mTweets, Context context) {
        this.mTweets = mTweets;
        this.context = context;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // pass in the Tweets array in the constructor

    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }
    // for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        final ViewHolder viewHolder = new ViewHolder(tweetView);

        viewHolder.buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Tweet tweet = mTweets.get(position);
                tweet.toggleFavorite(new JsonHttpResponseHandler());
                viewHolder.buttonFavorite.setImageResource(R.drawable.ic_vector_heart);
                viewHolder.tvFavoriteCount.setText(tweet.getFavoriteCount());

            }
        });

        viewHolder.buttonRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Tweet tweet = mTweets.get(position);
                viewHolder.tvRetweetCount.setText(tweet.getRetweetCount());
            }
        });

        viewHolder.buttonReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Tweet tweet = mTweets.get(position);
                Intent intent = new Intent(context, ComposeActivity.class);

                // serialize the movie using parceler, use short name as key
                intent.putExtra("screenname", tweet.user.screenName);

                // show activity
                context.startActivity(intent);

            }
        });


        return viewHolder;
    }

    private void updateButton(ImageButton b, boolean isActive, int strokeResId, int fillResId, int activeColor) {
        b.setImageResource(isActive ? fillResId : strokeResId);
        b.setColorFilter(ContextCompat.getColor(context, isActive ? activeColor : R.color.twitter_grey));
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to the position
        Tweet tweet = mTweets.get(position);

        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvDate.setText(getRelativeTimeAgo(tweet.createdAt));
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivProfileImage) public ImageView ivProfileImage;
        @BindView(R.id.tvUserName) public TextView tvUsername;
        @BindView(R.id.tvBody) public TextView tvBody;
        @BindView(R.id.tvDate) public TextView tvDate;
        @BindView(R.id.tvRetweetCount) TextView tvRetweetCount;
        @BindView(R.id.tvFavoriteCount) TextView tvFavoriteCount;
        @BindView(R.id.buttonRetweet) ImageButton buttonRetweet;
        @BindView(R.id.buttonFavorite) ImageButton buttonFavorite;
        @BindView(R.id.buttonReply) ImageButton buttonReply;

        public ViewHolder (View itemView) {
            super (itemView);
            ButterKnife.bind(this, itemView);

            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();

            // make sure the position is valid ie. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {

                // get the movie at the position
                Tweet tweet = mTweets.get(position);

                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetailsActivity.class);

                // serialize the movie using parceler, use short name as key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));

                // show activity
                context.startActivity(intent);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
