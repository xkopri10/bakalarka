package cz.mendelu.xkopri10.bp.database;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.everythingUnderAdd.CategoryDetail;
import cz.mendelu.xkopri10.bp.list.DetailOfListActivity;
import cz.mendelu.xkopri10.bp.list.DetailOfListActivityGratitude;

/**
 * Created by Martin on 26.02.2018.
 */

public class GreatfulnessAdapter extends RecyclerView.Adapter<GreatfulnessAdapter.GreatfulnessViewHolder> {

    private List<Greatfulness> greatfulnessList;
    private DatabaseHelper db;

    private String datumek;


    public GreatfulnessAdapter(List<Greatfulness> greatfulnessList) {
        this.greatfulnessList = greatfulnessList;
    }

    @Override
    public GreatfulnessAdapter.GreatfulnessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_card_gratitude, parent,false);
        GreatfulnessAdapter.GreatfulnessViewHolder holder = new GreatfulnessAdapter.GreatfulnessViewHolder(view);
        db = new DatabaseHelper(parent.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(GreatfulnessAdapter.GreatfulnessViewHolder holder, int position) {
        final Greatfulness greatfulness = greatfulnessList.get(position);

        datumek = konverze(greatfulness.getDateGreatfulness());
        Log.e("datumek: ", datumek);

        holder.textViewDate.setText(datumek);
        holder.textViewNote.setText(greatfulness.getNoteGreatfulness());
        holder.cardView.setCardBackgroundColor(Color.parseColor(greatfulness.getColorGreatfulness()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long idecko = greatfulness.getId();

                Intent intent = new Intent( view.getContext(),DetailOfListActivityGratitude.class);
                intent.putExtra(DetailOfListActivityGratitude.EXTRA_GREATFULNESS_DETAIL,idecko);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return greatfulnessList.size();
    }

    class GreatfulnessViewHolder extends RecyclerView.ViewHolder{

        TextView textViewDate, textViewNote;
        View colorView;
        CardView cardView;

        public GreatfulnessViewHolder(View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.textViewDateGreatfulness);
            textViewNote = itemView.findViewById(R.id.textViewNoteGreatfulness);
            cardView = itemView.findViewById(R.id.myCardViewGreatfulness);
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
}
