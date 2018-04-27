package cz.mendelu.xkopri10.bp.settings;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.MotivationNot;
import cz.mendelu.xkopri10.bp.everythingUnderAdd.MainAddActivity;
import cz.mendelu.xkopri10.bp.notifications.NotificationReciever;
import io.fabric.sdk.android.Fabric;

public class SettingActivity extends AppCompatActivity {

    private CardView cardViewDailyTime, cardViewMotivationTime, cardViewSelectCategory, cardViewStartDate, cardViewDuration;
    private TextView timeDaily, timeMotivations, textViewMySelectedDate, textViewMyDurationSelected;
    private DatabaseHelper db;

    DatePickerDialog datePickerDialog;
    Switch switchDailyNot, switchMotivationNot;
    String selectedDay, myDay, datumek, casUpozorneni, yyyyy;
    private int day,month,year, myDuration , stateMotNot, stateDayNot;
    Calendar mCurrentDate;
    boolean clickTime, clickStartDate, clickDuration, clickState, clickStateDaily, clickTimeDay;

    int mySelectedHour, mySelectedMinute, zz1, zz2, zz21, zz22, mySelectedHour2, mySelectedMinute2;

    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_setting);

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardViewDailyTime = (CardView) findViewById(R.id.time_daily_notification);
        cardViewMotivationTime = (CardView) findViewById(R.id.time_motivation_notification);
        cardViewSelectCategory = (CardView) findViewById(R.id.select_motivation_categories);
        cardViewDuration = (CardView) findViewById(R.id.cardViewDuration);
        cardViewStartDate = (CardView) findViewById(R.id.cardViewStartDate);

        timeDaily = (TextView) findViewById(R.id.time_daily);
        timeMotivations = (TextView) findViewById(R.id.time_motivation);
        textViewMySelectedDate = (TextView) findViewById(R.id.textViewMySelectedDate);
        textViewMyDurationSelected = (TextView) findViewById(R.id.myDurationSelected);

        switchDailyNot = (Switch) findViewById(R.id.switchDailyNot);
        switchMotivationNot = (Switch) findViewById(R.id.switchMotivatedNot);

        db = new DatabaseHelper(this);

        clickState = false;
        clickDuration = false;
        clickTime = false;
        clickStartDate = false;
        clickTimeDay = false;
        clickStateDaily = false;

        //promene kde si pamatuji cas z predesleho anstaveni - v pripade ze dám zrušit se nastaví tyto hodnoty
        //zzzzz = db.getDailyNot(1).getTime();
        zz1 = db.getDailyNot(1).getHour();
        zz2 = db.getDailyNot(1).getMinute();

        if (db.getDailyNot(1).getState() == 1){switchDailyNot.setChecked(true);}
        if (zz2 < 10){
            timeDaily.setText(String.valueOf(zz1) + ":0"+ String.valueOf(zz2));
        }else {
            timeDaily.setText(String.valueOf(zz1) + ":"+ String.valueOf(zz2));
        }

        zz21 = db.getMotivationNot(1).getHour();
        zz22 = db.getMotivationNot(1).getMinute();
        yyyyy = db.getMotivationNot(1).getStartDate();

        if (db.getMotivationNot(1).getState() == 1){switchMotivationNot.setChecked(true);}
        if (zz22 < 10){
            timeMotivations.setText(String.valueOf(zz21) + ":0"+ String.valueOf(zz22));
        }else{
            timeMotivations.setText(String.valueOf(zz21) + ":"+ String.valueOf(zz22));
        }

        if (db.getMotivationNot(1).getStartDate() == null){
            textViewMySelectedDate.setText(konverze("2018-01-01"));
        }else{
            textViewMySelectedDate.setText(konverze(db.getMotivationNot(1).getStartDate()));
        }

        textViewMyDurationSelected.setText(String.valueOf(db.getMotivationNot(1).getDuration()));

        //pro to aby se pri rozkliknutí kalendáře nastavil datum na dnešní
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        if ((month < 10) && (day < 10)){
            selectedDay = ("0"+day+".0"+month+"."+year);
        }else if(month < 10){
            selectedDay = (day + ".0" + month + "." + year);
        }else if (day < 10){
            selectedDay = ("0" + day + ".0" + month + "." + year);
        }

        cardViewDailyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTimeDay = true;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                final TimePickerDialog timePickerDialog = new TimePickerDialog(SettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //v pripade spatneho nacitani - ulozit puvodni (bez :0) a udelat konverzi pro zobrazeni
                        if (selectedMinute<10){
                            timeDaily.setText( selectedHour + ":0" + selectedMinute);
                        }else {
                            timeDaily.setText(selectedHour + ":" + selectedMinute);
                        }

                        mySelectedHour = selectedHour;
                        mySelectedMinute = selectedMinute;

                        Log.e("hodina: ", String.valueOf(mySelectedHour));
                        Log.e("minuta: ", String.valueOf(mySelectedMinute));

                        try {
                            db.updateDailyNot(1,db.getDailyNot(1).getState(),mySelectedHour,mySelectedMinute);
                        }catch (Exception e){e.printStackTrace();}
                        }
                    },hour,minute,true);

                timePickerDialog.setTitle("Čas upozornění");
                timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ZRUŠIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mySelectedHour = zz1;
                        mySelectedMinute = zz2;

                        try {
                            db.updateDailyNot(1,db.getDailyNot(1).getState(),mySelectedHour,mySelectedMinute);
                        }catch (Exception e){e.printStackTrace();}
                        timePickerDialog.dismiss();
                    }
                });
                timePickerDialog.show();
                timePickerDialog.setCanceledOnTouchOutside(true);
                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mySelectedHour = zz1;
                        mySelectedMinute = zz2;

                        try {
                            db.updateDailyNot(1,db.getDailyNot(1).getState(),mySelectedHour,mySelectedMinute);
                        }catch (Exception e){e.printStackTrace();}
                    }
                });
                }
        });

        switchDailyNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                clickStateDaily = true;
                if (!buttonView.isChecked()){
                    stateDayNot = 0;
                    Toast.makeText(SettingActivity.this, "Notifikace vypnuty", Toast.LENGTH_LONG).show();
                }else if (buttonView.isChecked()){
                    stateDayNot = 1;
                    Toast.makeText(SettingActivity.this, "Notifikace zapnuty", Toast.LENGTH_LONG).show();
                }
                try {
                    db.updateDailyNot(1,stateDayNot,db.getDailyNot(1).getHour(),db.getDailyNot(1).getMinute());
                }catch (Exception e){e.printStackTrace();}
            }
        });

        cardViewMotivationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTime = true;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                final TimePickerDialog timePickerDialog = new TimePickerDialog(SettingActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedMinute<10){
                            timeMotivations.setText( selectedHour + ":0" + selectedMinute);
                        }else {
                            timeMotivations.setText(selectedHour + ":" + selectedMinute);
                        }
                        casUpozorneni = (String) timeMotivations.getText();
                        Log.e("cas motiv: ", casUpozorneni);

                        mySelectedHour2 = selectedHour;
                        mySelectedMinute2 = selectedMinute;

                        try {
                            db.updateMotivationNot(1,db.getMotivationNot(1).getState(),mySelectedHour2,mySelectedMinute2, db.getMotivationNot(1).getDuration(),db.getMotivationNot(1).getStartDate());
                        }catch (Exception e){e.printStackTrace();}

                        Log.e("hodina2: ", String.valueOf(mySelectedHour2));
                        Log.e("minuta2: ", String.valueOf(mySelectedMinute2));

                    }
                },hour,minute,true);

                timePickerDialog.setTitle("Čas upozornění");
                timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ZRUŠIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mySelectedHour2 = zz21;
                        mySelectedMinute2 = zz22;

                        try {
                            db.updateMotivationNot(1,db.getMotivationNot(1).getState(),mySelectedHour2,mySelectedMinute2, db.getMotivationNot(1).getDuration(),db.getMotivationNot(1).getStartDate());
                        }catch (Exception e){e.printStackTrace();}
                        timePickerDialog.dismiss();
                    }
                });
                timePickerDialog.setCanceledOnTouchOutside(true);
                timePickerDialog.show();
                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mySelectedHour2 = zz21;
                        mySelectedMinute2 = zz22;

                        try {
                            db.updateMotivationNot(1,db.getMotivationNot(1).getState(),mySelectedHour2,mySelectedMinute2, db.getMotivationNot(1).getDuration(),db.getMotivationNot(1).getStartDate());
                        }catch (Exception e){e.printStackTrace();}
                    }
                });

            }
        });

        switchMotivationNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                clickState = true;
                if (!buttonView.isChecked()){
                    stateMotNot = 0;
                    Toast.makeText(SettingActivity.this, "Notifikace vypnuty", Toast.LENGTH_LONG).show();
                }else if (buttonView.isChecked()){
                    stateMotNot = 1;
                    Toast.makeText(SettingActivity.this, "Notifikace zapnuty", Toast.LENGTH_LONG).show();
                }
                try {
                    db.updateMotivationNot(1,stateMotNot,db.getMotivationNot(1).getHour(),db.getMotivationNot(1).getMinute(),db.getMotivationNot(1).getDuration(),db.getMotivationNot(1).getStartDate());
                }catch (Exception e){e.printStackTrace();}
            }
        });

        cardViewDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(SettingActivity.this);
                View viewAlertDialog = layoutInflater.inflate(R.layout.alert_dialog_edit_text,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingActivity.this);

                alertDialogBuilder.setView(viewAlertDialog);
                final EditText userInputDuration = (EditText) viewAlertDialog.findViewById(R.id.editTextDialogUserInput);
                userInputDuration.setText(String.valueOf(db.getMotivationNot(1).getDuration()));
                userInputDuration.setSelection(userInputDuration.getText().length());
                showKeyboard();

                alertDialogBuilder
                        .setNegativeButton("ZRUŠIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clickDuration = false;
                                closeKeyboard();
                            }
                        })
                        .setPositiveButton("POTVRDIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clickDuration = true;
                                Log.e("duration se ulozi: ", "ZDE");
                                if ((userInputDuration.getText().toString().matches(""))){
                                    userInputDuration.setError("Vyplň počet dní");
                                }else{
                                textViewMyDurationSelected.setText(userInputDuration.getText());
                                myDuration = Integer.parseInt(String.valueOf(textViewMyDurationSelected.getText()));
                                Log.e("myDuration: ", String.valueOf(myDuration));

                                try {
                                    db.updateMotivationNot(1,db.getMotivationNot(1).getState(),db.getMotivationNot(1).getHour(),db.getMotivationNot(1).getMinute(),myDuration,db.getMotivationNot(1).getStartDate());
                                }catch (Exception e){e.printStackTrace();}}

                                closeKeyboard();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        clickDuration = false;
                        closeKeyboard();
                    }
                });
            }
        });

        cardViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStartDate = true;
                datePickerDialog = new DatePickerDialog(SettingActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        if ((monthOfYear < 10) && (dayOfMonth < 10)){
                            myDay = (year+"-0"+monthOfYear+"-0"+dayOfMonth);
                        }else if(month < 10){
                            myDay = (year+"-0"+monthOfYear+"-"+dayOfMonth);
                        }else if (day < 10){
                            myDay = (year+"-0"+monthOfYear+"-0"+dayOfMonth);
                        }
                        datumek = myDay;
                        Log.e("date jak se uklozi: ", datumek);
                        textViewMySelectedDate.setText(konverze(datumek));
                        Log.e("date jak se zobrazi: ", konverze(datumek));

                        try {
                            db.updateMotivationNot(1,db.getMotivationNot(1).getState(),db.getMotivationNot(1).getHour(),db.getMotivationNot(1).getMinute(),db.getMotivationNot(1).getDuration(),datumek);
                        }catch (Exception e){e.printStackTrace();}

                    }
                },year,month,day);
                datePickerDialog.setTitle("Začátek upozorňování");
                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ZRUŠIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        datumek = yyyyy;

                        try {
                            db.updateMotivationNot(1,db.getMotivationNot(1).getState(),db.getMotivationNot(1).getHour(),db.getMotivationNot(1).getMinute(),db.getMotivationNot(1).getDuration(),datumek);
                        }catch (Exception e){e.printStackTrace();}
                        datePickerDialog.dismiss();
                    }
                });
                datePickerDialog.show();
                datePickerDialog.setCanceledOnTouchOutside(true);
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        datumek = yyyyy;

                        try {
                            db.updateMotivationNot(1,db.getMotivationNot(1).getState(),db.getMotivationNot(1).getHour(),db.getMotivationNot(1).getMinute(),db.getMotivationNot(1).getDuration(),datumek);
                        }catch (Exception e){e.printStackTrace();}
                    }
                });
            }
        });

        cardViewSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ChoosingCategorySettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    //metoda prro tlačítko zpět
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //ukončí aktivitu
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String konverze(String datum){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
        String inputDateStr= datum;
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
}