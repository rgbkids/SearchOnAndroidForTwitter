package biz.r8b.twitter.basic;

//import twitter4j.Twitter;
//import twitter4j.TwitterException;
//import twitter4j.TwitterFactory;
//import twitter4j.http.AccessToken;
import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences.Editor;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;

public class AuthBaseXAuth extends Activity {
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.auth_x);
//
//        //
//        BaseActivity.setTwitterAPIKey();
//    }
//
//    // OKボタンが押されたら呼び出され�?//    public void onOKButton(View view) {
//        // 入力されたユーザIDとパスワードを取�?//        String userID =
//          ((EditText)findViewById(R.id.user_id)).getText().toString();
//        String password =
//         ((EditText)findViewById(R.id.password)).getText().toString();
//        // とりあえずユーザIDとパスワードでTwitterインスタンスを生�?//        Twitter twitter =
//            new TwitterFactory().getInstance(userID, password);
//        try {
//            // AccessTokenを取�?//            AccessToken accessToken = twitter.getOAuthAccessToken();
//            // tokenとtokenSecretを取�?//            String token = accessToken.getToken();
//            String tokenSecret = accessToken.getTokenSecret();
//            // プリファレンスのEditorを取�?//            Editor e = getSharedPreferences(
//                    TabTweetActivity.PREF_NAME, MODE_PRIVATE).edit();
//            // tokenとtokenSecretを書き込んで
//            e.putString(TabTweetActivity.PREF_KEY_TOKEN, token);
//            e.putString(TabTweetActivity.PREF_KEY_TOKEN_SECRET, tokenSecret);
//            // 保存�?//            e.commit();
//
//            //
//            Intent intent = new Intent(this, TestActivity.class);
//            startActivity(intent);
//
//            // Authアク�?��ビティを終�?//            finish();
//
//        } catch (TwitterException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),
//                    "ユーザIDかパスワードが間違って�?��す�?",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
}
