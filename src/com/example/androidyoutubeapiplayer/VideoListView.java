package com.example.androidyoutubeapiplayer;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import yt.sdk.access.YTSDK;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.androidyoutubeapiplayer.RestClient.RequestMethod;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class VideoListView extends Activity {

	ListView lv;
	static String API_KEY="AIzaSyB7KdfoGLiX4nBBnoXtnYI2FSSgaSM9SKc";
	static String CHANNEL_KEY="UCBmr9bBy6EdQKWfkALQ4Oaw";
	int startidx=1;
	ArrayList<String> Title=new ArrayList<String>();
	ArrayList<String> Id=new ArrayList<String>();
	ArrayList<HashMap<String, String>> arrData=new ArrayList<HashMap<String,String>>();
	ListViewAdapter adapter;
	ImageLoaderConfiguration config;
	ImageLoader imageLoder;
	EditText et;
	ProgressDialog pd;
	vedio task;
	View loadingView;
	String searchText="";
	boolean loading=false;
	@Override
	public void onCreate(Bundle saved)
	{
		super.onCreate(saved);
		setContentView(R.layout.videolist_layout);
		lv=(ListView)findViewById(R.id.listView1);
		et=(EditText)findViewById(R.id.editText1);
		task=new vedio();
		loadingView=getLayoutInflater().inflate(R.layout.item_listview_footer, null);
		et.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId==EditorInfo.IME_ACTION_SEARCH)
				{
					arrData.clear();
					if(task.getStatus()==AsyncTask.Status.RUNNING)
					{
						task.cancel(true);	
						startidx=1;
						searchText=v.getText().toString();
						task=new vedio();
						task.execute("");
					}
					else
					{
						startidx=1;
						searchText=v.getText().toString();
						task=new vedio();
						task.execute("");
					}
				}
				return false;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				showAlert("Select your option ?", ((HashMap<String, String>)adapter.getItem(position)).get("Id"), ((HashMap<String, String>)adapter.getItem(position)).get("Title"));				
			}
		});
		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if ((firstVisibleItem+visibleItemCount)==totalItemCount && loading)
				{	
					if(task.getStatus()!=AsyncTask.Status.RUNNING)
					{
					loading=false;					
					task=new vedio();
					task.execute("");
					}
				}								
			}
		});
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
		.defaultDisplayImageOptions(defaultOptions)
		.memoryCache(new WeakMemoryCache()).build();
		ImageLoader.getInstance().init(config);
		imageLoder=ImageLoader.getInstance();	
		adapter=new ListViewAdapter(VideoListView.this, arrData,imageLoder);
		lv.setAdapter(adapter);
		task.execute("");
	}
	public class vedio extends AsyncTask<String, Void, Integer>
	{
		String res;
		@Override
		public void onPreExecute()
		{		
			//pd=ProgressDialog.show(VideoListView.this, null, "Loading videos....");
			lv.addFooterView(loadingView);		
		}
		@SuppressLint("NewApi")
		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			String URL="https://gdata.youtube.com/feeds/api/videos?q="+searchText+"&v=2&alt=jsonc&max-results=10&order=date&start-index="+startidx;
			URL=URL.replace(" ", "+");
			RestClient c=new RestClient(URL);
			try {
				c.Execute(RequestMethod.GET);
				res=c.getResponse();	
				JSONObject ob=new JSONObject(res);
				JSONObject jb=ob.getJSONObject("data");
				startidx=9+Integer.parseInt(jb.getString("startIndex"));
				JSONArray ja=jb.getJSONArray("items");
				for(int i=0;i<ja.length();i++)
				{
					JSONObject jo=ja.getJSONObject(i);
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("Id", jo.getString("id"));
					map.put("Title", jo.getString("title"));
					map.put("Duration", jo.getString("duration"));
					map.put("Category", jo.getString("category"));
					map.put("Rating", jo.getString("rating").isEmpty()?"0.0":jo.getString("rating"));
					JSONObject jsob=new JSONObject(jo.getString("thumbnail"));
					map.put("Image",jsob.getString("sqDefault"));	
					arrData.add(map);					
				}		
				return 1;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
		@Override
		public void onPostExecute(Integer response)
		{
			if(response==1)
			{
			adapter.notifyDataSetChanged();
			}
			try{lv.removeFooterView(loadingView);}catch(Exception e){}				
			//startidx=startidx+10;
			loading=true;
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		imageLoder.destroy();
	}
	public  void showAlert(String msg,final String VIDEO_ID,final String VIDEO_NAME ) {

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);		
		alert.setTitle(R.string.app_name);
		alert.setMessage(msg);
		alert.setNegativeButton("Download", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Do something with value!
				YTSDK.initialize(VideoListView.this);

				Log.e("Vedio Id",VIDEO_ID);		
				YTSDK.setDownloadFolderPath("/YouTube");
				YTSDK.setVideoPreview(false);	
				YTSDK.download(VIDEO_ID,VIDEO_NAME,VideoListView.this);
			}
		});
		alert.setPositiveButton("View", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Do something with value!
				Intent i=new Intent(VideoListView.this,MainActivity.class);		
				i.putExtra("Id", VIDEO_ID);
				startActivity(i);
			}
		});
		alert.show();
	}
}
