package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
//import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SwipeActivity extends BaseActivity {
    static int Page = 1;
	static String Lang;
	private int page;
	private String lang;

	List<ListItem> list;
	View prevView;


	//
	void init() {
		page = Page;
		lang = Lang;

		Page = 1;
		Lang = "";
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_swipe);

        //
        init();

        ///////////////////////////////

        //
    	SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
       	String token         = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret   = sp.getString(PREF_KEY_TOKEN_SECRET, "");
    	if (getTwitter() == null) {
        	Twitter twitter = new TwitterFactory().getInstance();
        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

            setTwitter(twitter, this);
    	}



    	//
    	List<Status> twitterSerches = null;
        /*List<ListItem>*/ list = new ArrayList<ListItem>();

        //
//        int page = 1;
//        String word = "pic.twitter.com lang:" + lang + " -RT";
        String word = "pic.twitter.com " + lang + " -RT";

        //
        try {
        	Query query = new Query(word);
//        	query = query.page(page);
        	QueryResult result = getTwitter().search(query);
        	twitterSerches = result.getTweets();
        } catch (Exception e) {
        	errorHandling(e);
        }

        //
        if (twitterSerches == null || twitterSerches.isEmpty()) {
        	list.add(getMessageItem("NO DATA"));
        }
        else {
	        for (Status tweet : twitterSerches) {
	        	ListItem item1 = TwQueryResultActivity.toListItem(tweet);
	            list.add(item1);
	        }

	        list.add(getMessageItem("Loading..."));
	        list.add(getMessageItem("NO DATA"));
        }



        //
        if (page == 1) {
        	toast(this, (ja)?"スワイプで切り替えます":"Please, swipe.");
        }

    	////////////////////////////////////////////


        //
        ViewPager mViewPager = (ViewPager)findViewById(R.id.viewpager);
        PagerAdapter mPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
    }

    private class MyPagerAdapter extends PagerAdapter {
		@Override
        public Object instantiateItem(ViewGroup container, int position) {


//            int[] pages = {R.layout.swipe_page, R.layout.swipe_page, R.layout.swipe_page};
//            int[] pages = {R.layout.list_item_c, R.layout.list_item_c, R.layout.list_item_c};
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            TextView tv = (TextView)inflater.inflate(pages[position], null);
//            LinearLayout ll = (LinearLayout)inflater.inflate(pages[position], null);



            //////////////////
            //
//            List<ListItem> list = new ArrayList<ListItem>();
//            ListItem item = new ListItem();
//            item.screenName = "test";
//            item.name = "test";
//            item.comment = "test";
//            item.date="test";
//            item.image = defaultImage;
//            list.add(item);
            ListItemAdapter adapter = new ListItemAdapter(SwipeActivity.this, 0, list, R.layout.swipe_page, true);
            View view = adapter.getView(position);

            //////////////////
            container.addView(view);

            //
        	if (position == (list.size() - 1) && list.size() > 2) {
        		intent(SwipeActivity.this, SwipeActivity.class);
        		Lang = lang;
        		Page = page + 1;
        		finish();
        	}

        	//
        	try {
	        	ImageView imgView = (ImageView)prevView.findViewById(R.id.imageAttached);
	        	imgView.getDrawingCache().recycle();
	        	imgView.setImageDrawable(null);
        	} catch (Throwable t) {}
        	try {
	        	System.gc();
        	} catch (Throwable t) {}
        	prevView = view;

        	//
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView((View)object);
        }

        @Override
        public int getCount() {
//            return 3;
        	return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }

	@Override
	public String getDispTitle() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
