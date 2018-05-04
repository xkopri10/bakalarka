package cz.mendelu.xkopri10.bp.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.Calendar;

import cz.mendelu.xkopri10.bp.MainActivity;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;

public class BlendNotificationReciever extends BroadcastReceiver {

    public static final long DEFAULT_ID_DAILY_NOT = 1;
    DatabaseHelper db;
    MainActivity mainActivity;
    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.setVolume(0,0);
        mediaPlayer.start();
        Log.e("Zvoni", "to");

        setNeverKilledNotification(context);
        mainActivity.callSetDailyNotification();
        mainActivity.callSetMotivationNotification();

        //db = new DatabaseHelper(context);
        //db.close();
        //db = new DatabaseHelper(context);
        //Log.e("refresuju", "to");
    }

    private void setNeverKilledNotification(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        Long myTime = calendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BlendNotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 800, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (alarmManager != null) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,myTime,pendingIntent);
            }
        } else {
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }
/*
    private void setDailyNotification(Context context){
        long currentTimeNow = System.currentTimeMillis();
        int hod = db.getDailyNot(DEFAULT_ID_DAILY_NOT).getHour();
        int min = db.getDailyNot(DEFAULT_ID_DAILY_NOT).getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hod);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, 0);

        long myMillisecods = calendar.getTimeInMillis();
        if (currentTimeNow <=myMillisecods){
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, myMillisecods, AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }
    */
}
