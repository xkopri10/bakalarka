package cz.mendelu.xkopri10.bp.everythingUnderHelp;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.math.BigDecimal;
import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.GratitudeAdapter;
import cz.mendelu.xkopri10.bp.database.Greatfulness;
import cz.mendelu.xkopri10.bp.database.GreatfulnessAdapter;
import io.fabric.sdk.android.Fabric;

public class HelpDetailRowGratitude extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView textViewCount, percentageTextView, emptyBoxText;
    private ImageView emptyBoxImage;
    private DatabaseHelper db;
    List<Greatfulness> greatfulnessList;

    private long myCount, myCountGratitudes, summarize;
    private boolean emptyBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_help_detail_row_gratitude);

        db = new DatabaseHelper(this);
        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyBoxImage = (ImageView) findViewById(R.id.emptyBoxImageG);
        emptyBoxText = (TextView) findViewById(R.id.emptyBoxTextG);

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerStatisticsGreatfulness);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // Nastaví recycleview na to aby se nescrolloval
                return rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
            }
        });

        textViewCount = (TextView) findViewById(R.id.countOfGreatfulness);
        percentageTextView = (TextView) findViewById(R.id.percentageGreat);
        myCount = db.getCountOfGreatfulness(1);
        myCountGratitudes = db.getCountOfGratitudes(1);
        textViewCount.setText(String.valueOf(myCount));

        summarize = myCount + myCountGratitudes;
        if (summarize !=0){
            try {
                percentageTextView.setText(String.valueOf(getPercentage(summarize,myCount))+"%");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else percentageTextView.setText("0%");

        getList();
        showEmptyBox();
    }

    public long getPercentage(long celkem, long pocetVdecnosti){
        long vysledek = (long) Math.ceil(100*pocetVdecnosti/celkem);
        return vysledek;
    }

    public void getList() {
        //pripojeni a dostfani se k obsahu
        DatabaseHelper db = new DatabaseHelper(this);
        greatfulnessList = db.getAllGreatfulness(4,null);
        if (greatfulnessList.size() == 0){
            emptyBox = true;
        }else emptyBox = false;
        GreatfulnessAdapter dataAdapter = new GreatfulnessAdapter(greatfulnessList);
        recyclerView.setAdapter(dataAdapter);
    }

    public void showEmptyBox(){
        if (emptyBox){
            emptyBoxText.setVisibility(View.VISIBLE);
            emptyBoxImage.setVisibility(View.VISIBLE);
        } else {
            emptyBoxText.setVisibility(View.INVISIBLE);
            emptyBoxImage.setVisibility(View.INVISIBLE);
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

}
