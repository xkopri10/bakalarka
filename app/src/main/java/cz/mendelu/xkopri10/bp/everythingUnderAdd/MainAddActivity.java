package cz.mendelu.xkopri10.bp.everythingUnderAdd;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.Category;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Gratitude;
import cz.mendelu.xkopri10.bp.database.Greatfulness;
import cz.mendelu.xkopri10.bp.database.Type;
import cz.mendelu.xkopri10.bp.settings.SettingActivity;
import io.fabric.sdk.android.Fabric;

public class MainAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner1, spinner2;
    private String vyhlJmeno, vyhlJmenoTyp;

    private SeekBar seekBar;
    private TextView textViewScale, textViewDate, textViewNote2, categoryTittleTextView, typeTittleTextView, ratingTittleTextView, noteTittleTextView, noTypeTextView, noCategoryTextView;
    public EditText editTextNote, editTextNote2;

    private int hodnoceni = 5;  //inicializace defaultní hodnoty (koresponduje s počátečním setProgress

    private FloatingActionButton fab1, fab2, fab3;
    private Animation fabopen, fabclose, fabrotate, fabnorotate;
    private boolean isOpen = false, newPleasure;
    private long mID, myIdType, ideckoTypu;
    private Button addPleasure, addGratitude;
    private String xxxxx, poznamka, myPath, mCurrentPhotoPath,cestaKFotce;
    private DatabaseHelper db;

    private ScrollView pleasureScrollView;
    private LinearLayout linearLayout,linearLayout2;
    private Button addCategoryButton;

    private ImageView imageView;
    protected CardView cardView, firstCard, secondCard, thirdCard, fourthCard;
    public int CAMERA_REQUEST =10985, SELECT_FILE = 58745,  RESULT_BACK =99874;
    CameraPhoto cameraPhoto;
    private byte[] byteArray;
    Bitmap bmp;
    View viewXXXXX;
    ConstraintLayout constraintLayout;
    RelativeLayout obstructor;
    boolean refreshNow;
    private Menu menu;
    private boolean pressed, jePrazdny, jePrazdnyCat;
    Spinner typeSpinner, categorySpinner;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main_add);

        newPleasure = true; //kvuli nastaveni 1. stranky na RADOST - a hned vyplnování
        //definuje objekty pro práci
        definitionObjects();
        disableScrolling(2);

        //připojení db
        db = new DatabaseHelper(this);

        getAlertWhenCountIsMax(1);

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //nastav do setview datum - pokud se to nepovede tak vyhoď vyjímku
        try {
            //xxxxx = "2018-04-02";
            xxxxx = db.getTodayDate();
            konverze(); //puvodni datum na upravený datum
            Log.e("xxxxx: ",xxxxx);
            Log.e("konverze: ",konverze());
            textViewDate.setText(konverze());
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        }

        //práce se spinnerem č1 - spinner2 se načítá uvnitř spinneru1
        loadSpinnerData();
        settingVisibleSpinner();

        //práce se scale barem na hodnocení
        textViewScale.setText(""+hodnoceni);
        seekBar.setMax(10);
        seekBar.setProgress(5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                hodnoceni = seekBar.getProgress();
                textViewScale.setText(""+hodnoceni);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        addPleasure.setBackground(getDrawable(R.drawable.button_borders_click2));
        addGratitude.setBackground(getDrawable(R.drawable.button_borders));

        addGratitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableScrolling(1);
                hideKeyboard();
                newPleasure = false;
                getAlertWhenCountIsMax(2);

                //pro radost
                linearLayout.setVisibility(View.VISIBLE);
                textViewDate.setVisibility(View.VISIBLE);
                fourthCard.setVisibility(View.VISIBLE);

                spinner1.setVisibility(View.INVISIBLE);
                spinner2.setVisibility(View.INVISIBLE);
                textViewScale.setVisibility(View.INVISIBLE);
                seekBar.setVisibility(View.INVISIBLE);
                editTextNote.setVisibility(View.INVISIBLE);
                fab1.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                addCategoryButton.setVisibility(View.INVISIBLE);
                categoryTittleTextView.setVisibility(View.INVISIBLE);
                typeTittleTextView.setVisibility(View.INVISIBLE);
                ratingTittleTextView.setVisibility(View.INVISIBLE);
                noteTittleTextView.setVisibility(View.INVISIBLE);
                cardView.setVisibility(View.INVISIBLE);
                //pro vděčnost
                textViewNote2.setVisibility(View.VISIBLE);
                editTextNote2.setVisibility(View.VISIBLE);

                addGratitude.setBackground(getDrawable(R.drawable.button_borders_click));
                addPleasure.setBackground(getDrawable(R.drawable.button_borders2));
                addGratitude.setTextColor(Color.WHITE);
                addPleasure.setTextColor(getColor(R.color.darkpurple));

                firstCard.setVisibility(View.INVISIBLE);
                secondCard.setVisibility(View.INVISIBLE);
                thirdCard.setVisibility(View.INVISIBLE);

                noTypeTextView.setVisibility(View.INVISIBLE);
            }
        });



        addPleasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableScrolling(2);
                hideKeyboard();
                getAlertWhenCountIsMax(1);

                newPleasure = true;
                //pro radost
                linearLayout.setVisibility(View.VISIBLE);
                textViewDate.setVisibility(View.VISIBLE);
                spinner1.setVisibility(View.VISIBLE);
                spinner2.setVisibility(View.VISIBLE);
                textViewScale.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
                editTextNote.setVisibility(View.VISIBLE);
                fab1.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                addCategoryButton.setVisibility(View.VISIBLE);
                categoryTittleTextView.setVisibility(View.VISIBLE);
                typeTittleTextView.setVisibility(View.VISIBLE);
                ratingTittleTextView.setVisibility(View.VISIBLE);
                noteTittleTextView.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
                //pro vdecnost
                textViewNote2.setVisibility(View.INVISIBLE);
                editTextNote2.setVisibility(View.INVISIBLE);


                addPleasure.setBackground(getDrawable(R.drawable.button_borders_click2));
                addGratitude.setBackground(getDrawable(R.drawable.button_borders));
                addPleasure.setTextColor(Color.WHITE);
                addGratitude.setTextColor(getColor(R.color.darkpurple));

                firstCard.setVisibility(View.VISIBLE);
                secondCard.setVisibility(View.VISIBLE);
                thirdCard.setVisibility(View.VISIBLE);
                fourthCard.setVisibility(View.INVISIBLE);

                //noTypeTextView.setVisibility(View.INVISIBLE);

                if (!jePrazdnyCat && jePrazdny){
                    spinner2.setVisibility(View.INVISIBLE);
                    noTypeTextView.setVisibility(View.VISIBLE);
                }else if (!jePrazdnyCat && jePrazdny){
                    spinner1.setVisibility(View.INVISIBLE);
                    spinner2.setVisibility(View.INVISIBLE);
                    noCategoryTextView.setVisibility(View.VISIBLE);
                    noTypeTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        //práce s tlačítkem fotografie a jeho animace
        floatingButtonDefinition();

        editTextNote.setFocusable(false);

        editTextNote.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                editTextNote.setFocusableInTouchMode(true);
                return false;
            }
        });

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (editTextNote.isFocused()) {
                        Rect outRect = new Rect();
                        editTextNote.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)motionEvent.getRawX(), (int)motionEvent.getRawY())) {
                            editTextNote.clearFocus();
                            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                }
                editTextNote.setFocusable(false);
                return false;
            }
        });

        obstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrClosedObstructorAndFloatButtonFirst();
            }
        });
    }

    public void getAlertWhenCountIsMax(int type){
        if (type == 1) {
            if (db.getCountOfGratitudes(2) == 3) {
                getAlertDialog(1);
                seekBar.setEnabled(false);
                spinner1.setEnabled(false);
                spinner2.setEnabled(false);
                editTextNote.setEnabled(false);
                editTextNote.setHint("Dnes jsi již přidal/a 3 radosti. Další můžeš už zítra.");
                fab1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.second_grey)));
                fab1.setEnabled(false);
            }
        }else if (type == 2){
            if (db.getCountOfGreatfulness(2) == 1) {
                editTextNote2.setEnabled(false);
                editTextNote2.setHint("Dnes jsi již vděčnost přidal/a, další můžeš už zítra.");
                getAlertDialog(2);
            }
        }
    }

    public void getAlertDialog(int varianta){
        View viewAlertDialog = null;
        LayoutInflater layoutInflater = LayoutInflater.from(MainAddActivity.this);
        if (varianta == 1){
            viewAlertDialog = layoutInflater.inflate(R.layout.alert_dialog_warning_pleasure, null);
        }else if (varianta == 2){
            viewAlertDialog = layoutInflater.inflate(R.layout.alert_dialog_warning, null);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainAddActivity.this);
        alertDialogBuilder.setView(viewAlertDialog);
        alertDialogBuilder.setPositiveButton("ROZUMÍM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        myPath = mCurrentPhotoPath;
        Log.e("ulozeno v: ", myPath + "");
        return image;
    }


    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "cz.mendelu.xkopri10.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    boolean done;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == SELECT_FILE) {
                refreshNow = false;
                final Uri selectedUri = data.getData();
                Log.e("taham z mista....", selectedUri +"");
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                final File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    if(!storageDirectory.exists())
                        storageDirectory.mkdirs();
                }catch (Exception e){
                    e.printStackTrace();
                }

                //progress dialog - bezi jen v pripade ze se fotografie načítá
                final ProgressDialog ringProgressDialog = ProgressDialog.show(this, "Načítání fotografie", "Prosím čekejte", true);
                ringProgressDialog.setCancelable(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myFilePath = new File(storageDirectory, "IMG_"+timestamp+".JPG");
                        FileOutputStream fileOutput = null;
                        try {
                            fileOutput = new FileOutputStream(myFilePath);
                            bmp.compress(Bitmap.CompressFormat.PNG, 1, fileOutput);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }finally {
                            try {
                                fileOutput.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(MainAddActivity.this)
                                        .load(myFilePath)
                                        .fit()
                                        .centerCrop()
                                        .into(imageView);

                                Log.e("","image saved to >>>" + myFilePath.getAbsolutePath());
                                myPath = myFilePath+"";
                                Log.e("ulozeno v: ", myFilePath + "");
                                Toast.makeText(MainAddActivity.this, "Fotografie načtena", Toast.LENGTH_LONG).show();
                                done = true;
                                ringProgressDialog.dismiss();
                            }
                        });
                    }
                }).start();

            } else if (resultCode == RESULT_OK) {
                final ProgressDialog ringProgressDialog = ProgressDialog.show(this, "Načítání fotografie", "Prosím čekejte", true);
                ringProgressDialog.setCancelable(false);
                refreshNow = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(MainAddActivity.this)
                                        .load(new File(mCurrentPhotoPath))
                                        .fit()
                                        .centerCrop()
                                        .into(imageView);
                                myPath = mCurrentPhotoPath;
                                Toast.makeText(MainAddActivity.this, "Fotografie načtena", Toast.LENGTH_LONG).show();
                                ringProgressDialog.dismiss();
                            }
                        });
                    }
                }).start();
            } else if (requestCode == 1){
                if (resultCode == RESULT_BACK ){
                    if(data.getIntExtra("flag",0)==1){
                        refreshNow = true;
                    }
                }
            }
        }
    }

    public void showOrClosedObstructorAndFloatButtonFirst(){
        if (isOpen){
            pressed = false;
            onPeresFloatButtonHideCheck();

            obstructor.setVisibility(View.INVISIBLE);
            fab2.startAnimation(fabclose);
            fab3.startAnimation(fabclose);
            fab1.startAnimation(fabnorotate);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isOpen = false;
        }else{
            pressed = true;
            onPeresFloatButtonHideCheck();

            obstructor.setVisibility(View.VISIBLE);
            fab2.startAnimation(fabopen);
            fab3.startAnimation(fabopen);
            fab1.startAnimation(fabrotate);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isOpen = true;
        }
    }

    public void floatingButtonDefinition(){
        fabopen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabclose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabrotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate);
        fabnorotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_anti_rotate);
        fab1.setVisibility(View.VISIBLE);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrClosedObstructorAndFloatButtonFirst();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_FILE);
                obstructor.setVisibility(View.INVISIBLE);
                pressed = false;
                onPeresFloatButtonHideCheck();

                fab2.startAnimation(fabclose);
                fab3.startAnimation(fabclose);
                fab1.startAnimation(fabnorotate);
                fab2.setClickable(false);
                fab3.setClickable(false);
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
                obstructor.setVisibility(View.INVISIBLE);
                pressed = false;
                onPeresFloatButtonHideCheck();

                fab2.startAnimation(fabclose);
                fab3.startAnimation(fabclose);
                fab1.startAnimation(fabnorotate);
                fab2.setClickable(false);
                fab3.setClickable(false);
            }
        });


    }

    public void disableScrolling(final int varianta){
        pleasureScrollView.setOnTouchListener( new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (varianta == 1)
                return true;
                else return false;
            }
        });
    }

    //metoda pro skrytí klávesnice při přejíždění z TABu Vděčnost na Radost to házelo Warning
    public void hideKeyboard() {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void definitionObjects(){

        constraintLayout = (ConstraintLayout) findViewById(R.id.pleasureLayout) ;
        obstructor = (RelativeLayout) findViewById(R.id.obstructor);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        noTypeTextView = (TextView) findViewById(R.id.noTypeTextView);
        noCategoryTextView = (TextView) findViewById(R.id.noCategoryTextView);
        firstCard = (CardView) findViewById(R.id.firstCard);
        secondCard = (CardView) findViewById(R.id.secondCard);
        thirdCard = (CardView) findViewById(R.id.thirdCard);
        fourthCard = (CardView) findViewById(R.id.fourthCard);
        cardView = (CardView) findViewById(R.id.cardViewForPhotoAddActivity);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        textViewScale = (TextView) findViewById(R.id.scaleTextView);
        addGratitude = (Button) findViewById(R.id.addGratitude);
        addPleasure = (Button) findViewById(R.id.addPleasure);
        spinner2 = (Spinner) findViewById(R.id.typeSpinner);
        spinner1 = (Spinner) findViewById(R.id.categorySpinner);
        seekBar = (SeekBar) findViewById(R.id.scaleSekkBar);
        textViewDate = (TextView)findViewById(R.id.dateTextView);
        pleasureScrollView = (ScrollView) findViewById(R.id.pleasureScrollView);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout2);
        imageView = (ImageView) findViewById(R.id.imageView);
        addCategoryButton = (Button) findViewById(R.id.add_category);
        categoryTittleTextView = (TextView) findViewById(R.id.categoryTittleTextView);
        typeTittleTextView = (TextView) findViewById(R.id.typeTittleTextView);
        ratingTittleTextView = (TextView) findViewById(R.id.ratingTittleTextView);
        noteTittleTextView = (TextView) findViewById(R.id.noteTittleTextView);
        fab1 = (FloatingActionButton) findViewById(R.id.addPhotoButton);
        fab2 = (FloatingActionButton) findViewById(R.id.galleryButton);
        fab3 = (FloatingActionButton) findViewById(R.id.cameraButton);
        editTextNote = (EditText) findViewById(R.id.noteEditText);
        editTextNote2 = (EditText) findViewById(R.id.editTextGrat2);
        textViewNote2 = (TextView) findViewById(R.id.noteTittleTextViewGratitude);
    }

    public void onPeresFloatButtonHideCheck(){
        if (pressed){
            MenuItem menuItem = menu.findItem(R.id.check);
            menuItem.setVisible(false);
        }else {
            MenuItem menuItem = menu.findItem(R.id.check);
            menuItem.setVisible(true);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        if (pressed){
            menu.findItem(R.id.check).setVisible(false);
        }else {
            menu.findItem(R.id.check).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_add_menu,menu);
        return true;
    }

    //metoda pro kliknutí na tlačítko EDITACE KATEGORIE
    public void onClickEditCategory(View view){
        Intent intent = new Intent(this,EditingCategoryActivity.class);
        startActivityForResult(intent,1);
    }


    //metoda pro spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        Spinner spinner1 = (Spinner)parent;
        Spinner spinner2 = (Spinner)parent;
        if(spinner1.getId() == R.id.categorySpinner)
        {
            myIdType = -1;  //zde musim vynulovat ID - aby jsem odstranil predchazejici ID typu v pripade ze kateorie nema typ
            String label = parent.getItemAtPosition(position).toString();
            vyhlJmeno = label;
            mID = db.getIdCategoryByName(vyhlJmeno);
            Log.e("Spinner kategorie: ",vyhlJmeno);
            Log.e("Spinner ID kategorie: ",mID+"");

            loadSpinnerData2(); //zde MUSÍM načítat 2. spinner -nikde jinde to nefunguje
            //pokud je spinner 2 (typový) prázdný pak se skryje a zobrazi text
            settingViewSpinnerType();

        }
        if(spinner2.getId() == R.id.typeSpinner) {
            String label = parent.getItemAtPosition(position).toString();

            vyhlJmenoTyp = label;
            Log.e("Spinner typ: ",label);

                myIdType = db.getIdTypeByName(vyhlJmenoTyp);
                Log.e("Spinner ID typu: ",myIdType+"");
        }
    }


    //metoda pro spinner
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

        }

    public void loadSpinnerData() {
        // dostání a připojení databáze
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        // Spinner drop down elemt - vyfiltrování všech kategorií
        List<Category> categories = db.getAllCategory(1);

        String[] nameList=new String[categories.size()];
        for(int i=0;i<categories.size();i++){
            nameList[i]=categories.get(i).getName(); //create array of name
        }
        //pokud je prazdny tak se v onSelected zakaze (nezobrazi se)
        if (categories.size() == 0){
            jePrazdnyCat = true;
        }else if (categories.size() == 1){
            refreshNow = true;
            jePrazdnyCat = false;
        }else jePrazdnyCat = false;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // připojení adaptéru na spinner
        spinner1.setAdapter(dataAdapter);
        spinner1.setOnItemSelectedListener(this);
    }

    public void loadSpinnerData2() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Type> types = db.getAllTypesByCategory(vyhlJmeno);

        String[] nameList=new String[types.size()];
        for(int i=0;i<types.size();i++){
            nameList[i]=types.get(i).getName(); //create array of name
        }
        //pokud je prazdny tak se v onSelected zakaze (nezobrazi se)
        if (types.size() == 0){
            jePrazdny = true;
        }else jePrazdny = false;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
        spinner2.setOnItemSelectedListener(this);
    }

    //metoda pro tlačítko zpět
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            //ukončí aktivitu
            this.finish();
        } else if (id == R.id.check){

            if (newPleasure){
                Gratitude gratitude = new Gratitude();
                //gratitude.setDate(textViewDate.getText().toString());
                gratitude.setDate(xxxxx);
                gratitude.setIdCategory(mID);
                //if mtype id != -1...
                gratitude.setIdType(myIdType);
                gratitude.setNote(editTextNote.getText().toString());
                gratitude.setRate(hodnoceni);
                gratitude.setPath(myPath);

                ideckoTypu = gratitude.getIdType();
                Log.e("idecko typu XXX: ",ideckoTypu+"");

                cestaKFotce = gratitude.getPath();
                if (cestaKFotce == null){
                    gratitude.setPath("nenalezeno");
                }

                poznamka = gratitude.getNote();
                if (poznamka.matches("")){
                   // Toast.makeText(this, "Vyplň poznámku", Toast.LENGTH_LONG).show();   //napisu hlasku
                    editTextNote.setError("Vyplň poznámku");
                }else if (jePrazdnyCat) {
                    Toast.makeText(this, "Zadej kategorii", Toast.LENGTH_LONG).show();   //napisu hlasku
                } else{
                    try {
                        db.createGratitude(gratitude);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(this, "Uloženo", Toast.LENGTH_LONG).show();   //napisu hlasku
                    this.finish();
                }
            }else {
                Greatfulness greatfulness = new Greatfulness();
                greatfulness.setDateGreatfulness(xxxxx);
                greatfulness.setNoteGreatfulness(editTextNote2.getText().toString());
                greatfulness.setColorGreatfulness("#F0AE00");

                poznamka = greatfulness.getNoteGreatfulness();

                if (poznamka.matches("")) {
                    //Toast.makeText(this, "Vyplň poznámku", Toast.LENGTH_LONG).show();   //napisu hlasku
                    editTextNote2.setError("Vyplň poznámku");
                } else {
                    try {
                        db.createGreatfulness(greatfulness);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(this, "Uloženo", Toast.LENGTH_LONG).show();   //napisu hlasku
                    this.finish();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public String konverze(){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
        String inputDateStr= xxxxx;
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public void settingVisibleSpinner(){
        if (jePrazdnyCat){
            noCategoryTextView.setVisibility(View.VISIBLE);
            categorySpinner.setVisibility(View.INVISIBLE);
            typeSpinner.setVisibility(View.INVISIBLE);
        } else {
            noCategoryTextView.setVisibility(View.INVISIBLE);
            categorySpinner.setVisibility(View.VISIBLE);
            typeSpinner.setVisibility(View.VISIBLE);
        }
    }

    public void settingViewSpinnerType(){
        if (jePrazdny){
            typeSpinner.setVisibility(View.INVISIBLE);
            noTypeTextView.setVisibility(View.VISIBLE);
        }else{
            typeSpinner.setVisibility(View.VISIBLE);
            noTypeTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refreshNow){
            loadSpinnerData();
            settingVisibleSpinner();
        }
    }
}


