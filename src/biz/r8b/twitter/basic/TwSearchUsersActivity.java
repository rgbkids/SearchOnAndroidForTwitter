package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
//import twitter4j.Tweet;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//TwAtActivityをコピ�?
public class TwSearchUsersActivity extends BaseActivity implements IfLoad {

	private Activity activity;
//	private Twitter twitter;
	Handler mHandler = new Handler();
	private ListView listView;
	private ListItem itemMessLoading;
	private ListItem itemEmpty;
	private ListItemAdapter adapter;
	private ListItemAdapter myAdapter;
	private Paging paging;
	ArrayList<String> downloadIdList;

	//
    static String Word = "";
    String word;

	//
	private void init() {
		word = Word;
		initPage();
	}

	//
	void initPage() {
		paging = new Paging(1, downloadNum);
    	downloadIdList = new ArrayList<String>();
    }

	//
    public static void setQuery(String _word) {
    	Word = _word;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        initPage();
        init();

        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv4, null);
        setContentView(view);

        setSkin(view);
        activity = this;

        SharedPreferences sp =
            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
        if ("".equals(token) || "".equals(tokenSecret)) {
            Intent intent = new Intent(this, Auth.class);
            startActivity(intent);
            finish();
        }
        else {
        	if (getTwitter() == null) {
	        	Twitter twitter = new TwitterFactory().getInstance();
	        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
	        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

	            setTwitter(twitter, this);
        	}

            load();
        }

        settingGestures();
        setHeaderMessage(this);
    }

    public void load() {
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
    public void loadMain() {
//    	paging = new Paging(1, 20);

    	ResponseList<User> result = null;
        try {
//        	timeline = getTwitter().getFavorites(screenNameBase, paging.getPage());
        	result = getTwitter().searchUsers(word, paging.getPage());
        } catch (Exception e) {
        	errorHandling(e);
//            e.printStackTrace();
        }

        List<ListItem> list = new ArrayList<ListItem>();

        //
        ListItem AD = getMessageItem(getAD());
        list.add(AD);

        // add 2002.08.12
        ArrayList<String> profileImageURLs = new ArrayList<String>();

        //
        for (User status : result) {
        	ListItem item1 = toListItem(status);
        	if (item1 != null) {
	            list.add(item1);

	            // add 2002.08.12
	        	profileImageURLs.add(item1.profileImageURL);
        	}
        }

        // add 2002.08.12
        ImageCache.loadImages(profileImageURLs);

        //
        itemMessLoading = getMessageItem("Loading...");
        list.add(itemMessLoading);

        itemEmpty = getMessageItem("");
        list.add(itemEmpty);

        adapter = new ListItemAdapter(this, 0, list, getTweetLayoutRID(this), true);

        listView = (ListView) findViewById(R.id.ListView01);
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
					Log.d(TAG, "LASTTTTTTTTTTTTT");

					//
					if (doing) return;
					doing = true;

					//
					myAdapter = (ListItemAdapter)view.getAdapter();

					if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals("NO DATA")) { //
//						final long lastId = myAdapter.getItem(myAdapter.getCount() - 3).id; //

			            mHandler.post(new Runnable() {
							@Override
							public void run() {
								paging.setPage(paging.getPage() + 1);

								myAdapter.remove(itemMessLoading);
								myAdapter.remove(itemEmpty);

								ResponseList<User> result = null;
						        try {
//						        	result = getTwitter().getFavorites(screenNameBase, paging.getPage());
						        	result = getTwitter().searchUsers(word, paging.getPage());
						        } catch (Exception e) {
						        	errorHandling(e);
//						            e.printStackTrace();
						        }

						        if (result == null || result.isEmpty()) {
						        	String comment = "NO DATA";
						        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(comment)) {
						        		myAdapter.add(getMessageItem(comment));
						        	}
						        }
						        else {
						        	// add 2002.08.12
						            ArrayList<String> profileImageURLs = new ArrayList<String>();

							        for (User status : result) {
							        	ListItem item1 = toListItem(status);
							        	if (item1 != null) {
								            if (!downloadIdList.contains("" + item1.id)) { //
								        		myAdapter.add(item1);
								        		downloadIdList.add("" + item1.id);

								        		// add 2002.08.12
								            	profileImageURLs.add(item1.profileImageURL);
								        	}
							        	}
							        }

							        // add 2002.08.12
								    ImageCache.loadImages(profileImageURLs);

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

//    	Toast.makeText(activity, "onReloadButton" + view, Toast.LENGTH_SHORT).show();

    	initPage();
    	load();
    }


    protected ListItem toListItem (User user) {
    	try {
    	ListItem item1 = new ListItem();

        item1.image = getDefaultImage();


//        user.getId();
//
//        user.getName();
//        user.getScreenName()
//        user.getLang();
//        user.getLocation();
//        user.getProfileImageURL();
//        user.getDescription()
//
//        user.getStatus().
//
//        user.getStatusesCount()
//        user.getFavouritesCount()
//        user.getFollowersCount()


        String loc = user.getLocation();
        String dsc = user.getDescription();
        String userInfo = ""
        		+ ((loc == null || loc.trim().length() == 0)?"":(" / " + loc))
        		+ ((dsc == null || dsc.trim().length() == 0)?"":"\n" + dsc)
        		+ "";



        Status status = user.getStatus();
//        status.


//
        item1.name = user.getName() + userInfo;
        item1.comment = status.getText();
//        if (status.getGeoLocation() != null) {
//        	item1.comment += "\n\n" + "Geo: " + "http://maps.google.co.jp/maps?q=" + status.getGeoLocation().getLatitude() + ",+" + status.getGeoLocation().getLongitude();
//        }
        item1.geoLocation = status.getGeoLocation();

        item1.id = status.getId();
        item1.user = user;
        item1.profileImageURL = user.getProfileImageURL().toString();
        item1.screenName = user.getScreenName();

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
        	try {
//        	item1.profileImageURL = status.getRetweetedStatus().getUser().getProfileImageURL().toString();
//        	item1.screenName = status.getRetweetedStatus().getUser().getScreenName();
//        	item1.name = status.getRetweetedStatus().getUser().getName();
//        	item1.comment = status.getRetweetedStatus().getText();
//        	item1.date = diffDate(status.getRetweetedStatus().getCreatedAt());
        	item1.retweetScreenName = user.getScreenName();
        	} catch (Exception e) {}
        }

        item1.inReplyToStatusId = status.getInReplyToStatusId();
        item1.isProtected = user.isProtected();

        //
        item1.source = status.getSource();

        return item1;

    	} catch (Exception e) {
    		return null;
    	}
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}