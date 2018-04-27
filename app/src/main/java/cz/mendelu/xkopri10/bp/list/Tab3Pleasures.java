package cz.mendelu.xkopri10.bp.list;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.GratitudeAdapter;
import cz.mendelu.xkopri10.bp.database.Greatfulness;
import cz.mendelu.xkopri10.bp.database.GreatfulnessAdapter;
import cz.mendelu.xkopri10.bp.settings.SettingActivity;

/**
 * Created by Martin on 26.02.2018.
 */

public class Tab3Pleasures extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHelper database;

    List<Greatfulness> greatfulness;
    DatePickerDialog datePickerDialog;
    Calendar mCurrentDate;
    private int day,month,year;
    String selectedDay, myDay;
    private boolean pressed, emptyBox;
    private Menu menu;
    private ImageView emptyBox4;
    private TextView emptyBoxText4;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        database = new DatabaseHelper(getContext());

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3pleasures, container, false);

        emptyBox4 = (ImageView) rootView.findViewById(R.id.emptyBox4);
        emptyBoxText4 = (TextView) rootView.findViewById(R.id.emptyBoxText4);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.myRecyclerViewTab3);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getList(1);
        showEmptyBox();
        return rootView;
    }

    public void getList(int variata) {
        //pripojeni a dostfani se k obsahu
        DatabaseHelper db = new DatabaseHelper(getContext());
        if (variata == 1) {
            greatfulness = db.getAllGreatfulness(1, null);
            if (greatfulness.size() == 0){
                emptyBox = true;
            }else emptyBox = false;
        }else if (variata == 2){
            greatfulness = db.getAllGreatfulness(3, myDay);
            if (greatfulness.size() == 0){
                emptyBox = true;
            }else emptyBox = false;
        }
        GreatfulnessAdapter dataAdapter= new GreatfulnessAdapter(greatfulness);
        recyclerView.setAdapter(dataAdapter);
        showEmptyBox();
    }

    public void showEmptyBox(){
        if (emptyBox){
            emptyBoxText4.setVisibility(View.VISIBLE);
            emptyBox4.setVisibility(View.VISIBLE);
        } else {
            emptyBoxText4.setVisibility(View.INVISIBLE);
            emptyBox4.setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==  R.id.date_range){
            pressed = true;

            datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
                    Log.e("myDay 1 = ", myDay+"");
                    getList(2);
                    onPeresFilter();
                }
            },year,month,day);
            datePickerDialog.setTitle("Výběr podle datumu");
            datePickerDialog.show();
            datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    pressed = false;
                }
            });

        }else if (id == R.id.nofilter) {
            getList(1);
            pressed = false;
            item.setVisible(false);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPeresFilter(){
        if (pressed){
            MenuItem menuItem = menu.findItem(R.id.nofilter);
            menuItem.setVisible(true);
        }else {
            MenuItem menuItem = menu.findItem(R.id.nofilter);
            menuItem.setVisible(false);
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        if (pressed){
            menu.findItem(R.id.nofilter).setVisible(true);
        }else {
            menu.findItem(R.id.nofilter).setVisible(false);
        }
        menu.findItem(R.id.filter).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        getList(1);
        showEmptyBox();
    }
}
