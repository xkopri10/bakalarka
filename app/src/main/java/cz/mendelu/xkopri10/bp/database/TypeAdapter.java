package cz.mendelu.xkopri10.bp.database;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.everythingUnderAdd.CategoryDetail;

/**
 * Created by Martin on 25.02.2018.
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {

    private List<Type> typeList;
    DatabaseHelper db;
    Type type;
    CategoryDetail cd;

    public TypeAdapter(List<Type> typeList) {
        this.typeList = typeList;
    }

    @Override
    public TypeAdapter.TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.type_list_row, parent,false);
        TypeAdapter.TypeViewHolder holder = new TypeAdapter.TypeViewHolder(view);

        db = new DatabaseHelper(parent.getContext());
        cd = new CategoryDetail();
        type = new Type();
        return holder;
    }

    @Override
    public void onBindViewHolder(final TypeAdapter.TypeViewHolder holder, int position) {
        final Type typ = typeList.get(position);

        final long typID = type.getId();
        type = db.getType(typID);

        holder.textViewType.setText(typ.getName());

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Odstranit");
                builder.setMessage("Opravdu si přeješ typ odstranit? "
                        + "V případě, že je typ použit, nebude nahrazen.");
                builder.setPositiveButton(
                        "ODSTRANIT",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                long idecko = typ.getId();
                                try {
                                    db.deleteType(idecko);
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                                typeList.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton(
                        "ZRUŠIT",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    class TypeViewHolder extends RecyclerView.ViewHolder{

        TextView textViewType;
        ImageButton del;

        public TypeViewHolder(View itemView) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.myType);
            del = itemView.findViewById(R.id.del_button);

        }
    }
}
