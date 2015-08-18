package biz.r8b.twitter.basic;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpUtil {






    //HTTP�ʐM��������
    static String http2str(String path) throws Exception {
        byte[] w=http2data(path);
        return new String(w);
    }

    //HTTP�ʐM
    public static byte[] http2data(String path) throws Exception
    {
        int size;
        byte[] w=new byte[1024];
        HttpURLConnection c=null;
        InputStream in=null;
        ByteArrayOutputStream out=null;
        try {
            //HTTP�ڑ��̃I�[�v��(2)
            URL url=new URL(path);
            c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            in=c.getInputStream();

            //�o�C�g�z��̓ǂݍ���
            out=new ByteArrayOutputStream();
            while (true) {
                size=in.read(w);
                if (size<=0) break;
                out.write(w,0,size);
            }
            out.close();

            //HTTP�ڑ��̃N���[�Y(3)
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

        // ��ɃJ���}�̐��𒲂ׂ�
        int idx = -delim.length();
        while((idx = strWk.indexOf(delim, idx + delim.length())) > 0) {
            delimCnt ++;
        }

        if(delimCnt == 0) {
            // �J���}�Ȃ��̏ꍇ
            return new String[]{str};
        }

        // ����
        paras = new String[delimCnt + 1];

        int pIdx = -delim.length(); // 1�O��index
        int nIdx = -delim.length(); // ���݂�index

        for(int i = 0; ; i++) {
            nIdx = strWk.indexOf(delim, nIdx + delim.length());
            if(nIdx > 0) {
                paras[i] = strWk.substring(pIdx + delim.length(), nIdx);
                pIdx = nIdx;
            }
            else {
                // �Ō�̒l
                paras[i]
                     = strWk.substring(pIdx + delim.length(), strWk.length());
                break;
            }
        }

        return paras;
    }
}
