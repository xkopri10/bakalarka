package cz.mendelu.xkopri10.bp.list;

import android.app.DatePickerDialog;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.crashlytics.android.Crashlytics;

import java.util.Calendar;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.list.Tab1Today;
import cz.mendelu.xkopri10.bp.list.Tab2All;
import io.fabric.sdk.android.Fabric;

public class ListActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    // bude obsahovat obsah karty
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_list);

       // db = new DatabaseHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Vytvoří adaptér, který bude vracet jeden ze tří fragmentů
        // primární část aktivity
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Nastaví ViewPager pomocí adaptéru sekcí
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //přidání tlačítka ZPĚT v action baru
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
}

    //metoda pro tlačítko zpět
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            //ukončí aktivitu
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list,menu);
        return true;
    }

    // smazána PlaceholderFragment pro zobrazení - nahrazeno jednotlivýma třídama Tab1Today a Tab2All
    //A {@link FragmentPagerAdapter} vrací fragment, který odpovídá jedné ze sekcí/karet

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //vrací aktuální kartu
           switch (position){
               case 0:
                   Tab1Today tab1Today = new Tab1Today();
                   return tab1Today;
               case 1:
                   Tab2All tab2All = new Tab2All();
                   return tab2All;
               case 2:
                   Tab3Pleasures tab3Pleasures = new Tab3Pleasures();
                   return tab3Pleasures;
               default:
                   return null;
           }
        }

        @Override
        public int getCount() {
            // Ukaž mi celkem 3 sekce/karty
            return 3;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
