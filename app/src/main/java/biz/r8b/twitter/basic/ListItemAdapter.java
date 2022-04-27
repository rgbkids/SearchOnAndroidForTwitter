package biz.r8b.twitter.basic;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import biz.r8b.twitter.basic.DemoActivity.CustomAdapter;

import com.google.ads.AdView;

import twitter4j.IDs;
import twitter4j.MediaEntity;
import twitter4j.MediaEntity.Size;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListItemAdapter extends ArrayAdapter<ListItem> {

    private LayoutInflater mInflater;
    private int layoutId;
	private boolean isSpannable;
//	private NoSync noSync;
	private boolean imageOn;

    public ListItemAdapter(Context context,
                           int rid, List<ListItem> list, int layoutId, boolean isSpannable) {
        super(context, rid, list);
        mInflater = (LayoutInflater)context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.layoutId = layoutId;
        this.isSpannable = isSpannable;

//        this.noSync = new NoSync(this);

        //
        imageOn = (BaseActivity.getString(context, "imageOn")).equals("");

        //
        if (
		!(context instanceof TwQueryListActivity)
		&& !(context instanceof TwListTab1Activity)
		&& !(context instanceof TwCameraImageActivity)
		&& !(context instanceof TwOptionMenuActivity)
		) {
        	textSizeSetState = true;
        }

        //
        if (getContext() instanceof TwDMActivity || getContext() instanceof TwAtActivity) {
        	midokuState = true;
        }

        //
        tweetLayoutRID = BaseActivity.getTweetLayoutRID(context);

        //
        if (getContext() instanceof TwListSampleActivity) {
        	isSamplePage = true;
        }

        //
        if (getContext() instanceof TwTimelineActivity) {
        	isTimeline = true;
        }
    }


    Handler mHandler = new Handler();
	private boolean textSizeSetState;
	private boolean midokuState;
	private boolean isSamplePage;
	private boolean isTimeline;
	private int tweetLayoutRID;

	//
    public View getView(final int position, final View convertView, ViewGroup parent) { // このメソッドはListViewが表示されている間、常に呼ばれ続ける
    	return getView(position);
    }

    //
    public View getView(final int position) { // このメソッドはListViewが表示されている間、常に呼ばれ続ける

//    	if (convertView != null) return convertView;



    	// データを取り出す
        final ListItem item = (ListItem)getItem(position);


        //
        if (item.comment == null || item.comment.equals("")) {
             View view = mInflater.inflate(R.layout.list_item2, null);
        	 return view;
        }
        else if (item.comment.equals("Loading...")) {
             View view = mInflater.inflate(R.layout.list_item5, null);

             if (getContext() instanceof TwTimelineActivity || getContext() instanceof SwipeActivity) {
	             //
	             LinearLayout layout = (LinearLayout)view.findViewById(R.id.ll01);
	             layout.setOrientation(LinearLayout.VERTICAL);
	             layout.setGravity(Gravity.CENTER);

	             //
	             ProgressBar pbar = new ProgressBar(getContext());

	             //
	             LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	            		 ViewGroup.LayoutParams.WRAP_CONTENT,
	            		 ViewGroup.LayoutParams.WRAP_CONTENT
	             );

	         	 //
	             param.setMargins(0, 0, 0, 0);
	         	 layout.addView(pbar, param);
             }

         	return view;
        }
        else if (item.comment.equals("NO DATA")) {
//        else if (item.comment.startsWith("NO DATA")) {
            View view = mInflater.inflate(R.layout.list_item6, null);

            if (false) {
            TextView tv = (TextView)view.findViewById(R.id.textNoData);
        	if (!BaseActivity.isProduct(view.getContext())) {
	            tv.setText((BaseActivity.ja?"- Free Version -":tv.getText()));
        	}
            }

       	 	return view;
        }
        else if (item.comment.equals("READ")) {
            View view = mInflater.inflate(R.layout.list_item9, null);

            //
            TextView tv = (TextView)view.findViewById(R.id.name);
            try {
            	int midokuNum = ((TwTimelineActivity)getContext()).midokuNum;

	            //
            	if (midokuNum > 0) {
		            tv.setText(
//		            		tv.getText()
//		            				+ ((BaseActivity.ja)?" 新": " New!")
//		            				+ "(" + ((TwTimelineActivity)getContext()).midokuNum + ")"
//		            				+ ((BaseActivity.ja)?"↑": "")

		            		""

		            		+ ""
		            		+ ((BaseActivity.ja)?"↓ここまで読んだ (": "read")
		            		+ ((TwTimelineActivity)getContext()).midokuNum
		            		+ ((BaseActivity.ja)?"件の新しいﾂｲｰﾄ↑)": "new Tweets")


//		            		+ "↑ "
//		            		+ ((TwTimelineActivity)getContext()).midokuNum
//		            		+ ((BaseActivity.ja)?"件の新しいﾂｲｰﾄ": "new Tweets")
//		            		+ "  "
//		            		+ ((BaseActivity.ja)?"ここまで読んだ↓": "read")





//		            		+ "\n"
		            );
            	}
            } catch (Exception e) {}

       	 	return view;
        }
        else if (item.isNewTweetInfo) {
        	//
            View view = mInflater.inflate(R.layout.list_item11, null);

//            View view = item.newTweetInfoView;

            //
            LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);


            //
            TextView tv = (TextView)view.findViewById(R.id.name);
            try {
//            	int midokuNum = ((TwTimelineActivity)getContext()).midokuNum;

            	int padding = BaseActivity.newTweetNumPadding;

	            //
//            	if (midokuNum > 0) {
		            tv.setText(
//		            		tv.getText()
//		            				+ ((BaseActivity.ja)?" 新": " New!")
//		            				+ "(" + ((TwTimelineActivity)getContext()).midokuNum + ")"
//		            				+ ((BaseActivity.ja)?"↑": "")
		            		item.newTweetNum + "" + ((BaseActivity.ja)?"件の新しいツイート(タップで反映)":" new Tweets (Tap!)")
		            );
		            tv.setTextColor(Color.WHITE);
		            tv.setBackgroundColor(Color.BLACK);

		            tv.setGravity(Gravity.CENTER);
		            tv.setPadding(padding, padding, padding, padding);
//            	}

		        //
//		        try {
//		        	((TwTimelineActivity)getContext()).setNewTweetInfoTextView(tv);
//		        } catch (Exception e) {}


            	//
            	tv.setOnClickListener(new OnClickListener() {
					@Override
					public synchronized void onClick(View arg0) {
						try {
							// first item
							item.isNewTweetInfo = false;

							// nokori
							try {
								ArrayList<ListItem> ustHiddenItems = ((TwTimelineActivity)getContext()).getUstHiddenItemsForNewTweetInfoTap(); //ustHiddenItems;
								for (int i=1; i<ustHiddenItems.size(); i++) {
									insert(ustHiddenItems.get(i), 1);
								}
								ustHiddenItems.clear();
							} catch (Exception e) {}

							//
							ListItemAdapter.this.notifyDataSetChanged();

						} catch (Exception e) {}
					}
            	});
            } catch (Exception e) {}

       	 	return view;
        }
        else if (item.comment.equals("AD")) {
            View view = mInflater.inflate(R.layout.list_item7, null);

            //
            LinearLayout layout = (LinearLayout)view.findViewById(R.id.ll01);

            //
            AdView adView = null;
            try {
            	adView = new AdMob(getContext()).getAdView();
            } catch (Exception e) {}

            //
        	LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	    			ViewGroup.LayoutParams.FILL_PARENT,
	    			ViewGroup.LayoutParams.WRAP_CONTENT
//	    			ViewGroup.LayoutParams.FILL_PARENT
//	    			28
	    	);

        	if (adView == null) {
        		param.setMargins(0, 14, 0, 14);
    	        layout.addView(new TextView(getContext()), param);
        	}
        	else {
        		param.setMargins(0, 0, 0, 0);
    	        layout.addView(adView, param);
        	}

       	 	return view;
        }
        else if (item.comment.equals("NOAD")) {
            View view = mInflater.inflate(R.layout.list_item7, null);

            //
            LinearLayout layout = (LinearLayout)view.findViewById(R.id.ll01);
//            TextView adView = new TextView(getContext());

            layout.setOrientation(LinearLayout.HORIZONTAL);

//        	LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
////	    			ViewGroup.LayoutParams.FILL_PARENT,
//	    			ViewGroup.LayoutParams.WRAP_CONTENT,
//	    			ViewGroup.LayoutParams.WRAP_CONTENT
//	    	);
//	        param.setMargins(0, 14, 0, 14);
//	        param.setMargins(0, 0, 0, 0);

	        //
    		ImageView imgView = new ImageView(getContext());
//    		imgView.setImageResource(R.drawable.icon_miku);
    		imgView.setImageResource(BaseActivity.getDispIconRes());

    		//test
//    		imgView.setImageBitmap(BaseActivity.profileImageBase);

//    		imgView.setBackgroundResource(R.drawable.icon_miku);

    		//
    		String title = "";
    		try {
    			BaseActivity bact = (BaseActivity)getContext();
    			title = BaseActivity.nvl(bact.getDispTitle(), "");
    		} catch (Exception e) {}

    		//
    		TextView txtView = new TextView(getContext());
    		txtView.setText(title);
    		txtView.setTextColor(BaseActivity.getColorTweet(view.getRootView().getContext()));

    		//
//    		if (isTimeline) {
//    			txtView.setText(title + " [" + "ust view" + "]");
//    			txtView.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						BaseActivity.intent(getContext(), Twitter4JUserStreamActivity.class);
//					}
//    			});
//    		}

    		//
//    		imgView.setGravity(Gravity.LEFT);
    		layout.setGravity(Gravity.LEFT);

    		{
            	LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//    	    			ViewGroup.LayoutParams.FILL_PARENT,
    	    			ViewGroup.LayoutParams.WRAP_CONTENT,
    	    			ViewGroup.LayoutParams.WRAP_CONTENT
    	    	);
            	param.setMargins(0, 0, 0, 0);
            	layout.addView(imgView, param);
    		}
    		{
            	LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//    	    			ViewGroup.LayoutParams.FILL_PARENT,
    	    			ViewGroup.LayoutParams.WRAP_CONTENT,
    	    			ViewGroup.LayoutParams.WRAP_CONTENT
    	    	);
            	param.setMargins(5, 5, 0, 0);
            	layout.addView(txtView, param);
    		}
//	        layout.addView(adView, param);

       	 	return view;
        }

        //test
//        item.name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
//        item.screenName = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
//        item.isProtected=true;


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 遅い！？
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //
//        if (viewCashe.get("" + position) != null) {
//        	return viewCashe.get("" + position);
//        }



        // レイアウトファイルからViewを生成
//        View view = mInflater.inflate(R.layout.list_item, null);
        View view = mInflater.inflate(layoutId, null);

        //
        if (!BaseActivity.setSkinItem(view, item, tweetLayoutRID)) {
//        if (true) {
            View _view = mInflater.inflate(R.layout.list_item_mute, null);
            BaseActivity.setSkinItem(_view, item, tweetLayoutRID);

            //
            TextView screenName = (TextView)_view.findViewById(R.id.screenName);
            screenName.setText("@" + item.screenName);

            return _view;
        }


//        if (convertView == null) {


        //
        if (isSamplePage) {
	        View buttonVoice = (View)view.findViewById(R.id.buttonVoice);
	        if (buttonVoice != null) {
	        	if (_App.BUTTON_VOICE) {
			        buttonVoice.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Voice voice = Voice.getInstance((Activity)view.getContext());
							voice.startVoice(item.comment);
						}
			        });
	        	}
	        	else {
	        		buttonVoice.setVisibility(View.INVISIBLE);
	        	}
	        }
        }


        ////////////////////////////////////////
        //
//        Button buttonCtrl = (Button)view.findViewById(R.id.buttonCtrl);
        View buttonCtrl = (View)view.findViewById(R.id.buttonCtrl);

        final boolean marking = item.marking;
        final TextView viewProtected = (TextView)view.getRootView().findViewById(R.id.Protected);
//        final TextView viewFavorited = (TextView)view.getRootView().findViewById(R.id.Favorited);
        final View viewFavorited = view.getRootView().findViewById(R.id.Favorited);
        final TextView viewRetweeted = (TextView)view.getRootView().findViewById(R.id.Retweeted);
        final View viewRetweetedLl = view.getRootView().findViewById(R.id.RetweetedLl);

        //
        if (marking) {
	        if (viewProtected != null) {
		        if (item.isProtected) {
		        	viewProtected.setVisibility(View.VISIBLE);
		        	viewProtected.setText((BaseActivity.ja)?"非公開":"Protect");
		        }
//		        else {
//		        	viewProtected.setVisibility(View.INVISIBLE);
//		        	viewProtected.setText("");
//		        }
	        }
	        if (viewFavorited != null) {
		        if (item.isFavorited) {
		        	viewFavorited.setVisibility(View.VISIBLE);
		        }
//		        else {
//		        	viewFavorited.setVisibility(View.INVISIBLE);
//		        }
	        }
	        if (viewRetweeted != null) {
		        if (item.isRetweet || item.isRetweetedByMe) {
//			    if (item.isRetweet) {
		        	viewRetweetedLl.setVisibility(View.VISIBLE);

		        	//
		        	String rtMess = "";
		            if (item.isRetweetedByMe) {
//		            	rtMess = "Retweet by " + "@" + BaseActivity.screenNameBase; // isRetweetがtrueの場合は、isRetweetedByMeはfalse
		            	rtMess = "" + "@" + BaseActivity.screenNameBase + ((BaseActivity.ja)?" がﾘﾂｲｰﾄ":" Retweet"); // isRetweetがtrueの場合は、isRetweetedByMeはfalse
		            }
		            else if (item.isRetweet) {
//		            	rtMess = "Retweet by " + "@" + item.retweetScreenName + ((item.retweetCount > 1)?(" 他" + (item.retweetCount-1) + "人"):"");
		            	rtMess = "" + "@" + item.retweetScreenName + ((BaseActivity.ja)?" がﾘﾂｲｰﾄ":" Retweet") + ((item.retweetCount > 1)?((BaseActivity.ja)?" 他":" " + (item.retweetCount-1) + ""):"");
		            }
		            viewRetweeted.setText(rtMess);
		        	new Clickable4Name(item).enable(viewRetweeted);
		        }
//		        else {
//		        	viewRetweetedLl.setVisibility(View.INVISIBLE);
//		        	viewRetweeted.setText("");
//		        }
	        }
        }

        // 未読向け（DMはProtected使わない、@は値が取れない？ので利用）
        if (viewProtected != null) {
//        	if (getContext() instanceof TwDMActivity || getContext() instanceof TwAtActivity) {
//		        if (item.midoku) {
//		        	viewProtected.setVisibility(View.VISIBLE);
//		        	viewProtected.setText((BaseActivity.ja)?"未読":"unread");
//		        	viewProtected.setTextColor(Color.RED);
//		        	viewProtected.setTextSize(12);
//		        }
//		        else {
//		        	viewProtected.setVisibility(View.INVISIBLE);
//		        	viewProtected.setText("");
//		        }
//        	}

	        if (item.midoku) {
	        	if (midokuState) {
		        	viewProtected.setVisibility(View.VISIBLE);
		        	viewProtected.setText((BaseActivity.ja)?"未読":"unread");
		        	viewProtected.setTextColor(Color.RED);
		        	viewProtected.setTextSize(12);
	        	}
	        }
	        else {
	        	if (midokuState) {
		        	viewProtected.setVisibility(View.INVISIBLE);
		        	viewProtected.setText("");
	        	}
	        }
        }


//        //test
//        viewProtected.setVisibility(View.VISIBLE);
//    	viewProtected.setText("aaaaaaaaaagg");



        //
        if (buttonCtrl != null) {
//        final boolean favorite = item.isFavorited;
//        final boolean retweet  = item.isRetweet;
//        final boolean isRetweetedByMe = item.isRetweetedByMe;
        final ListItem selectedItem = item;
        final Twitter twitter = BaseActivity.getTwitter();
        final Context context = getContext();

        //
        if (BaseActivity.fast) {
        view.setOnTouchListener(new OnTouchListener() {
 			private float xDown;
			private float yDown;

			int frameCnt;
			private long prevCurrentTimeMillis;

			@Override
 			public boolean onTouch(View view, MotionEvent event) {
				//
//				BaseActivity.toast(context, "" + frameCnt++);
////				((BaseActivity)view.getRootView().getContext()).setSkin(view);
//				BitmapDrawable bitmapDrawable = (BitmapDrawable)context.getResources().getDrawable(BaseActivity.SKINS_RID[frameCnt]);
//				bitmapDrawable.setAlpha(BaseActivity.getAlphaSkin(context));
//				view.setBackgroundDrawable(bitmapDrawable);





				//
 				if (event.getAction() == MotionEvent.ACTION_DOWN) {
 					xDown = event.getX();
 					yDown = event.getY();
 				}
 				else if (event.getAction() == MotionEvent.ACTION_UP) {
// 					BaseActivity.toast(getContext(), "onTouch");

// 					if ((int)xDown != (int)event.getX() || (int)yDown != (int)event.getY()) return true;
 					boolean check = false;
 					if (Math.abs((int)(xDown - event.getX())) < 20) { // gosa
 						if (Math.abs((int)(yDown - event.getY())) < 20) {
 							check = true;
 						}
 					}
 					if (!check) return true;

 					//
 					if (BaseActivity.fast) {
 						if (System.currentTimeMillis() - prevCurrentTimeMillis < 1000) { // for 2 tap
 							return true;
 						}
 						prevCurrentTimeMillis = System.currentTimeMillis();

 						//
// 						Object[] res = DemoActivity.CustomAdapter.getCtrlMenus(context, selectedItem, false);
// 						String[] TITLES = (String[]) res[0];
 						String[] TITLES = new String[0];

 						//
 						ArrayList<String> entries = BaseActivity.getEntries(context, selectedItem);
 						if (entries.size() > 0) {
 							String[] ENTRIES = new String[entries.size()];
 							ENTRIES = entries.toArray(ENTRIES);

 							TITLES = (String[])BaseActivity.addArray(String.class, TITLES, ENTRIES);

// 							// osoi
// 							for (int i=0; i<TITLES.length; i++) {
// 								if (TITLES[i].startsWith("http")) {
// 									TITLES[i] = BaseActivity.parseExpandUrl(TITLES[i], selectedItem);
// 								}
// 							}
 						}

 						if (true) {
 							final String[] ITEM = TITLES;


 							// osoi
 							for (int i=0; i<ITEM.length; i++) {
 								if (ITEM[i].startsWith("http")) {
 									ITEM[i] = BaseActivity.parseExpandUrl(ITEM[i], selectedItem);
 								}
 							}


 			               	new AlertDialog.Builder((Activity)context)
 			               		.setTitle((BaseActivity.ja)?"リンク一覧":"Links")
 			               		.setPositiveButton(
 			               				(BaseActivity.ja)?"URLの展開":"Expanded URL",
 			               				new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
//										cancel();

//										ITEM[0] = "click";


										// ---------------------------------------------------------

			 							// osoi
			 							for (int i=0; i<ITEM.length; i++) {
			 								if (ITEM[i].startsWith("http")) {
			 									ITEM[i] = BaseActivity.parseExpandUrl(ITEM[i], selectedItem);
			 								}
			 							}

			 							//
			 			               	new AlertDialog.Builder((Activity)context)
		 			               		.setTitle((BaseActivity.ja)?"リンク一覧":"Links")
		 			               		.setNegativeButton("Close", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
//												cancel();
											}
		 			               		})
		 			               		.setItems(ITEM, new DialogInterface.OnClickListener() {
		 									@Override
		 			                       	public void onClick(DialogInterface dialog, int which) {
		 										doProc(context,
		 												(Activity)context,
		 									    		 which,
		 									    		 selectedItem,
		 									    		 twitter,
		 									    		 mHandler,
		 									    		 marking,
		 									    		 viewRetweetedLl,
		 									    		 viewRetweeted,
		 									    		 viewFavorited,
		 									    		 ITEM);
		 			                       	}
		 			               	})
		 			               	.create()
		 			               	.show();











										// ---------------------------------------------------------


									}
 			               		})
 			               		.setNegativeButton("Close", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
//										cancel();
									}
 			               		})
 			               		.setItems(ITEM, new DialogInterface.OnClickListener() {
 									@Override
 			                       	public void onClick(DialogInterface dialog, int which) {
 										doProc(context,
 												(Activity)context,
 									    		 which,
 									    		 selectedItem,
 									    		 twitter,
 									    		 mHandler,
 									    		 marking,
 									    		 viewRetweetedLl,
 									    		 viewRetweeted,
 									    		 viewFavorited,
 									    		 ITEM);
 			                       	}
 			               	})
 			               	.create()
 			               	.show();
 						}

// 						return;
 					}



 					return true;
 				}

 				return true;
 			}
         });
        }




        //
        if (true) {
        buttonCtrl.setOnClickListener(new OnClickListener() {
			private Activity activity;
			private Context context;

			@Override
			public void onClick(final View view) {
//				activity = (Activity)view.getRootView().getContext();
				activity = (Activity)view.getContext();
				context = activity;



				/////////////////////////////
				// ctrl
				/////////////////////////////

//				tvDate.setText("click");

				// create the quick action view, passing the view anchor
				QuickActionView qa = QuickActionView.Builder( view );

				// set the adapter
//				qa.setAdapter( new CustomAdapter( view.getRootView().getContext(), /*selectedItem.isRetweetedByMe, */selectedItem.isFavorited) );
				qa.setAdapter( new CustomAdapter( view.getRootView().getContext(), selectedItem) );

				// set the number of columns ( setting -1 for auto )
//				qa.setNumColumns( (int) (2 + (Math.random() * 10)) );
				qa.setNumColumns( 5 );
				qa.setOnClickListener( new DialogInterface.OnClickListener() {

					@Override
					public void onClick( DialogInterface dialog, int which ) {

//						tvDate.setText("which " + which);


					//
					if (context instanceof TwDMActivity) {
						TwDMActivity twDMActivity = (TwDMActivity)context;
						if (twDMActivity.mode == twDMActivity.MODE_GET) {
							which = 2100 + which; // 記述のしやすさの都合で引数入れ替え
						}
						else if(twDMActivity.mode == twDMActivity.MODE_SET) {
							which = 2200 + which; // 記述のしやすさの都合で引数入れ替え
						}
					}


					doProc(context,
				    		 activity,
				    		 which,
				    		 selectedItem,
				    		 twitter,
				    		mHandler,
				    		 marking,
				    		 (LinearLayout)viewRetweetedLl,
				    		 (TextView)viewRetweeted,
				    		 viewFavorited,
				    		 null);

						dialog.dismiss();
//						Toast.makeText( view.getRootView().getContext(), "Selected item: " + which, Toast.LENGTH_SHORT ).show();
					}
				} );

				// finally show the view
				qa.show();
			}
        });
        }
        }
        ////////////////////////////////////////


        //
        //
        if (viewFavorited != null
        		&& item.isFavorited // add
        		)
        {
//	        viewFavorited.setOnClickListener(new OnClickListener() {
//				private Context context;
//				private Twitter twitter;
//
//				@Override
//				public void onClick(View view) {
//					context = view.getRootView().getContext();
//					twitter = BaseActivity.getTwitter();
//
//					//
//					BaseActivity.alertOKCancelAndClose(context, BaseActivity.botMess("お気に入りを解除しますか？", "お気に入りを解除しますか？", "Are you sure you want to cancel this Favorite?"), new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							if (which == DialogInterface.BUTTON_POSITIVE) {
//								try {
//									twitter.destroyFavorite((item.id));
//
////									Toast.makeText(context, "success. お気に入りを解除しました。", Toast.LENGTH_SHORT).show();
//									BaseActivity.successHandling(context, (BaseActivity.ja)?"お気に入りを解除":"success");
//
//									item.isFavorited = false;
//					//		    	notifyDataSetChanged();
//
//									if (marking) {
//										viewFavorited.setVisibility(View.INVISIBLE);
//									}
//
//									if (context instanceof TwFavoritesActivity) {
//										remove(item);
//									}
//								} catch (Exception e) {
////									Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
//									BaseActivity.errorHandling(context, e);
//								}
//							}
//						}
//					});
//				}
//	        });

        	favoritedOnClick(viewFavorited, item, marking);
        }

        // 画像をセット
        ImageView image;
        image = (ImageView)view.findViewById(R.id.image);
        if (image != null) {
        	image.setImageBitmap(item.image);
        }

        // 画像表示
//        boolean imageOn = (BaseActivity.getString(view.getRootView().getContext(), "imageOn")).equals("");

        // バックグラウンドで画像取得
        if (imageOn) {
	        if (item.profileImageURL != null) {
		        try {
			        DownloadTask task = new DownloadTask(item, image);
			        task.execute(item.profileImageURL);
		        } catch (Throwable e){} // Loading...は画像ないため
	        }
//        }

        // 添付画像をセット
//        if (imageOn) {
        	try {
		        if (item.mediaEntities != null && item.mediaEntities.length > 0) {
		        	//
			        Map<Integer, Size> sizes = item.mediaEntities[0].getSizes();
			        Size size = sizes.get(MediaEntity.Size.SMALL);
			        int w = size.getWidth();
			        int h = size.getHeight();

			        //
		            final ImageView imageAttached;
		            imageAttached = (ImageView)view.findViewById(R.id.imageAttached);
//		            imageAttached.setImageBitmap(BaseActivity.resizeImage(BaseActivity.defaultImage, w, h));
		            imageAttached.setImageBitmap(BaseActivity.resizeImage(BaseActivity.getDefaultImageStatic(getContext()), w, h));

		//            imageAttached.setMaxHeight(20);
		//            imageAttached.setMinimumHeight(20);

		            //
		            imageAttached.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							try {
								BaseActivity.intentWebNormal(view.getContext(), item.mediaEntities[0].getMediaURL().toString(), true);
							}
							catch (Exception e) {
							}
						}
		            });

		            // must permission
if(false) {
					imageAttached.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							try {
								 // sdcardフォルダを指定
								 File root = Environment.getExternalStorageDirectory();

								 // 日付でファイル名を作成
								 Date mDate = new Date();
								 SimpleDateFormat fileName = new SimpleDateFormat("yyyyMMdd_HHmmss");

								 //
								 File file = new File(root, fileName.format(mDate) + ".jpg");

								 // 保存処理開始
								 FileOutputStream fos = null;
								 fos = new FileOutputStream(file);

								 //
//								 Bitmap bmp = ((BitmapDrawable)imageAttached.getDrawable()).getBitmap();

								 String url = item.mediaEntities[0].getMediaURL().toString();

								 Bitmap bmp = HttpImage.getBitmap(url);

								 // jpegで保存
//								 bmp.compress(CompressFormat.JPEG, 100, fos);
								 bmp.compress(CompressFormat.PNG, 100, fos);

								 // 保存処理終了
								 fos.close();

								 //
								 _AppUtil.updateGarally(view.getContext(), file.getAbsolutePath());
							} catch (Exception e) {
								Log.e("Error", "" + e.toString());
							}
						}
		            });
}

		            //
		        	if (Runtime.getRuntime().freeMemory() > 100 * 1024) { // over
				        try {
					        DownloadTask task = new DownloadTask(item, imageAttached, false);

					        task.execute(item.mediaEntities[0].getMediaURL().toString());
				        } catch (Throwable e){
				        	BaseActivity.toast((Activity)getContext(), "error");
				        } // Loading...は画像ないため
		        	}
		        	else {
//		        		BaseActivity.toast((Activity)getContext(),
//		        				"Memory: " +
//		        						(int)((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime.getRuntime().totalMemory()) +
//		        						"%");

		        		BaseActivity.toastMem(getContext());
		        	}
		        }
		        else {
		        	// not normal
		        	// sankou: http://blog.irons.jp/2009/12/23/twitter_thumb_url/

		        	if (item.urlEntities != null && item.urlEntities.length > 0) {
				        String url = null;
		        		String dispUrl = item.urlEntities[0].getDisplayURL();
//		        		String dispUrl = item.urlEntities[0].getExpandedURL().toString();
	        			String eUrl = item.urlEntities[0].getExpandedURL().toString();
	        			boolean youtube = false;

		        		if (dispUrl.startsWith("twitpic.com")) {
		        			if (dispUrl.indexOf("photos") < 0) {
		        				url = "http://" + dispUrl.replace("twitpic.com/", "twitpic.com/show/thumb/");
		        			}
		        		}
		        		else if (dispUrl.startsWith("yfrog.com")) {
			        		url = "http://" + dispUrl + ".th.jpg";
			        	}
		        		else if (dispUrl.startsWith("instagr.am")) {
			        		url = "http://" + dispUrl + "media/?size=t";
			        	}
		        		else if (dispUrl.startsWith("p.twipple.jp")) {
		        			url = "http://" + dispUrl.replace("p.twipple.jp/", "p.twipple.jp/show/thumb/");
			        	}
		        		else if (dispUrl.startsWith("youtube.com")) {
		        			youtube = true;
		        			String vId = _AppUtil.getQueryValue("http://" + eUrl, "v");

		        			//
		        			url = "http://i.ytimg.com/vi/" + vId + "/default.jpg";
		        		}
		        		else if (dispUrl.startsWith("youtu.be")) {
		        			youtube = true;
		        			URL aUrl = new URL("http://" + dispUrl);
		        			String[] vals = BaseActivity.csv(aUrl.getPath(), "/");
		        			String vId = vals[vals.length - 1];

		        			//
		        			url = "http://i.ytimg.com/vi/" + vId + "/default.jpg";
			        	}
//		        		else if (dispUrl.startsWith("photozou.jp")) {
//		        			url = "http://" + dispUrl.replace("photozou.jp/photo/show/0000/", "photozou.jp/p/thumb/");
//			        	}

		        		if (url != null) {
				        	//
				            ImageView imageAttached;
				            imageAttached = (ImageView)view.findViewById(R.id.imageAttached);
				            imageAttached.setImageBitmap(BaseActivity.getDefaultImageStatic(getContext()));

				            //
				            final String _url = url;
//				            final String _dispUrl = dispUrl;
//				            final boolean _youtube = youtube;
				            if (!youtube) {
					            imageAttached.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View view) {
										try {
	//										if (_youtube) {
	//											BaseActivity.intentWebNormal(view.getContext(), _dispUrl, true);
	//										}
	//										else {
												BaseActivity.intentWebNormal(view.getContext(), _url, true);
	//										}
										}
										catch (Exception e) {
										}
									}
					            });
				            }

				            //
				        	if (Runtime.getRuntime().freeMemory() > 100 * 1024) { // over
						        try {
							        DownloadTask task = new DownloadTask(item, imageAttached, false);
							        task.execute(url);
						        } catch (Throwable e){
						        	BaseActivity.toast((Activity)getContext(), "error");
						        } // Loading...は画像ないため
				        	}
				        	else {
				        		BaseActivity.toastMem(getContext());
				        	}
		        		}
		        	}
		        }
        	} catch (Exception e) {
//        		BaseActivity.errorHandling(e);
        	}
        }




//        if (image != null) {
//			image.setOnFocusChangeListener(new OnFocusChangeListener() {
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					Log.d("ListItemAdapter#onFocusChange", "ListItemAdapter#onFocusChange");
//				}
//	        });
//        }

        // ユーザ名をセット
        TextView name;
        name = (TextView)view.findViewById(R.id.name);
        if (name != null) {
        name.setText(item.name);
        name.setTextColor(BaseActivity.getColorTweet(view.getRootView().getContext())); //Color.argb(90, 255, 255, 0));
//        if (isSpannable) {
//        	new Clickable4Name().enable(name);
//        }
        }

        // screenNameをセット
        String atScreenName = "@" + item.screenName;
        TextView screenName;
        screenName = (TextView)view.findViewById(R.id.screenName);
        if (screenName != null) {
        screenName.setText(atScreenName);
        if (!BaseActivity.fast && isSpannable) {
        	new Clickable4Name(item).enable(screenName);
        }
        else {
        	// underline
        	SpannableString spannableString = new SpannableString(atScreenName);
        	spannableString.setSpan(new URLSpan(atScreenName), 0, spannableString.length(), 0);
        	screenName.setText(spannableString);
        }
        }

        // コメントをセット
        TextView comment;
        comment = (TextView)view.findViewById(R.id.comment);
        if (comment != null) {
//            comment.setText(BaseActivity.replaceN(item.comment));
            String comm = BaseActivity.replaceN(item.comment);



            if (item.urlEntities != null && item.urlEntities.length > 0) {
            	for (int i=0; i<item.urlEntities.length; i++) {
            		comm = comm.replace(item.urlEntities[i].getURL().toString(), item.urlEntities[i].getExpandedURL().toString());
            	}
            }


        	comment.setText(comm);

        //
        comment.setTextColor(BaseActivity.getColorTweet(view.getRootView().getContext())); //Color.argb(90, 255, 255, 0));

        if (item.geoLocation != null) {
        	comment.setText(comment.getText() + "\n\n" + item.getGeoCode());
        }

//        //
//        String rep = "";
//        if (item.inReplyToStatusId > 0) {
//        	long repId = item.inReplyToStatusId;
//
//        	try {
//        		while (repId > 0) {
//        			rep += "\n" + repId;
//
//        			Status s = BaseActivity.getTwitter().showStatus(repId);
//
//        			repId = s.getInReplyToStatusId();
//        		}
//			} catch (TwitterException e) {
//			}
//        }

        if (item.inReplyToStatusId > 0) {
        	if (!(view.getContext() instanceof TwReplyActivity)) {
        		comment.setText(comment.getText() + "\n\n" + BaseActivity.stringReply());
        	}
        }

        if (isSpannable) {
        	new Clickable(item).enable(comment, item.urlEntities);
        }


//        if (true)
        {
        	// 文字サイズ設定
        	Context context = view.getRootView().getContext();

        	if (
//        			!(context instanceof TwQueryListActivity)
//        			&& !(context instanceof TwListTab1Activity)
//        			&& !(context instanceof TwCameraImageActivity)
//        			&& !(context instanceof TwOptionMenuActivity)

        			textSizeSetState
        	) {
        		// TODO:毎回取得していいのか？少し不安
//        		BaseActivity baseActivity = (BaseActivity)context;
//        		int textSize = (int)Float.parseFloat(BaseActivity.nvl(baseActivity.getString("textSize_tweet"), "" + BaseActivity.TEXTSIZE_TWEET_INI));
//        		comment.setTextSize(textSize);

        		comment.setTextSize(BaseActivity.getSizeTweet(context));
        	}
        }
//        }
        }


        // dateをセット
        boolean sourceOn = BaseActivity.sourceOn;

		TextView date;
        date = (TextView)view.findViewById(R.id.Date01);
        if (date != null) {
        	if (item.isUserStream) {
        		Context __context = view.getRootView().getContext();

        		date.setText(
        				((sourceOn)?(BaseActivity.parseTwitterSourse(item.source) + "\n"):"") +
        				BaseActivity.diffDate(item.createdAt));
//        		date.setText( BaseActivity.fmtMdhm.format(item.createdAt) + "" );

//        		BaseActivity BaseActivity = (BaseActivity)view.getRootView().getContext();
//        		date.setTextColor(Color.parseColor(__context.getString(R.color.loading_bg)));
//				date.setBackgroundColor(Color.parseColor(__context.getString(R.color.loading_tx)));

            	date.setTextColor(BaseActivity.getColorTweet(view.getRootView().getContext())); //Color.argb(90, 255, 255, 0));


            	//
//            	view.setBackgroundColor(Color.argb(32, 255, 255, 255));
//            	view.setBackgroundColor(BaseActivity.getColorMainBg(__context));
//            	view.setBackgroundColor(Color.argb(88, 255, 255, 255));

            	//
            	if (this.getItem(1).isUserStream) {
            		view.setBackgroundColor(Color.argb(88, 255, 255, 255));
//            		view.setBackgroundColor(Color.argb(255, 255, 255, 255));

	            	final View _view = view;
	            	mHandler.post(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.yield();
								Thread.sleep(100);

								_view.setBackgroundColor(Color.argb(0, 0, 0, 0));
								item.isUserStream = false;
							}
							catch (Exception e) {}
						}
	            	});
            	}
            	else {
            		item.isUserStream = false;
            	}
			}
        	else {
        		date.setText(
        				((sourceOn)?(BaseActivity.parseTwitterSourse(item.source) + "\n"):"") +
        				item.date);
            	date.setTextColor(BaseActivity.getColorTweet(view.getRootView().getContext())); //Color.argb(90, 255, 255, 0));
        	}

//        	date.setTextColor(BaseActivity.getColorTweet(view.getRootView().getContext())); //Color.argb(90, 255, 255, 0));
//        if (isSpannable) {
//        	new Clickable().enable(date);
//        }
        }

        //
//        View ll02 = view.findViewById(R.id.ll02);
//        if (isSpannable) {
//        	ll02.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Context con = v.getRootView().getContext();
//
//					String url = item.user.getScreenName();
//					if (url == null) url = item.name;
//
//
//				}
//        	});
//        }


        //
//        viewCashe.put(""+position, view);

        return view;
    }

    //
//    HashMap<String, View> viewCashe = new HashMap<String, View>();



    //
    public void doProc(
    		final Context context,
    		final Activity activity,
    		/*final*/ int which,
    		final ListItem selectedItem,
    		final Twitter twitter,
    		Handler mHandler,
    		final boolean marking,
    		final View viewRetweetedLl,
    		final TextView viewRetweeted,
    		final View viewFavorited,
    		final String[] ITEM
    		)
    {
    	// @,url etc
    	if (ITEM != null) {
	    	if (BaseActivity.controlTag (activity, ITEM, which, selectedItem)) {
	    		return;
	    	}
    	}

    	//
    	if (!_App.BUTTON_VOICE) {
    		if (which >= 4) which ++;
    	}

    	// menu
        if (which == 0) { // @
//       		TabTweetActivity.setText("@"+selectedItem.name);
//       		TestActivity.setCurrentTab(TabTweetActivity.tabNo);

        	//
//       		TwTweetActivity.setText("@"+selectedItem.screenName);

        	//
        	if (!BaseActivity.isLogin(context)) {
				BaseActivity.toast(activity, (BaseActivity.ja)?"サインインしてください\n(menu) > アカウント":"(menu) > Accounts\n\nPlease, sign in.");
				return;
        	}

       		//
   			ArrayList<String> entries = BaseActivity.getEntries(context, selectedItem);
   			final HashSet<String> replySet = new HashSet<String>();
   			for (int i=0; i<entries.size(); i++) {
   				if (entries.get(i).startsWith("@")
   						&& !entries.get(i).equals("@" + selectedItem.screenName))
   				{
   					if (!entries.get(i).endsWith(BaseActivity.stringUserSearch())) {
   						replySet.add(entries.get(i));
   					}
   				}
   			}

   			//
//        	if (selectedItem.userMentionEntities.length > 0) {
//           	if (entries.size() > 0) {
            if (replySet.size() > 0) {

       			//
       			BaseActivity.alertTwoButton(activity, "",
//       					(BaseActivity.ja)?"返信先の選択です。":"Reply all ?",
       					BaseActivity.botMess("返信先を選択できます。", "返信先を選択できます。", "Reply all ?"),
       					(BaseActivity.ja)?("@"+selectedItem.screenName):("@"+selectedItem.screenName),
       					new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								String replyScreenNames = "@"+selectedItem.screenName;

					       		TwTweetActivity.setText(replyScreenNames);
					       		TwTweetActivity.setInReplyToStatusId(activity, selectedItem.id);

					       		TwTweetActivity.setInReplyToScreenName(activity, selectedItem.screenName);
				       			TwTweetActivity.setInReplyToComment(activity, selectedItem.comment);

								Intent intent = new Intent(activity, TwTweetActivity.class);
								activity.startActivity(intent);
							}
       					},
       					(BaseActivity.ja)?"全員":"ALL",
       					new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								String replyScreenNames = "@"+selectedItem.screenName;

					       		try {
//					    			for (int i= 0; i<selectedItem.userMentionEntities.length; i++) {
//					    				replyScreenNames += " " + ("@"+selectedItem.userMentionEntities[i].getScreenName());
//					    			}

//					    			for (int i= 0; i<replySet.size(); i++) {
//					    				replyScreenNames += " " + replySet.;
//					    			}

					       			Iterator<String> it = replySet.iterator();
					       	        while (it.hasNext()) {
					       	            replyScreenNames += " " + it.next();
					       	        }


					    		}catch(Exception e){}

					       		//
					       		TwTweetActivity.setText(replyScreenNames);
					       		TwTweetActivity.setInReplyToStatusId(activity, selectedItem.id);

					       		TwTweetActivity.setInReplyToScreenName(activity, selectedItem.screenName);
				       			TwTweetActivity.setInReplyToComment(activity, selectedItem.comment);

								Intent intent = new Intent(activity, TwTweetActivity.class);
								activity.startActivity(intent);
							}
       					}
       			);
       		}
       		else {
       			TwTweetActivity.setText("@"+selectedItem.screenName);
       			TwTweetActivity.setInReplyToStatusId(activity, selectedItem.id);

       			TwTweetActivity.setInReplyToScreenName(activity, selectedItem.screenName);
       			TwTweetActivity.setInReplyToComment(activity, selectedItem.comment);

				Intent intent = new Intent(activity, TwTweetActivity.class);
				activity.startActivity(intent);
       		}
       	}
       	else if (which == 1) { // RT

       		//
        	if (!((BaseActivity)context).isLogin(context)) {
				BaseActivity.toast(activity, (BaseActivity.ja)?"サインインしてください\n(menu) > アカウント":"(menu) > Accounts\n\nPlease, sign in.");
				return;
        	}

//        	//
//       		mHandler.post(new Runnable() {
//				@Override
//				public void run() {
//
//					if (selectedItem.screenName.equals(BaseActivity.screenNameBase)) {
//						// 自分のTweetをRT
//						BaseActivity.alertAndClose(context, BaseActivity.botMess("リツイートできません。", "あれ？リツイートできないみたいです。", "failed"));
//					}
//					else {
//
////					if (!isRetweetedByMe) {
////					if (viewRetweeted.getVisibility() == View.INVISIBLE || !selectedItem.isRetweetedByMe) {
//						try {
//							twitter.retweetStatus(selectedItem.id);
////					    	Toast.makeText(context, "success. リツイートしました。", Toast.LENGTH_SHORT).show();
//							BaseActivity.successHandling(context, (BaseActivity.ja)?"リツイート":"success");
//
//					    	//
////					    	selectedItem.isRetweetedByMe = true;
//
//					    	if (marking) {
//					    		viewRetweetedLl.setVisibility(View.VISIBLE);
////					    	notifyDataSetChanged();
//
//				        	//
//
//					            if (viewRetweeted.getText().equals("")) {
////					            	viewRetweeted.setText("Retweet by " + "@" + BaseActivity.screenNameBase);
//					            	viewRetweeted.setText("" + "@" + BaseActivity.screenNameBase + ((BaseActivity.ja)?" がﾘﾂｲｰﾄ":" Retweet"));
//					            	new Clickable(selectedItem).enable(viewRetweeted);
//					            }
//					    	}
//						}
//						catch (Exception e) {
//							//
//							if (/*!marking && */e.getMessage().startsWith("403")) {
//								// RT情報が無い検索機能向けに403をツイート済みとする
//								// あと、自分もRTしたかどうかはAPI通さないといけないので、403で判断する
//
//								BaseActivity.alertAndClose(activity,
//										BaseActivity.botMess(
//												"リツイート済みです。\n取消しは、あなたのツイート一覧から[削除]でできます。",
////										"userName、さっきリツイートしましたよ・・・\n取消はuserNameのツイート一覧からできます。"),
//												"リツイート済みです。\n取消しは、userNameのツイート一覧から[削除]でできます。",
//												"Retweeted."
//										),
//										new DialogInterface.OnClickListener() {
//				        	           		public void onClick(DialogInterface dialog, int id) {
//												//
////												TimelineTab2Activity.screenName = BaseActivity.screenNameBase;
////												Intent intent = new Intent(activity, TimelineTab2Activity.class);
////												activity.startActivity(intent);
//				        	           	}
//								});
//								if (marking) {
//									viewRetweetedLl.setVisibility(View.VISIBLE);
//						            if (viewRetweeted.getText().equals("")) {
//						            	viewRetweeted.setText("@" + BaseActivity.screenNameBase + ((BaseActivity.ja)?" がﾘﾂｲｰﾄ":"Retweet"));
//						            	new Clickable(selectedItem).enable(viewRetweeted);
//						            }
//							    }
//							}
//							else {
////								Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
//								BaseActivity.errorHandling(context, e);
//							}
//						}
////					}
////					else {
////						BaseActivity.alertAndClose(activity, "Retweet済みです。\n取消しは、あなたのツイート一覧からできます。",
////								new DialogInterface.OnClickListener() {
////		        	           		public void onClick(DialogInterface dialog, int id) {
////										//
//////										TimelineTab2Activity.screenName = BaseActivity.screenNameBase;
//////										Intent intent = new Intent(activity, TimelineTab2Activity.class);
//////										activity.startActivity(intent);
////		        	           	}
////						});
////
////
////
//////						try {
//////							twitter.destroyStatus(selectedItem.id); //TODO: ? 多分
//////					    	Toast.makeText(context, "success. Retweetを取消しました。", Toast.LENGTH_SHORT).show();
//////						}
//////						catch (Exception e) {
//////							Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
//////						}
////					}
//
//					}
//				}
//       		});



        	//
        	final Handler _mHandler = mHandler;
   			BaseActivity.alertTwoButton(activity, "",
   					BaseActivity.botMess("リツイートの種類を選択できます。", "リツイートの種類を選択できます。", "Retweet"),
   					(BaseActivity.ja)?("公式RT"):("RT"),
   					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
//--------------
				        	//
							_mHandler.post(new Runnable() {
								@Override
								public void run() {

									if (selectedItem.screenName.equals(BaseActivity.screenNameBase)) {
										// 自分のTweetをRT
										BaseActivity.alertAndClose(context, BaseActivity.botMess("リツイートできません。", "あれ？リツイートできないみたいです。", "failed"));
									}
									else {

//									if (!isRetweetedByMe) {
//									if (viewRetweeted.getVisibility() == View.INVISIBLE || !selectedItem.isRetweetedByMe) {
										try {
											twitter.retweetStatus(selectedItem.id);
//									    	Toast.makeText(context, "success. リツイートしました。", Toast.LENGTH_SHORT).show();
											BaseActivity.successHandling(context, (BaseActivity.ja)?"リツイート":"success");

									    	//
//									    	selectedItem.isRetweetedByMe = true;

									    	if (marking) {
									    		viewRetweetedLl.setVisibility(View.VISIBLE);
//									    	notifyDataSetChanged();

								        	//

									            if (viewRetweeted.getText().equals("")) {
//									            	viewRetweeted.setText("Retweet by " + "@" + BaseActivity.screenNameBase);
									            	viewRetweeted.setText("" + "@" + BaseActivity.screenNameBase + ((BaseActivity.ja)?" がﾘﾂｲｰﾄ":" Retweet"));
									            	new Clickable(selectedItem).enable(viewRetweeted);
									            }
									    	}
										}
										catch (Exception e) {
											//
											if (/*!marking && */e.getMessage().startsWith("403")) {
												// RT情報が無い検索機能向けに403をツイート済みとする
												// あと、自分もRTしたかどうかはAPI通さないといけないので、403で判断する

												BaseActivity.alertAndClose(activity,
														BaseActivity.botMess(
																"リツイート済みです。\n取消しは、あなたのツイート一覧から[削除]でできます。",
//														"userName、さっきリツイートしましたよ・・・\n取消はuserNameのツイート一覧からできます。"),
																"リツイート済みです。\n取消しは、userNameのツイート一覧から[削除]でできます。",
																"Retweeted."
														),
														new DialogInterface.OnClickListener() {
								        	           		public void onClick(DialogInterface dialog, int id) {
																//
//																TimelineTab2Activity.screenName = BaseActivity.screenNameBase;
//																Intent intent = new Intent(activity, TimelineTab2Activity.class);
//																activity.startActivity(intent);
								        	           	}
												});
												if (marking) {
													viewRetweetedLl.setVisibility(View.VISIBLE);
										            if (viewRetweeted.getText().equals("")) {
										            	viewRetweeted.setText("@" + BaseActivity.screenNameBase + ((BaseActivity.ja)?" がﾘﾂｲｰﾄ":"Retweet"));
										            	new Clickable(selectedItem).enable(viewRetweeted);
										            }
											    }
											}
											else {
//												Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
												BaseActivity.errorHandling(context, e);
											}
										}
//									}
//									else {
//										BaseActivity.alertAndClose(activity, "Retweet済みです。\n取消しは、あなたのツイート一覧からできます。",
//												new DialogInterface.OnClickListener() {
//						        	           		public void onClick(DialogInterface dialog, int id) {
//														//
////														TimelineTab2Activity.screenName = BaseActivity.screenNameBase;
////														Intent intent = new Intent(activity, TimelineTab2Activity.class);
////														activity.startActivity(intent);
//						        	           	}
//										});
				//
				//
				//
////										try {
////											twitter.destroyStatus(selectedItem.id); //TODO: ? 多分
////									    	Toast.makeText(context, "success. Retweetを取消しました。", Toast.LENGTH_SHORT).show();
////										}
////										catch (Exception e) {
////											Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
////										}
//									}

									}
								}
				       		});


//--------------
						}
   					},
   					(BaseActivity.ja)?"非公式RT":"Comment + RT",
   					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							String replyScreenNames = "@"+selectedItem.screenName;
//
//				       		try {
////				    			for (int i= 0; i<selectedItem.userMentionEntities.length; i++) {
////				    				replyScreenNames += " " + ("@"+selectedItem.userMentionEntities[i].getScreenName());
////				    			}
//
////				    			for (int i= 0; i<replySet.size(); i++) {
////				    				replyScreenNames += " " + replySet.;
////				    			}
//
//				       			Iterator<String> it = replySet.iterator();
//				       	        while (it.hasNext()) {
//				       	            replyScreenNames += " " + it.next();
//				       	        }
//
//
//				    		}catch(Exception e){}
//
				       		//
				       		TwTweetActivity.setText(" RT " + replyScreenNames + ": " + selectedItem.comment);
				       		TwTweetActivity.setInReplyToStatusId(activity, selectedItem.id);

				       		TwTweetActivity.setInReplyToScreenName(activity, selectedItem.screenName);
			       			TwTweetActivity.setInReplyToComment(activity, selectedItem.comment);

							Intent intent = new Intent(activity, TwTweetActivity.class);
							activity.startActivity(intent);
						}
   					}
   			);



       	}
		else if (which == 2) { // Favalite
//			//
//        	if (!((BaseActivity)context).isLogin()) {
//				BaseActivity.toast(activity, (BaseActivity.ja)?"サインインしてください\n(menu) > アカウント":"(menu) > Accounts\n\nPlease, sign in.");
//				return;
//        	}





        	//
        	final Handler _mHandler = mHandler;
   			BaseActivity.alertTwoButton(activity, "",
   					BaseActivity.botMess(
   							"お気に入りの種類を選択できます。\n\n※ あとで読む はLocalへの保存のため、\n　相手への通知がありません。",
   							"お気に入りの種類を選択できます。\n\n※ あとで読む はLocalへの保存のため、\n　相手への通知がありません。",
   							"Favorite"),
   					(BaseActivity.ja)?("お気に入り"):("Favorite"),
   					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
//--------------
							//
				        	if (!((BaseActivity)context).isLogin(context)) {
								BaseActivity.toast(activity, (BaseActivity.ja)?"サインインしてください\n(menu) > アカウント":"(menu) > Accounts\n\nPlease, sign in.");
								return;
				        	}

				        	//
							_mHandler.post(new Runnable() {
								@Override
								public void run() {
//									if (!favorite) {
									if (viewFavorited.getVisibility() == View.INVISIBLE) {
										try {
											twitter.createFavorite((selectedItem.id));
//											Toast.makeText(context, "success. お気に入りに登録しました。", Toast.LENGTH_SHORT).show();
											BaseActivity.successHandling(context, (BaseActivity.ja)?"お気に入りに登録":"success");

											selectedItem.isFavorited = true;
//									    	notifyDataSetChanged();

											if (marking) {
//									    	tvDate.setText(tvDate.getText() + "\n☆");
											viewFavorited.setVisibility(View.VISIBLE);
//											viewFavorited.setText("★");

											favoritedOnClick(viewFavorited, selectedItem, marking); // add
											}
										}
										catch (Exception e) {
											//
											if (/*!marking && */e.getMessage().startsWith("403")) {
												// Favorites情報が無い検索機能向けに403をFavorites済みとする
												try {
													BaseActivity.alertAndClose(activity, BaseActivity.botMess("お気に入りに登録済みです。", "お気に入りに登録済みです。", "Already saved"));

													selectedItem.isFavorited = true;

													if (marking) {
														viewFavorited.setVisibility(View.VISIBLE);
//														viewFavorited.setText("★");
													}
												}
												catch (Exception e2) {
//													Toast.makeText(context, "failed." + e2, Toast.LENGTH_SHORT).show();
													BaseActivity.errorHandling(context, e);
												}
											}
											else {
//												Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
												BaseActivity.errorHandling(context, e);
											}
										}
									}
									else {
										try {
											twitter.destroyFavorite((selectedItem.id));
//											Toast.makeText(context, "success. お気に入りを解除しました。", Toast.LENGTH_SHORT).show();
											BaseActivity.successHandling(context, (BaseActivity.ja)?"お気に入りを解除":"success");

											selectedItem.isFavorited = false;
//									    	notifyDataSetChanged();

											if (marking) {
//									    	tvDate.setText(tvDate.getText() + "\n--");
											viewFavorited.setVisibility(View.INVISIBLE);
//											viewFavorited.setText("");
											}

											if (context instanceof TwFavoritesActivity) {
												remove(selectedItem);
											}
										}
										catch (Exception e) {
//											Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
											BaseActivity.errorHandling(context, e);
										}
									}


								}
				       		});



//--------------
						}
   					},
   					(BaseActivity.ja)?"あとで読む":"Watch later",
   					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {

							try {
								TwWatchlaterActivity.create(context, selectedItem);
								BaseActivity.successHandling(context, (BaseActivity.ja)?"あとで読むに登録":"success");
							} catch (Exception e) {}

//							// idx
//							int idx = 0;//TwWatchlaterActivity.WATCHLATER_MAX-1;
//							for (int i=0; i<TwWatchlaterActivity.WATCHLATER_MAX; i++) {
//								if (BaseActivity.getString(context, TwWatchlaterActivity.KEY + i).equals("")) {
//									idx = i;
//									break;
//								}
//							}
////							if (idx == -1) idx = TwWa;
//
////
//							//
//							String wlData = "";
//
//
//
//							//
//////							if (selectedItem.isRetweet) {
////							if (selectedItem.comment.startsWith("RT ")) {
//////					        	item1.profileImageURL = status.getRetweetedStatus().getUser().getProfileImageURL().toString();
//////					        	item1.screenName = status.getRetweetedStatus().getUser().getScreenName();
//////					        	item1.name = status.getRetweetedStatus().getUser().getName();
//////					        	item1.comment = status.getRetweetedStatus().getText();
//////					        	item1.date = diffDate(status.getRetweetedStatus().getCreatedAt());
//////					        	item1.retweetScreenName = status.getUser().getScreenName();
////
////					        	wlData += selectedItem.id + ",";
////								wlData += selectedItem.screenName + ",";
//////								wlData += selectedItem.name + ",";
////								wlData += selectedItem.comment.replace(",", " ") + ",";
////								wlData += selectedItem.date + ",";
////								wlData += selectedItem.profileImageURL + ",";
////								wlData += selectedItem.retweetScreenName;
////					        }
////							else {
//								wlData += selectedItem.id + ",";
//								wlData += selectedItem.screenName + ",";
////								wlData += selectedItem.name + ",";
//								wlData += selectedItem.comment.replace(",", " ") + ",";
//								wlData += selectedItem.date + ",";
//								wlData += selectedItem.profileImageURL; // + ",";
////								wlData += "null";
////							}
//
//							//
//							BaseActivity.putString(context, TwWatchlaterActivity.KEY + idx, wlData);
						}
   					}
   			);


		}
		else if (which == 3) {
			((BaseActivity)context).setClipboard(selectedItem.comment);
			BaseActivity.toast(context, (BaseActivity.ja)?"コピーしました。":"success");
		}
		else if (which == 4) {
			if (!BaseActivity.ja) {
//				BaseActivity.alertAndClose(context, "Japanese version only.");
				BaseActivity.toast(context, "Japanese text only.");
			}

			Voice voice = Voice.getInstance((Activity)context);
			voice.startVoice(selectedItem.comment);
		}
		else if (which == 5) { // Tweet削除
			if (context instanceof TwWatchlaterActivity) {
				BaseActivity.alertOKCancelAndClose(context, BaseActivity.botMess("削除しますか？", "削除しますか？", "Are you sure you want to delete?"), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
		                try {
//							twitter.destroyStatus(selectedItem.id);
							TwWatchlaterActivity.delete(context, selectedItem.id);

							//
							BaseActivity.successHandling(context, (BaseActivity.ja)?"削除しました。":"success");

							//
							remove(selectedItem);
						} catch (Exception e) {
							BaseActivity.errorHandling(context, e);
						}
					}
				});
			}
			else {
				BaseActivity.alertOKCancelAndClose(context, BaseActivity.botMess("ツイートを削除しますか？", "ツイートを削除しますか？", "Are you sure you want to delete this tweet?"), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
		                try {
							twitter.destroyStatus(selectedItem.id);
	//						Toast.makeText(context, "success. ツイートを削除しました。", Toast.LENGTH_SHORT).show();
							BaseActivity.successHandling(context, (BaseActivity.ja)?"ツイートを削除":"success");

							//
							remove(selectedItem);
						} catch (Exception e) {
	//						Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
							BaseActivity.errorHandling(context, e);
						}
					}
				});
			}
		}
		else if (which == 2100) {
			// DMへ
			TwDMNewActivity.ToScreenName = selectedItem.screenName;
			Intent intent = new Intent(activity, TwDMNewActivity.class);
            activity.startActivity(intent);
		}
		else if (which == 2101) {
			// 受信 削除
			try {
				twitter.destroyDirectMessage(selectedItem.id);
//				Toast.makeText(context, "success. メッセージを削除しました。", Toast.LENGTH_SHORT).show();
				BaseActivity.successHandling(context, (BaseActivity.ja)?"メッセージを削除":"success");

				//
				remove(selectedItem);
			} catch (Exception e) {
//				Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
				BaseActivity.errorHandling(context, e);
			}
		}
		else if (which == 2200) {
			// 送信 削除
			try {
				twitter.destroyDirectMessage(selectedItem.id);
//				Toast.makeText(context, "success. メッセージを削除しました。", Toast.LENGTH_SHORT).show();
				BaseActivity.successHandling(context, (BaseActivity.ja)?"削除":"success");

				//
				remove(selectedItem);
			} catch (Exception e) {
//				Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
				BaseActivity.errorHandling(context, e);
			}
		}
		else if (which == 100005) {
//			MensionActivity.inReplyToStatusId = selectedItem.inReplyToStatusId;
////			MensionActivity.urlEntities = selectedItem.urlEntities;
//            Intent intent = new Intent(activity, MensionActivity.class);
//            activity.startActivity(intent);
		}
		else if (which == 100006) {
//			ListTab1Activity.mode = ListTab1Activity.MODE_ADD;
//            Intent intent = new Intent(activity, ListTab1Activity.class);
//            activity.startActivity(intent);
		}
    }





    //
    private void favoritedOnClick(final View viewFavorited, final ListItem item, final boolean marking) {
    	viewFavorited.setOnClickListener(new OnClickListener() {
			private Context context;
			private Twitter twitter;

			@Override
			public void onClick(View view) {
				context = view.getRootView().getContext();
				twitter = BaseActivity.getTwitter();

				//
				BaseActivity.alertOKCancelAndClose(context, BaseActivity.botMess("お気に入りを解除しますか？", "お気に入りを解除しますか？", "Are you sure you want to cancel this Favorite?"), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == DialogInterface.BUTTON_POSITIVE) {
							try {
								twitter.destroyFavorite((item.id));

//								Toast.makeText(context, "success. お気に入りを解除しました。", Toast.LENGTH_SHORT).show();
								BaseActivity.successHandling(context, (BaseActivity.ja)?"お気に入りを解除":"success");

								item.isFavorited = false;
				//		    	notifyDataSetChanged();

								if (marking) {
									viewFavorited.setVisibility(View.INVISIBLE);
								}

								if (context instanceof TwFavoritesActivity) {
									remove(item);
								}
							} catch (Exception e) {
//								Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
								BaseActivity.errorHandling(context, e);
							}
						}
					}
				});
			}
        });
    }
}



class DownloadTask extends AsyncTask<String, Void, Bitmap> {
    // アイコンを表示するビュー
    private ListItem item;
    private ImageView imageView;
	private boolean cache;
	private String url;

    // コンストラクタ
    public DownloadTask(ListItem item, ImageView imageView) {
//        this.imageView = imageView;
    	this(item, imageView, true);
    }

    // コンストラクタ
    public DownloadTask(ListItem item, ImageView imageView, boolean cache) {
        this.item = item;
        this.imageView = imageView;
        this.cache = cache;
    }

    // バックグラウンドで実行する処理
    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap image = null;

        //
        url = urls[0];

        //
		if(cache) /*Bitmap*/ image  = ImageCache.getImage(urls[0]);

        if (image == null) {
//            image = HttpClient.getImage(urls[0]);

        	if (ImageCache.loadThreadState) { // dl
        		ImageCache.loadThreadState = false;
        		try {
        			Thread.yield();
        			Thread.sleep(100);
        		} catch (Exception e) {}
        	}

//        	for (int i=0; i<3 && image==null; i++) {
//	        	try {
//	        		image = HttpImage.getBitmap(urls[0]);
//	        	}
//	        	catch (Throwable t) {
//	        		BaseActivity.toast(imageView.getRootView().getContext(), "Error: " + t);
//
//	        		try {
//	        			Thread.yield();
//	        			Thread.sleep(100);
//	        		} catch (Exception e) {}
//
//	        		System.gc();
//	        	}
//        	}

        	image = getImageIfNull(image, urls[0]);

			if(cache) ImageCache.setImage(urls[0], image);
        }
        return image;
    }

    // メインスレッドで実行する処理
    @Override
    protected void onPostExecute(Bitmap result) {
    	if (result != null) {
    		this.imageView.setImageBitmap(result);
    	}
    	else {
//    		BaseActivity.toast(imageView.getRootView().getContext(), "failed...");

//    		try {
//        		Bitmap image = HttpImage.getBitmap(url);
//
//        		if (image != null) {
//        			this.imageView.setImageBitmap(image);
//        		}
//        		else {
//        			BaseActivity.toast(imageView.getRootView().getContext(), "failed...");
//        		}
//        	}
//        	catch (Throwable t) {
//        		BaseActivity.toast(imageView.getRootView().getContext(), "Error: " + t);
//        	}




    		Bitmap image = getImageIfNull(result, url);

    		if (image != null) {
    			this.imageView.setImageBitmap(image);
    		}
    		else {
    			BaseActivity.toast(imageView.getRootView().getContext(),
//    					(BaseActivity.ja)?
//    							"読込失敗\n\n画像をタップ > " + BaseActivity.stringDetail() + "\n\nで閲覧できます。"
//    							:
//        						"failed...\n\nTap > " + BaseActivity.stringDetail() + ""
//        				,false
    					"failed..."
    			);
    		}
    	}
    }

    //
    protected Bitmap getImageIfNull(Bitmap _image, String url) {
    	Bitmap image = _image;

    	//
    	for (int i=0; i<2 && image==null; i++) {
        	try {
        		image = HttpImage.getBitmap(url + ((i==0)?"":":thumb"));
        	}
        	catch (Throwable t) {
        		BaseActivity.toast(imageView.getRootView().getContext(), "Error: " + t);

        		try {
        			Thread.yield();
        			Thread.sleep(100);
        		} catch (Exception e) {}

        		System.gc();
        	}
    	}

//    	//
//    	if (image == null) {
//    		try {
//    			Thread.yield();
//    			Thread.sleep(20);
//    		} catch (Exception e) {}
//
//			try {
//				twitter4j.Status status = BaseActivity.getTwitter().showStatus(item.id);
//				ListItem item1 = TwReplyActivity.toListItem(status);
//				String _url = item.mediaEntities[0].getMediaURL().toString() + ":thumb";
//				image = HttpImage.getBitmap(_url);
//			} catch (Exception e) {
//				String s = e.toString();
//			}
//    	}

    	return image;
    }
}

//class ImageCache {
//    private static HashMap<String,Bitmap> cache = new HashMap<String,Bitmap>();
//
//    public static Bitmap getImage(String key) {
//        if (cache.containsKey(key)) {
//            Log.d("cache", "cache hit!");
//            return cache.get(key);
//        }
//        return null;
//    }
//
//    public static void setImage(String key, Bitmap image) {
//        cache.put(key, image);
//    }
//}

//class NoSync {
//	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//	private ListItemAdapter listItemAdapter;
//
//	public NoSync(ListItemAdapter listItemAdapter) {
//		this.listItemAdapter = listItemAdapter;
//	}
//
//	public void putItemList(int position, ListItem item) {
//		HashMap<String, Object> map = new HashMap<String, Object>();
//
//		map.put("int", position);
//		map.put("ListItem", item);
//
//		list.add(map);
//	}
//
//	public void start() {
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				for(int i=0; i<list.size(); i++) {
//					HashMap<String, Object> map = list.get(i);
//
//					int position = Integer.parseInt((map.get("int")).toString());
//					ListItem item = (ListItem)map.get("ListItem");
//
////					item.image = BitmapFactory.decodeResource(v.getRootView().getResources(), R.drawable.icon);
//		        	item.image = HttpImage.getBitmap(item.user.getProfileImageURL().toString());
//
//					listItemAdapter.notifyDataSetChanged();
//
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//					}
//				}
//			}
//
//		}).start();
//	}
//}