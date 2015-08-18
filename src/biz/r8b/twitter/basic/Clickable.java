package biz.r8b.twitter.basic;

import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.User;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Clickable {

	private ListItem item;

	public Clickable(ListItem item) {
		this.item = item;
	}

	//
	boolean isClickable; // kyousei
	public Clickable clickable() {
		isClickable = true;
		return this;
	}

	public void enable(TextView tvPost) {
		enable(tvPost, null);
	}

	public void enable(TextView tvPost, URLEntity[] urlEntities) {
		tvPost.setText(tvPost.getText(), TextView.BufferType.SPANNABLE);



		Spannable spannable = (Spannable)tvPost.getText();
		String text = tvPost.getText().toString();

		// id����(@hoge)
		if (text.indexOf("@") >= 0) { // ���x�΍�H
		Pattern patternId = Pattern.compile("(@[^\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}\\s:;()@]+)");
		Matcher matcherId = patternId.matcher(text);

		while(matcherId.find()){
			spannable.setSpan(new CSpan(matcherId.group(1)), matcherId.start(1), matcherId.end(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		}

		// �n�b�V���^�O����(#hoge)
////		Pattern patternHashtag = Pattern.compile("(#[0-9a-zA-Z]+)");
////		Pattern patternHashtag = Pattern.compile("#(+)?\\s");
//		Pattern patternHashtag = Pattern.compile("(#[^\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}\\s:;()@]+)");
////		Pattern patternHashtag = Pattern.compile("(#.+?\\s)");
//		Matcher matcherHashtag = patternHashtag.matcher(text);
//
//		while(matcherHashtag.find()){
//			spannable.setSpan(new CSpan(matcherHashtag.group(1)), matcherHashtag.start(1), matcherHashtag.end(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}


		ArrayList<String> hashList = BaseActivity.getHashList(text);
		if (hashList != null) {
		for (int i=0; i<hashList.size(); i++) {
			String hash = hashList.get(i).toString();

//			//escape
//			String[] esc = {"\\", "*", "+", ".", "?", "{", "}", "(", ")", "[", "]", "^", "$", "-", "|"};
//			for (int k=0; k<esc.length; k++) {
//				hash = hash.replace(esc[k], "\\" + esc[k]);
//			}

			hash = BaseActivity.regexEscape(hash);

			//
			Pattern patternHashtag = Pattern.compile("(" + hash + ")");
			Matcher matcherHashtag = patternHashtag.matcher(text);

			while(matcherHashtag.find()){
				spannable.setSpan(new CSpan(matcherHashtag.group(1)), matcherHashtag.start(1), matcherHashtag.end(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		}







		// URL����(http)
//		{
////		Pattern patternWeb = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+");//, Pattern.CASE_INSENSITIVE);
////		Pattern patternWeb = Pattern.compile("(http|https):([^\\x00-\\x20()\"<>\\x7F-\\xFF])*", Pattern.CASE_INSENSITIVE);
////		Pattern patternWeb = Pattern.compile("(http[://\\.0-9a-zA-Z]+)");
////		Pattern patternWeb = Pattern.compile("(http|https)([://\\.0-9a-zA-Z-,?=&%_\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+)");
//		Pattern patternWeb = Pattern.compile("(http|https)://([\\.0-9a-zA-Z-,?=&%_\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+)");
//		Matcher matcherWeb = patternWeb.matcher(text);
//
//		while(matcherWeb.find()){
//			spannable.setSpan(new CSpan(matcherWeb.group(1) + "://" + matcherWeb.group(2)), matcherWeb.start(1), matcherWeb.end(2), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//		}

//		if (urlEntities != null) {
//			for (int i=0; i<urlEntities.length; i++) {
//				String url = urlEntities[i].getURL().toString();
//
//				Pattern patternWeb = Pattern.compile("(" + url + ")");
//				Matcher matcherWeb = patternWeb.matcher(text);
//
//				while(matcherWeb.find()){
//					spannable.setSpan(new CSpan(matcherWeb.group(1)), matcherWeb.start(1), matcherWeb.end(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//				}
//			}
//		}

		ArrayList<String> urlList = BaseActivity.getUrlList(text);
		if (urlList != null) {
		for (int i=0; i<urlList.size(); i++) {
			String url = urlList.get(i).toString();

//			//escape
//			url = BaseActivity.regexEscape(url);
//			//
//			Pattern patternWeb = Pattern.compile("(" + url + ")");
//			Matcher matcherWeb = patternWeb.matcher(text);
//
//			while(matcherWeb.find()){
//				spannable.setSpan(new CSpan(matcherWeb.group(1)), matcherWeb.start(1), matcherWeb.end(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			}

			//
			int start = text.indexOf(url);
			int end   = start + url.length();
			spannable.setSpan(new CSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
	}


		// Reply����
		if (item != null && item.inReplyToStatusId > 0) {
			if (!(tvPost.getContext() instanceof TwReplyActivity)) {
				String keyRep = BaseActivity.stringReply();
				if (text.indexOf(keyRep) >= 0) { // ���x�΍�H
					String url = keyRep;

					// last one
					int start = text.lastIndexOf(url);
					int end   = start + url.length();
		//			int end   = text.lastIndexOf(url);
		//			int start = end - url.length();

					spannable.setSpan(new CSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}


		if (!BaseActivity.fast || isClickable) tvPost.setMovementMethod(LinkMovementMethod.getInstance()); // TODO: ����x���I
	}





	class CSpan5 extends URLSpan {

		public CSpan5(String group) {
			// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
			super(group);


		}

		@Override
		public void onClick(View v){

		}
	}

	class CSpan4 extends UnderlineSpan {

		public CSpan4(String group) {
			// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
			super();
		}

	}

	class CSpan3 extends BackgroundColorSpan{

		public CSpan3(String str) {
			this(Color.BLUE);
		}

		public CSpan3(int color) {
			super(color);
			// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		}

	}

	class CSpan extends ClickableSpan{
		String url;
		private AlertDialog alertDialog;
		private boolean isFriend;
		private boolean isBlock;

		public CSpan(String url) {
			super();
			this.url = url;
		}

		@Override
		public void onClick(View v){
			// TODO:ID���N���b�N���ꂽ�ꍇ�̏����������ɏ���
			// TODO:�n�b�V���^�O���N���b�N���ꂽ�ꍇ�̏����������ɏ���


//			TextView tv = (TextView)v;

			Log.d("", "onClick----------------- url " + url);


			if (url.startsWith("#")) {
				// �������ʉ�ʂ�

//				QueryTab2Activity.setQuery(url);
//
//                TestActivity.setCurrentTab(TabSearchActivity.tabNo);
//                TabSearchActivity.setCurrentTab(1);
//
//                QueryTab2Activity.repaint();


                TwQueryResultActivity.setQuery(url);
        		Intent intent = new Intent(v.getRootView().getContext(), TwQueryResultActivity.class);
        		v.getRootView().getContext().startActivity(intent);
			}
			else if(url.startsWith("@")) {
//				// ���e��ʂ�
//				Tab1Activity.setText(url);
//                TestActivity.setCurrentTab(0);

//				((BaseActivity)v.getContext()).showUserInfo(url, item);
				BaseActivity.showUserInfoStatic((BaseActivity)v.getContext(), url, item);


//				//////////////////////////////////////////////////////////////////////////////////////////
//		        final String screenName = url.substring((url.startsWith("@"))?1:0, url.length());
//
//		        Twitter twitter = BaseActivity.getTwitter(); // �F�؊����؂�Ă��痎����
//
////		        Bitmap image = null;
//		        String description = null;
//		        int followersCount = 0;
//		        int friendsCount = 0;
//		        String location = null;
//		        String name = null;
//		        URL userUrl = null;
//
//				//
//		        try {
//		            // ScreenName�������āAUser�̃f�[�^���擾�B
//		            User user = twitter.showUser(screenName);
//		            // ScreenName�������āAUser��Profile�摜��URL���擾�B
//		            URL imageURL = user.getProfileImageURL();
//
//		            //
//		            description = user.getDescription();
//		            followersCount = user.getFollowersCount();
//		            friendsCount = user.getFriendsCount();
//		            location = user.getLocation();
//		            name = user.getName();
////		            screenName = user.getScreenName();
//		            userUrl = user.getURL();
//
//		            /* �A�N�Z�X���ԑ΍�
//		            try {
//		                // BitmapFactory.decodeStream�Ńr�b�g�}�b�v���쐬�B
//		            	image = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
//		            } catch (Exception e) {
//		            }
//		            */
//
//
//
//
//
//			        //���C�A�E�g�̌Ăяo��
//			        LayoutInflater factory = LayoutInflater.from(v.getContext());
//			        final View inputView = factory.inflate(R.layout.dialog_custom, null);
//
//			        //
//			        TextView textView = (TextView)inputView.findViewById(R.id.TextView01);
//			        textView.setText("Friends : " + friendsCount + "     " + "Followers : " + followersCount + "\n\n" + description + "\n\n" + "Location : " + location);
//			        new Clickable().enable(textView);
//
//			        //
//			        long myId = twitter.getId();
//
//
//
//
//			        ////////////////////////////
//			        // Fllow
//			        ///////////////////////////
//if (false) {
//			        //
//			        Button btnFollow = (Button)inputView.findViewById(R.id.BtnFollow);
//
//			        //TODO:�t�H�����[�����@����ł����̂��H�t�H�����[���������璲�ׂ�̂��H
////			        IDs ids = twitter.getFollowersIDs(screenName);
////			        int[] idList = ids.getIDs();
////			        for(int i=0; i<idList.length; i++) {
////			        	if (idList[i] == myId) {
////			        		isFriend = true;
////			        		break;
////			        	}
////			        }
//			        isFriend = twitter.existsFriendship(screenName, twitter.getScreenName());
//
//			        btnFollow.setText(isFriend?"Unfollow":"Follow");
//
//			        //
//			        btnFollow.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							// Follow
//							Twitter twitter = BaseActivity.getTwitter(); // �F�؊����؂�Ă��痎����
//
//							try {
//								if (!isFriend) {
//									twitter.createFriendship(screenName);
//									isFriend = true; // �ێ����Ă���ۂ��̂ŕύX
//								}
//								else {
//									twitter.destroyFriendship(screenName);
//									isFriend = false; // �ێ����Ă���ۂ��̂ŕύX
//								}
//						    	Toast.makeText(v.getRootView().getContext(), "success.", Toast.LENGTH_SHORT).show();
//							} catch (TwitterException e) {
//						    	Toast.makeText(v.getRootView().getContext(), "failed." + e, Toast.LENGTH_SHORT).show();
//							}
//
//							alertDialog.dismiss();
//						}
//			        });
//}
//
//
//			        ////////////////////////////
//			        // Block
//			        ///////////////////////////
//if (false) {
//			        //
//			        Button btnBlock = (Button)inputView.findViewById(R.id.BtnBlock);
//
//			        //
//		        	isBlock = twitter.existsBlock(screenName);
//
//			        btnBlock.setText(isBlock?"Unblock":"Block");
//
//			        //
//			        btnBlock.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							// Follow
//							Twitter twitter = BaseActivity.getTwitter(); // �F�؊����؂�Ă��痎����
//
//							try {
//								if (!isBlock) {
//									twitter.createBlock(screenName);
//									isBlock = true; // �ێ����Ă���ۂ��̂ŕύX
//								}
//								else {
//									twitter.destroyBlock(screenName);
//									isBlock = false; // �ێ����Ă���ۂ��̂ŕύX
//								}
//						    	Toast.makeText(v.getRootView().getContext(), "success.", Toast.LENGTH_SHORT).show();
//							} catch (TwitterException e) {
//						    	Toast.makeText(v.getRootView().getContext(), "failed." + e, Toast.LENGTH_SHORT).show();
//							}
//
//							alertDialog.dismiss();
//						}
//			        });
//}
//
//
//
//
//			        //
//			        Button btnTweet = (Button)inputView.findViewById(R.id.BtnTweet);
//			        btnTweet.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							// tweet
//							alertDialog.dismiss();
//
////					        TimelineTab2Activity.screenName = screenName;
////					        TestActivity.setCurrentTab(5);
////					        TimelineTab2Activity.repaint();
//
////							TimelineTab2Activity.screenName = screenName;
////			                TestActivity.setCurrentTab(TabTimelineActivity.tabNo);
////			                TabTimelineActivity.setCurrentTab(1);
////			                TimelineTab2Activity.repaint();
//
//
//
//
////							if (BaseActivity.getCurrentTabNo() == TabTimelineActivity.tabNo) {
////								TimelineTab2Activity.screenName = screenName;
////				                TestActivity.setCurrentTab(TabTimelineActivity.tabNo);
////				                TabTimelineActivity.setCurrentTab(5);
////				                TimelineTab2Activity.repaint();
////							}
////							else if (BaseActivity.getCurrentTabNo() == TabSearchActivity.tabNo) {
////								QueryTab3Activity.screenName = screenName;
////				                TestActivity.setCurrentTab(TabSearchActivity.tabNo);
////				                TabSearchActivity.setCurrentTab(2);
////				                QueryTab3Activity.repaint();
////							}
//
//
////							if (BaseActivity.getCurrentTabNo() == TabTimelineActivity.tabNo) {
////								TimelineTab2Activity.screenName = screenName;
////				                TestActivity.setCurrentTab(TabTimelineActivity.tabNo);
////				                TabTimelineActivity.setCurrentTab(1);
////				                TimelineTab2Activity.repaint();
////							}
////							else if (BaseActivity.getCurrentTabNo() == TabSearchActivity.tabNo) {
////								QueryTab3Activity.screenName = screenName;
////				                TestActivity.setCurrentTab(TabSearchActivity.tabNo);
////				                TabSearchActivity.checkAndAddTabView();
////				                TabSearchActivity.setCurrentTab(2);
////				                QueryTab3Activity.repaint();
////							}
//
//
//							//
//							TimelineTab2Activity.screenName = screenName;
//							Intent intent = new Intent(v.getRootView().getContext(), TimelineTab2Activity.class);
//							v.getRootView().getContext().startActivity(intent);
//
//
//						}
//			        });
//
//			        //
//			        Button btnReplay = (Button)inputView.findViewById(R.id.BtnReplay);
//			        btnReplay.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							// ���e��ʂ�
////							TabTweetActivity.setText("@"+screenName);
////			                TestActivity.setCurrentTab(TabTweetActivity.tabNo);
//
//							TabTweetActivity.setText("@"+screenName);
//							Intent intent = new Intent(v.getRootView().getContext(), TabTweetActivity.class);
//							v.getRootView().getContext().startActivity(intent);
//
//			                alertDialog.dismiss();
//						}
//			        });
//
//
//
//			        //�_�C�A���O�̍쐬(AlertDialog.Builder)
////			        new AlertDialog.Builder(v.getContext())
//			        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
//			        alertDialog = alertDialogBuilder
////			            .setIcon(android.R.drawable.)
//			            .setTitle(name + " @" + screenName)
//			            .setView(inputView)
////			            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
////			                public void onClick(DialogInterface dialog, int whichButton) {
////
////			                    /* int whichButton �ɂ��A�����ꂽ�{�^���𔻒� */
////			                    /* ��t���� �F���͂��ꂽ�e�L�X�g�̏����Ȃ� */
////			                }
////			            })
//			            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//			                public void onClick(DialogInterface dialog, int whichButton) {
//			                    /* �L�����Z������ */
//			                }
//			            })
//			            .create();
//			        alertDialog.show();
//
//
//
//		        } catch (Exception e) {
//		            e.printStackTrace();
//		        }
//				//////////////////////////////////////////////////////////////////////////////////////////



			}
			else if(url.startsWith("http")) {
				// Web��
//				Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
//				v.getRootView().getContext().startActivity(intent);

				if (url.indexOf("twitter") >= 0) {
					BaseActivity.intentWebNormal(v.getRootView().getContext(), url);
				}
				else {
					BaseActivity.intentWeb(v.getRootView().getContext(), url);
				}
			}
			else if(url.startsWith(BaseActivity.stringReply())) {
	        	TwReplyActivity.InReplyToStatusId = item.id;
	        	BaseActivity.intent(v.getRootView().getContext(), TwReplyActivity.class);
			}
		}
	}

}