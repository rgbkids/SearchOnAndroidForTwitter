package biz.r8b.twitter.basic;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

//ホームスクリーンウィジェットプロバイダー(1)
public class AppWidgetEx extends AppWidgetProvider {
    //ホームスクリーンウィジェット更新時に呼ばれる
    @Override
    public void onUpdate(Context context,
        AppWidgetManager appWidgetManager,int[] appWidgetIds) {

        //ホームスクリーンウィジェットのイベント処理を担当するサービスの起動(2)
        Intent intent=new Intent(context,AppWidgetService.class);
        context.startService(intent);
    }
}