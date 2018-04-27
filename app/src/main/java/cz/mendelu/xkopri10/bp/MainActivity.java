package cz.mendelu.xkopri10.bp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Gratitude;
import cz.mendelu.xkopri10.bp.database.GratitudeAdapter;
import cz.mendelu.xkopri10.bp.database.Greatfulness;
import cz.mendelu.xkopri10.bp.database.GreatfulnessAdapter;
import cz.mendelu.xkopri10.bp.everythingUnderAdd.MainAddActivity;
import cz.mendelu.xkopri10.bp.everythingUnderHelp.HelpActivity;
import cz.mendelu.xkopri10.bp.everythingUnderHelp.HelpDetailRow;
import cz.mendelu.xkopri10.bp.list.ListActivity;
import cz.mendelu.xkopri10.bp.notifications.NotificationReciever;
import cz.mendelu.xkopri10.bp.notifications.NotificationRecieverMotivation;
import cz.mendelu.xkopri10.bp.settings.SettingActivity;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    public static final String DEFAULT_DATE = "2018-03-01";
    public static final long DEFAULT_ID_MOTIVATION_NOT = 1;
    public static final long DEFAULT_ID_DAILY_NOT = 1;

    DatabaseHelper db;

    RecyclerView recyclerView, myRecGrat;
    RecyclerView.LayoutManager layoutManager, layManGrat;
    PieChart pieChart;
    int hod, min, hod2, min2, myDuration;
    long myMillisecods, myMillisecods2;

    Date current, startDate, endDate;
    private ImageView noChartDataImage;
    private TextView noChartDataText, randomGreat, countGrat, countGreat, randGr;
    boolean noDataInChart,emptyBox, emptyBox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        noChartDataImage = (ImageView) findViewById(R.id.noChartDataImage);
        noChartDataText = (TextView) findViewById(R.id.noChartDataText);
        randomGreat = (TextView) findViewById(R.id.randGreatTextView);
        randGr = (TextView) findViewById(R.id.randGr);
        countGrat = (TextView) findViewById(R.id.countGrat);
        countGreat= (TextView) findViewById(R.id.countGreat);
        pieChart = (PieChart) findViewById(R.id.pieChart);

        //-----------zavolej notifikace
        callSetDailyNotification();
        callSetMotivationNotification();
        //-----------konec

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setExtraOffsets(5, 5, 45, 5);
        pieChart.animateY(1200, Easing.EasingOption.EaseInCubic);
        pieChart.setTouchEnabled(false);

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewRandom);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // Nastaví recycleview na to aby se nescrolloval
                return rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
            }
        });

        myRecGrat = (RecyclerView) findViewById(R.id.myRecGrat);
        myRecGrat.setNestedScrollingEnabled(false);
        myRecGrat.setFocusable(false);
        layManGrat = new LinearLayoutManager(this);
        myRecGrat.setLayoutManager(layManGrat);
        myRecGrat.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // Nastaví recycleview na to aby se nescrolloval
                return rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
            }
        });

        getList();
        getDataForChart();
        getList2();
        getCounts();
        refreshState();
    }

    public void refreshState(){
        db.close();
        db = new DatabaseHelper(getApplicationContext());
        Log.e("refresh DB","!");
    }


    // nastaveni notifikaci - metoda zavolana v oncreate a v onresume
    public void callSetMotivationNotification(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        myDuration = db.getMotivationNot(DEFAULT_ID_MOTIVATION_NOT).getDuration();
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
            notificationStartMotivation();
        }

        //Toast.makeText(MainActivity.this,"duration: " + String.valueOf(myDuration),Toast.LENGTH_SHORT).show();
        Log.e("current: ", String.valueOf(current));
        Log.e("duration: ", String.valueOf(myDuration));
        Log.e("start date: ", String.valueOf(startDate));
        Log.e("end date: ", String.valueOf(endDate));

    }

    public void callSetDailyNotification(){
        //zasilej notifikaci pouze pokud stav je ZAPNUTO a pocet dnesnich radosti je mensi jak 3 (tzn kdyz 2 tak zazvon a kdyz 3 tak uz ne)
        if ((db.getDailyNot(DEFAULT_ID_DAILY_NOT).getState() == 1) && (db.getCountOfGratitudes(2)<3)) {
            notificationStartDaily();
        }
    }

    public void getCounts(){
        countGrat.setText(String.valueOf(db.getCountOfGratitudes(1)));
        countGreat.setText(String.valueOf(db.getCountOfGreatfulness(1)));
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

    public void notificationStartDaily(){
        long currentTimeNow = System.currentTimeMillis();
        hod = db.getDailyNot(DEFAULT_ID_DAILY_NOT).getHour();
        min = db.getDailyNot(DEFAULT_ID_DAILY_NOT).getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hod);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        myMillisecods = calendar.getTimeInMillis();
        if (currentTimeNow <=myMillisecods){
            setNotificationDaily(myMillisecods);
        }
    }

    public void notificationStartMotivation(){
        long currentTimeNow = System.currentTimeMillis();
        hod2 = db.getMotivationNot(DEFAULT_ID_MOTIVATION_NOT).getHour();
        min2 = db.getMotivationNot(DEFAULT_ID_MOTIVATION_NOT).getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hod2);
        calendar.set(Calendar.MINUTE, min2);
        calendar.set(Calendar.SECOND, 0);

        myMillisecods2 = calendar.getTimeInMillis();
        if (currentTimeNow <= myMillisecods2){
            setNotificationMotivation(myMillisecods2);
        }
    }
/*
    private void setNotificationDaily(long timeInMillis) {

        // this nahrait za getApplicationContext
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, 0);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
*/
private void setNotificationDaily(long timeInMillis) {

    // this nahrait za getApplicationContext
    AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);
    intent.putExtra("time",timeInMillis);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, 0);
    //PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    if (alarmManager != null) {
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }
}
    private void setNotificationMotivation(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), NotificationRecieverMotivation.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 99, intent, 0);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 99, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }



    public void getDataForChart() {
        DatabaseHelper db = new DatabaseHelper(this);

        ArrayList<PieEntry> values = new ArrayList<>();
        for(int i=0;i<db.getCountForChart().size();i++){
            values.add(new PieEntry(db.getCountForChart().get(i)));
        }
        if (db.getCountForChart().size()==0){
            noDataInChart = true;
        }else noDataInChart = false;

        PieDataSet dataSet = new PieDataSet(values,"Legenda");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int i = 0; i < db.getColorsForChart().size(); i++) {
            colors.add(Color.parseColor(db.getColorsForChart().get(i)));
        }
        dataSet.setColors(colors);
        Legend legend = pieChart.getLegend();
        List<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < db.getNamesForChart().size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = Color.parseColor(db.getColorsForChart().get(i));
            entry.label = db.getNamesForChart().get(i);
            entries.add(entry);
        }
        legend.setCustom(entries);
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        data.setValueFormatter(
                new DecimalRemover(
                        new DecimalFormat("###,###,###")));

        pieChart.setData(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    public void onClickInformationButtonMenu(MenuItem item){
        Intent intent = new Intent(this,InformationActivity.class);
        startActivity(intent);
    }

    public void onClickHelpButton(View view){
        Intent intent = new Intent(this,HelpActivity.class);
        startActivity(intent);
    }

    public void onClickMainAddButton(View view){
        Intent intent = new Intent(this,MainAddActivity.class);
        startActivity(intent);
    }

    public void onClickSettingButton(View view){
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    public void onClickListButton(View view){
        Intent intent = new Intent(this,ListActivity.class);
        startActivity(intent);
    }

    public void getList() {
        DatabaseHelper db = new DatabaseHelper(this);
        List<Gratitude> gratitudes = db.getAllGratitude(5,null,0);

        if (gratitudes.size() == 0){
            emptyBox = true;
        }else emptyBox = false;

        GratitudeAdapter dataAdapter= new GratitudeAdapter(gratitudes);
        recyclerView.setAdapter(dataAdapter);
    }

    public void getList2() {
        DatabaseHelper db = new DatabaseHelper(this);
        List<Greatfulness> greatfulness = db.getAllGreatfulness(5,null);

        if (greatfulness.size() == 0){
            emptyBox2 = true;
        }else emptyBox2 = false;

        GreatfulnessAdapter dataAdapter= new GreatfulnessAdapter(greatfulness);
        myRecGrat.setAdapter(dataAdapter);
    }

    public class DecimalRemover extends PercentFormatter {
        protected DecimalFormat mFormat;
        public DecimalRemover(DecimalFormat format) {
            this.mFormat = format;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if(value < 8) return "";
            return mFormat.format(value) + " %";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
        getDataForChart();
        getList2();
        getCounts();

        //v případě že nejsou data tak se zobrazi obrazek, že nejsou data
        if (noDataInChart){
            noChartDataText.setVisibility(View.VISIBLE);
            noChartDataImage.setVisibility(View.VISIBLE);
        } else {
            noChartDataText.setVisibility(View.INVISIBLE);
            noChartDataImage.setVisibility(View.INVISIBLE);
        }
        if (emptyBox){
            randGr.setVisibility(View.INVISIBLE);
        } else {
            randGr.setVisibility(View.VISIBLE);
        }
        if (emptyBox2){
            randomGreat.setVisibility(View.INVISIBLE);
        }else {
            randomGreat.setVisibility(View.VISIBLE);
        }

        // volam i zde skrz to ze se cas ulozil ale pri oncreate se to enprovedlo po 2. v pripade ze jsme cas pak zmenil
        callSetDailyNotification();
        callSetMotivationNotification();

        refreshState();

    }
}
