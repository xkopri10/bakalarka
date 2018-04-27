package cz.mendelu.xkopri10.bp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.BaseColumns;

/**
 * Created by Martin on 10.02.2018.
 */

public class Category {

    //static final String TABLE_CATEGORY = "categories";

    private int id;
    private String name;
    private String colorCategory;
    private int selectedState;

    private Cursor cursor;

    protected ContentValues values;

    //konstruktory
    public Category() {
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(Cursor cursor) {
        this.cursor = cursor;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorCategory() {
        return colorCategory;
    }

    public void setColorCategory(String colorCategory) {
        this.colorCategory = colorCategory;
    }

    public int getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(int selectedState) {
        this.selectedState = selectedState;
    }

}
