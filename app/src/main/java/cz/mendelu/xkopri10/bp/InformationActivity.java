package cz.mendelu.xkopri10.bp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class InformationActivity extends AppCompatActivity {

    private TextView textViewAbout;
    private TextView textViewAuthor;
    private Button hintButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_information);

        hintButton = (Button) findViewById(R.id.hintButton);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformationActivity.this,HintActivity.class);
                startActivity(intent);
            }
        });

        setTextView();

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void setTextView(){
        textViewAbout = (TextView)findViewById(R.id.about);
        textViewAuthor = (TextView)findViewById(R.id.author);

        textViewAbout.setText(
                "Nástroj na podporu pozitivního myšlení, udržení dobré nálady a šťastného života.\nAplikace byla vytvořena jako bakalářská práce.\n\nO principu metody se dozvíš v Nápovědě."
        );
        textViewAuthor.setText(
                "Martin Kopřiva\n" +
                "Student Mendelovy univerzity v Brně\n" +
                "2018\n"
        );
    }
}
