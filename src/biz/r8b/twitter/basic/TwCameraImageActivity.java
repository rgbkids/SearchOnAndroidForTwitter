package biz.r8b.twitter.basic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TwCameraImageActivity extends BaseActivity {
	//������static���Inohistory�ɂ����??????
	/*static*/ TwCameraImageActivity activity;
//	private static Twitter twitter;

	static Button btn_delete;

	static Handler mHandler = new Handler();
//	private static AlertDialog alert;

	static Bitmap picHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_lv5);

        //
        System.gc();

        //
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv5, null);
        setContentView(view);

        setSkin(view);

        // �\�t�g�E�F�A�L�[�{??�h���??
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        activity = this;

//        load();
        repaint();

        settingGestures();
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();

    	try {
    		picHistory.recycle();
    		picHistory = null;
    	} catch (Throwable t) {}

		System.gc();
    }

    public /*static*/ void repaint() {
    	if (activity == null) return;

        load();
    }

    public /*static*/ void load() {

		List<ListItem> list = new ArrayList<ListItem>();


		//
		File filePic = new File(PIC_FULLPATH);
		if (filePic.exists()) {
			ListItem item1 = new ListItem();

			//
			try {
				picHistory.recycle();
				picHistory = null;
			} catch (Throwable t) {}

			System.gc();

			picHistory = BitmapFactory.decodeFile(PIC_FULLPATH);
//			item1.image = BitmapFactory.decodeFile(PIC_FULLPATH);
//    		item1.image = resizeImage(item1.image, item1.image.getWidth()/10, item1.image.getHeight()/10);

			item1.image = picHistory;

    		//
			item1.name = "";
			item1.comment = ((ja)?"����":"History") + " \n" + new Date(filePic.lastModified());

			list.add(item1);
		}


		//
		list.add(getMessageItem((ja)?"�ʐ^�����Ȃ�":"No image"));


    	if (false) {

		// �摜��????�����[??
    	String urldata = "";
		try {
			urldata = http2str(
//					"http://rgb-kids.com/pixiv/blog/api?name=lwnicosei"
					"http://rgb-kids.com/tmp/up/cameraimg/"
					);
		} catch (Exception e) {
			errorHandling(e);
//			e.printStackTrace();
		}
		String[] rankIds = explode(urldata, ",");

//		int randNo = Math.abs(rand.nextInt()) % urls.length;

//		byte[] imgdata = http2data("http://rgb-kids.com/pixiv/blog/images/" + urls[randNo]);
//		imgNicosei = new Image(BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length));







//		List<ListItem> list = new ArrayList<ListItem>();
//
//		list.add(getMessageItem("�ʐ^��??��??));

//		for (String urlNico : rankUrls) {
//		for (int i=0; i<10; i++) {
		for (int i=0; i<rankIds.length; i++) {

			String id = rankIds[i];
			String profileImageURL = "http://rgb-kids.com/tmp/up/cameraimg/images/" + id;
//			byte[] imgdata = null;
//			try {
//				imgdata = http2data(
////						"http://rgb-kids.com/pixiv/blog/images/" + id
////						"http://rgb-kids.com/tmp/up/cameraimg/images/" + id
//						profileImageURL
//						);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			Bitmap bitmap = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);

//			id = id.replace("nicosei_", "");
//			id = id.replace("i", "");
//			id = id.replace(".JPEG", "");

    		String urlNico = "" + id;

			//
			ListItem item1 = new ListItem();
//			item1.image = bitmap;
			item1.image = defaultImage;

			item1.name = "";
			item1.comment = "" + urlNico;

			item1.profileImageURL = profileImageURL;

			list.add(item1);

		}

    	}


    	for (int i=0; i<SKINS.length; i++) {
    		ListItem item1 = new ListItem();

    		item1.id = i;



    		// for memory error
//    		System.gc();
//    		Bitmap image = getBitmap(SKINS_RID[i]);
//			item1.image = getBitmap(SKINS_RID[i]);
//    		item1.image = resizeImage(image, image.getWidth()/10, image.getHeight()/10);



    		item1.name = "";
			item1.comment = SKINS[i];

			list.add(item1);
    	}







	        // ListItemAdapter��??
    		ListItemAdapter adapter;
	        adapter = new ListItemAdapter(
//	        		this,
	        		activity,
	        		0, list, R.layout.list_item3, false);

	        // ListView��ListItemAdapter���Z????
	        ListView listView = (ListView)
//	        	findViewById(
	        	activity.findViewById(
	        			R.id.ListView01);
//	        listView.setFastScrollEnabled(true);
	        listView.setScrollingCacheEnabled(false);

	        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
	        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                ListView listView = (ListView) parent;
	//                ListItem item = (ListItem)listView.getSelectedItem(); // item��null
	                ListItem item = (ListItem)listView.getItemAtPosition(position);
//	                Log.d(TAG, "�I�����ꂽ�A�C????��comment=" + item.comment);
//	                TextView textView = (TextView)view.findViewById(R.id.name);
//	                Log.d(TAG, "�I�����ꂽView��TextView(name)��text=" + textView.getText());


	                if (item.comment.startsWith((ja)?"����":"History")) {
	                	TwTweetActivity.uploadFilePathImage = PIC_FULLPATH;
		                TwTweetActivity.uploadFileNameImage = PIC_NAME;
		                TwTweetActivity.addMess("dummy");

		                activity.finish();
	                }
	                else {
	//	                QueryTab2Activity.setQuery(textView.getText().toString());
	////	                QueryTab2Activity.buttonVisible();
	//	                QueryTab2Activity.repaint();
	//	                Tab4Activity.setCurrentTab(1);

	//	                /*TwCameraActivity.image = ImageCache.getImage(item.profileImageURL);//item.image;*/
//		                TwCameraActivity.image = item.image;

	                	if (position >= 2) TwCameraActivity.Image = getBitmap(SKINS_RID[(int)item.id]);

	                	//
	                	alertTwoButton(activity,
	                			"",
//	                			(ja)?"�������̎ʐ^��I�����Ă��������B":"Mode ?",
	                			BaseActivity.botMess("�������̎ʐ^��I��ł��������B", "�������̎ʐ^��I�����Ă��������B", "Mode ?"),
	                			(ja)?"�V�K�B�e":"Camera",
	                			new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										TwCameraActivity.Mode = TwCameraActivity.MODE_CAMERA;

										//
										Intent intent=new Intent(activity, biz.r8b.twitter.basic.TwCameraActivity.class);
						                activity.startActivity(intent);
						                activity.finish();
									}
	                			},
	                			(ja)?"�M�������[":"Gallery",
	                			new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
//										TwCameraActivity.Mode = TwCameraActivity.MODE_RESULT;
//
//										//
//										Intent intent=new Intent(activity, biz.r8b.miku.twitter.TwCameraActivity.class);
//						                activity.startActivity(intent);
//						                activity.finish();


//										// GALLERY
//										Intent intent = new Intent();
//										intent.setType("image/*");
//										intent.setAction(Intent.ACTION_GET_CONTENT);
//										startActivityForResult(intent, REQUEST_GALLERY);

										intent(activity, TwCameraGalleryActivity.class);
										finish();
									}
	                			}
	                	);



	                	{
//		                Intent intent=new Intent(activity, biz.r8b.miku.twitter.TwCameraActivity.class);
//		                activity.startActivity(intent);
	                	}
	                }

//	                activity.finish();
	            }
	        });

//	        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//	        	private int selectedItemId;
//
//	            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//	                Log.d(TAG, "onItemLongClick position=" + position);
//	                // ���Ȃ݂ɁAfalse��Ԃ��ƃC�x���g���p������??��onItemClick���Ăяo����܂�??
//
//
//
////	            	ListItemAdapter adapter = (ListItemAdapter)parent.getAdapter();
////	            	ListItem item = adapter.getItem(position);
//
//	                ListView listView = (ListView) parent;
//	                ListItem item = (ListItem)listView.getItemAtPosition(position);
//	                selectedItemId = Integer.parseInt(item.comment);
//	                TextView textView = (TextView)view.findViewById(R.id.name);
//
//
//
////	                alert.show();
//
//
//
////	                LinearLayout llsub = (LinearLayout)view.findViewById(R.id.ll_sub);
////	                for (int i=0; true; i++) {
////	                	try {
////	                		llsub.removeView(llsub.getChildAt(i));
////	                	}
////	                	catch (Exception e) {
////	                		break;
////	                	}
////	                }
////
////	                llsub.addView(textView);
////	                llsub.addView(textView);
////	                llsub.addView(textView);
//
//
////	                btn_delete = (Button)view.findViewById(R.id.btn_delete);
////	                btn_delete.setVisibility(Button.VISIBLE);
//
////	            	Toast.makeText(activity, "onItemLongClick" + item.name, Toast.LENGTH_SHORT).show();
//
//
//
//					// ���X�g�\������??????
//	            	final String[] ITEM = new String[]{"Delete"};
//	            	new AlertDialog.Builder(Tab4Activity.getActivity()) // ��??Tab�̃C���X�^���X�g??//	            		.setTitle("�F��I�����Ă�������.")
//	            		.setItems(ITEM, new DialogInterface.OnClickListener() {
//
//
//							@Override
//			            	public void onClick(DialogInterface dialog, int which) {
//				            	// TODO Auto-generated method stub
//				            	// �A�C????���I�����ꂽ�Ƃ�??��?? which���I�����ꂽ�A�C????�̔ԍ�.
//				            	Log.v("Alert", "Item No : " + which);
//
//				            	selectedItemId = which;
//
//				            	mHandler.post(new Runnable() {
//
//									@Override
//									public void run() {
//										String mess = "";
//
//										try {
//											// dialog????����??���[����????���Đ���??�Ƃ�ĂȂ������炱��??//											twitter.destroySavedSearch(selectedItemId);
//											mess = "success.";
//											load();
//										} catch (TwitterException e) {
//											e.printStackTrace();
//											mess = "failed." + e;
//										}
//
//										Toast.makeText(activity, mess, Toast.LENGTH_SHORT).show();
//
//									}
//
//				            	});
//			            	}
//	            	})
//	            	.create()
//	            	.show();
//
//
//
//	                return true;
//	            }
//	        });

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


    // OK�{�^���������ꂽ��Ăяo����??
    public void onOKButton(View view) {

    	String word =
            ((EditText)findViewById(R.id.word)).getText().toString();

//    	QueryTab2Activity.setQuery(word);
//        QueryTab2Activity.repaint();
//        TabSearchActivity.setCurrentTab(1);
    }

    // OK�{�^���������ꂽ��Ăяo����??
    public void onReloadButton(View view) {

//    	Toast.makeText(activity, "onReloadButton" + view, Toast.LENGTH_SHORT).show();

    	load();
    }

    // OK�{�^���������ꂽ��Ăяo����??
    public void onDeleteButton(View view) {

//    	Toast.makeText(activity, "onDeleteButton" + view, Toast.LENGTH_SHORT).show();

    }












//    // NETWORK ------------------------------------------------------------------------------
//    //
//    public byte[] httpGet(String url) throws Exception {
//    	return http2data(url);
//    }
//
//    public static String httpGetString(String url) throws Exception {
//    	return http2str(url);
//    }
//
//    //HTTP�ʐM������??
//    private static String http2str(
//    		//Context context,
//    		String path)
//    throws Exception
//    {
//        byte[] w=http2data(path);
//        return new String(w);
//    }
//
//    //HTTP�ʐM
//    public static byte[] http2data(String path) throws Exception
//    {
//        int size;
//        byte[] w=new byte[1024];
//        HttpURLConnection c=null;
//        InputStream in=null;
//        ByteArrayOutputStream out=null;
//        try {
//            //HTTP�ڑ�??�I�[�v��(2)
//            URL url=new URL(path);
//            c=(HttpURLConnection)url.openConnection();
//            c.setRequestMethod("GET");
//            c.connect();
//            in=c.getInputStream();
//
//            //�o�C�g??��??�ǂݍ���
//            out=new ByteArrayOutputStream();
//            while (true) {
//                size=in.read(w);
//                if (size<=0) break;
//                out.write(w,0,size);
//            }
//            out.close();
//
//            //HTTP�ڑ�??�N���[�Y(3)
//            in.close();
//            c.disconnect();
//            return out.toByteArray();
//        } catch (Exception e) {
//            try {
//                if (c!=null) c.disconnect();
//                if (in!=null) in.close();
//                if (out!=null) out.close();
//            } catch (Exception e2) {
//            }
//            throw e;
//        }
////        return null;
//    }
//    // NETWORK ------------------------------------------------------------------------------ END



    public static String[] explode(String str, String delim) {

        if(str == null) {
            throw new NullPointerException("explode:str is null");
        }

        String[] paras = null;
        String strWk = str.toString();
        int delimCnt = 0;

        // ��ɃJ���}??���𒲂�??
        int idx = -delim.length();
        while((idx = strWk.indexOf(delim, idx + delim.length())) > 0) {
            delimCnt ++;
        }

        if(delimCnt == 0) {
            // �J���}�Ȃ�??��??
        	return new String[]{str};
        }

        // ��??
        paras = new String[delimCnt + 1];

        int pIdx = -delim.length(); // 1�O??index
        int nIdx = -delim.length(); // ���݂�index

        for(int i = 0; ; i++) {
            nIdx = strWk.indexOf(delim, nIdx + delim.length());
            if(nIdx > 0) {
                paras[i] = strWk.substring(pIdx + delim.length(), nIdx);
                pIdx = nIdx;
            }
            else {
                // ??????�l
                paras[i]
                     = strWk.substring(pIdx + delim.length(), strWk.length());
                break;
            }
        }

        return paras;
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}

//	//
//	private static final int REQUEST_GALLERY = 0x00000001;
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		if(requestCode == REQUEST_GALLERY  && resultCode == RESULT_OK) {
//			try {
//				InputStream in = getContentResolver().openInputStream(data.getData());
//				Bitmap img = BitmapFactory.decodeStream(in);
//				in.close();
//
//				// �I�������摜��\��
////				imgView.setImageBitmap(img);
//
//				//
//				TwCameraActivity.Picture = img;
//
//			} catch (Exception e) {
//			}
//
//			//
//			TwCameraActivity.Mode = TwCameraActivity.MODE_RESULT;
//
//			//
//			Intent intent=new Intent(activity, biz.r8b.miku.twitter.TwCameraActivity.class);
//			activity.startActivity(intent);
////			activity.finish();
//		}
//	}

}