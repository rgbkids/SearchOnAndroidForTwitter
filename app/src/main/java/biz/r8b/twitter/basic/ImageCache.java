package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;

public class ImageCache {
    private static HashMap<String,Bitmap> cache = new HashMap<String,Bitmap>();

    public static Bitmap getImage(String key) {
        if (cache.containsKey(key)) {
            Log.d("cache", "cache hit!");
            return cache.get(key);
        }
        return null;
    }

    public static void setImage(String key, Bitmap image) {
        cache.put(key, image);
    }




    //
    public static void clear() {
    	cache.clear();

    	if (BaseActivity.getMem() > 50) {
    		System.gc();
    	}
    }

    //
    public static void clear(boolean recycle) {
    	if (recycle) {
	    	for (Map.Entry<String, Bitmap> entry : cache.entrySet()) {
	    		  String key = entry.getKey();
	    		  Bitmap value = entry.getValue();

	    		  try {
	    			  value.recycle();
	    		  } catch (Exception e) {}
	    	}
    	}

    	clear();
    }

    // キャッシュ使わないときは呼ばないこと！
    public static boolean loadThreadState;
    static Thread th;
    public static void loadImages(final ArrayList<String> profileImageURLs) {

    	//
    	if (!BaseActivity.imageOn) { // only image on
    		return;
    	}

    	//
    	while (th != null && th.isAlive()) {
    		loadThreadState = false;
    		try {
//    			th.destroy();

    			Thread.yield();
    			Thread.sleep(100);
    		} catch (Exception e) {}
    	}

    	//
    	loadThreadState = true;

    	th = new Thread(new Runnable() {
			@Override
			public void run() {
		    	try {
		    		for (int i=0; i<profileImageURLs.size(); i++) {
		    	        Bitmap image = getImage(profileImageURLs.get(i));

		    	        if (image == null) {
		    				image = HttpImage.getBitmap(profileImageURLs.get(i));
		    				setImage(profileImageURLs.get(i), image);
		    	        }

		    	        if (loadThreadState) {
		    	        	Thread.sleep(100);
		    	        }
		    	        else {
		    	        	break;
		    	        }
		    		}
		    	}
		    	catch (Throwable t) {
		    	}
			}
    	});
    	th.start();
    }
}