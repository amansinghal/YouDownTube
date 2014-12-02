package com.example.androidyoutubeapiplayer;

import yt.sdk.access.YTSDK;
import yt.sdk.access.YTSDK.DownloadFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity implements
YouTubePlayer.OnInitializedListener{
	
	public static final String API_KEY = "AIzaSyCe6tORd9Ch4lx-9Ku5SQ476uS9OtZYsWA";
	public static String VIDEO_ID = "o7VVHhK9zf0";
	public static String VIDEO_NAME = "Default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VIDEO_ID=getIntent().getStringExtra("Id");
        YTSDK.initialize(this);
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtubeplayerview);        
        youTubePlayerView.initialize(API_KEY, this);
        
    }

	@Override
	public void onInitializationFailure(Provider provider,
			YouTubeInitializationResult result) {
		Toast.makeText(getApplicationContext(), 
				"onInitializationFailure()", 
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onInitializationSuccess(Provider provider, YouTubePlayer player,
			boolean wasRestored) {
		if (!wasRestored) {
		      player.cueVideo(VIDEO_ID);
		      player.setFullscreen(true);
		      player.setPlayerStateChangeListener(new PlayerStateChangeListener() {				
				@Override
				public void onVideoStarted() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onVideoEnded() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoading() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoaded(String VedioId) {
					// TODO Auto-generated method stub
				
				}
				
				@Override
				public void onError(ErrorReason arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAdStarted() {
					// TODO Auto-generated method stub
					
				}
			});
		    }
	}


}
