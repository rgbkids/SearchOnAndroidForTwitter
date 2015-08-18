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

        // ここは別途OAuth認証して情報を取得する。。。
//        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        String oAuthConsumerKey 		= TWITTER_CONSUMER_KEY;
//        String oAuthConsumerSecret 		= TWITTER_CONSUMER_SECRET;
//        String oAuthAccessToken 		= sp.getString(PREF_KEY_TOKEN, "");
//        String oAuthAccessTokenSecret 	= sp.getString(PREF_KEY_TOKEN_SECRET, "");

        // Twitter4Jに対してOAuth情報を設定
        ConfigurationBuilder builder = new ConfigurationBuilder();
        {
            // アプリ固有の情報
            builder.setOAuthConsumerKey(oAuthConsumerKey);
            builder.setOAuthConsumerSecret(oAuthConsumerSecret);
            // アプリ＋ユーザー固有の情報
            builder.setOAuthAccessToken(oAuthAccessToken);
            builder.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        }

        // 1. TwitterStreamFactory をインスタンス化する
        Configuration conf = builder.build();
        TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(conf);
        // 2. TwitterStream をインスタンス化する
        TwitterStream twitterStream = twitterStreamFactory.getInstance();

        // ユーザーストリーム操作
        {
            // 4. TwitterStream に UserStreamListener を実装したインスタンスを設定する
            twitterStream.addListener(mMyUserStreamAdapter);
            // 5. TwitterStream#user() を呼び出し、ユーザーストリームを開始する
            twitterStream.user();
        }
    }

    // 3. UserStream 受信時に応答する（UserStreamListener）リスナーを実装する
    class MyUserStreamAdapter extends UserStreamAdapter {

        // 新しいツイート（ステータス）を取得する度に呼び出される
        @Override
        public void onStatus(Status status) {
            super.onStatus(status);
            // 6. UserStream 受信時、3 で実装したメソッドが呼び出されるので必要な処理をする
            // サンプルログ出力
            Log.v("Twitter4JUserStreamActivity", status.getText());
            // ここではサンプルとして通知発行メソッドを呼び出している
//            Twitter4JUserStream.notify(Twitter4JUserStream.this,
//                    status.getId(), status.getText(),
//                    status.getUser().getId(), status.getUser().getScreenName());

            //
            ustIF.onStatus(status);
        }
    }
}
