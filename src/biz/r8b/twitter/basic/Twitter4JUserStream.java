package biz.r8b.twitter.basic;

import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class Twitter4JUserStream {
    private MyUserStreamAdapter mMyUserStreamAdapter;
	private Twitter4JUserStreamIF ustIF;

    public Twitter4JUserStream(String oAuthConsumerKey, String oAuthConsumerSecret, String oAuthAccessToken, String oAuthAccessTokenSecret, Twitter4JUserStreamIF ustIF) {

        mMyUserStreamAdapter = new MyUserStreamAdapter();
        this.ustIF = ustIF;

        // �����͕ʓrOAuth�F�؂��ď����擾����B�B�B
//        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        String oAuthConsumerKey 		= TWITTER_CONSUMER_KEY;
//        String oAuthConsumerSecret 		= TWITTER_CONSUMER_SECRET;
//        String oAuthAccessToken 		= sp.getString(PREF_KEY_TOKEN, "");
//        String oAuthAccessTokenSecret 	= sp.getString(PREF_KEY_TOKEN_SECRET, "");

        // Twitter4J�ɑ΂���OAuth����ݒ�
        ConfigurationBuilder builder = new ConfigurationBuilder();
        {
            // �A�v���ŗL�̏��
            builder.setOAuthConsumerKey(oAuthConsumerKey);
            builder.setOAuthConsumerSecret(oAuthConsumerSecret);
            // �A�v���{���[�U�[�ŗL�̏��
            builder.setOAuthAccessToken(oAuthAccessToken);
            builder.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        }

        // 1. TwitterStreamFactory ���C���X�^���X������
        Configuration conf = builder.build();
        TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(conf);
        // 2. TwitterStream ���C���X�^���X������
        TwitterStream twitterStream = twitterStreamFactory.getInstance();

        // ���[�U�[�X�g���[������
        {
            // 4. TwitterStream �� UserStreamListener �����������C���X�^���X��ݒ肷��
            twitterStream.addListener(mMyUserStreamAdapter);
            // 5. TwitterStream#user() ���Ăяo���A���[�U�[�X�g���[�����J�n����
            twitterStream.user();
        }
    }

    // 3. UserStream ��M���ɉ�������iUserStreamListener�j���X�i�[����������
    class MyUserStreamAdapter extends UserStreamAdapter {

        // �V�����c�C�[�g�i�X�e�[�^�X�j���擾����x�ɌĂяo�����
        @Override
        public void onStatus(Status status) {
            super.onStatus(status);
            // 6. UserStream ��M���A3 �Ŏ����������\�b�h���Ăяo�����̂ŕK�v�ȏ���������
            // �T���v�����O�o��
            Log.v("Twitter4JUserStreamActivity", status.getText());
            // �����ł̓T���v���Ƃ��Ēʒm���s���\�b�h���Ăяo���Ă���
//            Twitter4JUserStream.notify(Twitter4JUserStream.this,
//                    status.getId(), status.getText(),
//                    status.getUser().getId(), status.getUser().getScreenName());

            //
            ustIF.onStatus(status);
        }
    }
}
