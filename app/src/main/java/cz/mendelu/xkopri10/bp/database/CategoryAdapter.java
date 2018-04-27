package cz.mendelu.xkopri10.bp.database;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.mendelu.xkopri10.bp.everythingUnderHelp.HelpDetailRow;
import cz.mendelu.xkopri10.bp.R;
import cz.mendelu.xkopri10.bp.everythingUnderAdd.CategoryActivity;
import cz.mendelu.xkopri10.bp.everythingUnderAdd.CategoryDetail;


/**
 * Created by Martin on 23.02.2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private List<Category> categoryList;
    DatabaseHelper db;
    private String mojecategory, mujNazev;
    Category category;
    private int whichActivity;

    public CategoryAdapter(List<Category> categoryList, int activity) {
        this.categoryList = categoryList;
        whichActivity = activity;
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_list_row, parent,false);
        CategoryAdapter.CategoryViewHolder holder = new CategoryAdapter.CategoryViewHolder(view);
        db = new DatabaseHelper(parent.getContext());
        category = new Category();
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        final Category cat = categoryList.get(position);

        long catID = category.getId();
        mojecategory = db.getNameOfCategioryById(catID);
        category = db.getCategory(catID);

        holder.textViewCategory.setText(cat.getName());
        //pro kulate tlacitko
        holder.colorView.setBackgroundResource(R.drawable.circle);
        GradientDrawable backgroundGradient = (GradientDrawable)holder.colorView.getBackground();
        backgroundGradient.setColor(Color.parseColor(cat.getColorCategory()));

        //pokud je 1 pak se jedná o seznam ze kterého se jde na TYPY
        if (whichActivity == 1){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long idecko = cat.getId();

                    Intent intent = new Intent( view.getContext(),CategoryDetail.class);
                    intent.putExtra(CategoryDetail.EXTRA_CATEGORY_ID,idecko);
                    view.getContext().startActivity(intent);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    long idecko = cat.getId();
                    String mojeColor = cat.getColorCategory();
                    Intent intent = new Intent( view.getContext(),CategoryActivity.class);

                    intent.putExtra(CategoryActivity.EXTRA_CAT_ID,idecko);
                    intent.putExtra(CategoryActivity.EXTRA_COLOR,mojeColor);
                    intent.putExtra(CategoryActivity.EXTRA_NAME_CAT,mujNazev);

                    view.getContext().startActivity(intent);
                    return true;
                }
            });
        //pokud je tu 2 tak se jde na detail v HELP activity
        }else if (whichActivity == 2){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long idecko = cat.getId();

                    Intent intent = new Intent( view.getContext(),HelpDetailRow.class);
                    intent.putExtra(HelpDetailRow.EXTRA_CATEGORY_ID_HELP,idecko);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView textViewCategory;
        View colorView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.myCategory);
            colorView = itemView.findViewById(R.id.color_cat_view);
        }
    }
}
