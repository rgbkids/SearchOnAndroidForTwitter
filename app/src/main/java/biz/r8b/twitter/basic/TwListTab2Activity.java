package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
//import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//QueryTab2Activity
public class TwListTab2Activity extends BaseActivity implements IfLoad {
    static int Lid; // 引数
    static String Lname; // 引数

    private static final String TAG = "";

    /*static*/ TwListTab2Activity activity;
//	private /*static*/ Twitter twitter;
    int lid;
	private String lname;
//	private int page;
	private ArrayList<String> downloadIdList;

    /*static*/ Handler mHandler = new Handler();

	private /*static*/ int listId;
	private ArrayAdapter<ListItem> myAdapter;
	private ListItem itemMessLoading;
	private ListItem itemEmpty;
	private /*Abs*/ListView listView;
	private Paging paging;
	private ListItemAdapter adapter;

    //
    private void init() {
    	lid = Lid;
    	lname = Lname;

    	initPage();
    }

    void initPage() {
//    	page = 1;
    	paging = new Paging(1, downloadNum);
    	downloadIdList = new ArrayList<String>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        //
        //test
//        alertAndClose(this, "" + lid);




//        setContentView(R.layout.main_lv3);
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv3, null);
        setContentView(view);

        setSkin(view);
        activity = this;

//        TextView tv = (TextView)findViewById(R.id.TextView01);
//        if (lid > 0) {
//        	buttonVisible(View.VISIBLE);
//        }
//        else if (tv.getText().equals("")) {
//        	buttonVisible(View.INVISIBLE);
//        }
//        else {
//        	buttonVisible(View.VISIBLE);
//        }

        //
        findViewById(R.id.Button01).setVisibility(View.INVISIBLE);

        repaint();

        settingGestures();
        setHeaderMessage(this);
    }

    /*static*/ void buttonVisible(int state) {
    	try {
    		activity.findViewById(R.id.Button01).setVisibility(state);
    		activity.findViewById(R.id.Button02).setVisibility(state);
    	} catch (Exception e){}
    }

    public /*static*/ void repaintMain() {
    	if (activity == null || lid <= 0/*word == null*/) return;

    	SharedPreferences sp =
        	activity.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    	String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");



        if ("".equals(token) || "".equals(tokenSecret)) {
            Intent intent = new Intent(activity, Auth.class);
            activity.startActivity(intent);
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




    }

	public /*static*/ void repaint() {

        try {
        	TextView tv = (TextView)activity.findViewById(R.id.TextView01);
        	tv.setText("" + lname);
        }catch(Exception e){}

        ListView listView = (ListView) activity.findViewById(R.id.ListView01);
        ((AdapterView<ListAdapter>) listView).setAdapter(null);

    	mHandler.post(new Runnable() {
			@Override
			public void run() {
				try {
					repaintMain();
				}
				catch (Exception e) {
					errorHandling(e);
//			    	Toast.makeText(activity, "failed." + e, Toast.LENGTH_SHORT).show();
				}
			}
   		});
    }

	public /*static*/ void load() {
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
        ResponseList<Status> result = null;
        List<ListItem> list = new ArrayList<ListItem>();

        //
        ListItem AD = getMessageItem(getAD());
        list.add(AD);

//for (int page=1; page<=1; page++) {

        try {
        	result = getTwitter().getUserListStatuses(lid, new Paging(1));
        } catch (Exception e) {
        	errorHandling(e);
//            e.printStackTrace();
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

        for (Status tweet : result) {
        	ListItem item1 = toListItem(tweet);
            list.add(item1);

            downloadIdList.add("" + item1.id);

            // add 2002.08.12
        	profileImageURLs.add(item1.profileImageURL);
        }

        // add 2002.08.12
        ImageCache.loadImages(profileImageURLs);

//}

        //
        itemMessLoading = getMessageItem("Loading...");
        list.add(itemMessLoading);

        itemEmpty = getMessageItem("");
        list.add(itemEmpty);

//        ListItemAdapter adapter;
        adapter = new ListItemAdapter(activity, 0, list, getTweetLayoutRID(this), true);

        listView = (ListView) activity.findViewById(R.id.ListView01);
//        listView.setScrollingCacheEnabled(false);
//        listView.setAdapter(adapter);

//        /*AbsListView*/ listView = (AbsListView) findViewById(R.id.ListView01);
////        listView.setAdapter(adapter);
//        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);

//    listView.setAdapter(adapter);
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);



        listView.setScrollingCacheEnabled(false);
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

					if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals("NO DATA")) { // NO DATA
//						final long lastId = myAdapter.getItem(myAdapter.getCount() - 3).id; // 3

			            mHandler.post(new Runnable() {
							@Override
							public void run() {
//								page ++;
								paging.setPage(paging.getPage() + 1);

								myAdapter.remove(itemMessLoading);
								myAdapter.remove(itemEmpty);

						        ResponseList<Status> result = null;

								try {
						        	result = getTwitter().getUserListStatuses(lid, paging);
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

						        	for (Status tweet : result) {
							        	ListItem item1 = toListItem(tweet);

							            if (!downloadIdList.contains("" + item1.id)) { //
							        		myAdapter.add(item1);
							        		downloadIdList.add("" + item1.id);

							        		// add 2002.08.12
							            	profileImageURLs.add(item1.profileImageURL);
							        	}
							        }

						        	// add 2002.08.12
								    ImageCache.loadImages(profileImageURLs);

								    //
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
    }

    	public void onOKButton(View view) {
    }


	public void onReloadButton(View view) {
    	initPage();
    	load();
    }

    protected static ListItem toListItem (Status tweet) {
    	ListItem item1 = new ListItem();

        item1.image = defaultImage;
        item1.id = tweet.getId();

        item1.screenName = /*"@" +*/ tweet.getUser().getScreenName();
        item1.name = /*"@" +*/ tweet.getUser().getName();
        item1.comment = tweet.getText();
//        if (tweet.getGeoLocation() != null) {
//        	item1.comment += "\n\n" + "Geo: " + "http://maps.google.co.jp/maps?q=" + tweet.getGeoLocation().getLatitude() + ",+" + tweet.getGeoLocation().getLongitude();
//        }
        item1.geoLocation = tweet.getGeoLocation();
        item1.profileImageURL = tweet.getUser().getProfileImageURL().toString();
        item1.date = diffDate(tweet.getCreatedAt());

        //
        item1.source = tweet.getSource();

        return item1;
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}