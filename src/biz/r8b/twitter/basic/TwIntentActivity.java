package biz.r8b.twitter.basic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import biz.r8b.twitter.basic.Clickable.CSpan;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;

//
public class TwIntentActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_boot, null);
        setContentView(view);

//        setSkin(view);

        //---------------------------
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
        	Uri uri = getIntent().getData();
            if(uri != null && uri.toString().startsWith("https://mobile.twitter.com")) {

            }
        }
        else if (Intent.ACTION_SEND.equals(action)) {
          Bundle extras = intent.getExtras();
          if (extras != null) {
            CharSequence ext = extras.getCharSequence(Intent.EXTRA_TEXT);
//              if (ext != null) {
////                editText_.setText(ext);
//              }

            String word = ext.toString();


            String id = null;
            if (word.indexOf("twitter.com/") >= 0) {
//            	Pattern patternId = Pattern.compile("(http://|https://){1}[\\twitter\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+");
            	Pattern patternId = Pattern.compile("twitter\\.com/.+?/status/([0-9]+)");
        		Matcher matcherId = patternId.matcher(word);

        		if (matcherId.find()){
        			id = matcherId.group(1);
//        			String val2 = matcherId.group(2);
//        			System.out.println(""+val1+ "    " + val2);

        			if (id != null) {
        				toast(this, "id=" + id);



        				TwReplyActivity.InReplyToStatusId = Long.parseLong(id);
        				TwReplyActivity.Title = "id=" + Long.parseLong(id);
        				intent(this, TwReplyActivity.class);

        				finish();
        			}
        		}

            }
//            else {
            if (id == null) {
	            TwQueryResultActivity.setQuery(word);
	            intent(this, TwQueryResultActivity.class);
	            finish();
            }
//            }
          }
        }
        //---------------------------

//        settingGestures();
        setHeaderMessage(this);
    }

	@Override
	public String getDispTitle() {
		return null;
	}
}