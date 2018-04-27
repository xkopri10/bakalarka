package cz.mendelu.xkopri10.bp.everythingUnderHelp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import cz.mendelu.xkopri10.bp.MainActivity;
import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.Category;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Gratitude;
import cz.mendelu.xkopri10.bp.database.GratitudeAdapter;
import cz.mendelu.xkopri10.bp.database.Greatfulness;
import cz.mendelu.xkopri10.bp.database.GreatfulnessAdapter;
import io.fabric.sdk.android.Fabric;

public class HelpDetailRow extends AppCompatActivity {

    private long catID, myCount, myAmount;
    private Category category;
    private DatabaseHelper db;
    private TextView nameOfCategory, countOfCategory, averageCaterogyRate, percentageTextView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Gratitude> gratitudeList;
    Button topWeekButton, topMonthButton;
    double vysl;
    private ImageView emptyBoxImageStat;
    private TextView emptyBoxTextStat;
    boolean clickLeftList, clickRightList, emptyBox;

    public static final String EXTRA_CATEGORY_ID_HELP = "cz.mendelu.xkopri10.bp.extraDetailID_help";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_help_detail_row);

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clickRightList = false;
        clickLeftList = true;

        db = new DatabaseHelper(this);
        emptyBoxImageStat = (ImageView) findViewById(R.id.emptyBoxStat);
        emptyBoxTextStat = (TextView) findViewById(R.id.emptyBoxTextStat);

        nameOfCategory = (TextView) findViewById(R.id.categoryNameStat);
        countOfCategory = (TextView) findViewById(R.id.countOfCategory);
        averageCaterogyRate = (TextView) findViewById(R.id.averageRate);
        percentageTextView = (TextView) findViewById(R.id.percentageTextView);

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerStatisticsCategory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // Nastaví recycleview na to aby se nescrolloval
                return rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        topWeekButton = (Button) findViewById(R.id.topLastWeek);
        topMonthButton = (Button) findViewById(R.id.topLastMonth);
        topWeekButton.setBackground(getDrawable(R.drawable.button_borders_click));
        topMonthButton.setBackground(getDrawable(R.drawable.button_borders2));

        topWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getList(1);
                clickRightList = false;
                clickLeftList = true;
                topWeekButton.setBackground(getDrawable(R.drawable.button_borders_click));
                topMonthButton.setBackground(getDrawable(R.drawable.button_borders2));

                topWeekButton.setTextColor(getColor(R.color.white));
                topMonthButton.setTextColor(getColor(R.color.darkpurple));
                showEmptyBox();
            }
        });

        topMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getList(2);
                clickRightList = true;
                clickLeftList = false;
                topWeekButton.setBackground(getDrawable(R.drawable.button_borders));
                topMonthButton.setBackground(getDrawable(R.drawable.button_borders_click2));

                topMonthButton.setTextColor(getColor(R.color.white));
                topWeekButton.setTextColor(getColor(R.color.darkpurple));
                showEmptyBox();

            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(EXTRA_CATEGORY_ID_HELP)){
            catID = extras.getLong(EXTRA_CATEGORY_ID_HELP);
            Log.e("idecko: ",catID + "");
            category = db.getCategory(catID);
            nameOfCategory.setText(category.getName());
            nameOfCategory.setTextColor(Color.parseColor(category.getColorCategory()));

            myCount = db.getCountOfGratitudeUseingCategory(catID);
            countOfCategory.setText(String.valueOf(myCount));

            myAmount = db.getSumOfRateUseingCategory(catID);
            if (myCount!=0){
                vysl = ((double) myAmount/myCount);
                NumberFormat formatter = NumberFormat.getInstance(Locale.US);
                formatter.setMaximumFractionDigits(1);
                averageCaterogyRate.setText(formatter.format(vysl));
            }else{
                averageCaterogyRate.setText("0");
            }
        }

        if (db.getCountOfGratitudes(1) != 0){
            try {
                percentageTextView.setText(String.valueOf(getPercentage(db.getCountOfGratitudes(1),myCount)) + "%");
            }catch (Exception e){
                e.printStackTrace();
            }
        } else percentageTextView.setText("0%");


    }

    public long getPercentage(long celkem, long pocetVKategorii){
        long vysledek = Math.round(100*pocetVKategorii/celkem);
        Log.e("vysledek", String.valueOf(vysledek));
        return vysledek;
    }

    public void getList(int varianta) {
        //pripojeni a dostfani se k obsahu
        DatabaseHelper db = new DatabaseHelper(this);

        if (varianta == 1) {
            gratitudeList = db.getAllGratitude(6, null, catID);
            GratitudeAdapter dataAdapter = new GratitudeAdapter(gratitudeList);
            recyclerView.setAdapter(dataAdapter);
            if (gratitudeList.size() == 0){
                emptyBox = true;
            }else emptyBox = false;

        } else if (varianta == 2){
            gratitudeList = db.getAllGratitude(7,null, catID);
            GratitudeAdapter dataAdapter = new GratitudeAdapter(gratitudeList);
            recyclerView.setAdapter(dataAdapter);
            if (gratitudeList.size() == 0){
                emptyBox = true;
            }else emptyBox = false;
        }
    }

    public void showEmptyBox(){
        if (emptyBox){
            if (clickRightList){
                emptyBoxTextStat.setText("Žádné TOP radosti");
            }else if (clickLeftList){
                emptyBoxTextStat.setText("Žádné TOP radosti");
            }
            emptyBoxTextStat.setVisibility(View.VISIBLE);
            emptyBoxImageStat.setVisibility(View.VISIBLE);
        } else {
            emptyBoxTextStat.setVisibility(View.INVISIBLE);
            emptyBoxImageStat.setVisibility(View.INVISIBLE);
        }
    }


    //metoda prro tlačítko zpět
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            //ukončí aktivitu
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (clickLeftList){
            getList(1);
            showEmptyBox();
        }else if (clickRightList){
            getList(2);
        }else {
            getList(1);
            showEmptyBox();
        }
    }
}
