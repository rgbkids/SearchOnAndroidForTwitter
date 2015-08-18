package biz.r8b.twitter.basic;

import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

//��������
public class AudioActivity extends BaseActivity {
	static final String EXT = "3gp";

	//
    protected static String FileName;
    private String fileName;
    protected static Activity Parent;
    private Activity parent;
    void init() {
    	fileName = FileName;
    	FileName = null;

    	parent = Parent;
    	Parent = null;
    }

    //
	private MediaRecorder recorder; //���f�B�A���R�[�_�[

    //�A�v���̏�����
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        init();


        //���C�A�E�g�̐���
//        LinearLayout layout=new LinearLayout(this);
//        layout.setBackgroundColor(Color.rgb(255,255,255));
//        layout.setOrientation(LinearLayout.VERTICAL);
//        setContentView(layout);
        setContentView(R.layout.main_audio);

        //�e�L�X�g�r���[�̐���
        TextView textView=(TextView)findViewById(R.id.TextView01);//new TextView(this);
//        textView.setText("�^����(BACK�ŏI��)");
        textView.setText("REC ... <BACK>");
        textView.setTextSize(16.0f);
        textView.setTextColor(Color.rgb(255,0,0));
//        layout.addView(textView);

        try {
            //�^���̊J�n(1)
            recorder=new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            File dir=Environment.getExternalStorageDirectory();

//            File file=File.createTempFile(fileName/*"test"*/,".3gp",dir);
            File file = new File(fileName + "." + EXT);

            TwTweetActivity.uploadFilePathVoice = file.getAbsolutePath();
            TwTweetActivity.uploadFileNameVoice = file.getName();
//            TwTweetActivity.addMessVoice(string(R.string.btn_voice) + " " + TwTweetActivity.URL_STORAGE + TwTweetActivity.uploadFileNameVoice);
//            String[] nameVals = csv(fileName, "_");
//            TwTweetActivity.addMessVoice(string(R.string.btn_voice) + " " + TwTweetActivity.URL_STORAGE + nameVals[2] + "." + EXT);
//            TwTweetActivity.addMessVoice(string(R.string.btn_voice), TwTweetActivity.URL_STORAGE + nameVals[2] + "." + EXT);

            recorder.setMaxFileSize(140000L);
            recorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
				@Override
				public void onError(MediaRecorder mr, int what, int extra) {
					finish();
					Log.d("", "");
				}
            });
            recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
				@Override
				public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
					if (arg1 == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED) {
						finish();
					}
					Log.d("", "");
				}
            });

            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            recorder.start();
       } catch (Exception e) {
            android.util.Log.e("",e.toString());
        }

       settingGestures();
    }

    //�A�v���̒�~
    @Override
    public void onStop() {
        super.onStop();
        try {
            //�^���̒�~(2)
            recorder.stop();
            recorder.release();

            //
            ((TwTweetActivity) parent).showVoiceFinish("" + fileName + "." + EXT + ((ja)?" �ɕۑ�":" saved."), fileName);

        } catch (Exception e) {
        }
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}