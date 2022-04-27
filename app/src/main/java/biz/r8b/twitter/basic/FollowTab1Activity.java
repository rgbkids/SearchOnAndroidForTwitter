package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
//import twitter4j.http.AccessToken;
//import twitter4j.http.Authorization;
//import twitter4j.http.OAuthAuthorization;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FollowTab1Activity extends BaseActivity {

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
//
//	static String screenName;
//	static String screenNameDefault;
//
//    private static final String TAG = "Recipe041";
//	private static Twitter twitter;
//	private static Paging paging;
//
//	private static ListItemAdapter myAdapter;
//
//	static Handler mHandler = new Handler();
//
////	private ListItem itemMess;
//	private static ListItem itemMessLoading;
//	private static FollowTab1Activity activity;
//	private static ListItem itemEmpty;
//
//
//	static ListItem getMessageItem(String mess) {
//		ListItem itemMess = new ListItem();
//		itemMess.image = null;
//		itemMess.name = "";
//		itemMess.comment = mess;
//		return itemMess;
//	}
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.main_lv6);
//
//
//        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = mInflater.inflate(R.layout.main_lv6, null);
////        view.setBackgroundColor(Color.YELLOW);
////        view.setBackgroundResource(R.drawable.tab_fav);
//        setContentView(view);
//
//
//
//
////        setCurrentTabStack(5);
//
//        activity = this;
//
//        // 繝励Μ繝輔ぃ繝ｬ繝ｳ繧ｹ繧貞叙?ｽ?//        SharedPreferences sp =
//            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        // token縺ｨtokenSecret繧貞叙?ｽ?//        String token       = sp.getString(PREF_KEY_TOKEN, "");
//        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
//        // 蛟､縺後↑縺代ｌ縺ｰAuth繧｢繧ｯ?ｽ??ｽ?ｽ繝薙ユ繧｣繧定ｵｷ?ｽ?//        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(this, Auth.class);
//            startActivity(intent);
//        }
//        else {
//
//
//
//
////        // twitter4j縺ｮConfiguration繧貞叙?ｽ?////        Configuration conf = ConfigurationContext.getInstance();
////        // AccessToken繧堤函?ｽ?////        AccessToken accessToken = new AccessToken(token, tokenSecret);
////        // OAuthAuthorization繧堤函?ｽ?////        Authorization auth = new OAuthAuthorization(conf,
////                conf.getOAuthConsumerKey(),
////                conf.getOAuthConsumerSecret(),
////                accessToken);
////        // OAuthAuthorization繧剃ｽｿ縺｣縺ｦTwitter繧､繝ｳ繧ｹ繧ｿ繝ｳ繧ｹ繧堤函?ｽ?////        /*Twitter*/ twitter = new TwitterFactory().getInstance(auth);
////
////        setTwitter(twitter);
//
//        	TwitterFactory factory = new TwitterFactory();
////            twitter = factory.getOAuthAuthorizedInstance(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, new AccessToken(token, tokenSecret));
////        	factory.
//
//        	twitter = new TwitterFactory().getInstance();
//        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//
//            setTwitter(twitter);
//
//
//        //
//        if (screenNameDefault == null) {
//			try {
//				screenNameDefault = twitter.getScreenName(); // default縺ｯ閾ｪ?ｽ?//			} catch (Exception e1) {}
//    	}
//
//        load();
//        }
//
//        settingGestures();
//    }
//
//    public static void load() {
//    	if (screenName == null) {
////			try {
////				screenName = twitter.getScreenName(); // default縺ｯ閾ｪ?ｽ?////			} catch (Exception e1) {}
//
//    		screenName = screenNameDefault;
//    	}
//
//    	mHandler.post(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					loadMain();
//				}
//				catch (Exception e) {
//			    	Toast.makeText(activity, "failed." + e, Toast.LENGTH_SHORT).show();
//				}
//			}
//   		});
//    }
//
//    public static void loadMain() {
//        /*Paging*/ paging = new Paging(1, 20);
//
//
//
//        IDs timeline = null;
//        try {
////        	timeline = twitter.getFavorites(screenName, paging.getPage()); // home timeline
//        	timeline = twitter.getFavorites(screenName, paging.getPage()); // home timeline
////        	timeline = twitter.getFriendsIDs(twitter.getId()); // home timeline
//        } catch (TwitterException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        // 繧｢繧､繧ｳ繝ｳ逕ｻ蜒上ｒ逕滂ｿｽ?
//        Bitmap defaultImage;
//        defaultImage = BitmapFactory.decodeResource(
//                           activity.getResources(), R.drawable.default_image);
//
//        // ?ｽ??ｽ?ｽ繝医ョ繝ｼ繧ｿ繧剃ｽ懶ｿｽ?
//        List<ListItem> list = new ArrayList<ListItem>();
//
////        for (int i=0; i<timeline.size(); i++) {
////        	Status status = timeline.get(i);
//        for (Status status : timeline) {
////        for (timeline.hasNext()) {
//
////        	timeline.
//
//        	ListItem item1 = new ListItem();
////            item1.image = defaultImage;
//        	item1.image = HttpImage.getBitmap(status.getUser().getProfileImageURL().toString());
//
//            item1.name = status.getUser().getName();
//            item1.comment = status.getText();
//            if (status.getGeoLocation() != null) {
//            	item1.comment += "\n\n" + "Geo: " + "http://maps.google.co.jp/maps?q=" + status.getGeoLocation().getLatitude() + ",+" + status.getGeoLocation().getLongitude();
//            }
//
//            item1.id = status.getId();
//
//            item1.user = status.getUser();
//            item1.profileImageURL = status.getUser().getProfileImageURL().toString();
//            item1.screenName = status.getUser().getScreenName();
//
//            item1.date = diffDate(status.getCreatedAt());
//
//            list.add(item1);
//        }
//
//
//
//
//
//
//
//		//
////		itemMess = new ListItem();
////		itemMess.image = null;
////		itemMess.name = "";
////		itemMess.comment = "Loading...";
////        list.add(itemMess);
//
//        itemMessLoading = getMessageItem("?ｽ???ｽ??Loading...");
//        list.add(itemMessLoading);
//
//        itemEmpty = getMessageItem("");
//        list.add(itemEmpty);
//
//
//
//
//
//
//
//
//
//
//
//
//        // ListItemAdapter繧堤函?ｽ?//        ListItemAdapter adapter;
//        adapter = new ListItemAdapter(activity, 0, list, R.layout.list_item, true);
//
//        // ListView縺ｫListItemAdapter繧偵そ?ｽ??ｽ?ｽ
//        ListView listView = (ListView) activity.findViewById(R.id.ListView01);
////        listView.setFastScrollEnabled(true);
//        listView.setScrollingCacheEnabled(false);
//
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ListView listView = (ListView) parent;
////                ListItem item = (ListItem)listView.getSelectedItem(); // item縺系ull
//                ListItem item = (ListItem)listView.getItemAtPosition(position);
//                Log.d(TAG, "驕ｸ謚槭＆繧後◆繧｢繧､?ｽ??ｽ?ｽ縺ｮcomment=" + item.comment);
//                TextView textView = (TextView)view.findViewById(R.id.name);
//                Log.d(TAG, "驕ｸ謚槭＆繧後◆View縺ｮTextView(name)縺ｮtext=" + textView.getText());
//            }
//        });
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            private ListItem selectedItem;
//			private Context context;
//
//			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "onItemLongClick position=" + position);
//                // 縺｡縺ｪ縺ｿ縺ｫ縲’alse繧定ｿ斐☆縺ｨ繧､繝吶Φ繝医′邯咏ｶ壹☆繧具ｿｽ?縺ｧonItemClick繧ょ他縺ｳ蜃ｺ縺輔ｌ縺ｾ縺呻ｿｽ?
//
//                //
//                context = view.getRootView().getContext();
//
//                //
//                ListView listView = (ListView) parent;
//                selectedItem = (ListItem)listView.getItemAtPosition(position);
//
//
//        	    ///////////////
//           	 // 繝ｪ繧ｹ繝郁｡ｨ遉ｺ縺吶ｋ?ｽ??ｽ?ｽ?ｽ?
//               	String[] ITEM = new String[]{"Replay", "Retweet", "Favorite", "Copy", "Voice", "Delete"};
//               	if (selectedItem.name.indexOf(screenNameDefault) < 0) { // TODO:譛ｬ蠖難ｿｽ?equals縺ｧ?ｽ??ｽ?ｽ縺溘＞縲Ｏame縺ｨ?ｽ??ｽ?ｽ縺ｦ陦ｨ遉ｺ縺輔○縺溘ｉ?ｽ??ｽ?ｽ
//               		ITEM = new String[]{"Replay", "Retweet", "Favorite", "Copy", "Voice"};
//               	}
//
//               	new AlertDialog.Builder(context)
//               		.setTitle("濶ｲ繧帝∈謚槭＠縺ｦ縺上□縺輔＞.")
//               		.setItems(ITEM, new DialogInterface.OnClickListener() {
//                       	@Override
//                       	public void onClick(DialogInterface dialog, int which) {
//           	            	// TODO Auto-generated method stub
//           	            	// 繧｢繧､?ｽ??ｽ?ｽ縺碁∈謚槭＆繧後◆縺ｨ縺搾ｿｽ?蜃ｦ?ｽ? which縺碁∈謚槭＆繧後◆繧｢繧､?ｽ??ｽ?ｽ縺ｮ逡ｪ蜿ｷ.
//           	            	Log.v("Alert", "Item No : " + which);
//
//
//           	            	if (which == 0) { // @
////           	            		TabTweetActivity.setText("@"+selectedItem.name);
////           	            		TestActivity.setCurrentTab(TabTweetActivity.tabNo);
//
//           	            		TabTweetActivity.setText("@"+selectedItem.name);
//    							Intent intent = new Intent(activity, TabTweetActivity.class);
//    							activity.startActivity(intent);
//           	            	}
//           	            	else if (which == 1) { // RT
//           	            		mHandler.post(new Runnable() {
//									@Override
//									public void run() {
//										try {
//											twitter.retweetStatus(selectedItem.id);
//									    	Toast.makeText(context, "success.", Toast.LENGTH_SHORT).show();
//										}
//										catch (Exception e) {
//									    	Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
//										}
//									}
//           	            		});
//           	            	}
//							else if (which == 2) { // Favalite
//								mHandler.post(new Runnable() {
//									@Override
//									public void run() {
//										try {
//										twitter.createFavorite((selectedItem.id));
//											Toast.makeText(context, "success.", Toast.LENGTH_SHORT).show();
//										}
//										catch (Exception e) {
//									    	Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
//										}
//									}
//           	            		});
//							}
//							else if (which == 3) {
//								activity.setClipboard(selectedItem.comment);
//							}
//							else if (which == 4) {
//				                Voice voice = Voice.getInstance((Activity)context);
//				                voice.startVoice(selectedItem.comment);
//							}
//							else if (which == 5) { // delete
//								mHandler.post(new Runnable() {
//									@Override
//									public void run() {
//										try {
//											twitter.destroyStatus(selectedItem.id);
//									    	Toast.makeText(context, "success.", Toast.LENGTH_SHORT).show();
//
//									    	repaintHome();
//										}
//										catch (Exception e) {
//									    	Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
//										}
//									}
//           	            		});
//							}
//
//                       	}
//               	})
//               	.create()
//               	.show();
//               	///////////////////
//
//
//
//
//                return true;
////                return false;
//            }
//        });
//
//
//
//
//
//
//
//
//
//
//
//        listView.setOnScrollListener(new OnScrollListener() {
//
////			private ProgressDialog progressDialog;
//
//			@Override
//			public void onScroll(AbsListView view,
//					int firstVisibleItem,
//					int visibleItemCount,
//					int totalItemCount)
//			{
//                Log.d(TAG, "" + firstVisibleItem + " " + visibleItemCount + " " + totalItemCount);
//
//				if (firstVisibleItem + visibleItemCount == totalItemCount) {
//					Log.d(TAG, "LASTTTTTTTTTTTTT");
//
//
//					/*ListItemAdapter*/ myAdapter = (ListItemAdapter)view.getAdapter();
//
//
//					if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals("NO DATA")) { // NO DATA 縺ｧ縺ｪ縺代ｌ縺ｰ蜀榊叙?ｽ?//
//						final long lastId = myAdapter.getItem(myAdapter.getCount() - 3).id; // 3縺ｯ繝｡?ｽ??ｽ?ｽ繝ｼ繧ｸ?ｽ??縺医※
//
////						mHandler.post(new Runnable() {
////						@Override
////						public void run() {
//							// 騾壻ｿ｡荳ｭ?ｽ??ｽ?ｽ繧｢繝ｭ繧ｰ繧定｡ｨ遉ｺ縺輔○繧具ｿｽ?
////				            progressDialog = new ProgressDialog(activity);
////				            progressDialog.setTitle("");
////				            progressDialog.setMessage("Loading...");
////				            progressDialog.setIndeterminate(false);
////				            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////				            progressDialog.show();
////						}
////					});
//
//
//
//
//
//					//////////////////////////////////////////////
//
////		            Thread th = new Thread(new Runnable(){
//		            mHandler.post(new Runnable() {
//
//						@Override
//						public void run() {
//
//
//
//
//
//
//
//							myAdapter.remove(itemMessLoading);
//							myAdapter.remove(itemEmpty);
//
////							paging.setPage(paging.getPage() + 1);
//							paging.maxId(lastId);
//
//
//					        ResponseList<Status> timeline = null;
//					        try {
////					        	timeline = twitter.getUserTimeline(screenName, paging); // home timeline
//					        	timeline = twitter.getFavorites(screenName, paging.getPage()); // home timeline
//					        } catch (TwitterException e) {
//					            e.printStackTrace();
//					        }
//
//					        if (timeline.isEmpty()) {
//					        	String comment = "NO DATA";
//					        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(comment)) {
//					        		myAdapter.add(getMessageItem(comment));
//					        	}
//					        }
//					        else {
//						        // 繧｢繧､繧ｳ繝ｳ逕ｻ蜒上ｒ逕滂ｿｽ?
//						        Bitmap defaultImage;
//						        defaultImage = BitmapFactory.decodeResource(
//						        		activity.getResources(), R.drawable.default_image);
//
//						        // ?ｽ??ｽ?ｽ繝医ョ繝ｼ繧ｿ繧剃ｽ懶ｿｽ?
//						        List<ListItem> list = new ArrayList<ListItem>();
//
//						        for (Status status : timeline) {
//
//						        	ListItem item1 = new ListItem();
//	//					            item1.image = defaultImage;
//						        	item1.image = HttpImage.getBitmap(status.getUser().getProfileImageURL().toString());
//
//						            item1.name = status.getUser().getName();
//						            item1.comment = status.getText();
//						            if (status.getGeoLocation() != null) {
//						            	item1.comment += "\n\n" + "Geo: " + "http://maps.google.co.jp/maps?q=" + status.getGeoLocation().getLatitude() + ",+" + status.getGeoLocation().getLongitude();
//						            }
//
//	//					            list.add(item1);
//
//						            item1.id = status.getId();
//
//						            item1.user = status.getUser();
//						            item1.profileImageURL = status.getUser().getProfileImageURL().toString();
//						            item1.screenName = status.getUser().getScreenName();
//
//						            item1.date = diffDate(status.getCreatedAt());
//
//						        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(item1.comment)) {
//						        		myAdapter.add(item1);
//						        	}
//						        }
//
//						        myAdapter.add(itemMessLoading);
//						        myAdapter.add(itemEmpty);
//					        }
//
//
////					        progressDialog.cancel();
//						}
//		            });
////		            th.start();
//
//
//
//					//////////////////////////////////////////////
//
//
//		        	} //add
//
//				}
//			}
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//			}
//
//        });
//
//
//
//
//
//
//
//
//
//    }
//
//    // OK繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻ｼ縺ｳ蜃ｺ縺輔ｌ?ｽ?//    public void onReloadButton(View view) {
//
//    	load();
//
//    	Toast.makeText(activity, "onReloadButton" + view, Toast.LENGTH_SHORT).show();
//    }
//
//    // 繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻ｼ縺ｳ蜃ｺ縺輔ｌ?ｽ?//    public void onHomeButton(View view) {
//
//    	try {
////			screenName = twitter.getScreenName(); // default縺ｯ閾ｪ?ｽ?//    		screenName = screenNameDefault;
//		} catch (Exception e1) {}
//
//    	load();
//
//    	Toast.makeText(activity, "onHomeButton" + view, Toast.LENGTH_SHORT).show();
//    }
//
//	String[] ITEM;
//	String[] ITEM_ID;
//
//    // 繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻ｼ縺ｳ蜃ｺ縺輔ｌ?ｽ?//    public void onFriendButton(View view) {
//
//    	//
//        Context context = view.getRootView().getContext();
//
//   	    ///////////////
//		 // 繝ｪ繧ｹ繝郁｡ｨ遉ｺ縺吶ｋ?ｽ??ｽ?ｽ?ｽ?
////		String[] ITEM = null;
//
//		//
//		ArrayList<User> users = getFollowingUsers2(twitter, screenNameDefault);
//		ITEM = new String[users.size()];
//		ITEM_ID = new String[users.size()];
//		for(int i=0; i<users.size(); i++) {
//			ITEM_ID[i] = users.get(i).getScreenName();
//			ITEM[i] = users.get(i).getName() + "\n@" + ITEM_ID[i];
//		}
//
//		if (ITEM.length == 0) ITEM = new String[]{"NO DATA"};
//
//		new AlertDialog.Builder(context)
//			.setTitle("Friends")
//			.setItems(ITEM, new DialogInterface.OnClickListener() {
//		      	@Override
//		      	public void onClick(DialogInterface dialog, int which) {
//		        	// TODO Auto-generated method stub
//		        	// 繧｢繧､?ｽ??ｽ?ｽ縺碁∈謚槭＆繧後◆縺ｨ縺搾ｿｽ?蜃ｦ?ｽ? which縺碁∈謚槭＆繧後◆繧｢繧､?ｽ??ｽ?ｽ縺ｮ逡ｪ蜿ｷ.
//		        	Log.v("Alert", "Item No : " + which);
//
//        	    	try {
//        	    		screenName = ITEM_ID[which];
//        			} catch (Exception e1) {}
//
//        	    	load();
//		      	}
//		})
//		.create()
//		.show();
//		///////////////////
//
//    	Toast.makeText(activity, "onFriendButton" + view, Toast.LENGTH_SHORT).show();
//    }
//
//	public static void repaint() {
//		load();
//	}
//
//	public static void repaintHome() {
//		screenName = screenNameDefault;
//
//		load();
//	}
}