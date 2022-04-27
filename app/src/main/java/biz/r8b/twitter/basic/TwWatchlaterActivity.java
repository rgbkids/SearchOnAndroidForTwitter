package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
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

//TwAtActivity繧偵さ繝費ｿｽ?
public class TwWatchlaterActivity extends BaseActivity implements IfLoad {

	static final String KEY = "watchlater:";
    static final int WATCHLATER_MAX = 40;


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
//	void initPage() {
//		paging = new Paging(1, downloadNum);
//    	downloadIdList = new ArrayList<String>();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        initPage();

        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv4, null);
        setContentView(view);

        setSkin(view);
        activity = this;

//        SharedPreferences sp =
//            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        String token       = sp.getString(PREF_KEY_TOKEN, "");
//        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
//        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(this, Auth.class);
//            startActivity(intent);
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

            load();
//        }

        settingGestures();
        setHeaderMessage(this);
    }

    public void load() {
    	loadMain(this, mHandler);
    }

    @Override
    public void loadMain() {
//    	paging = new Paging(1, 20);

//        ResponseList<Status> timeline = null;
//        try {
//        	timeline = getTwitter().getFavorites(screenNameBase, paging.getPage());
//        } catch (Exception e) {
//        	errorHandling(e);
////            e.printStackTrace();
//        }

        List<ListItem> list = new ArrayList<ListItem>();

        //
        ListItem AD = getMessageItem(getAD());
        list.add(AD);

        // add 2002.08.12
        ArrayList<String> profileImageURLs = new ArrayList<String>();

        //
//        for (Status status : timeline) {
//        	ListItem item1 = toListItem(status);
//            list.add(item1);
//
//            // add 2002.08.12
//        	profileImageURLs.add(item1.profileImageURL);
//        }

        //
//        for (int i=0; i<WATCHLATER_MAX; i++) {
        for (int i=WATCHLATER_MAX-1; i>=0; i--) {
        	ListItem item1 = toListItemWatchlater(i);
        	if (item1 != null) {
	            list.add(item1);

	            // add 2002.08.12
	        	profileImageURLs.add(item1.profileImageURL);
        	}
        }

        // add 2002.08.12
        ImageCache.loadImages(profileImageURLs);

        // sort
        Object[] oa = list.toArray();
        Arrays.sort(oa, new Comparator() {
			@Override
			public int compare(Object arg0, Object arg1) {
				try {
					ListItem item0 = (ListItem)arg0;
					ListItem item1 = (ListItem)arg1;

					String[] dateVals0 = csv(item0.date, " ");
					String[] dateVals1 = csv(item1.date, " ");

					String dateVal0 = dateVals0[0].replace("/", "");
					String dateVal1 = dateVals1[0].replace("/", "");

					int dateInt0 = Integer.parseInt(dateVal0);
					int dateInt1 = Integer.parseInt(dateVal1);

					return dateInt1 - dateInt0;
				}
				catch (Exception e) {
//					System.out.println(e);
//					toast(TwWatchlaterActivity.this, e.toString());
					return 0;
				}
			}
        });
        list.clear();
        for(int i = 0; i < oa.length; i++){
          // ソート前の配列とソート後の配列を並べて表示
//          System.out.println((Data)list.toArray()[i]+”, \t”+(Data)oa[i]);
        	list.add((ListItem)oa[i]);
        }

        //
//        itemMessLoading = getMessageItem("Loading...");
//        list.add(itemMessLoading);
//
//        itemEmpty = getMessageItem("");
//        list.add(itemEmpty);

        adapter = new ListItemAdapter(this, 0, list, getTweetLayoutRID(this), true);

        listView = (ListView) findViewById(R.id.ListView01);
        listView.setScrollingCacheEnabled(false);
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
        listView.setSelection(0);

        if (!BaseActivity.fast) {
        }

	}


	public void onReloadButton(View view) {
		load();
    }


    protected ListItem toListItem (Status status) {
    	ListItem item1 = new ListItem();

        item1.image = getDefaultImage();

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


    private ListItem toListItemWatchlater(int i) {
    	ListItem item1 = new ListItem();

        //
        String wlDatas = getString(TwWatchlaterActivity.KEY + i);
        String[] wlData = BaseActivity.csv(wlDatas, ","); // , wo escape ??

        if (wlDatas.equals("")) {
        	return null;
        }


        item1.image = getDefaultImage();



        //
        item1.id = Long.parseLong(wlData[0]);//status.getId();
        item1.screenName = wlData[1];
//        item1.name = wlData[2];
        item1.comment = wlData[2];
//        item1.date = wlData[3];
        item1.profileImageURL = wlData[4];//status.getUser().getProfileImageURL().toString();

        //
        String[] dates = csv(wlData[3], " ");
        for (int k=0; k<dates.length-1; k++) {
        	item1.date += dates[k] + " ";
        }

        //
//        ArrayList<String> atList = getAtList(item1.comment);
//        for (int k=0; k<atList.size(); k++) {
//        	String val = atList.get(k);
//
//        }


        //
//        if (!wlData[5].equals("null")) {
//        	item1.isRetweet = true;
//        	item1.retweetScreenName = wlData[5];
//        }

//        item1.geoLocation = status.getGeoLocation();
//
//        item1.user = status.getUser();


//
//
//        item1.isFavorited = status.isFavorited();
//        item1.isRetweet   = status.isRetweet();
//        item1.isRetweetedByMe = status.isRetweetedByMe();
//        item1.retweetCount = status.getRetweetCount();
//
//        //
//        item1.hashtagEntities = status.getHashtagEntities();
//        item1.mediaEntities = status.getMediaEntities();
//        item1.urlEntities = status.getURLEntities();
//        item1.userMentionEntities = status.getUserMentionEntities();
//
//        //
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
//
//        item1.inReplyToStatusId = status.getInReplyToStatusId();
//        item1.isProtected = status.getUser().isProtected();
//
//        //
//        item1.source = status.getSource();

        return item1;
 	}



	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}

	//
	public static void delete(Context context, long id) {
		for (int i=0; i<WATCHLATER_MAX; i++) {
			String wlDatas = getString(context, TwWatchlaterActivity.KEY + i);

			if (wlDatas.startsWith(id + ",")) {
				putString(context, TwWatchlaterActivity.KEY + i, "");
			}
		}
	}

	//
	public static void create(Context context, ListItem selectedItem) {

		// check kouritsu wrui
		for (int i=0; i<WATCHLATER_MAX; i++) {
			if (BaseActivity.getString(context, KEY + i).startsWith(selectedItem.id + ",")) {
				return;
			}
		}



		// idx
		int idx = 0;//TwWatchlaterActivity.WATCHLATER_MAX-1;
		for (int i=0; i<WATCHLATER_MAX; i++) {
			if (BaseActivity.getString(context, KEY + i).equals("")) {
				idx = i;
				break;
			}
		}
//		if (idx == -1) idx = TwWa;

//
		//
		String wlData = "";



		//
////		if (selectedItem.isRetweet) {
//		if (selectedItem.comment.startsWith("RT ")) {
////        	item1.profileImageURL = status.getRetweetedStatus().getUser().getProfileImageURL().toString();
////        	item1.screenName = status.getRetweetedStatus().getUser().getScreenName();
////        	item1.name = status.getRetweetedStatus().getUser().getName();
////        	item1.comment = status.getRetweetedStatus().getText();
////        	item1.date = diffDate(status.getRetweetedStatus().getCreatedAt());
////        	item1.retweetScreenName = status.getUser().getScreenName();
//
//        	wlData += selectedItem.id + ",";
//			wlData += selectedItem.screenName + ",";
////			wlData += selectedItem.name + ",";
//			wlData += selectedItem.comment.replace(",", " ") + ",";
//			wlData += selectedItem.date + ",";
//			wlData += selectedItem.profileImageURL + ",";
//			wlData += selectedItem.retweetScreenName;
//        }
//		else {
			wlData += selectedItem.id + ",";
			wlData += selectedItem.screenName + ",";
//			wlData += selectedItem.name + ",";
			wlData += selectedItem.comment.replace(",", " ") + ",";
			wlData += selectedItem.date + ",";
			wlData += selectedItem.profileImageURL; // + ",";
//			wlData += "null";
//		}

		//
		BaseActivity.putString(context, TwWatchlaterActivity.KEY + idx, wlData);
	}
}