package cz.mendelu.xkopri10.bp.list;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.database.DatabaseHelper;
import cz.mendelu.xkopri10.bp.database.Gratitude;
import cz.mendelu.xkopri10.bp.database.GratitudeAdapter;
import cz.mendelu.xkopri10.bp.database.Greatfulness;
import cz.mendelu.xkopri10.bp.database.GreatfulnessAdapter;

/**
 * Created by Martin on 08.02.2018.
 */

public class Tab1Today extends Fragment{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHelper db;
    List<Gratitude> gratitudeList;
    List<Greatfulness> greatfulnessList;
    Button todayGratitude, todayPleasures;
    private ImageView emptyBoxImage2;
    private TextView emptyBoxText2;

    boolean clickLeftList, clickRightList, emptyBox;

    //vytvorim si instanci na zobrazeni MENU -nastavím na TRUE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        clickRightList = false;
        clickLeftList = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1today, container, false);
        emptyBoxImage2 = (ImageView) rootView.findViewById(R.id.emptyBox2);
        emptyBoxText2 = (TextView) rootView.findViewById(R.id.emptyText2);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.myRecyclerViewTab1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //toto je vděčnost - v DB jako Greatfulness
        todayGratitude = (Button) rootView.findViewById(R.id.todayGratitudes);
        //toto je radost - v DB jako Gratitudes
        todayPleasures = (Button) rootView.findViewById(R.id.todayPleasures);
        todayPleasures.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_borders_click,null));
        todayGratitude.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_borders2,null));

        todayGratitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getList(2);
                clickRightList = true;
                clickLeftList = false;
                todayPleasures.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_borders,null));
                todayGratitude.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_borders_click2,null));

                todayGratitude.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                todayPleasures.setTextColor(ContextCompat.getColor(getContext(),R.color.darkpurple));
                showEmptyBox();
            }
        });

        todayPleasures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getList(1);
                clickLeftList = true;
                clickRightList = false;
                todayPleasures.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_borders_click,null));
                todayGratitude.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_borders2,null));

                todayPleasures.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                todayGratitude.setTextColor(ContextCompat.getColor(getContext(),R.color.darkpurple));
                showEmptyBox();
            }
        });


        return rootView;
    }

    public void showEmptyBox(){
        if (emptyBox){
            if (clickRightList){
                emptyBoxText2.setText("Žádná dnešní vděčnost");
            }else if (clickLeftList){
                emptyBoxText2.setText("Žádné dnešní radosti");
            }
            emptyBoxText2.setVisibility(View.VISIBLE);
            emptyBoxImage2.setVisibility(View.VISIBLE);
        } else {
            emptyBoxText2.setVisibility(View.INVISIBLE);
            emptyBoxImage2.setVisibility(View.INVISIBLE);
        }
    }

    //tady skryju tlacitko menu v pripade ze je na karte DNES
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.date_range).setVisible(false);
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.nofilter).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    public void getList(int varianta) {
        //pripojeni a dostfani se k obsahu
        DatabaseHelper db = new DatabaseHelper(getContext());

        if (varianta==1){
            gratitudeList = db.getAllGratitude(2,null,0);
            GratitudeAdapter dataAdapter = new GratitudeAdapter(gratitudeList);
            recyclerView.setAdapter(dataAdapter);

            if (gratitudeList.size() == 0){
                emptyBox = true;
            }else emptyBox = false;

        } else if (varianta == 2){
            greatfulnessList = db.getAllGreatfulness(2,null);
            GreatfulnessAdapter dataAdapter = new GreatfulnessAdapter(greatfulnessList);
            recyclerView.setAdapter(dataAdapter);

            if (greatfulnessList.size() == 0){
                emptyBox = true;
            }else emptyBox = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //podle toho zda klikám na radost/vděčnost a z toho an detail a pak zpět, aby se nastavil správný list
        if (clickLeftList){
            getList(1);
            showEmptyBox();
        }else if (clickRightList){
            getList(2);
            showEmptyBox();
        }else {
            getList(1);
        }
    }
}
