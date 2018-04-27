package cz.mendelu.xkopri10.bp.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Gratitude;
import cz.mendelu.xkopri10.bp.list.DetailOfListActivity;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Martin on 15.03.2018.
 */

public class NotificationRecieverMotivation extends BroadcastReceiver {

    DatabaseHelper db;
    String notificationNote;
    long mojeidecko;

    //konstanta na index v arrazlistu - necham si vytahnout jen 1 notifikaci ktera je na pozici 0
    public static final int INDEX = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Fabric.with(context, new Crashlytics());
        db = new DatabaseHelper(context);

        List<Gratitude> list = db.getAllGratitude(8, null, 0);
        if (list.size() != 0) {
            notificationNote = list.get(INDEX).getNote();
            mojeidecko = list.get(INDEX).getId();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent repeatingIntent = new Intent(context, DetailOfListActivity.class);
            repeatingIntent.putExtra(DetailOfListActivity.EXTRA_ID_NOT, mojeidecko);

            //repeatingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            //u pending intent MUSÍ BÝT FLAG_UPDATE_CURRENT - pokud je tam 0 tak se nezobrazí a spadne do default value
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 99, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 99, repeatingIntent, 0);
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.iconka3)
                    .setContentTitle("Připomeň si co jsi přidal/a za radost: " + konverze(list.get(INDEX).getDate()))
                    .setContentText("")
                    .setSound(uri)
                    .setStyle(new Notification.InboxStyle()
                            .setBigContentTitle("Připomeň si co jsi přidal/a za radost: " + konverze(list.get(INDEX).getDate()))
                            .setSummaryText("Motivační notifikace")
                            .addLine("Poznámka: " + notificationNote))
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setAutoCancel(true);
            notificationManager.notify(99, builder.build());

        }
    }

    public String konverze(String neco){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
        String inputDateStr= neco;
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }
}
