package biz.r8b.twitter.basic;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class _AppUtil {

	// http://www.adakoda.com/adakoda/2010/08/android-34.html
	public static void updateGarally(Context context, String fileName) {
//		String title = "test";
//		String fileName = title + ".jpg";
		String mimeType = "image/jpeg";

		//
		File file = new File(
		    Environment.getExternalStorageDirectory(),
		    fileName);

		// MediaScannerConnection ?�?�?�g?�p?�?�?�?��?
		MediaScannerConnection.scanFile( // API Level 8
			context, // Context
		    new String[] { file.getPath() },
		    new String[] { mimeType },
		    null);
	}

	//
	public static Bitmap decodeFile(String path) {
		//読み込み用のオプションオブジェクトを生�?
		BitmapFactory.Options options = new BitmapFactory.Options();
		//こ�?値をtrueにすると実際には画像を読み込まず�?
		//画像�?サイズ�??�?��を取得することができます�?
		options.inJustDecodeBounds = true;

		//画像ファイル読み込み
		//ここでは上記�?オプションがtrueのため実際の
		//画像�?読み込まれな�?��す�?
		BitmapFactory.decodeFile(path, options);

		//読み込んだサイズはoptions.outWidthとoptions.outHeightに
		//格納されるので、その値から読み込�?��の縮尺を計算します�?
		//こ�?サンプルではどんな大きさの画像で�?VGAに収まるサイズ�?
		//計算して�?��す�?
		int scaleW = options.outWidth / 380 + 1;
		int scaleH = options.outHeight / 420 + 1;

		//縮尺は整数値で�?なら画像�?縦横のピクセル数�?/2にしたサイズ�?
		//3な�?/3にしたサイズで読み込まれます�?
		int scale = Math.max(scaleW, scaleH);

		//今度は画像を読み込みたいのでfalseを指�?
		options.inJustDecodeBounds = false;

		//先程計算した縮尺値を指�?
		options.inSampleSize = scale;

		//これで�?��した縮尺で画像を読み込めます�?
		//もちろん容量も小さくなる�?で扱�?��すいです�?
		Bitmap image = BitmapFactory.decodeFile(path, options);

		return image;
	}


	// http://d.hatena.ne.jp/wistery_k/20110824/1314145373
	public static Map<String, String> getQueryMap(String query)
    {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
            {
		String[] splitted = param.split("=");
		map.put(splitted[0], splitted[1]);
	    }
        return map;
    }

	//
	public static String getQueryValue(String url, String name) {
		try {
			URL dUrl = new URL("http://" + url);
			Map<String, String> query = getQueryMap(dUrl.getQuery());
			return query.get(name);

			}catch(Exception e){}

		return null;
	}
}