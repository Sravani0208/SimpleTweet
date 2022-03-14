    package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

    class TimelineActivity : AppCompatActivity() {

    lateinit var  client: TwitterClient
    lateinit var rvTweets: RecyclerView
    lateinit var adaptor: TweetsAdaptor
    lateinit var swipeContainer: SwipeRefreshLayout

    val tweets = ArrayList<Tweet>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client =TwitterApplication.getRestClient(this)
        swipeContainer= findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            Log.i(TAG,"Refreshing the Timeline")
            popularHomeTimeline()
        }
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        rvTweets=findViewById(R.id.rvTweets)
        adaptor= TweetsAdaptor(tweets)

        rvTweets.layoutManager=LinearLayoutManager(this)
        rvTweets.adapter=adaptor

        popularHomeTimeline()
    }

    fun popularHomeTimeline(){
        client.getHomeTimeline(object :JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "onSuccess! $json")

                val jsonArray=json?.jsonArray

                try {
                    //clear  out our  currently fetched
                    adaptor.clear()
                val listOfNewTweetsRetrived=Tweet.fromJsonArray(jsonArray)
                tweets.addAll(listOfNewTweetsRetrived)
                adaptor.notifyDataSetChanged()
                    // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false)
            } catch (e: JSONException){
                Log.e(TAG, "Json Exception $e")
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure! $statusCode")
            }


        })
    }
        companion object{
            val TAG = "TimelineActivity"
        }
}