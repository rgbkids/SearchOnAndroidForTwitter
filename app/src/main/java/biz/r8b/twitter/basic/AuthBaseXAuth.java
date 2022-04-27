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
//    // OK繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻ｼ縺ｳ蜃ｺ縺輔ｌ繧?//    public void onOKButton(View view) {
//        // 蜈･蜉帙＆繧後◆繝ｦ繝ｼ繧ｶID縺ｨ繝代せ繝ｯ繝ｼ繝峨ｒ蜿門ｾ?//        String userID =
//          ((EditText)findViewById(R.id.user_id)).getText().toString();
//        String password =
//         ((EditText)findViewById(R.id.password)).getText().toString();
//        // 縺ｨ繧翫≠縺医★繝ｦ繝ｼ繧ｶID縺ｨ繝代せ繝ｯ繝ｼ繝峨〒Twitter繧､繝ｳ繧ｹ繧ｿ繝ｳ繧ｹ繧堤函謌?//        Twitter twitter =
//            new TwitterFactory().getInstance(userID, password);
//        try {
//            // AccessToken繧貞叙蠕?//            AccessToken accessToken = twitter.getOAuthAccessToken();
//            // token縺ｨtokenSecret繧貞叙蠕?//            String token = accessToken.getToken();
//            String tokenSecret = accessToken.getTokenSecret();
//            // 繝励Μ繝輔ぃ繝ｬ繝ｳ繧ｹ縺ｮEditor繧貞叙蠕?//            Editor e = getSharedPreferences(
//                    TabTweetActivity.PREF_NAME, MODE_PRIVATE).edit();
//            // token縺ｨtokenSecret繧呈嶌縺崎ｾｼ繧薙〒
//            e.putString(TabTweetActivity.PREF_KEY_TOKEN, token);
//            e.putString(TabTweetActivity.PREF_KEY_TOKEN_SECRET, tokenSecret);
//            // 菫晏ｭ假ｼ?//            e.commit();
//
//            //
//            Intent intent = new Intent(this, TestActivity.class);
//            startActivity(intent);
//
//            // Auth繧｢繧ｯ繝?ぅ繝薙ユ繧｣繧堤ｵゆｺ?//            finish();
//
//        } catch (TwitterException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),
//                    "繝ｦ繝ｼ繧ｶID縺九ヱ繧ｹ繝ｯ繝ｼ繝峨′髢馴＆縺｣縺ｦ縺?∪縺吶?",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
}
