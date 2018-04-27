package cz.mendelu.xkopri10.bp.list;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import java.io.File;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Gratitude;
import io.fabric.sdk.android.Fabric;

public class DetailPhotoGalleryActivity extends AppCompatActivity {

    public static final String EXTRA_GALLERY = "galerka";

    private long graID;
    private Gratitude gratitude;
    private DatabaseHelper db;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_detail_photo_gallery);

        db = new DatabaseHelper(this);
        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.imageViewGalleryPhoto);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(EXTRA_GALLERY)) {
            graID = extras.getLong(EXTRA_GALLERY);
            Log.e("idecko: ", graID + "");
            gratitude = db.getGratitude(graID);

            final String cesta = gratitude.getPath();
            Log.e("cesta k fotce: ", cesta);

            Picasso.with(this)
                    .load(new File(cesta))
                    .into(imageView);
        }
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
        return true;
    }
}
