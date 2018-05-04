package cz.mendelu.xkopri10.bp.notifications;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class NeverEndingService extends Service {
    public int couter = 0;
    Context context;

    public NeverEndingService(Context applicationContext){
        super();
        context = applicationContext;
        Log.e("ZDE"," je vytvořena service");
    }

    public NeverEndingService(){
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("EXIT", " z onDestroy");
        Intent broadcastIntent = new Intent("ac.in.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer(){
        timer = new Timer();
        initializeTimerTask();
        //probud me kazdou sekundu
        timer.schedule(timerTask,60000,60000);
    }

    public void initializeTimerTask(){
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.e("Timer",(couter++)+"");
            }
        };
    }

    public void stoptimertask(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
