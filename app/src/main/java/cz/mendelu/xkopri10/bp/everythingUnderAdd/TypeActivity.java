package cz.mendelu.xkopri10.bp.everythingUnderAdd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.Category;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Type;
import io.fabric.sdk.android.Fabric;

public class TypeActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_ID = "cz.mendelu.xkopri10.bp.extraDetailID";

    private DatabaseHelper db;
    private EditText editTextNameType;

    long myId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_type);

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        editTextNameType = (EditText) findViewById(R.id.editTextNameType);
    }

    //metoda prro tlačítko zpět
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            //ukončí aktivitu
            this.finish();
        } else if (id == R.id.check_type){
            Type type = new Type();
            type.setName(editTextNameType.getText().toString());
            String nazev = type.getName();
            if (nazev.matches("")){
                //Toast.makeText(this, "Vyplň název typu", Toast.LENGTH_LONG).show();   //napisu hlasku
                editTextNameType.setError("Vyplň název typu");
            }else {
                Intent mujIntent = getIntent();
                long myId = mujIntent.getLongExtra("idecko", 0);
                Log.e("id kategorie je : ", String.valueOf(myId));

                db.createType(type, myId);
                Toast.makeText(this, "Typ vytvořen", Toast.LENGTH_LONG).show();   //napisu hlasku
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.type_add_menu,menu);
        return true;
    }


}
