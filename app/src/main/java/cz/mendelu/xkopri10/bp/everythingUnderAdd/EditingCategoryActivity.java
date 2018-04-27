package cz.mendelu.xkopri10.bp.everythingUnderAdd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.Category;
import cz.mendelu.xkopri10.bp.database.CategoryAdapter;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.GratitudeAdapter;
import io.fabric.sdk.android.Fabric;

public class EditingCategoryActivity extends AppCompatActivity {

    DatabaseHelper db;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ImageView emptyFolder;
    private TextView emptyFolderText;
    private boolean emptyBox;

    public int RESULT_BACK =99874;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_editing_category);

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyFolder = (ImageView) findViewById(R.id.emptyFolder1);
        emptyFolderText = (TextView) findViewById(R.id.emptyFolderText1);


        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(layoutManager);

        db = new DatabaseHelper(this);

        //floating tlačítko
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean nova = true;
                Intent intent = new Intent(view.getContext(),CategoryActivity.class);
                intent.putExtra(CategoryActivity.EXTRA_NOVA_KAT,nova);
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
            Intent returnIntent = new Intent();
            returnIntent.putExtra("flag",1);
            setResult(RESULT_BACK,returnIntent);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getList() {
        //pripojeni a dostfani se k obsahu
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Category> categories = db.getAllCategory(1);
        if (categories.size() == 0){
            emptyBox = true;
        }else emptyBox = false;

        CategoryAdapter dataAdapter= new CategoryAdapter(categories,1);
        recyclerView.setAdapter(dataAdapter);
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
