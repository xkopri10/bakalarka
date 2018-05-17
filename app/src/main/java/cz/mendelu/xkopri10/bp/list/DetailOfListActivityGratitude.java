package cz.mendelu.xkopri10.bp.list;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Greatfulness;
import io.fabric.sdk.android.Fabric;

public class DetailOfListActivityGratitude extends AppCompatActivity {

    public static final String EXTRA_GREATFULNESS_DETAIL = "cz.mendelu.xkopri10.bp.extraDetailGratfulness";

    long greatID;
    private DatabaseHelper db;
    private Greatfulness greatfulness;
    private String datumek;
    private TextView dateview, noteView;
    private View colorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_detail_of_list_gratitude);

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        dateview = (TextView)findViewById(R.id.dateViewGreatfulnessDetail);
        noteView = (TextView)findViewById(R.id.noteViewGreatfulnessDetail);
        colorView = (View) findViewById(R.id.colorviewGrat);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(EXTRA_GREATFULNESS_DETAIL)) {

            greatID = extras.getLong(EXTRA_GREATFULNESS_DETAIL);
            Log.e("idecko: ", greatID + "");
            greatfulness = db.getGreatfulness(greatID);

            datumek = konverze(greatfulness.getDateGreatfulness());

            dateview.setText(datumek);
            noteView.setText(greatfulness.getNoteGreatfulness());
        }

        colorView.setBackgroundColor(getColor(R.color.gold3));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.detail_list_menu,menu);
        return true;
    }
}
