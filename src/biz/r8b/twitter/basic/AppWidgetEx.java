package biz.r8b.twitter.basic;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

//�z�[���X�N���[���E�B�W�F�b�g�v���o�C�_�[(1)
public class AppWidgetEx extends AppWidgetProvider {
    //�z�[���X�N���[���E�B�W�F�b�g�X�V���ɌĂ΂��
    @Override
    public void onUpdate(Context context,
        AppWidgetManager appWidgetManager,int[] appWidgetIds) {

        //�z�[���X�N���[���E�B�W�F�b�g�̃C�x���g������S������T�[�r�X�̋N��(2)
        Intent intent=new Intent(context,AppWidgetService.class);
        context.startService(intent);
    }
}