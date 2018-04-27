package cz.mendelu.xkopri10.bp.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.Category;
import cz.mendelu.xkopri10.bp.database.CategoryAdapter;
import cz.mendelu.xkopri10.bp.database.CategoryAdapterSettings;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import io.fabric.sdk.android.Fabric;

public class ChoosingCategorySettingsActivity extends AppCompatActivity {

    DatabaseHelper db;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CheckBox checkBox;
    private boolean emptyBox;
    private ImageView emptyBoxImage;
    private TextView emptyBoxText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_choosing_category_settings);
        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyBoxImage = (ImageView) findViewById(R.id.emptyBoxCat);
        emptyBoxText = (TextView) findViewById(R.id.emptyBoxCatText);

        recyclerView = (RecyclerView) findViewById(R.id.recycleViewSettingsChoosing);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(layoutManager);

        checkBox = (CheckBox) findViewById(R.id.checkBoxChoosingCategory);

        db = new DatabaseHelper(this);

        getList();
    }

    public void getList() {
        //pripojeni a dostfani se k obsahu
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Category> categories = db.getUsedCategory();
        if (categories.size() == 0){
            emptyBox = true;
        }else emptyBox = false;

        CategoryAdapterSettings dataAdapter= new CategoryAdapterSettings(categories);
        recyclerView.setAdapter(dataAdapter);
        showEmptyBox();
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
