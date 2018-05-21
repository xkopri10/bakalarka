package cz.mendelu.xkopri10.bp.everythingUnderAdd;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.Category;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import io.fabric.sdk.android.Fabric;
import petrov.kristiyan.colorpicker.ColorPicker;

public class CategoryActivity extends AppCompatActivity {

    public static final String EXTRA_CAT_ID = "xxxxxxxxxxxxxxxxxx";
    public static final String EXTRA_NOVA_KAT = "nova";
    public static final String EXTRA_COLOR = "coloris";
    public static final String EXTRA_NAME_CAT = "namis";

    public static final String DEFAULT_COLOR = "#9D9476";

    public DatabaseHelper databaseHelper;
    public EditText editTextNameCategory;
    private Button colorButton;
    private String myColor ; //= DEFAULT_COLOR;// = "#E0E0E0"; //defaultni barva pokud uzivatel neprida svoji konkretni

    public long mojeID;
    private String mojeBarva, barvaYY, nazev, mujNamis;
    private Category cat;
    private boolean editace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_category);


        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //připojení db
        databaseHelper = new DatabaseHelper(this);
        editTextNameCategory = (EditText) findViewById(R.id.editTextNameCategory);

        //pro color picker
        colorButton = (Button) findViewById(R.id.colorPickerButton);
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        //pro poslani id z minule aktivity
        bundleFromPreviousActivity();
        if (editace){
            myColor = mojeBarva;
        }else myColor = DEFAULT_COLOR;

    }

    //metoda prro tlačítko zpět
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            //ukončí aktivitu
            this.finish();
        } else if (id == R.id.check) {
            Category category = new Category();
            category.setName(editTextNameCategory.getText().toString());
            category.setColorCategory(myColor);
            category.setSelectedState(0);   //nastavim na 0 pri tvorbe - a menim az v astaveni notifikaci

            nazev = category.getName();
            barvaYY = category.getColorCategory();

            if (editace) {
                if (editTextNameCategory.getText().toString().matches("")){
                    //Toast.makeText(this, "Vyplň název kategorie", Toast.LENGTH_LONG).show();   //napisu hlasku
                    editTextNameCategory.setError("Vyplň název kategorie");
                }else {
                    try {
                        databaseHelper.updateCategories1(mojeID, editTextNameCategory.getText().toString(), myColor);
                        this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (nazev.matches("")) {
                    //Toast.makeText(this, "Vyplň název kategorie", Toast.LENGTH_LONG).show();   //napisu hlasku
                    editTextNameCategory.setError("Vyplň název kategorie");
                } else{
                    databaseHelper.createCategory(category);
                    Toast.makeText(this, "Kategorie vytvořena", Toast.LENGTH_LONG).show();   //napisu hlasku
                    this.finish();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_add_menu,menu);
        return true;
    }

    public void bundleFromPreviousActivity(){
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(EXTRA_CAT_ID) && extras.containsKey(EXTRA_COLOR) && extras.containsKey(EXTRA_NAME_CAT)) {
            editace = true;

            mojeBarva = (extras.getString(EXTRA_COLOR));
            mojeID = (extras.getLong(EXTRA_CAT_ID));
            mujNamis = (extras.getString(EXTRA_NAME_CAT));

            Log.e("ID vybrane kategorie", String.valueOf(mojeID));
            Log.e("barvicka", mojeBarva);

            cat = databaseHelper.getCategory(mojeID);
            editTextNameCategory.setText(cat.getName());
            colorButton.setBackgroundColor(Color.parseColor(cat.getColorCategory()));

        }else if (extras.containsKey(EXTRA_NOVA_KAT)){
            editace = false;
        }
    }


    public void openColorPicker(){
        final ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setTitle("Vyber barvu");
        final ArrayList<String> colors = new ArrayList<>();

        colors.add("#DB504A");      //cervena
        colors.add("#f97171");      //cervenoruzova
        colors.add("#ff9800");      //oranzova

        colors.add("#66923c");      //zelena
        colors.add("#94b359");      //tmave zelena
        colors.add("#247ba0");      //modra
        colors.add("#66beb2");
        colors.add("#57457F");      //fialova svetla
        colors.add("#8d6e63");      //hneda svetla
        colors.add("#9c9a9a");      //seda

        colorPicker.setColors(colors)
          .setColumns(5)
          .setRoundColorButton(true)
          .setOnFastChooseColorListener(
              new ColorPicker.OnFastChooseColorListener() {
              @Override
              public void setOnFastChooseColorListener(int position, int color)
              {
                  myColor = colors.get(position);
                  colorButton.setBackgroundColor(Color.parseColor(myColor));
              }

              @Override
              public void onCancel() {
              }
          })
         .show();
    }


}
