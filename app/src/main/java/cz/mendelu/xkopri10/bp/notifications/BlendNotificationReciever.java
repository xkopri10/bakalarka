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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.mendelu.xkopri10.bp.database.DatabaseHelper;

public class BlendNotificationReciever extends BroadcastReceiver {

    public static final String DEFAULT_DATE = "2018-03-01";
    public static final long DEFAULT_ID_MOTIVATION_NOT = 1;
    public static final long DEFAULT_ID_DAILY_NOT = 1;
    Date current, startDate, endDate;

    DatabaseHelper db;
    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.setVolume(0,0);
        mediaPlayer.start();
        Log.e("Zvoni", "to");
        db = new DatabaseHelper(context);

        callBlendNotification(context);
        callSetDailyNotification(context);
        callSetMotivationNotification(context);
    }

    public void callSetDailyNotification(Context context){
        if ((db.getDailyNot(DEFAULT_ID_DAILY_NOT).getState() == 1) && (db.getCountOfGratitudes(2)<3)) {
            setDailyNotification(context);
        }
    }

    private void callBlendNotification(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Long myTime = calendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BlendNotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 800, intent, 0);

        Toast.makeText(context, "Blend notification reciever" , Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (alarmManager != null) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,myTime,pendingIntent);
            }
        } else {
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, myTime, AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }

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

    //------------------------------------------------------------------------------------------------------

    public void callSetMotivationNotification(Context context){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int myDuration = db.getMotivationNot(DEFAULT_ID_MOTIVATION_NOT).getDuration();
        try {
            current = format.parse(getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if (db.getMotivationNot(DEFAULT_ID_MOTIVATION_NOT).getStartDate() == null){
                startDate = format.parse(DEFAULT_DATE);
            } else
                startDate = format.parse(db.getMotivationNot(DEFAULT_ID_MOTIVATION_NOT).getStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            endDate = format.parse(plusDurationToDate(myDuration));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (((startDate.before(current)) || (startDate.equals(current)))
                && ((current.before(endDate)) || (current.equals(endDate)))
                && (db.getMotivationNot(DEFAULT_ID_MOTIVATION_NOT).getState() == 1)){
            notificationStartMotivation(context);
        }
    }

    public String plusDurationToDate(int duration){
        String dateFromDB = db.getMotivationNot(DEFAULT_ID_MOTIVATION_NOT).getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            if (dateFromDB == null){
                calendar.setTime(sdf.parse(DEFAULT_DATE));
            }else
                calendar.setTime(sdf.parse(dateFromDB));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DATE,duration);   //pocet dni pridanych k datumu
        Date resultDate = new Date(calendar.getTimeInMillis());
        dateFromDB = sdf.format(resultDate);
        return dateFromDB;
    }

    public String getCurrentDate(){
        String myCurrentDate;
        Calendar calendar = Calendar.getInstance();
        int rok = calendar.get(Calendar.YEAR);
        int mesic = calendar.get(Calendar.MONTH)+1;
        int den = calendar.get(Calendar.DAY_OF_MONTH);
        if (mesic<=10){
            Log.e("curentdate: ",rok + "-0"+mesic+"-"+den);
            myCurrentDate = rok + "-0"+mesic+"-"+den;
        }else if (den <=10){
            Log.e("curentdate: ",rok + "-"+mesic+"-0"+den);
            myCurrentDate = rok + "-"+mesic+"-0"+den;
        }else if (mesic<=10 && den<=10){
            Log.e("curentdate: ",rok + "-0" +mesic+"-0"+den);
            myCurrentDate = rok + "-0" +mesic+"-0"+den;
        }else{
            Log.e("curentdate: ",rok + "-"+mesic+"-"+den);
            myCurrentDate = rok + "-"+mesic+"-"+den;
        }
        return myCurrentDate;
    }

    private void notificationStartMotivation(Context context){
        long currentTimeNow = System.currentTimeMillis();
        int hod2 = db.getMotivationNot(DEFAULT_ID_MOTIVATION_NOT).getHour();
        int min2 = db.getMotivationNot(DEFAULT_ID_MOTIVATION_NOT).getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hod2);
        calendar.set(Calendar.MINUTE, min2);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationRecieverMotivation.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 99, intent, 0);

        long myMillisecods2 = calendar.getTimeInMillis();
        if (currentTimeNow <= myMillisecods2){
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, myMillisecods2,AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }
}
