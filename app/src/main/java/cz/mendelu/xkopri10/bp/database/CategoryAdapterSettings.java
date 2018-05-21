package cz.mendelu.xkopri10.bp.database;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.List;

import cz.mendelu.xkopri10.bp.R;

/**
 * Created by Martin on 07.03.2018.
 */

public class CategoryAdapterSettings extends RecyclerView.Adapter<CategoryAdapterSettings.CategoryViewHolderSettings>{

    private List<Category> categoryList;
    DatabaseHelper db;
    Category category;
    private String mojecategory;

    public CategoryAdapterSettings(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public CategoryViewHolderSettings onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_list_row_settings, parent,false);
        CategoryAdapterSettings.CategoryViewHolderSettings holder = new CategoryAdapterSettings.CategoryViewHolderSettings(view);
        db = new DatabaseHelper(parent.getContext());
        category = new Category();
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolderSettings holder, int position) {
        final Category cat = categoryList.get(position);

        final long catID = cat.getId();
        category = db.getCategory(catID);

        holder.textViewCategory.setText(cat.getName());

        if (db.getCategory(catID).getSelectedState() == 1){
            holder.checkBoxChoosed.setChecked(true);
        }else if (category.getSelectedState() == 0){
            holder.checkBoxChoosed.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("selected state: ", cat.getSelectedState() + " + ID" +cat.getId());
            }
        });

        holder.checkBoxChoosed.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isChecked()){
                    db.updateCategories2(catID,0);
                }else if (buttonView.isChecked()){
                    db.updateCategories2(catID,1);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolderSettings extends RecyclerView.ViewHolder{

        TextView textViewCategory;
        CheckBox checkBoxChoosed;

        public CategoryViewHolderSettings(View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.categoryChoosingName);
            checkBoxChoosed = itemView.findViewById(R.id.checkBoxChoosingCategory);
        }
    }

}
