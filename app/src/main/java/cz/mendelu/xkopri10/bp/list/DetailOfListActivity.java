package cz.mendelu.xkopri10.bp.list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.mendelu.xkopri10.bp.InformationActivity;
import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.Category;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Gratitude;
import cz.mendelu.xkopri10.bp.database.Type;
import cz.mendelu.xkopri10.bp.everythingUnderAdd.CategoryActivity;
import cz.mendelu.xkopri10.bp.notifications.NotificationRecieverMotivation;
import io.fabric.sdk.android.Fabric;

import static cz.mendelu.xkopri10.bp.list.DetailPhotoGalleryActivity.EXTRA_GALLERY;

public class DetailOfListActivity extends AppCompatActivity {

    public static final String EXTRA_DETAIL_OF_LIST = "cz.mendelu.xkopri10.bp.extraDetailListID";
    public static final String EXTRA_ID_NOT = "drbnuteIdeckoZnotifikaci";

    private ImageView imageViewDetail;
    private View colorView;
    private DatabaseHelper db;
    private TextView dateview, categoryview, typeview, rateview, noteview;
    private String datumek;
    private CardView cardViewWithImageView;

    private Gratitude gratitude;
    private Category category;
    private Type type;
    private long graID;
    Boolean openGalery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_detail_of_list);

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = new DatabaseHelper(this);
        dateview = (TextView)findViewById(R.id.textViewDetailDate);
        categoryview = (TextView)findViewById(R.id.textViewDetailCategory);
        typeview = (TextView)findViewById(R.id.textViewDetailType);
        rateview = (TextView)findViewById(R.id.textViewDetailRate);
        noteview = (TextView)findViewById(R.id.textViewDetailNote);
        imageViewDetail = (ImageView) findViewById(R.id.imageViewPhotoDetail);
        cardViewWithImageView = (CardView) findViewById(R.id.cardViewWithImageView);
        colorView = (View) findViewById(R.id.colorview);

        bundlesFromActivities();

        imageViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long idecko = gratitude.getId();
                Intent intent = new Intent(DetailOfListActivity.this,DetailPhotoGalleryActivity.class);
                intent.putExtra(DetailPhotoGalleryActivity.EXTRA_GALLERY,idecko);
                startActivity(intent);
            }
        });
    }

    public void bundlesFromActivities(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // pro kliknutí na notifikaci
            if (extras.containsKey(EXTRA_ID_NOT)) {
                graID = getIntent().getLongExtra(EXTRA_ID_NOT,1);
                Log.e("idecko z notifikace: ", graID + "");
                setAtributes(graID);
            } else
            // pro kliknutí ze seznamu na položku
            if (extras.containsKey(EXTRA_DETAIL_OF_LIST)){
                graID = extras.getLong(EXTRA_DETAIL_OF_LIST);
                Log.e("idecko ze seznamu: ",graID + "");
                setAtributes(graID);

            } else if (extras.containsKey(EXTRA_GALLERY)){
                openGalery = true;
            }
        }
    }

    public void setAtributes(long idecko){

        gratitude = db.getGratitude(idecko);
        datumek = konverze(gratitude.getDate());

        dateview.setText(datumek);
        rateview.setText(String.valueOf(gratitude.getRate()));
        noteview.setText(gratitude.getNote());

        long catID = gratitude.getIdCategory();
        category = db.getCategory(catID);
        categoryview.setText(category.getName());

        long typID = gratitude.getIdType();
        type = db.getType(typID);
        //v pripade ze byl typ smazán nastaví se automaticky defaultni ID na 0 a tak se zobrazí hláška
        if (type.getId() == 0){
            String defaultText = "Neznámý";
            typeview.setText(defaultText);
        }else {
            typeview.setText(type.getName());
        }
        Log.e("ID typu v detailu: ", String.valueOf(type.getId()));



        final String cesta = gratitude.getPath();
        if (!cesta.matches("nenalezeno")){
            Picasso.with(this)
                    .load(new File(cesta))
                    .fit()
                    .centerCrop()
                    .into(imageViewDetail);
        }else{
            cardViewWithImageView.setVisibility(View.INVISIBLE);
        }

        colorView.setBackgroundColor(Color.parseColor(db.getCategory(catID).getColorCategory()));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.detail_list_menu,menu);
        return true;
    }
}
