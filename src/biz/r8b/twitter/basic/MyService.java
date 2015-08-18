package biz.r8b.twitter.basic;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

    private MyService context;
	private Handler mHandler = new Handler();
	private static Thread t;//�}���`�A�J�E���g���ӁI�I�I

	@Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    	//�T�[�r�X���N�������^�C�~���O�ŌĂяo�����B
    	//�����T�[�r�X�𕡐���N�����Ă��A���łɃT�[�r�X�����݂��邽�߁A���̃��\�b�h�͌Ăяo����Ȃ�

//    	Toast.makeText(this, "create service", Toast.LENGTH_SHORT).show();

    	BaseActivity.setJa(this);

        context = this;
    }

    @Override
    public synchronized void onStart(Intent intent, int StartId) {
    	//�T�[�r�X���J�n���ꂽ�^�C�~���O�ŌĂяo�����B
    	//�N���C�A���g�������ɂ킽��T�[�r�X���N�������ꍇ�A���̃��\�b�h�͕�����Ăяo�����

    	if (!BaseActivity.checkNetwork(this)) {
//    		BaseActivity.alertAndClose(this, "�l�b�g���[�N�G���[");
//    		BaseActivity.toast(this, "�l�b�g���[�N�G���[");
//    		this.stopSelf();
    		this.stopMyService();
        }
    	else if (BaseActivity.getString(this, "notify").equals("false")) { // �ʒm�ݒ�FOFF
    		this.stopMyService();
    	}


//    	Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show();


//        String message = intent.getStringExtra("Message");
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

//    	Toast.makeText(this, "t = " + t, Toast.LENGTH_SHORT).show();

    	if (t != null) return;

//    	this.stopSelf();

        /*Thread*/
    	t = new Thread() {

            @Override
            public void run() {
            	while (true) {



	                try {
//	                	mHandler.post(new Runnable() {
//                    		@Override
//                            public void run() {
//                    			Toast.makeText(context, "run() ", Toast.LENGTH_SHORT).show();
//                    		}
//                    	});



	                	//
	                	if (BaseActivity.getString(context, "notify").equals("false")) { // �ʒm�ݒ�FOFF�i�����I���j
	                		break;
	                	}



//	                	// �l�b�g���[�N�`�F�b�N
//	                	ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
//	                	final NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
//
//	                	mHandler.post(new Runnable() {
//                    		@Override
//                            public void run() {
//                    			Toast.makeText(context, "networkInfo.isConnected() " + networkInfo.isConnected(), Toast.LENGTH_SHORT).show();
//                    		}
//                    	});

//	                	if (networkInfo == null || !networkInfo.isConnected()) {
	                	if (!BaseActivity.checkNetwork(context)) {
//	                		Toast.makeText(context, "stop service", Toast.LENGTH_SHORT).show();
//	                		stopSelf();
	                		Thread.sleep(1000);
	                		continue;
	                	}






	                    // �v���t�@�����X���擾
	                    SharedPreferences sp =
	                        getSharedPreferences(BaseActivity.PREF_NAME, MODE_PRIVATE);
	                    // token��tokenSecret���擾
	                    String token       = sp.getString(BaseActivity.PREF_KEY_TOKEN, "");
	                    String tokenSecret = sp.getString(BaseActivity.PREF_KEY_TOKEN_SECRET, "");



	                    // �l���Ȃ�
	                    if ("".equals(token) || "".equals(tokenSecret)) {
	                    	stopMyService();
	                    	break;
	                    }
	                    else {
	                    	// ���񐶐����� network_state��change off�̂Ƃ����Ă΂��̂ŁA���ꂾ�Ƃ��������Ȃ��Ă�i�����j
//	                    	if (BaseActivity.getTwitter() == null) {
	            		        Twitter twitter = new TwitterFactory().getInstance();
	            		    	twitter.setOAuthConsumer(BaseActivity.TWITTER_CONSUMER_KEY, BaseActivity.TWITTER_CONSUMER_SECRET);
	            		    	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

	            		    	BaseActivity.setTwitter(twitter, MyService.this);
//	                    	}
	                    }













//	                	if (BaseActivity.getTwitter() == null) {
//	        		        Twitter twitter = new TwitterFactory().getInstance();
//	        		    	twitter.setOAuthConsumer(BaseActivity.TWITTER_CONSUMER_KEY, BaseActivity.TWITTER_CONSUMER_SECRET);
//	        		    	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//
//	        		    	BaseActivity.setTwitter(twitter);
//	                	}


//	                    mHandler.post(new Runnable() {
//                    		@Override
//                            public void run() {
//                    			Toast.makeText(context, "run() timeline", Toast.LENGTH_SHORT).show();
//                    		}
//                    	});


	                	//
	                    Twitter twitter = BaseActivity.getTwitter();
	                    Paging paging = new Paging(1, 1);

	                    //
	                    String ustOn = BaseActivity.getString(context, "userStreamOn");
	                    if (BaseActivity.countFocusActivity() == 0 || ustOn.equals("false")) { // focus nai toki or ust de nai toki
		                    {
		                    	//�}���`�A�J�E���g�Ȃ̂Œ��ӁI�O�̎c����
			                    ResponseList<Status> timeline = null;
			                    try {
			                    	timeline = twitter.getHomeTimeline(paging); // home timeline

			                    	String mess = "";
			                    	for (Status status : timeline) {
			                    		long id = status.getId();

			                    		if (BaseActivity.isUpdatedTimeline(context, id)) {
			                    			mess += (BaseActivity.ja)?"�^�C�����C��(�V���Ǎ��Œʒm����)":"Timeline";
			                    		};
			                        }

			                    	//
			                    	if (!mess.equals("")) {
				                    	final String _mess = mess;
				                    	mHandler.post(new Runnable() {
				                    		@Override
				                            public void run() {
	//			                    			BaseActivity.notifyBarStatic(context, "�V������", _mess, NotifyBootActivity.class);
				                    			BaseActivity.notifyBarStatic(context, (BaseActivity.ja)?"�V������":"Timeline", _mess, TwTimelineActivity.class);
				                    		}
				                    	});
			                    	}
			                    } catch (TwitterException e) {
	//		                        e.printStackTrace();
			                    }
		                    }
	                    }



//	                    mHandler.post(new Runnable() {
//                    		@Override
//                            public void run() {
//                    			Toast.makeText(context, "run() dm", Toast.LENGTH_SHORT).show();
//                    		}
//                    	});

	                    //
	                    {
	                    	//�}���`�A�J�E���g�Ȃ̂Œ��ӁI�O�̎c����
	                    	ResponseList<DirectMessage> timeline = null;
		                    try {
		                    	timeline = twitter.getDirectMessages(paging);

		                    	String mess = "";
		                    	for (DirectMessage status : timeline) {
//		                    		long id = status.getId();
		                    		long id = status.getCreatedAt().getTime();

		                    		if (BaseActivity.isUpdatedDM(context, id)) {
		                    			mess += (BaseActivity.ja)?"�޲ڸ�ү����(�V���Ǎ��Œʒm����)":"Direct messages";
		                    		};
		                        }

		                    	//
		                    	if (!mess.equals("")) {
			                    	final String _mess = mess;
			                    	mHandler.post(new Runnable() {
			                    		@Override
			                            public void run() {
//			                    			BaseActivity.notifyBarStatic(context, "���b�Z�[�W����", _mess, NotifyBootActivity.class);
			                    			BaseActivity.notifyBarStatic(context, (BaseActivity.ja)?"���b�Z�[�W����":"Direct messages", _mess, TwDMActivity.class);
			                    		}
			                    	});
		                    	}
		                    } catch (TwitterException e) {
//		                        e.printStackTrace();
		                    }
	                    }


//	                    mHandler.post(new Runnable() {
//                    		@Override
//                            public void run() {
//                    			Toast.makeText(context, "run() @", Toast.LENGTH_SHORT).show();
//                    		}
//                    	});

	                    //
	                    {
	                    	//�}���`�A�J�E���g�Ȃ̂Œ��ӁI�O�̎c����
	                    	ResponseList<Status> mentions = null;
		                    try {
		                    	mentions = twitter.getMentions(paging);

		                    	String mess = "";
		                    	for (Status status : mentions) {
//		                    		long id = status.getId();
		                    		long id = status.getCreatedAt().getTime();

		                    		if (BaseActivity.isUpdatedAt(context, id)) {
		                    			mess += (BaseActivity.ja)?"���Ȃ�����(�V���Ǎ��Œʒm����)":"Reply";
		                    		};
		                        }

		                    	//
		                    	if (!mess.equals("")) {
			                    	final String _mess = mess;
			                    	mHandler.post(new Runnable() {
			                    		@Override
			                            public void run() {
//			                    			BaseActivity.notifyBarStatic(context, "�ԐM����", _mess, NotifyBootActivity.class);
			                    			BaseActivity.notifyBarStatic(context, (BaseActivity.ja)?"�ԐM����":"Reply", _mess, TwAtActivity.class);
			                    		}
			                    	});
		                    	}
		                    } catch (TwitterException e) {
//		                        e.printStackTrace();
		                    }
	                    }



	                    // hi acitive no jikan nagakere ba owaru
	                    if (BaseActivity.countFocusActivity() == 0) {
	                    	if (System.currentTimeMillis() - BaseActivity.lastOnCreateTimeMillis > 60 * 30 * 1000) {
	                    		BaseActivity.finishAllNotKill(context);
	                    	}
	                    }



	                    //10�b�x��ŏI��
//	                    Thread.sleep(10 * 1000);//test

	                    Thread.sleep(60*10 * 1000);
//	                    Thread.sleep(60*1 * 1000); // test

	//                    stopSelf();

	                }
	                catch (Exception e) {
//	                	Toast.makeText(context, "failed." + e, Toast.LENGTH_SHORT).show();
	                }
            	}
            }
        };

        t.start();
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "destroy service", Toast.LENGTH_SHORT).show();

        t = null;
    }

    //
    public void stopMyService() {
    	this.stopSelf();
    }







    //////////////////



	@Override
	public boolean onUnbind(Intent intent) {
		Log.i("ServiceLifecycle", "onUnbind");
		super.onUnbind(intent);
		return true;
	}

	@Override
	public void onRebind(Intent intent) {
		Log.i("ServiceLifecycle", "onRebind");
		super.onRebind(intent);
	}

}