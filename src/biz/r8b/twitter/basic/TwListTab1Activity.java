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

public class TwListTab1Activity extends BaseActivity implements IfLoad {

    private static final String TAG = "Recipe041";

    protected static final int MODE_VIEW = 0;
	protected static final int MODE_ADD = 1;

	/*static*/ TwListTab1Activity activity;
//	private /*static*/ Twitter twitter;

	/*static*/ Button btn_delete;

	/*static*/ Handler mHandler = new Handler();
	private /*static*/ AlertDialog alert;
	private /*static*/ ArrayList<ListItem> list;

	protected /*static*/ int mode = MODE_VIEW;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_lv9);

        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv9, null);
        setContentView(view);

        setSkin(view);
        activity = this;

        // プリファレンスを取??
        SharedPreferences sp =
            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // tokenとtokenSecretを取??
        String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
        // 値がなければAuthアク????ビティを起??
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

        //
        ImageView imgView = (ImageView)findViewById(R.id.imageView1);
        imgView.setImageResource(getDispIconRes());
        TextView  txtView = (TextView)findViewById(R.id.textView1);
        txtView.setText(getDispTitle());
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
        ResponseList<UserList> savedSearchList = null;

        try {
        	savedSearchList = getTwitter().getUserLists( // allList?
//        			twitter.getScreenName()
        			screenNameBase
        			);
        } catch (Exception e) {
        	errorHandling(e);
//            e.printStackTrace();
        }

        if (savedSearchList == null || savedSearchList.isEmpty()) {
        	toast(this, "リストがありません。");

		    ListView listView = (ListView)activity.findViewById(R.id.ListView01);
		    listView.setScrollingCacheEnabled(false);
		    ((AdapterView<ListAdapter>) listView).setAdapter(null);
        }
        else {
        	list = new ArrayList<ListItem>();

	        for (UserList serach : savedSearchList) {
	        	ListItem item1 = new ListItem();
//	            item1.name = serach.getName();
//	            item1.comment = ""+serach.getId();//serach.getQuery();
//	            item1.id = serach.getId();
//
//        		item1.description = serach.getDescription();
//        		item1.isPublic = serach.isPublic();

        		//
	        	String fullName = serach.getFullName();
	        	int memberCount = serach.getMemberCount();
	        	int subscriberCount = serach.getSubscriberCount();
	        	User user = serach.getUser();
	        	boolean isFollowing = serach.isFollowing();

        		item1.id = serach.getId();


        		item1.name = serach.getName();
//        		item1.screenName = serach.getFullName();
        		item1.comment = serach.getUser().getName() + "さんが作成" + "\n"+ serach.getDescription();
        		item1.date = (serach.isPublic())?"":"非公開";

        		item1.image = defaultImage;
        		item1.profileImageURL = serach.getUser().getProfileImageURL().toString();


	            list.add(item1);
	        }

	        // ListItemAdapterを生??
	        ListItemAdapter adapter;
	        adapter = new ListItemAdapter(activity, 0, list, R.layout.list_item4, false);

	        // ListViewにListItemAdapterをセ????
	        ListView listView = (ListView)activity.findViewById(R.id.ListView01);
	        listView.setScrollingCacheEnabled(false);

	        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
	        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                ListView listView = (ListView) parent;
	                ListItem item = (ListItem)listView.getItemAtPosition(position);
	                Log.d(TAG, "選択されたアイ????のcomment=" + item.comment);
	                TextView textView = (TextView)view.findViewById(R.id.name);
	                Log.d(TAG, "選択されたViewのTextView(name)のtext=" + textView.getText());

	                if (mode == MODE_VIEW) {
		                //
//		                ListTab2Activity.setListId((int)item.id);
	                	TwListTab2Activity.Lid = (int)item.id;
	                	TwListTab2Activity.Lname = item.name + item.screenName;
		        		Intent intent = new Intent(activity, TwListTab2Activity.class);
		        		activity.startActivity(intent);
	                }
	                else if(mode == MODE_ADD) {
	                	// TODO:動かな??
//	                	try {
//							getTwitter().subscribeUserList(
//									//twitter.getScreenName(),
//									screenNameBase,
//									(int)item.id);
////							Toast.makeText(activity, "success.", Toast.LENGTH_SHORT);
//							successHandling((ja)?"成功":"success");
//						} catch (Exception e) {
//							errorHandling(e);
////							Toast.makeText(activity, "error." + e, Toast.LENGTH_SHORT);
//						}
//	                	activity.finish();
	                }
	            }
	        });

/*
	        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	        	private int selectedItemId;
				private int selectedPosition;

	            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	                Log.d(TAG, "onItemLongClick position=" + position);
	                // ちなみに、falseを返すとイベントが継続する??でonItemClickも呼び出されます??

	                ListView listView = (ListView) parent;
	                ListItem item = (ListItem)listView.getItemAtPosition(position);
//	                selectedItemId = Integer.parseInt(item.comment);
	                selectedItemId = (int)item.id;
	                TextView textView = (TextView)view.findViewById(R.id.name);
	                selectedPosition = position;

					// リスト表示する??????
	            	final String[] ITEM = new String[]{"編??????, "削除する"};
	            	new AlertDialog.Builder(activity)
	            		.setTitle("")
	            		.setItems(ITEM, new DialogInterface.OnClickListener() {

							@Override
			            	public void onClick(DialogInterface dialog, int which) {
				            	// TODO Auto-generated method stub
				            	// アイ????が選択されたとき??処?? whichが選択されたアイ????の番号.
				            	Log.v("Alert", "Item No : " + which);

//				            	selectedItemId = Integer.parseInt(list.get(selectedPosition).comment);
				            	selectedItemId = (int)list.get(selectedPosition).id;

				            	ListItem item1 = list.get(selectedPosition);

				            	switch(which) {
					            	case  0 : {
					            		TwListEditActivity.setListId(selectedItemId);
					            		TwListEditActivity.setListTitle(item1.name);
					            		TwListEditActivity.setDescription(item1.description);
					            		TwListEditActivity.setSetting(item1.isPublic);

					            		TwListEditActivity.setMode(TwListEditActivity.UPDATE);
					            		Intent intent = new Intent(activity, TwListEditActivity.class);
					            		activity.startActivity(intent);
					            	}
					            	break;

					            	case  1 : {
						            	mHandler.post(new Runnable() {

											@Override
											public void run() {
												String mess = "";

												try {
													// dialog????こと??他端末で????って整合??とれてなかったらこわ??													getTwitter().destroyUserList(selectedItemId);
													mess = "success.";
													load();
												} catch (TwitterException e) {
//													e.printStackTrace();
													mess = "failed." + e;
												}

												Toast.makeText(activity, mess, Toast.LENGTH_SHORT).show();

											}
						            	});
					            	}
					            	break;
				            	}
			            	}
	            	})
	            	.create()
	            	.show();



	                return true;
	            }
	        });
*/



	        listView.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScroll(AbsListView view,
						int firstVisibleItem,
						int visibleItemCount,
						int totalItemCount)
				{
					try {
	                Log.d(TAG, "" + firstVisibleItem + " " + visibleItemCount + " " + totalItemCount);

	                if (btn_delete != null) {
	                	btn_delete.setVisibility(Button.INVISIBLE);
	                	btn_delete = null;
	                }

	                if (firstVisibleItem + visibleItemCount == totalItemCount) {
						Log.d(TAG, "LASTTTTTTTTTTTTT");
					}
					} catch (Throwable t) {}
				}

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
				}

	        });
        }
    }


    // ボタンが押されたら呼び出され??
	public void onOKButton(View view) {
    }

    // ボタンが押されたら呼び出され??
	public void onCreateButton(View view) {
    	TwListEditActivity.setMode(TwListEditActivity.CREATE);
    	Intent intent = new Intent(this, TwListEditActivity.class);
    	startActivity(intent);
    }

    // ボタンが押されたら呼び出され??
    public void onReloadButton(View view) {
//    	Toast.makeText(activity, "onReloadButton" + view, Toast.LENGTH_SHORT).show();

    	load();
    }

    // ボタンが押されたら呼び出され??
    public void onDeleteButton(View view) {
//    	Toast.makeText(activity, "onDeleteButton" + view, Toast.LENGTH_SHORT).show();
    }

	@Override
	public String getDispTitle() {
//		return "あなたのリスト";
		return getDispTitle(this);
	}
}