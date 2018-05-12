package cz.mendelu.xkopri10.bp.list;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.Category;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Gratitude;
import cz.mendelu.xkopri10.bp.database.GratitudeAdapter;

/**
 * Created by Martin on 08.02.2018.
 */

public class Tab2All extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    GratitudeAdapter gr;

    Calendar mCurrentDate;
    private int day,month,year;
    private long mCategoryID;
    String selectedDay, myDay, mCategory;
    private boolean pressed, emptyBox;
    List<Gratitude>gratitudes;
    DatabaseHelper database;
    DatePickerDialog datePickerDialog;
    private Menu menu;
    private ImageView emptyBox3;
    private TextView emptyBoxText;

    //vytvorim si instanci na zobrazeni MENU -nastavím na TRUE
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2all, container, false);

        emptyBox3 = (ImageView) rootView.findViewById(R.id.emptyBox3);
        emptyBoxText = (TextView) rootView.findViewById(R.id.emptyBoxText3);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.myRecyclerViewTab2);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getList(1);
        showEmptyBox();
        return rootView;
    }

    public void getList(int varianta) {
        //pripojeni a dostfani se k obsahu
        DatabaseHelper db = new DatabaseHelper(getContext());
        //ListView listView = (ListView) findViewById(R.id.listOfCategory);
        if (varianta == 1){
            gratitudes = db.getAllGratitude(1,null,0);
            if (gratitudes.size() == 0){
                emptyBox = true;
            }else emptyBox = false;
        }else if(varianta == 2) {
            gratitudes = db.getAllGratitude(3,myDay,0);
            if (gratitudes.size() == 0){
                emptyBox = true;
            }else emptyBox = false;
        } else if (varianta == 3){
            gratitudes = db.getAllGratitude(4,null,mCategoryID);
            if (gratitudes.size() == 0){
                emptyBox = true;
            }else emptyBox = false;
        }

        GratitudeAdapter dataAdapter= new GratitudeAdapter(gratitudes);
        recyclerView.setAdapter(dataAdapter);
        showEmptyBox();
    }

    public void showEmptyBox(){
        if (emptyBox){
            emptyBoxText.setVisibility(View.VISIBLE);
            emptyBox3.setVisibility(View.VISIBLE);
        } else {
            emptyBoxText.setVisibility(View.INVISIBLE);
            emptyBox3.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id ==  R.id.date_range){
            pressed = true;
            datePickerDialog = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    monthOfYear = monthOfYear+1;
                    if ((monthOfYear < 10) && (dayOfMonth < 10)){
                        //myDay = ("0"+dayOfMonth+".0"+monthOfYear+"."+year);
                        myDay = (year+"-0"+monthOfYear+"-0"+dayOfMonth);
                    }else if(month < 10){
                        //myDay = (dayOfMonth + ".0" + monthOfYear + "." + year);
                        myDay = (year+"-0"+monthOfYear+"-"+dayOfMonth);
                    }else if (day < 10){
                        //myDay = ("0" + dayOfMonth + ".0" + monthOfYear + "." + year);
                        myDay = (year+"-0"+monthOfYear+"-0"+dayOfMonth);
                    }
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


        } else if (id == R.id.nofilter){
            getList(1);
            pressed = false;
            item.setVisible(false);

        } else if (id == R.id.filter){
            pressed = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Výběr podle kategorie");
            builder.setNegativeButton(
                    "ZRUŠIT",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                        });

            DatabaseHelper db = new DatabaseHelper(getContext());
            List<Category> categories = db.getAllCategory(1);
            final String[] nameListCategory=new String[categories.size()];
            for(int i=0;i<categories.size();i++){
                nameListCategory[i]=categories.get(i).getName(); //create array of name
            }
            if (categories.size() == 0){
                builder.setMessage("Žádná kategorie");
            }

            builder.setItems(nameListCategory, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mCategory = nameListCategory[i];
                    mCategoryID = database.getIdCategoryByName(mCategory);
                    getList(3);
                    onPeresFilter();
                    Log.e("Vybraná kategorie: ", String.valueOf(mCategoryID));
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    pressed = false;
                }
            });

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

    //tady skryju tlacitko menu v pripade ze je na karte DNES
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        if (pressed){
            menu.findItem(R.id.nofilter).setVisible(true);
        }else {
            menu.findItem(R.id.nofilter).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        getList(1);
        showEmptyBox();
    }



}
