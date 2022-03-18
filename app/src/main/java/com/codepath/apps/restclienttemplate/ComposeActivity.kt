package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    lateinit var tvValue: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        client = TwitterApplication.getRestClient(this)
        tvValue = findViewById(R.id.tvValue)


        //Handles the content of user tweet
        btnTweet.setOnClickListener {
            val tweetContent = etCompose.text.toString()

            //for count the characters
            // etCompose.addTextChangedListener(object : TextWatcher {
            /*override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    Log.i(TAG, "On befor text changed the")
                }

                override fun afterTextChanged(s: Editable) {
                    // Fires right after the text has changed
                    //tvValue.setText(s.toString())
                    tvValue.setText(280 - s.toString().length);

                }
           // })*/
            //etCompose.addTextChangedListener("tvValue")


            //tweet is not empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets not allowed", Toast.LENGTH_SHORT).show()
            } else
            //tweet not exceed 140 character
                if (tweetContent.length > 280) {
                    Toast.makeText(
                        this,
                        "Tweet is to long! Limit is 280  characters",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    client.pulishTweet(tweetContent, object : JsonHttpResponseHandler() {

                        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                            Log.i(TAG, "Successfully published  tweet!")
                            val tweet = Tweet.fromJson(json.jsonObject)

                            val intent = Intent()
                            intent.putExtra("tweet", tweet)
                            setResult(RESULT_OK, intent)
                            finish()
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                            Log.e(TAG, "Failed to publish tweet", throwable)
                        }

                    })
                }
        }

        val mTextEditorWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //This sets a textview to the current length

            }

            override fun afterTextChanged(s: Editable) {
                tvValue.setText(s.length.toString())
                if (s.length.toString() <= 280.toString()) {
                    // alert the user to delete the exceeded characters
                    tvValue.setTextColor(Color.BLACK)
                } else
                if (s.length.toString() > 280.toString()) {
                    // alert the user to delete the exceeded characters
                    tvValue.setTextColor(Color.RED);
                }

            }
        }
        etCompose.addTextChangedListener(mTextEditorWatcher)
    }
    companion object{
        val TAG="ComposeActivity"
    }
}

