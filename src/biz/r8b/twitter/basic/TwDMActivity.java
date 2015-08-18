package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
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

//TwAtActivity
public class TwDMActivity extends BaseActivity implements IfLoad {
	public static final int MODE_GET = 0;
	public static final int MODE_SET = 1;

	static int Mode = MODE_GET; // à¯êî

//	int tabNo = -1;
    private static final String TAG = "";
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
	int mode;

	private long lastIdDM;
	private View mark01;
	private View mark02;
	private View mark03;

	void init() {
		mode = Mode;
		Mode = MODE_GET; // èâä˙
	}

	//
	void initPage() {
		paging = new Paging(1, downloadNum);
    	downloadIdList = new ArrayList<String>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setKidoku(TwDMActivity.class);
        notifyHomeActivity(TwDMActivity.class);

        init();
        initPage();

        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv7, null);
        setContentView(view);

        setSkin(view);
        activity = this;

        //
        /*View*/ mark01 = findViewById(R.id.btn01_mark);
        /*View*/ mark02 = findViewById(R.id.btn02_mark);
        /*View*/ mark03 = findViewById(R.id.btn03_mark);

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

            if (mode == MODE_GET) {
            	lastIdDM = getLastIdDM(this);
            }

            load();
        }

        setMark();

        settingGestures();
        setHeaderMessage(this);
    }

    private void setMark() {
        if (mode == MODE_GET) {
        	mark01.setVisibility(View.VISIBLE);
        	mark02.setVisibility(View.INVISIBLE);
        }
        else if (mode == MODE_SET) {
        	mark01.setVisibility(View.INVISIBLE);
        	mark02.setVisibility(View.VISIBLE);
        }
	}

	public void load() {
    	loadMain(this, mHandler);
    }

    @Override
    public void loadMain() {
//    	paging = new Paging(1, 20);

        //
        ResponseList<DirectMessage> timeline = null;
        try {
        	if (mode == MODE_SET) {
        		timeline = getTwitter().getSentDirectMessages(paging);
        	}
        	else {
        		timeline = getTwitter().getDirectMessages(paging);
        	}
        } catch (Exception e) {
        	errorHandling(e);
        }

        List<ListItem> list = new ArrayList<ListItem>();

        //
        ListItem AD = getMessageItem(getAD());
        list.add(AD);

        // add 2002.08.12
        ArrayList<String> profileImageURLs = new ArrayList<String>();

        //
        int i = 0;
        for (DirectMessage status : timeline) {
        	ListItem item1 = toListItem(status);

        	//
        	if (mode == MODE_GET) {
        		if (status.getCreatedAt().getTime() > lastIdDM) {
        			item1.midoku = true;
        		}
        	}

        	list.add(item1);

        	// add 2002.08.12
        	profileImageURLs.add(item1.profileImageURL);

            i ++;
        }

        // add 2002.08.12
        ImageCache.loadImages(profileImageURLs);

        //
        if (mode == MODE_GET) {
	        try {
	        	setLastIdDM(this, ((DirectMessage)timeline.get(0)).getCreatedAt().getTime());
	        } catch (Exception e) {
	        	errorHandling(e);
	        }
        }

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

					if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals("NO DATA")) { // NO DATA
//						final long lastId = myAdapter.getItem(myAdapter.getCount() - 3).id; // 3

			            mHandler.post(new Runnable() {
							@Override
							public void run() {
								paging.setPage(paging.getPage() + 1);

								myAdapter.remove(itemMessLoading);
								myAdapter.remove(itemEmpty);

								ResponseList<DirectMessage> timeline = null;
						        try {
						        	if (mode == MODE_SET) {
						        		timeline = getTwitter().getSentDirectMessages(paging);
						        	}
						        	else {
						        		timeline = getTwitter().getDirectMessages(paging);
						        	}
						        } catch (Exception e) {
						        	errorHandling(e);
//						            e.printStackTrace();
						        }

						        if (timeline == null || timeline.isEmpty()) {
						        	String comment = "NO DATA";
						        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(comment)) {
						        		myAdapter.add(getMessageItem(comment));
						        	}
						        }
						        else {
						        	// add 2002.08.12
						            ArrayList<String> profileImageURLs = new ArrayList<String>();

							        for (DirectMessage status : timeline) {
							        	if (mode == MODE_GET) {
								        	if (status.getId() == lastIdDM) {
									    		myAdapter.add(getMessageItem("READ"));
								        	}
							        	}

							        	ListItem item1 = toListItem(status);

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

    public void onReloadButton(View view) {

//    	Toast.makeText(activity, "onReloadButton" + view, Toast.LENGTH_SHORT).show();

    	initPage();

    	if (mode == MODE_GET) {
    		lastIdDM = getLastIdDM(this);
    	}

    	load();
    }

    public void onGetButton(View view) {
    	mode = MODE_GET;

//    	Toast.makeText(activity, "onGetButton" + view, Toast.LENGTH_SHORT).show();

    	setMark();
    	initPage();
    	load();
    }

    public void onSetButton(View view) {
    	mode = MODE_SET;

//    	Toast.makeText(activity, "onSetButton" + view, Toast.LENGTH_SHORT).show();

    	setMark();
    	initPage();
    	load();
    }

    public void onNewButton(View view) {
//    	Toast.makeText(activity, "onNewButton" + view, Toast.LENGTH_SHORT).show();

    	TwDMNewActivity.ToScreenName = "";
    	intent(TwDMNewActivity.class);
    }

    protected ListItem toListItem (DirectMessage status) {
    	ListItem item1 = new ListItem();

//    	status.getCreatedAt();
//    	status.getId();
//    	status.getSender();
//    	status.getSenderId();
//    	status.getSenderScreenName();
//    	status.getText();
//    	status.getRecipient();


        item1.comment = status.getText();
        item1.id = status.getId();
        item1.date = diffDate(status.getCreatedAt());
    	item1.image = defaultImage;

    	if (mode == MODE_GET) {
        	item1.profileImageURL = status.getSender().getProfileImageURL().toString();
        	item1.name = (ja)?"(" + status.getSenderScreenName() + " Å® " + "Ç†Ç»ÇΩ)":"From";
//        					+ "\n" + status.getSender().getName();
        	item1.screenName = status.getSenderScreenName();
    	}
    	else if(mode == MODE_SET) {
        	item1.profileImageURL = status.getSender().getProfileImageURL().toString();
        	item1.name = "" + ((ja)?"(Ç†Ç»ÇΩ Å® " + status.getRecipientScreenName() + ")":"From you To");
        	item1.screenName = status.getRecipientScreenName();
    	}




        //
        item1.image = getDefaultImage();

//        item1.name = status.getUser().getName();
        item1.comment = status.getText();
//        if (status.getGeoLocation() != null) {
//        	item1.comment += "\n\n" + "Geo: " + "http://maps.google.co.jp/maps?q=" + status.getGeoLocation().getLatitude() + ",+" + status.getGeoLocation().getLongitude();
//        }

        item1.id = status.getId();
//        item1.user = status.getUser();
//        item1.profileImageURL = status.getUser().getProfileImageURL().toString();
//        item1.screenName = status.getUser().getScreenName();

        item1.date = diffDate(status.getCreatedAt());

//        item1.isFavorited = status.isFavorited();
//        item1.isRetweet   = status.isRetweet();
//        item1.isRetweetedByMe = status.isRetweetedByMe();
//        item1.retweetCount = status.getRetweetCount();

        //
//        item1.hashtagEntities = status.getHashtagEntities();
//        item1.mediaEntities = status.getMediaEntities();
//        item1.urlEntities = status.getURLEntities();
//        item1.userMentionEntities = status.getUserMentionEntities();

        //
//        if (status.isRetweetedByMe()) {
//        }
//        else if (status.isRetweet()) {
//        	item1.profileImageURL = status.getRetweetedStatus().getUser().getProfileImageURL().toString();
//        	item1.screenName = status.getRetweetedStatus().getUser().getScreenName();
//        	item1.name = status.getRetweetedStatus().getUser().getName();
//        	item1.comment = status.getRetweetedStatus().getText();
//        	item1.date = diffDate(status.getRetweetedStatus().getCreatedAt());
//        	item1.retweetScreenName = status.getUser().getScreenName();
//        }

//        item1.inReplyToStatusId = status.getInReplyToStatusId();
//        item1.isProtected = status.getUser().isProtected();

        return item1;
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}