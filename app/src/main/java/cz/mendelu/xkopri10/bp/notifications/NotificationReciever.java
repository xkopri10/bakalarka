package cz.mendelu.xkopri10.bp.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.Calendar;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.everythingUnderAdd.MainAddActivity;

/**
 * Created by Martin on 14.03.2018.
 */

public class NotificationReciever extends BroadcastReceiver{

     long longMilis = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras() != null){
            longMilis = intent.getLongExtra("time",0);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context,MainAddActivity.class);
        setNotificationDaily(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.iconka3)
                .setContentTitle("Vyplň si dnešní radosti nebo vděčnost")
                .setContentText("")
                .setSound(uri)
                .setStyle(new Notification.InboxStyle()
                        .setSummaryText("Upomínková notifikace")
                        .addLine("Tak ať na mě nezapomeneš!"))
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPublicVersion(new Notification())
                .setAutoCancel(true);
            notificationManager.notify(100,builder.build());
    }

    private void setNotificationDaily(Context context) {

        // this nahrait za getApplicationContext
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReciever.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, 0);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (longMilis != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(longMilis);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (alarmManager != null) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
