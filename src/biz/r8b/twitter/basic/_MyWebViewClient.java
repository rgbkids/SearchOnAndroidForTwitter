package biz.r8b.twitter.basic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

// WebViewClient��Tab2���C���i�[�N���X�ŕʁX�ɂƂ����킯�ɂ͂����Ȃ��炵���B��������g���܂킷�B
public class _MyWebViewClient  extends WebViewClient {
	private String urlFinished;
	private WebView webview;
	private String urlTarget;
	private Activity activity;
	private ProgressDialog progressDialog;
	private int index;
	private boolean goback;
	private boolean autoRedirect = true;

	private int progressType = PROGRESS_TYPE_DIALOG;
	private IfMyWebViewClient ifMyWebViewClient;
	public static final int PROGRESS_TYPE_DIALOG = 0;
	public static final int PROGRESS_TYPE_NONE = 1;
	public static final int PROGRESS_TYPE_BAR = 2;

	public String getUrlFinished() {
		return urlFinished;
	}

	public _MyWebViewClient(Activity activity, WebView webview, String urlTarget, int index) {
        super();

        this.activity = activity;
        this.webview = webview;
        this.urlTarget = urlTarget;
        this.urlFinished = urlTarget;
        this.index = index;
    }

	public _MyWebViewClient(Activity activity, WebView webview, String urlTarget, int index, boolean autoRedirect) {
        this(activity, webview, urlTarget, index);

        this.autoRedirect = autoRedirect;
    }

	public _MyWebViewClient(Activity activity, WebView webview, String urlTarget, int index, boolean autoRedirect, int progressType, IfMyWebViewClient ifMyWebViewClient) {
        this(activity, webview, urlTarget, index, autoRedirect);

        this.progressType  = progressType;
        this.ifMyWebViewClient = ifMyWebViewClient;
    }

    @Override
    public void onPageFinished (WebView view, String url) { // redirect�����ƌĂ΂�Ȃ��炵���H
    	urlFinished = url;

    	if (ifMyWebViewClient != null) {
    		ifMyWebViewClient.setUrlFinished(url);
    	}

//    	if (autoRedirect) {
    	if (progressType == PROGRESS_TYPE_DIALOG && progressDialog != null) {
    		progressDialog.cancel();
    		progressDialog = null;
    	}
    	else if (progressType == PROGRESS_TYPE_BAR) {
    		activity.setProgressBarVisibility(false);
    	}

    	if (!goback) {
    		_WFrameActivity.setWebviewActiveStack(index);
    	}

    	goback = false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//    	if (autoRedirect) {
    	if (progressType == PROGRESS_TYPE_DIALOG) {
    		if (progressDialog != null) {
    			progressDialog.cancel();
    			progressDialog = null;
    		}
	    	progressDialog = new ProgressDialog(activity);
	        progressDialog.setTitle("");
	        progressDialog.setMessage("Loading...");
	        progressDialog.setIndeterminate(false);
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.show();
    	}
    	else if (progressType == PROGRESS_TYPE_BAR) {
    		// -------------------------------------------------------------------------------------------------
    		// requestWindowFeature(Window.FEATURE_PROGRESS); //��onCreate()���Asuper���\�b�h�ďo������ɋL�q
    		// -------------------------------------------------------------------------------------------------

    		//�v���O���X�o�[��\������
            activity.setProgressBarVisibility(true);

            //�s���� �v���O���X�i�i���j���Ȃ����
//            activity.setProgressBarIndeterminate(true);

            //�v���O���X�o�[��500��ݒ肷��B
            //�ݒ�ł���l�� 0 ���� 10000
            activity.setProgress(10000/2);
    	}


//	        if (progressDialog == null) {
		        if(autoRedirect && !url.equals(urlFinished)) { // �\�����ꂽ�y�[�W�ȊO�͕W���u���E�U��
		            webview.stopLoading();
		            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		            activity.startActivity(i);
		        }
//	        }

		    //
		    if (activity instanceof _WFrameActivity) {
			    if (url.indexOf("twitter.com/signup") >= 0) { // �V�K�A�J�E���g�͕W���u���E�U��
			    	BaseActivity.alertAndClose(activity, BaseActivity.botMess(
			    			"�A�J�E���g���쐬������A\n���̉�ʂɖ߂��āA�F�؎葱�����������Ă��������B",
			    			"�A�J�E���g���쐬������A\n���̉�ʂɖ߂��āA�F�؎葱�����������Ă��������B",
			    			"New to Twitter? Sign up\n\nThis is the app that can access your Twitter account."
			    	));

					webview.stopLoading();
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					activity.startActivity(i);
			    }
			    else if (url.indexOf("http://rgb-kids.com/") >= 0) { // �L�����Z���� �A�v���ɖ߂�
			    	webview.stopLoading();
			    	activity.finish();
			    }
		    }
		    else if (activity instanceof _WFrameBrowserActivity) {
		    	if (url.indexOf("youtube") >= 0) {
			    	webview.stopLoading();
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					activity.startActivity(i);
			    }
//			    else if (url.startsWith("https://mobile.twitter.com/")) {
//			    	if (url.startsWith("")) {
//
//			    	}
//			    	else if (url.endsWith("followings")) {
//
//			    	}
//			    }
		    }
    }

	public void isGoBack(boolean goback) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		this.goback = goback;
	}
}