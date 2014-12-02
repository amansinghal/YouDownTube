package com.example.androidyoutubeapiplayer;

import java.util.ArrayList;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	ArrayList<HashMap<String,String>> VideoData;
	LayoutInflater inflater;
	ImageLoaderConfiguration config;
	ImageLoader imageLoder;
	public ListViewAdapter(Context context,ArrayList<HashMap<String,String>> VideoData,ImageLoader imageLoader) {
		this.context = context;
		this.VideoData=VideoData;
		this.imageLoder=imageLoader;
	}

	@Override
	public int getCount() {
		return VideoData.size();
	}

	@Override
	public Object getItem(int position) {
		return VideoData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// Declare Variables
		viewHolder holder;
		if(convertView==null){
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder=new viewHolder();
			convertView = inflater.inflate(R.layout.listview_item, parent, false);
			// Locate the TextViews in listview_item.xml
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.iv_icon=(ImageView)convertView.findViewById(R.id.iv_icon);
			holder.tv_duaration=(TextView)convertView.findViewById(R.id.tv_duration);
			holder.rb_rating=(RatingBar)convertView.findViewById(R.id.rb_rating);
			holder.tv_catogary=(TextView)convertView.findViewById(R.id.tv_catogary);
			convertView.setTag(holder);
		}
		else
		{
			holder=(viewHolder)convertView.getTag();
		}	
		holder.tv_catogary.setText("Category: "+VideoData.get(position).get("Category"));
		holder.tv_title.setText(VideoData.get(position).get("Title"));
		holder.rb_rating.setRating(Float.parseFloat(VideoData.get(position).get("Rating")));
		int duration=Integer.parseInt(VideoData.get(position).get("Duration"));
		int duration_min=duration/60;
		int duration_second=duration%60;
		String val1=String.valueOf(duration_min);
		String val2=String.valueOf(duration_second).length()==1?duration_second+"0":""+duration_second;
		holder.tv_duaration.setText("Duration: "+val1+":"+val2);
		imageLoder.displayImage(VideoData.get(position).get("Image"), holder.iv_icon,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				Log.e("onLoadingFailed",arg2.toString());
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		return convertView;
	}
	public class viewHolder{
		TextView tv_title;
		ImageView iv_icon;
		RatingBar rb_rating;
		TextView tv_duaration,tv_catogary;
	}
}