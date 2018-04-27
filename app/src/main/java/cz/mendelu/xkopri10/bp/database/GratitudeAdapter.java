package cz.mendelu.xkopri10.bp.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.everythingUnderAdd.CategoryDetail;
import cz.mendelu.xkopri10.bp.everythingUnderAdd.EditingCategoryActivity;
import cz.mendelu.xkopri10.bp.list.DetailOfListActivity;
import cz.mendelu.xkopri10.bp.list.Tab1Today;

/**
 * Created by Martin on 21.02.2018.
 */

public class GratitudeAdapter extends RecyclerView.Adapter<GratitudeAdapter.GratitudeViewHolder>{

    //definice nového listu složeného z vděčností
    private List<Gratitude> gratitudeList;
    DatabaseHelper db;
    long idkategorie;
    String mojecategory, datumek;
    Category category;

    //konstruktor
    public GratitudeAdapter(List<Gratitude> gratitudeLists) {
        this.gratitudeList = gratitudeLists;
    }

    @Override
    public GratitudeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_cards, parent,false);
        GratitudeViewHolder holder = new GratitudeViewHolder(view);
        db = new DatabaseHelper(parent.getContext());
        category = new Category();
        return holder;
    }



    @Override
    public void onBindViewHolder(final GratitudeViewHolder holder, final int position) {
        final Gratitude gratitude = gratitudeList.get(position);

        long catID = gratitude.getIdCategory();
        mojecategory = db.getNameOfCategioryById(catID);
        category = db.getCategory(catID);

        datumek = konverze(gratitude.getDate());
        Log.e("datumek: ",datumek);

        //holder.textViewDate.setText(gratitude.getDate());
        holder.textViewDate.setText(datumek);
        holder.textViewNote.setText(gratitude.getNote());
        holder.textViewCategory.setText(mojecategory);
        //holder.colorView.setBackgroundColor(Color.parseColor(category.getColorCategory()));
        //funguje - použít ale s jemnějšíma barvama
        holder.cardView.setBackgroundColor(Color.parseColor(category.getColorCategory()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long idecko = gratitude.getId();

                Intent intent = new Intent( view.getContext(),DetailOfListActivity.class);
                intent.putExtra(DetailOfListActivity.EXTRA_DETAIL_OF_LIST,idecko);
                view.getContext().startActivity(intent);
                Log.e("idecko: ",gratitude.getId() + "");
            }
        });
    }



    @Override
    public int getItemCount() {
        return gratitudeList.size();
    }


    class GratitudeViewHolder extends RecyclerView.ViewHolder{

        TextView textViewDate, textViewNote, textViewCategory;
        View colorView;
        CardView cardView;

        public GratitudeViewHolder(View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.textViewHead);
            textViewNote = itemView.findViewById(R.id.textViewNote);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            //colorView = itemView.findViewById(R.id.color_card);
            cardView = itemView.findViewById(R.id.myCardView);
        }
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


    static void refreshActionBar(Activity activity){
        activity.invalidateOptionsMenu();
    }
}
