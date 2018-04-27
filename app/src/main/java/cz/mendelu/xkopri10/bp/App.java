package cz.mendelu.xkopri10.bp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import cz.mendelu.xkopri10.bp.database.DailyNot;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.MotivationNot;

/**
 * Created by Martin on 12.03.2018.
 */

public class App extends Application {

    private DatabaseHelper db;

    @Override
    public void onCreate() {
        super.onCreate();

        db = new DatabaseHelper(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- zde se provede kod pouze jednou pri instalaci aplikace
            MotivationNot motivationNot = new MotivationNot();
            motivationNot.setStartDate(db.getTodayDate());
            motivationNot.setDuration(1);
            motivationNot.setState(0);
            motivationNot.setHour(9);
            motivationNot.setMinute(0);
            try {
                db.createMotivationNot(motivationNot);
                Log.e("Motivation notification", "vytvorena");
            }catch (Exception e){
                e.printStackTrace();
            }

            DailyNot dailyNot = new DailyNot();
            dailyNot.setHour(14);
            dailyNot.setMinute(0);
            dailyNot.setState(1);
            try {
                db.createDailyNot(dailyNot);
                Log.e("Daily notification", "vytvorena");
            }catch (Exception e){
                e.printStackTrace();
            }

            // oznackuje se pri prvnim spusteni
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }

}
