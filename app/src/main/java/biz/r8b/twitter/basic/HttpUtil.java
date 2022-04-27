package biz.r8b.twitter.basic;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpUtil {






    //HTTP通信→文字列
    static String http2str(String path) throws Exception {
        byte[] w=http2data(path);
        return new String(w);
    }

    //HTTP通信
    public static byte[] http2data(String path) throws Exception
    {
        int size;
        byte[] w=new byte[1024];
        HttpURLConnection c=null;
        InputStream in=null;
        ByteArrayOutputStream out=null;
        try {
            //HTTP接続のオープン(2)
            URL url=new URL(path);
            c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            in=c.getInputStream();

            //バイト配列の読み込み
            out=new ByteArrayOutputStream();
            while (true) {
                size=in.read(w);
                if (size<=0) break;
                out.write(w,0,size);
            }
            out.close();

            //HTTP接続のクローズ(3)
            in.close();
            c.disconnect();
            return out.toByteArray();
        } catch (Exception e) {
            try {
                if (c!=null) c.disconnect();
                if (in!=null) in.close();
                if (out!=null) out.close();
            } catch (Exception e2) {
            }
            throw e;
        }
    }

    public static String[] explode(String str, String delim) {

        if(str == null) {
            throw new NullPointerException("explode:str is null");
        }

        String[] paras = null;
        String strWk = str.toString();
        int delimCnt = 0;

        // 先にカンマの数を調べる
        int idx = -delim.length();
        while((idx = strWk.indexOf(delim, idx + delim.length())) > 0) {
            delimCnt ++;
        }

        if(delimCnt == 0) {
            // カンマなしの場合
            return new String[]{str};
        }

        // 生成
        paras = new String[delimCnt + 1];

        int pIdx = -delim.length(); // 1つ前のindex
        int nIdx = -delim.length(); // 現在のindex

        for(int i = 0; ; i++) {
            nIdx = strWk.indexOf(delim, nIdx + delim.length());
            if(nIdx > 0) {
                paras[i] = strWk.substring(pIdx + delim.length(), nIdx);
                pIdx = nIdx;
            }
            else {
                // 最後の値
                paras[i]
                     = strWk.substring(pIdx + delim.length(), strWk.length());
                break;
            }
        }

        return paras;
    }
}
