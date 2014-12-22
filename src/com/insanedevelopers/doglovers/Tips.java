package com.insanedevelopers.doglovers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.insanedevelopers.doglovers.quickblox.DataHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Tips extends Fragment {
	
	public Tips(){}

	private AdView mAdView;
	public static final String TAG = Tips.class.getSimpleName();
	public static int getpos;
	ViewPager mViewPager;
	TextView content;
	TextView heading;
	RelativeLayout relay;
	int temp=0;
	View imageLayout;
	ImageView imageView;
	int i,j;
	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy",Locale.ENGLISH);
    String currentDate = sdf.format(new Date());
    int listsize=DataHolder.getDataHolder().getQbFileListSize();
    String tempImage;
    
	public static Tips newInstance() {
		return new Tips();
	}
	
	DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true)
				.cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300))
				.build();
		
		mAdView= new AdView(getActivity());
	}

	
	
	@Override
	public void onPause() {
		mAdView.pause();
		super.onPause();
	}



	@Override
	public void onDestroy() {
        // TODO Auto-generated method stub
        mAdView.destroy();
        super.onDestroy();
    }



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_item_one, container, false);
		
		
		mAdView = (AdView) v.findViewById(R.id.adViewfrag);
		mAdView.loadAd(new AdRequest.Builder().build());
		
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		mViewPager.setAdapter(new ImageAdapter());
		
        return v;
	}
	
	public void onShare(View v){
		shareimage();
	}
	
	protected void shareimage(){
    	Uri myUri1 = Uri.parse("file:///sdcard/"+currentDate);
    	Intent share = new Intent(Intent.ACTION_SEND);
    	share.setType("image/");
    	share.putExtra(Intent.EXTRA_STREAM,myUri1);
    	startActivity(Intent.createChooser(share, "Share Image"));}
	
	
	private class ImageAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		ImageAdapter() {
			
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String title="Petting Tip ";
			switch (position) {
			case 0:
				return ("Petting Tip of the Day");
			default:
				return (title+(position+1));
			}
		}
		
		@Override
		public int getCount() {
			return DataHolder.getDataHolder().getQbFileListSize();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			
			
				/* Error Error Error
				 * 
				 * The content position is not according to
				 * the images correct position.
				 * 
				 *****
				 *
				 *  The content of 1st and 2nd position are same.
				 *  
				 *  */
			
				imageLayout = inflater.inflate(R.layout.fragment_tips, view, false);
				assert imageLayout != null;
				if(position==1)temp=0;
				imageView = (ImageView) imageLayout.findViewById(R.id.imageView1);
				//final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
				final RelativeLayout imageViewTemp = (RelativeLayout) imageLayout.findViewById(R.id.imageViewTemp);
				content = (TextView) imageLayout.findViewById(R.id.textContent);
				heading = (TextView) imageLayout.findViewById(R.id.textHeading);
				
					for(i=0,j=listsize-1;i<listsize;i++,j--){
						if(position!=0){
					if(DataHolder.getDataHolder().getNotePosition(j)==listsize-mViewPager.getCurrentItem()-1){
						heading.setText(DataHolder.getDataHolder().getNoteTitle(j));
						content.setText(DataHolder.getDataHolder().getNoteContent(j));
						
						
				}}
					else{
						if((mViewPager.getCurrentItem()==0)&&(DataHolder.getDataHolder().getNotePosition(i)==listsize)){
							heading.setText(DataHolder.getDataHolder().getNoteTitle(i));
						content.setText(DataHolder.getDataHolder().getNoteContent(i));
						}
					}
						if(Integer.parseInt(DataHolder.getDataHolder().getName(listsize-position-1))==(i+1)){
							
							tempImage="http://qbprod.s3.amazonaws.com/" + DataHolder.getDataHolder().getPublicUrl(i);
						}
					
				}
				
				ImageLoader.getInstance().displayImage(tempImage
						, imageView, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						//spinner.setVisibility(View.VISIBLE);
						imageViewTemp.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
						}

						
						Log.e("Tips", message);
						//spinner.setVisibility(View.GONE);
						imageViewTemp.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						//spinner.setVisibility(View.GONE);
						imageViewTemp.setVisibility(View.GONE);
						
					}
				});
				
			view.addView(imageLayout, position);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}


}
