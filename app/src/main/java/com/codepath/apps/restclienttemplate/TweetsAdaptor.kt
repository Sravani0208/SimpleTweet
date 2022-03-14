package com.codepath.apps.restclienttemplate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet

class TweetsAdaptor(val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdaptor.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    //Populate data into the view through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //get data model based on position
        val tweet: Tweet = tweets.get(position)
        //set item view based on views and data model
        holder.tvUserName.text = tweet.user?.name
        holder.tvTweetBody.text = tweet.body
        holder.tvTimeStamp.setText(tweet.getFormattedTimestamp())
        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)

    }

    override fun getItemCount(): Int {
        return tweets.size
    }
    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
        val tvTimeStamp = itemView.findViewById<TextView>(R.id.tvTimeStamp)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)


    }
}
