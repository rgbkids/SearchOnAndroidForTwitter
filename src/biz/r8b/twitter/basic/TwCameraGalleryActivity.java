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

public class TwCameraGalleryActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        System.gc();

        // GALLERY
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_GALLERY);
    }

	//
	private static final int REQUEST_GALLERY = 0x00000001;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == REQUEST_GALLERY  && resultCode == RESULT_OK) {
			System.gc();

			try {
				InputStream in = getContentResolver().openInputStream(data.getData());
				Bitmap img = BitmapFactory.decodeStream(in);
				in.close();

				// ?½I?½?½?½?½?½?½?½æ‘œï¿½?½\?½?½
//				imgView.setImageBitmap(img);

				//
				TwCameraActivity.Picture = img;

			} catch (Exception e) {
			}

			//
			TwCameraActivity.Mode = TwCameraActivity.MODE_RESULT;

			//
			Intent intent=new Intent(this, biz.r8b.twitter.basic.TwCameraActivity.class);
			startActivity(intent);
			finish();
		}
	}
	@Override
	public String getDispTitle() {
		// TODO ?½?½?½?½?½?½?½?½?½?½?½ê‚½?½?½?½\?½b?½h?½E?½X?½^?½u
		return null;
	}

}