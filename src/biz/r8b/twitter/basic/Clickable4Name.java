package biz.r8b.twitter.basic;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
//import twitter4j.http.AccessToken;
//import twitter4j.http.Authorization;
//import twitter4j.http.OAuthAuthorization;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Clickable4Name {

	private ListItem item;

	public Clickable4Name(ListItem item) {
		this.item = item;
	}

	public void enable(TextView tvPost) {
		tvPost.setText(tvPost.getText(), TextView.BufferType.SPANNABLE);



		Spannable spannable = (Spannable)tvPost.getText();
		String text = tvPost.getText().toString();


		if (text.indexOf("@") >= 0) { // ���x�΍�H
		// name
//		Pattern patternId = Pattern.compile("([^\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}\\s:;()@]+)");
//		Pattern patternId = Pattern.compile("([^\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}:;()@]+)?\\s");
//		Pattern patternId = Pattern.compile("@([^\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}:;()]+)?\\s*");
//		Pattern patternId = Pattern.compile("(@[^\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}:;()]+)?\\s*");
		Pattern patternId = Pattern.compile("(@[^\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}\\s:;()@]+)");

		Matcher matcherId = patternId.matcher(text);

		while(matcherId.find()){
			spannable.setSpan(new CSpan(matcherId.group(1)), matcherId.start(1), matcherId.end(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		}

		if (!BaseActivity.fast) tvPost.setMovementMethod(LinkMovementMethod.getInstance());
	}

	class CSpan extends ClickableSpan{
		String url;
		private boolean isFriend;
		private Dialog alertDialog;
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



//			((BaseActivity)v.getContext()).showUserInfo(url, item);
			BaseActivity.showUserInfoStatic((BaseActivity)v.getContext(), url, item);


//			//////////////////////////////////////////////////////////////////////////////////////////
//	        final String screenName = url.substring((url.startsWith("@"))?1:0, url.length());
//
//	        Twitter twitter = BaseActivity.getTwitter(); // �F�؊����؂�Ă��痎����
//
////	        Bitmap image = null;
//	        String description = null;
//	        int followersCount = 0;
//	        int friendsCount = 0;
//	        String location = null;
//	        String name = null;
//	        URL userUrl = null;
//
//			//
//	        try {
//	            // ScreenName�������āAUser�̃f�[�^���擾�B
//	            User user = twitter.showUser(screenName);
//	            // ScreenName�������āAUser��Profile�摜��URL���擾�B
//	            URL imageURL = user.getProfileImageURL();
//
//	            //
//	            description = user.getDescription();
//	            followersCount = user.getFollowersCount();
//	            friendsCount = user.getFriendsCount();
//	            location = user.getLocation();
//	            name = user.getName();
////	            screenName = user.getScreenName();
//	            userUrl = user.getURL();
//
//	            /* �A�N�Z�X���ԑ΍�
//	            try {
//	                // BitmapFactory.decodeStream�Ńr�b�g�}�b�v���쐬�B
//	            	image = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
//	            } catch (Exception e) {
//	            }
//	            */
//
//
//
//
//
//		        //���C�A�E�g�̌Ăяo��
//		        LayoutInflater factory = LayoutInflater.from(v.getContext());
//		        final View inputView = factory.inflate(R.layout.dialog_custom, null);
//
//		        //
//		        TextView textView = (TextView)inputView.findViewById(R.id.TextView01);
//		        textView.setText("Friends : " + friendsCount + "     " + "Followers : " + followersCount + "\n\n" + description + "\n\n" + "Location : " + location);
//		        new Clickable().enable(textView);
//
//		        //
//		        long myId = twitter.getId();
//
//		        ////////////////////////////
//		        // Fllow
//		        ///////////////////////////
//if (false) {
//		        //
//		        Button btnFollow = (Button)inputView.findViewById(R.id.BtnFollow);
//
//		        //TODO:�t�H�����[�����@����ł����̂��H�t�H�����[���������璲�ׂ�̂��H
////		        IDs ids = twitter.getFollowersIDs(screenName);
////		        int[] idList = ids.getIDs();
////		        for(int i=0; i<idList.length; i++) {
////		        	if (idList[i] == myId) {
////		        		isFriend = true;
////		        		break;
////		        	}
////		        }
//		        isFriend = twitter.existsFriendship(screenName, twitter.getScreenName());
//
//		        btnFollow.setText(isFriend?"Unfollow":"Follow");
//
//		        //
//		        btnFollow.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// Follow
//						Twitter twitter = BaseActivity.getTwitter(); // �F�؊����؂�Ă��痎����
//
//						try {
//							if (!isFriend) {
//								twitter.createFriendship(screenName);
//								isFriend = true; // �ێ����Ă���ۂ��̂ŕύX
//							}
//							else {
//								twitter.destroyFriendship(screenName);
//								isFriend = false; // �ێ����Ă���ۂ��̂ŕύX
//							}
//					    	Toast.makeText(v.getRootView().getContext(), "success.", Toast.LENGTH_SHORT).show();
//						} catch (TwitterException e) {
//					    	Toast.makeText(v.getRootView().getContext(), "failed." + e, Toast.LENGTH_SHORT).show();
//						}
//
//						alertDialog.dismiss();
//					}
//		        });
//}
//
//
//		        ////////////////////////////
//		        // Block
//		        ///////////////////////////
//
//if(false) {
//  		        //
//
//		        Button btnBlock = (Button)inputView.findViewById(R.id.BtnBlock);
//
//		        //
//	        	isBlock = twitter.existsBlock(screenName);
//
//		        btnBlock.setText(isBlock?"Unblock":"Block");
//
//		        //
//		        btnBlock.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// Follow
//						Twitter twitter = BaseActivity.getTwitter(); // �F�؊����؂�Ă��痎����
//
//						try {
//							if (!isBlock) {
//								twitter.createBlock(screenName);
//								isBlock = true; // �ێ����Ă���ۂ��̂ŕύX
//							}
//							else {
//								twitter.destroyBlock(screenName);
//								isBlock = false; // �ێ����Ă���ۂ��̂ŕύX
//							}
//					    	Toast.makeText(v.getRootView().getContext(), "success.", Toast.LENGTH_SHORT).show();
//						} catch (TwitterException e) {
//					    	Toast.makeText(v.getRootView().getContext(), "failed." + e, Toast.LENGTH_SHORT).show();
//						}
//
//						alertDialog.dismiss();
//					}
//		        });
//}
//
//
//
//		        //
//		        Button btnTweet = (Button)inputView.findViewById(R.id.BtnTweet);
//		        btnTweet.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// tweet
//						alertDialog.dismiss();
//
////				        TimelineTab2Activity.screenName = screenName;
////				        TestActivity.setCurrentTab(5);
////				        TimelineTab2Activity.repaint();
//
////						if (BaseActivity.getCurrentTabNo() == TabTimelineActivity.tabNo) {
////							TimelineTab2Activity.screenName = screenName;
////			                TestActivity.setCurrentTab(TabTimelineActivity.tabNo);
////			                TabTimelineActivity.setCurrentTab(1);
////			                TimelineTab2Activity.repaint();
////						}
////						else if (BaseActivity.getCurrentTabNo() == TabSearchActivity.tabNo) {
////							QueryTab3Activity.screenName = screenName;
////			                TestActivity.setCurrentTab(TabSearchActivity.tabNo);
////			                TabSearchActivity.checkAndAddTabView();
////			                TabSearchActivity.setCurrentTab(2);
////			                QueryTab3Activity.repaint();
////						}
//
//
//
//						//
//						TimelineTab2Activity.screenName = screenName;
//						Intent intent = new Intent(v.getRootView().getContext(), TimelineTab2Activity.class);
//						v.getRootView().getContext().startActivity(intent);
//					}
//		        });
//
//		        //
//		        Button btnReplay = (Button)inputView.findViewById(R.id.BtnReplay);
//		        btnReplay.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// ���e��ʂ�
////						TabTweetActivity.setText("@"+screenName);
////		                TestActivity.setCurrentTab(TabTweetActivity.tabNo);
//
//						TabTweetActivity.setText("@"+screenName);
//						Intent intent = new Intent(v.getRootView().getContext(), TabTweetActivity.class);
//						v.getRootView().getContext().startActivity(intent);
//
//		                alertDialog.dismiss();
//					}
//		        });
//
//
//
//		        //�_�C�A���O�̍쐬(AlertDialog.Builder)
////		        new AlertDialog.Builder(v.getContext())
//		        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
//		        alertDialog = alertDialogBuilder
////		            .setIcon(android.R.drawable.)
//		            .setTitle(name + " @" + screenName)
//		            .setView(inputView)
////		            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
////		                public void onClick(DialogInterface dialog, int whichButton) {
////
////		                    /* int whichButton �ɂ��A�����ꂽ�{�^���𔻒� */
////		                    /* ��t���� �F���͂��ꂽ�e�L�X�g�̏����Ȃ� */
////		                }
////		            })
//		            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//		                public void onClick(DialogInterface dialog, int whichButton) {
//		                    /* �L�����Z������ */
//		                }
//		            })
//		            .create();
//		        alertDialog.show();
//
//
//
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//			//////////////////////////////////////////////////////////////////////////////////////////










//			/////////////
//	        Twitter twitter = BaseActivity.getTwitter(); // �F�؊����؂�Ă��痎����
//
////	        Bitmap image = null;
//	        String description = null;
//	        int followersCount = 0;
//	        int friendsCount = 0;
//	        String location = null;
//	        String name = null;
//	        String screenName = null;
//	        URL userUrl = null;
//
//			//
//	        try {
//	            // ScreenName�������āAUser�̃f�[�^���擾�B
//	            User user = twitter.showUser(url);
//	            // ScreenName�������āAUser��Profile�摜��URL���擾�B
//	            URL imageURL = user.getProfileImageURL();
//
//	            //
//	            description = user.getDescription();
//	            followersCount = user.getFollowersCount();
//	            friendsCount = user.getFriendsCount();
//	            location = user.getLocation();
//	            name = user.getName();
//	            screenName = user.getScreenName();
//	            userUrl = user.getURL();
//
//	            /* �A�N�Z�X���ԑ΍�
//	            try {
//	                // BitmapFactory.decodeStream�Ńr�b�g�}�b�v���쐬�B
//	            	image = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
//	            } catch (Exception e) {
//	            }
//	            */
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//		        //���C�A�E�g�̌Ăяo��
//		        LayoutInflater factory = LayoutInflater.from(v.getContext());
//		        final View inputView = factory.inflate(R.layout.dialog_custom, null);
//
//		        //
//		        TextView textView = (TextView)inputView.findViewById(R.id.TextView01);
//		        textView.setText("Name : " + name + " @" + screenName + "\n\n" + description + "\n\n" + "Location : " + location + "\n" + "Friends : " + friendsCount + " " + "Followers : " + followersCount);
//
//		        //�_�C�A���O�̍쐬(AlertDialog.Builder)
////		        new AlertDialog.Builder(v.getContext())
//		        new AlertDialog.Builder(v.getRootView().getContext())
//
////		            .setIcon(android.R.drawable.)
//		            .setTitle(screenName)
//		            .setView(inputView)
////		            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
////		                public void onClick(DialogInterface dialog, int whichButton) {
////
////		                    /* int whichButton �ɂ��A�����ꂽ�{�^���𔻒� */
////		                    /* ��t���� �F���͂��ꂽ�e�L�X�g�̏����Ȃ� */
////		                }
////		            })
//		            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//		                public void onClick(DialogInterface dialog, int whichButton) {
//		                    /* �L�����Z������ */
//		                }
//		            })
//		            .create()
//		            .show();
//
//
//
//
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//			/////////////

















			/*
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
	        // �A���[�g�_�C�A���O�̃^�C�g����ݒ肵�܂�
	        alertDialogBuilder.setTitle("�^�C�g��");
	        // �A���[�g�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
	        alertDialogBuilder.setMessage("���b�Z�[�W");
	        // �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
	        alertDialogBuilder.setPositiveButton("�m��",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    }
	                });
	        // �A���[�g�_�C�A���O�̒����{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
	        alertDialogBuilder.setNeutralButton("����",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    }
	                });
	        // �A���[�g�_�C�A���O�̔ے�{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
	        alertDialogBuilder.setNegativeButton("�ے�",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    }
	                });
	        // �A���[�g�_�C�A���O�̃L�����Z�����\���ǂ�����ݒ肵�܂�
	        alertDialogBuilder.setCancelable(true);
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        // �A���[�g�_�C�A���O��\�����܂�
	        alertDialog.show();
*/















//			if (url.startsWith("#")) {
//				// �������ʉ�ʂ�
//
//				QueryTab2Activity.setQuery(url);
//                QueryTab2Activity.repaint();
//
//                TestActivity.setCurrentTab(3);
//                Tab4Activity.setCurrentTab(1);
//			}
//			else if(url.startsWith("@")) {
//				// ���e��ʂ�
//				Tab1Activity.setText(url);
//                TestActivity.setCurrentTab(0);
//			}
//			else if(url.startsWith("http")) {
//				// Web��
//
//			}
		}
	}
}