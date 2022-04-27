package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TwSearchPhotosActivity extends BaseActivity implements IfLoad {
	TwSearchPhotosActivity activity;

	Handler mHandler = new Handler();
	private ArrayList<ListItem> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv12, null);
        setContentView(view);

        setSkin(view);
        activity = this;

        load();

        settingGestures();
        setHeaderMessage(this);

        //
        ImageView imgView = (ImageView)findViewById(R.id.imageView1);
        imgView.setImageResource(getDispIconRes());
        TextView  txtView = (TextView)findViewById(R.id.textView1);
        txtView.setText(getDispTitle());
    }

    //
	public void load() {
		loadMain(this, mHandler);
    }

	@Override
    public void loadMain() {
    	list = new ArrayList<ListItem>();


    	//
//	        for (UserList serach : savedSearchList) {
    	String[] countries = getResources().getStringArray(R.array.array_country);
        for (int i=0; i<countries.length; i++) {
        	ListItem item = new ListItem();

        	String langVal = countries[i];
        	String[] vals = csv(langVal, ":");
			String lang = "lang:" + vals[0];

        	item.name    = lang;
        	item.comment = vals[1];

            list.add(item);
        }

        //
        ListItemAdapter adapter = new ListItemAdapter(activity, 0, list, R.layout.list_item12, false);

        //
        ListView listView = (ListView)activity.findViewById(R.id.ListView01);
        listView.setScrollingCacheEnabled(false);

        //
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);

        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                ListItem item = (ListItem)listView.getItemAtPosition(position);
                TextView textView = (TextView)view.findViewById(R.id.name);

                //
//		                ListTab2Activity.setListId((int)item.id);
//            	TwListTab2Activity.Lid = (int)item.id;
//            	TwListTab2Activity.Lname = item.name + item.screenName;
//        		Intent intent = new Intent(activity, TwListTab2Activity.class);
//        		activity.startActivity(intent);

                if (position > 0) SwipeActivity.Lang = textView.getText().toString();
				SwipeActivity.Page = 1;
				intent(TwSearchPhotosActivity.this, SwipeActivity.class);
            }
        });
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}