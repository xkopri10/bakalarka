package cz.mendelu.xkopri10.bp.everythingUnderHelp;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import cz.mendelu.xkopri10.bp.InformationActivity;
import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.Category;
import cz.mendelu.xkopri10.bp.database.CategoryAdapter;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import io.fabric.sdk.android.Fabric;

public class HelpActivity extends AppCompatActivity {

    DatabaseHelper db;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CardView cardView, cardView2;
    TextView citeTextView, autorTextView;
    //LinearLayout linearLayout;
    private boolean emptyBox;
    private ImageView emptyBoxHelp;
    private TextView emptyBoxTextHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_help);

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyBoxHelp = (ImageView) findViewById(R.id.emptyBoxHelp);
        emptyBoxTextHelp = (TextView) findViewById(R.id.emptyBoxTextHelp);

        citeTextView = (TextView) findViewById(R.id.citeTextView);
        autorTextView = (TextView) findViewById(R.id.autorTextView);
        cardView = (CardView) findViewById(R.id.helpCardViewGratitude);
        cardView2 = (CardView) findViewById(R.id.cardView);
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewHelpActivity);
        //linearLayout = (LinearLayout) findViewById(R.id.temp);

        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);       //kvuli scrollingu (aby se nezacinalo scrollovat u recycle view)
        cardView2.requestFocus();               //ale u prvniho card view


        layoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this,HelpDetailRowGratitude.class);
                startActivity(intent);
            }
        });

        db = new DatabaseHelper(this);

        getList();
        //nacitani citatu
        try {
            loadJsonToTextView();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////

    public void loadJsonToTextView(){
        JSONObject obj = null;
        try {
            obj = new JSONObject(loadJSONFromAsset());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = obj.getJSONArray("polozky");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        //POZOR - zde musí být presný pocet polozek z quotes.json
        int n = random.nextInt(50)+1;
        JSONObject bla = null;
        try {
            bla = jsonArray.getJSONObject(n);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.e("log", "muj text: " + bla.getString("quote"));
            citeTextView.setText(bla.getString("quote"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.e("log", "muj text typ: " + bla.getString("autor"));
            autorTextView.setText(bla.getString("autor"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("quotes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //////////////////////////////////////////////////////////////////////////////


    public void getList() {
        //pripojeni a dostfani se k obsahu
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Category> categories = db.getAllCategory(1);
        if (categories.size() == 0){
            emptyBox = true;
        }else emptyBox = false;

        CategoryAdapter dataAdapter= new CategoryAdapter(categories,2);
        recyclerView.setAdapter(dataAdapter);
        showEmptyBox();
    }

    public void showEmptyBox(){
        if (emptyBox){
            emptyBoxTextHelp.setVisibility(View.VISIBLE);
            emptyBoxHelp.setVisibility(View.VISIBLE);
        } else {
            emptyBoxTextHelp.setVisibility(View.INVISIBLE);
            emptyBoxHelp.setVisibility(View.INVISIBLE);
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
