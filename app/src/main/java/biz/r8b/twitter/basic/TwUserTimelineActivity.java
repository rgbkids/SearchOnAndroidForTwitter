package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

// QueryTab2Activity縺ｨ蜷檎ｳｻ邨ｱ
public class TwUserTimelineActivity extends BaseActivity implements IfLoad {
	public static String ScreenName; // 蠑墓焚
	public static int PageNo; // 蠑墓焚

	//
	/*static*/ String screenName; // 蠑墓焚
	public /*static*/ int pageNo; // 蠑墓焚

	//
	private void init() {
		screenName = ScreenName;
		pageNo = PageNo;

		ScreenName = screenNameBase;
		pageNo = 1;
	}

	//
	private /*static*/ Paging paging;
	private /*static*/ ListItemAdapter myAdapter;

	private /*static*/ ListView listView;
	private /*static*/ TwUserTimelineActivity activity;


//	static String screenNameDefault;
//	private static Twitter twitter;

	/*static*/ Handler mHandler = new Handler();
	private /*static*/ ListItem itemMessLoading;
	private /*static*/ ListItem itemEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();

        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv8, null);

        setContentView(view);

        setSkin(view);
        activity = this;

        SharedPreferences sp =
            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
//        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(this, Auth.class);
//            startActivity(intent);
//
//            finish();
//        }
//        else {
//        	if (getTwitter() == null) {
//	        	Twitter twitter = new TwitterFactory().getInstance();
//	        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//	        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//
//	            setTwitter(twitter);
//        	}
//
//	        //
////	        if (screenNameDefault == null) {
////				try {
////					screenNameDefault = screenNameBase;
////				} catch (Exception e1) {}
////	    	}
//
//	        load();
//        }

        if (getTwitter() == null) {
        	Twitter twitter = new TwitterFactory().getInstance();
        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

            setTwitter(twitter, this);
    	}
        load();


        //
        Button filter1 = (Button)findViewById(R.id.filter1);
        filter1.setText((ja)?"URLを抽出":"urls");
        filter1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TwQueryResultActivity.setQuery("from:" + screenName + " http");
//    			TwQueryResultActivity.setSavedSearch(savedSearch);
				Intent intent = new Intent(TwUserTimelineActivity.this, TwQueryResultActivity.class);
				startActivity(intent);
			}
        });

        //
        Button filter2 = (Button)findViewById(R.id.filter2);
        filter2.setText((ja)?"写真を抽出":"photos");
        filter2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TwQueryResultActivity.setQuery("from:" + screenName + " pic.twitter.com");
//    			TwQueryResultActivity.setSavedSearch(savedSearch);
				Intent intent = new Intent(TwUserTimelineActivity.this, TwQueryResultActivity.class);
				startActivity(intent);
			}
        });


        //
        settingGestures();
        setHeaderMessage(this);
    }

    public /*static*/ void load() {
    	if (screenName == null) {
    		screenName = screenNameBase;
    	}

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

    	loadMain(this, mHandler);
    }

    @Override
    public /*static*/ void loadMain() {
    	if (pageNo < 1) pageNo = 1;

        paging = new Paging(pageNo, downloadNum);

        ResponseList<Status> timeline = null;
        try {
        	timeline = getTwitter().getUserTimeline(screenName, paging); // home timeline
        } catch (Exception e) {
        	errorHandling(e);
//            e.printStackTrace();
        }

        List<ListItem> list = new ArrayList<ListItem>();

        //
        ListItem AD = getMessageItem(getAD());
        list.add(AD);

        for (Status status : timeline) {
        	ListItem item1 = toListItem(status);
            list.add(item1);
        }

		//
        itemMessLoading = getMessageItem("Loading...");
        list.add(itemMessLoading);

        itemEmpty = getMessageItem("");
        list.add(itemEmpty);

        ListItemAdapter adapter;
        adapter = new ListItemAdapter(activity, 0, list, getTweetLayoutRID(this), true);

        listView = (ListView) activity.findViewById(R.id.ListView01);
        listView.setScrollingCacheEnabled(false);
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
        listView.setSelection(0);

        if (!BaseActivity.fast) {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final ListItem item = (ListItem)listView.getItemAtPosition(position);
				ArrayList<String> entries = getEntries(activity, item);

				if (entries.size() > 0) {
					final String[] ITEM = toArray(entries);
	               	new AlertDialog.Builder(activity)
	               		.setTitle("")
	               		.setItems(ITEM, new DialogInterface.OnClickListener() {
							@Override
	                       	public void onClick(DialogInterface dialog, int which) {
								controlTag (activity, ITEM, which, item);
	                       	}
	               	})
	               	.create()
	               	.show();
				}

                return true;
            }
        });
        }

        //
        listView.setOnScrollListener(new OnScrollListener() {
			private boolean doing;

			@Override
			public void onScroll(AbsListView view,
					int firstVisibleItem,
					int visibleItemCount,
					int totalItemCount)
			{
				try {
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					Log.d("", "LASTTTTTTTTTTTTT");

					//
					if (doing) return;
					doing = true;

					//
					myAdapter = (ListItemAdapter)view.getAdapter();

					if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals("NO DATA")) { //
						final long lastId = myAdapter.getItem(myAdapter.getCount() - 3).id; //

//						long _lastId = 0L;
//						try {
//							_lastId = myAdapter.getItem(myAdapter.getCount() - 3).id; //
//						} catch (Exception e){}
//						final long lastId = _lastId;

						//
			            mHandler.post(new Runnable() {
							@Override
							public void run() {
								myAdapter.remove(itemMessLoading);
								myAdapter.remove(itemEmpty);

								//
								if (lastId > 0) paging.maxId(lastId); // lastId繧ょ性縺ｾ繧後ｋ

						        ResponseList<Status> timeline = null;
						        try {
						        	timeline = getTwitter().getUserTimeline(screenName, paging);
						        } catch (Exception e) {
						        	errorHandling(e);
//						            e.printStackTrace();
						        }

						        if (timeline.isEmpty() || (timeline.size() == 1 && timeline.get(0).getId() == lastId)) {
						        	String comment = "NO DATA";
						        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(comment)) {
						        		myAdapter.add(getMessageItem(comment));
						        	}
						        }
						        else {
								    for (int i=0; i<timeline.size(); i++) {
								    	//
								    	Status status = timeline.get(i);

								    	ListItem item1 = toListItem(status);

							        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(item1.comment)) {
							        		myAdapter.add(item1);
							        	}
							        }

							        myAdapter.add(itemMessLoading);
							        myAdapter.add(itemEmpty);
						        }

						        //
						        doing = false;
							}
			            });
		        	}
				}
				} catch (Throwable t) {}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
        });
    }

    public void onReloadButton(View view) {
    	load();

//    	Toast.makeText(activity, "onReloadButton" + view, Toast.LENGTH_SHORT).show();
    }

    public void onHomeButton(View view) {
    	try {
    		screenName = screenNameBase;
		} catch (Exception e1) {}

    	load();

//    	Toast.makeText(activity, "onHomeButton" + view, Toast.LENGTH_SHORT).show();
    }

	String[] ITEM;
	String[] ITEM_ID;

	public void onFriendButton(View view) {
    	//
        Context context = view.getRootView().getContext();

   	    ///////////////
		//
		ArrayList<User> users = getFollowingUsers2(getTwitter(), screenNameBase);
		ITEM = new String[users.size()];
		ITEM_ID = new String[users.size()];
		for(int i=0; i<users.size(); i++) {
			ITEM_ID[i] = users.get(i).getScreenName();
			ITEM[i] = users.get(i).getName() + "\n@" + ITEM_ID[i];
		}

		if (ITEM.length == 0) ITEM = new String[]{"NO DATA"};

		new AlertDialog.Builder(context)
			.setTitle("Friends")
			.setItems(ITEM, new DialogInterface.OnClickListener() {
		      	@Override
		      	public void onClick(DialogInterface dialog, int which) {
		        	Log.v("Alert", "Item No : " + which);

        	    	try {
        	    		screenName = ITEM_ID[which];
        			} catch (Exception e1) {}

        	    	load();
		      	}
		})
		.create()
		.show();
		///////////////////

//    	Toast.makeText(activity, "onFriendButton" + view, Toast.LENGTH_SHORT).show();
    }

	public /*static*/ void repaint() {
		load();
	}

	public /*static*/ void repaintHome() {
		screenName = screenNameBase;

		load();
	}

    protected static ListItem toListItem (Status status) {
    	ListItem item1 = new ListItem();

        item1.image = defaultImage;

        item1.name = status.getUser().getName();
        item1.comment = status.getText();
//        if (status.getGeoLocation() != null) {
//        	item1.comment += "\n\n" + "Geo: " + "http://maps.google.co.jp/maps?q=" + status.getGeoLocation().getLatitude() + ",+" + status.getGeoLocation().getLongitude();
//        }
        item1.geoLocation = status.getGeoLocation();

        item1.id = status.getId();
        item1.user = status.getUser();
        item1.profileImageURL = status.getUser().getProfileImageURL().toString();
        item1.screenName = status.getUser().getScreenName();

        item1.date = diffDate(status.getCreatedAt());

        item1.isFavorited = status.isFavorited();
        item1.isRetweet   = status.isRetweet();
        item1.isRetweetedByMe = status.isRetweetedByMe();
        item1.retweetCount = status.getRetweetCount();

        //
        item1.hashtagEntities = status.getHashtagEntities();
        item1.mediaEntities = status.getMediaEntities();
        item1.urlEntities = status.getURLEntities();
        item1.userMentionEntities = status.getUserMentionEntities();

        //
        if (status.isRetweetedByMe()) {
        }
        else if (status.isRetweet()) {
        	item1.profileImageURL = status.getRetweetedStatus().getUser().getProfileImageURL().toString();
        	item1.screenName = status.getRetweetedStatus().getUser().getScreenName();
        	item1.name = status.getRetweetedStatus().getUser().getName();
        	item1.comment = status.getRetweetedStatus().getText();
        	item1.date = diffDate(status.getRetweetedStatus().getCreatedAt());
        	item1.retweetScreenName = status.getUser().getScreenName();
        }

        item1.inReplyToStatusId = status.getInReplyToStatusId();
        item1.isProtected = status.getUser().isProtected();

        //
        item1.source = status.getSource();

	    return item1;
    }

	public boolean isMe() {
		return screenName.equals(screenNameBase);
	}

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}