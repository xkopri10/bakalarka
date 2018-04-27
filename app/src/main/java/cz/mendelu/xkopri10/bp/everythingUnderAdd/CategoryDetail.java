package cz.mendelu.xkopri10.bp.everythingUnderAdd;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.Category;
import cz.mendelu.xkopri10.bp.database.CategoryAdapter;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Type;
import cz.mendelu.xkopri10.bp.database.TypeAdapter;
import io.fabric.sdk.android.Fabric;

public class CategoryDetail extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_ID = "cz.mendelu.xkopri10.bp.extraDetailID";

    private DatabaseHelper db;
    private TextView titleCategory;
    private Category cat;
    public long idecko;
    private boolean emptyBox;
    private ImageView emptyFolder;
    private TextView emptyFolderText;

    private ListView listView;
    private String mujNazev;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_category_detail);

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyFolder = (ImageView) findViewById(R.id.emptyFolder);
        emptyFolderText = (TextView) findViewById(R.id.emptyFolderText);

        //pro zobrazeni seznamu
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewType);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(layoutManager);

        db = new DatabaseHelper(this);

        titleCategory = (TextView) findViewById(R.id.categoryTitleDetail);
        recyclerView.invalidate();

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(EXTRA_CATEGORY_ID)){
            long mojeID = (extras.getLong(EXTRA_CATEGORY_ID));
            idecko = mojeID;
            Log.e("idecko MojeID-CatDetail", String.valueOf(idecko));
            cat = db.getCategory(mojeID);
            titleCategory.setText(cat.getName());
            mujNazev = cat.getName();
            titleCategory.setTextColor(Color.parseColor(cat.getColorCategory()));
        }


        //floating tlačítko
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatButtonAddType);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TypeActivity.class);
                intent.putExtra("idecko",idecko);
                startActivity(intent);
            }
        });

        getList();
        showEmptyBox();

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

    public void getList() {
        //pripojeni a dostfani se k obsahu
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Type> types = db.getAllTypesByCategory(mujNazev);
        if (types.size() == 0){
            emptyBox = true;
        }else emptyBox = false;

        TypeAdapter dataAdapter= new TypeAdapter(types);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.invalidate();
        showEmptyBox();
    }

    public void showEmptyBox(){
        if (emptyBox){
            emptyFolderText.setVisibility(View.VISIBLE);
            emptyFolder.setVisibility(View.VISIBLE);
        } else {
            emptyFolderText.setVisibility(View.INVISIBLE);
            emptyFolder.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }
}
