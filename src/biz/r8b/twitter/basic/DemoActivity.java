package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DemoActivity extends Activity {

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.main_demo );
	}

	public void onButtonClick( View v ) {
//
//		// create the quick action view, passing the view anchor
//		QuickActionView qa = QuickActionView.Builder( v );
//
//		// set the adapter
//		qa.setAdapter( new CustomAdapter( this ) );
//
//		// set the number of columns ( setting -1 for auto )
//		qa.setNumColumns( (int) (2 + (Math.random() * 10)) );
//		qa.setOnClickListener( new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick( DialogInterface dialog, int which ) {
//				dialog.dismiss();
//				Toast.makeText( getBaseContext(), "Selected item: " + which, Toast.LENGTH_SHORT ).show();
//			}
//		} );
//
//		// finally show the view
//		qa.show();
	}

	/**
	 * Custom Adapter just for custom values
	 * @author alessandro
	 *
	 */
	static class CustomAdapter extends BaseAdapter {

		static final int[] ICONS_ORG = new int[]{
			android.R.drawable.ic_menu_add,
			android.R.drawable.ic_menu_agenda,
			android.R.drawable.ic_menu_always_landscape_portrait,
			android.R.drawable.ic_menu_call,
			android.R.drawable.ic_menu_camera,
			android.R.drawable.ic_menu_close_clear_cancel,
			android.R.drawable.ic_menu_compass,
			android.R.drawable.ic_menu_crop,
			android.R.drawable.ic_menu_day,
			android.R.drawable.ic_menu_delete,
			android.R.drawable.ic_menu_directions,
			android.R.drawable.ic_menu_edit,
			android.R.drawable.ic_menu_gallery,
			android.R.drawable.ic_menu_help,
			android.R.drawable.ic_menu_info_details,
			android.R.drawable.ic_menu_manage,
			android.R.drawable.ic_menu_mapmode,
			android.R.drawable.ic_menu_month,
			android.R.drawable.ic_menu_more,
			android.R.drawable.ic_menu_mylocation,
			android.R.drawable.ic_menu_myplaces,
			android.R.drawable.ic_menu_preferences,
			android.R.drawable.ic_menu_revert,
			android.R.drawable.ic_menu_upload,
			android.R.drawable.ic_menu_sort_by_size,
		};

//		static final int[] ICONS = new int[]{
//			android.R.drawable.ic_menu_directions,
//			android.R.drawable.ic_menu_rotate,
//			android.R.drawable.ic_menu_compass,
//			android.R.drawable.ic_menu_set_as,
//			android.R.drawable.ic_menu_upload_you_tube,
//		};

//		static final String[] TITLES = new String[]{
//			"@",
//			"RT",
//			"Fav",
//			"Copy",
//			"Voice",
//		};
		LayoutInflater mLayoutInflater;
		List<ActionItemText> mItems;

		private String[] TITLES;
		private int[] ICONS;
		private int total;

		public CustomAdapter( Context context,
//				boolean retweetedByMe,
//				boolean favorites
				ListItem selectedItem
				)
		{
			boolean favorites = selectedItem.isFavorited;

			//
			mLayoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			mItems = new ArrayList<ActionItemText>();

//			if (context instanceof TwDMActivity) {
//				TwDMActivity twDMActivity = (TwDMActivity)context;
//				if (twDMActivity.mode == TwDMActivity.MODE_GET) {
//					TITLES = new String[]{
//							(BaseActivity.ja)?"DM送信":"Send message",
//							(BaseActivity.ja)?"削除":"Delete",
//						};
//
//					ICONS = new int[]{
//							R.drawable.icon_dm,
//							R.drawable.icon_del,
//						};
//					total = TITLES.length;
//				}
//				else if (twDMActivity.mode == TwDMActivity.MODE_SET) {
//					TITLES = new String[]{
//							(BaseActivity.ja)?"削除":"Delete",
//						};
//
//					ICONS = new int[]{
//							R.drawable.icon_del,
//						};
//					total = TITLES.length;
//				}
//			}
//			else {
//				boolean myTL = false;
//				if (context instanceof TwTweetTabActivity) {
//					TwTweetTabActivity twTweetTabActivity = (TwTweetTabActivity)context;
//					myTL = true;
//				}
//				else if (context instanceof TwUserTimelineActivity) {
//					TwUserTimelineActivity userTimelineActivity = (TwUserTimelineActivity)context;
//					if (userTimelineActivity.isMe()) {
//						myTL = true;
//					}
//				}
//
//				if (selectedItem.screenName.equals(BaseActivity.screenNameBase)) {
//					myTL = true;
//				}
//
////				mLayoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//
////				mItems = new ArrayList<ActionItemText>();
//	//			int total = (int) (4 + (Math.random() * 30));
//				/*int*/ total = 5 + ((myTL)?1:0);
//
//				/*final String[]*/ TITLES = new String[]{
//						(BaseActivity.ja)?"返信":"Reply",
//						(BaseActivity.ja)?"リツイート":"Retweet", //(!retweetedByMe)?"RT":"RT X",
//						(BaseActivity.ja)?((!favorites)?"お気に入り":"お気に入り"):((!favorites)?"Favorite":"Favorite"),
//						(BaseActivity.ja)?"コピー":"Copy",
//						(BaseActivity.ja)?"再生":"Voice",
//						(BaseActivity.ja)?"削除":"Delete",
//				};
//
//				/*final int[]*/ ICONS = new int[]{
////					android.R.drawable.ic_menu_directions,
////					android.R.drawable.ic_menu_rotate, //(!retweetedByMe)?android.R.drawable.ic_menu_rotate:android.R.drawable.ic_delete,
////					(!favorites)?android.R.drawable.ic_menu_compass:android.R.drawable.ic_delete,
////					android.R.drawable.ic_menu_set_as,
////					android.R.drawable.ic_menu_upload_you_tube,
////					android.R.drawable.ic_delete,
//
//					R.drawable.icon_at,
//					R.drawable.icon_retweet, //(!retweetedByMe)?R.drawable.ic_menu_rotate:R.drawable.ic_delete,
//					(!favorites)?R.drawable.icon_fav:R.drawable.icon_del,
//					R.drawable.icon_copy,
//					R.drawable.icon_voice,
//					R.drawable.icon_del,
//				};
//			}

			//
			Object[] res = getCtrlMenus(context, selectedItem, favorites);
			String[] TITLES = (String[]) res[0];
			int[] ICONS     = (int[]) res[1];
			int total       = (Integer)res[2];


			//
			for( int i = 0; i < total; i++ ) {
//				ActionItemText item = new ActionItemText( context, "Title " + i, ICONS[ (int)(Math.random() * ICONS.length) ] );
				ActionItemText item = new ActionItemText( context, TITLES[ i ], ICONS[ i ] );
				mItems.add( item );
			}
		}

		//
		public static Object[] getCtrlMenus(Context context, ListItem selectedItem, boolean favorites) {
			String[] TITLES = null;
			int[] ICONS = null;
			int total = 0;


			if (context instanceof TwDMActivity) {
				TwDMActivity twDMActivity = (TwDMActivity)context;
				if (twDMActivity.mode == TwDMActivity.MODE_GET) {
					TITLES = new String[]{
							(BaseActivity.ja)?"DM送信":"Send message",
							(BaseActivity.ja)?"削除":"Delete",
						};

					ICONS = new int[]{
							R.drawable.icon_dm,
							R.drawable.icon_del,
						};
					total = TITLES.length;
				}
				else if (twDMActivity.mode == TwDMActivity.MODE_SET) {
					TITLES = new String[]{
							(BaseActivity.ja)?"削除":"Delete",
						};

					ICONS = new int[]{
							R.drawable.icon_del,
						};
					total = TITLES.length;
				}
			}
			else {
				boolean myTL = false;
				if (context instanceof TwTweetTabActivity) {
					TwTweetTabActivity twTweetTabActivity = (TwTweetTabActivity)context;
					myTL = true;
				}
				else if (context instanceof TwUserTimelineActivity) {
					TwUserTimelineActivity userTimelineActivity = (TwUserTimelineActivity)context;
					if (userTimelineActivity.isMe()) {
						myTL = true;
					}
				}

				if (selectedItem.screenName.equals(BaseActivity.screenNameBase)) {
					myTL = true;
				}

//				mLayoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

//				mItems = new ArrayList<ActionItemText>();
	//			int total = (int) (4 + (Math.random() * 30));
				/*int*/
//				total = 5 + ((myTL)?1:0) + ((_App.BUTTON_VOICE)?0:-1);


				if (context instanceof TwWatchlaterActivity) {
					total = 5 + 1 + ((_App.BUTTON_VOICE)?0:-1);
				}
				else {
					total = 5 + ((myTL)?1:0) + ((_App.BUTTON_VOICE)?0:-1);
				}


				/*final String[]*/ TITLES = new String[]{
						(BaseActivity.ja)?"返信":"Reply",
						(BaseActivity.ja)?"リツイート":"Retweet", //(!retweetedByMe)?"RT":"RT X",
						(BaseActivity.ja)?((!favorites)?"お気に入り":"お気に入り"):((!favorites)?"Favorite":"Favorite"),
						(BaseActivity.ja)?"コピー":"Copy",
						(_App.BUTTON_VOICE)?((BaseActivity.ja)?"再生":"Voice"):((BaseActivity.ja)?"削除":"Delete"),
						(BaseActivity.ja)?"削除":"Delete",
				};

				/*final int[]*/ ICONS = new int[]{
//					android.R.drawable.ic_menu_directions,
//					android.R.drawable.ic_menu_rotate, //(!retweetedByMe)?android.R.drawable.ic_menu_rotate:android.R.drawable.ic_delete,
//					(!favorites)?android.R.drawable.ic_menu_compass:android.R.drawable.ic_delete,
//					android.R.drawable.ic_menu_set_as,
//					android.R.drawable.ic_menu_upload_you_tube,
//					android.R.drawable.ic_delete,

//					R.drawable.icon_at,
					R.drawable.icon_reply,
					R.drawable.icon_retweet, //(!retweetedByMe)?R.drawable.ic_menu_rotate:R.drawable.ic_delete,
					(!favorites)?R.drawable.icon_fav:R.drawable.icon_del,
					R.drawable.icon_copy,
					(_App.BUTTON_VOICE)?(R.drawable.icon_voice):(R.drawable.icon_del),
					R.drawable.icon_del,
				};
			}

			// check num
			String[] titles = new String[total];
			for (int i=0; i<titles.length; i++) {
				titles[i] = TITLES[i];
			}
			int[] icons = new int[total];
			for (int i=0; i<icons.length; i++) {
				icons[i] = ICONS[i];
			}

			//
			return new Object[] {titles, icons, total};
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem( int arg0 ) {
			return mItems.get( arg0 );
		}

		@Override
		public long getItemId( int arg0 ) {
			return arg0;
		}

		@Override
		public View getView( int position, View arg1, ViewGroup arg2 ) {
			View view = mLayoutInflater.inflate( R.layout.action_item, arg2, false );

			ActionItemText item = (ActionItemText) getItem( position );

			ImageView image = (ImageView) view.findViewById( R.id.image );
			TextView text = (TextView) view.findViewById( R.id.title );

			image.setImageDrawable( item.getIcon() );
			text.setText( item.getTitle() );

			return view;
		}

	};
}