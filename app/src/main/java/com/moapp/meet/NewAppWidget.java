package com.moapp.meet;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static CharSequence ss = null;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);


        //시작되면서 지정 이미지로 교체 위젯의 시작 이미지를 불러온다.
        views.setImageViewResource(R.id.imageView, R.drawable.icon_meet);

        //이미지 클릭스 앱을 띄우고 LoginActivity 이동
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(context, LoginActivity.class));
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.imageView, pi);

        views.setTextViewText(R.id.Chatname, ss);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views.setImageViewResource(R.id.imageView, R.drawable.icon_meet);
            views.setTextViewText(R.id.Chatname, ss);

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        CharSequence s = bundle.getCharSequence("chatname");

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

        ss = s;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName testWidget = new ComponentName(context.getPackageName(), NewAppWidget.class.getName());

        int[] widgetIds = appWidgetManager.getAppWidgetIds(testWidget);

        this.onUpdate(context, AppWidgetManager.getInstance(context), widgetIds);



    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
