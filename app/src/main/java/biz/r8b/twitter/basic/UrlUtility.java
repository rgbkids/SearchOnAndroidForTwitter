package biz.r8b.twitter.basic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public abstract class UrlUtility {

	//
	public static URL expandUrl(URL aUrl) throws IOException, ProtocolException {
        int size;
        byte[] w=new byte[1024];
        HttpURLConnection c=null;
        InputStream in=null;
        ByteArrayOutputStream out=null;

        try {
            //HTTP接続のオープン(2)
//            URL url=new URL(path);
            URL url=aUrl;

            c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.connect();

            //
            HttpURLConnection tHttpURLConnection = c;
            String tExpandedUrl = null;

            final String tLocation = tHttpURLConnection.getHeaderField("Location");
    		if (tLocation != null && tLocation.startsWith("http")) {
    			final int tResponseCode = tHttpURLConnection.getResponseCode();
    			if (tResponseCode == HttpURLConnection.HTTP_MOVED_PERM || tResponseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
//    				return expandUrl(new URL(encode(tLocation))).toExternalForm();
//    				return new URL(expandUrl(new URL(encode(tLocation))).toExternalForm());
    				return new URL((new URL(encode(tLocation))).toExternalForm());
    			}
    			tExpandedUrl = tLocation;
    		} else {
    			tExpandedUrl = tHttpURLConnection.getURL().toExternalForm();
    		}



            //
//            in=c.getInputStream();
//
//            //バイト配列の読み込み
//            out=new ByteArrayOutputStream();
//            while (true) {
//                size=in.read(w);
//                if (size<=0) break;
//                out.write(w,0,size);
//            }
//            out.close();
//
//            //HTTP接続のクローズ(3)
//            in.close();



            c.disconnect();


//            return out.toByteArray();
            return new URL(tExpandedUrl);


        } catch (Exception e) {
            try {
                if (c!=null) c.disconnect();
                if (in!=null) in.close();
                if (out!=null) out.close();
            } catch (Exception e2) {
            }
//            throw e;
        }
//        return null;

        return aUrl;
	}









	//
	public static URL expandUrl_org(URL aUrl) throws IOException, ProtocolException {
		final URLConnection tURLConnection = aUrl.openConnection(Proxy.NO_PROXY);
		if (!(tURLConnection instanceof HttpURLConnection)) {
			return aUrl;
		}
		final HttpURLConnection tHttpURLConnection = (HttpURLConnection) tURLConnection;
		tHttpURLConnection.setRequestMethod("HEAD");
		tHttpURLConnection.setInstanceFollowRedirects(false);
		tHttpURLConnection.connect();

		final String tExpandedUrl;
		final String tLocation = tHttpURLConnection.getHeaderField("Location");
		if (tLocation != null && tLocation.startsWith("http")) {
			final int tResponseCode = tHttpURLConnection.getResponseCode();
			if (tResponseCode == HttpURLConnection.HTTP_MOVED_PERM || tResponseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
//				return expandUrl(new URL(encode(tLocation))).toExternalForm();
//				return new URL(expandUrl(new URL(encode(tLocation))).toExternalForm());
				return new URL((new URL(encode(tLocation))).toExternalForm());
			}
			tExpandedUrl = tLocation;
		} else {
			tExpandedUrl = tHttpURLConnection.getURL().toExternalForm();
		}

		return new URL(encode(tExpandedUrl));
	}





	// @formatter:off
	private static final String[] HEX = {
		"80","81","82","83","84","85","86","87","88","89","8A","8B","8C","8D","8E","8F",
		"90","91","92","93","94","95","96","97","98","99","9A","9B","9C","9D","9E","9F",
		"A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","AA","AB","AC","AD","AE","AF",
		"B0","B1","B2","B3","B4","B5","B6","B7","B8","B9","BA","BB","BC","BD","BE","BF",
		"C0","C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC","CD","CE","CF",
		"D0","D1","D2","D3","D4","D5","D6","D7","D8","D9","DA","DB","DC","DD","DE","DF",
		"E0","E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF",
		"F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","FA","FB","FC","FD","FE","FF",
		"00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F",
		"10","11","12","13","14","15","16","17","18","19","1A","1B","1C","1D","1E","1F",
		"20","21","22","23","24","25","26","27","28","29","2A","2B","2C","2D","2E","2F",
		"30","31","32","33","34","35","36","37","38","39","3A","3B","3C","3D","3E","3F",
		"40","41","42","43","44","45","46","47","48","49","4A","4B","4C","4D","4E","4F",
		"50","51","52","53","54","55","56","57","58","59","5A","5B","5C","5D","5E","5F",
		"60","61","62","63","64","65","66","67","68","69","6A","6B","6C","6D","6E","6F",
		"70","71","72","73","74","75","76","77","78","79","7A","7B","7C","7D","7E","7F",
	};
	// @formatter:on

	private static String encode(String aUrl) throws UnsupportedEncodingException {
		final byte[] tBytes = aUrl.getBytes("ISO-8859-1");
		final int tLength = tBytes.length;
		final StringBuilder tBuilder = new StringBuilder(tLength * 3);
		for (int tIndex = 0; tIndex < tLength; tIndex++) {
			final int tIntAt = (int) tBytes[tIndex];
			if (tIntAt < 0) {
				tBuilder.append('%');
				tBuilder.append(HEX[tIntAt + 128]);
			} else {
				tBuilder.append((char) tIntAt);
			}
		}
		return tBuilder.toString();
	}
}