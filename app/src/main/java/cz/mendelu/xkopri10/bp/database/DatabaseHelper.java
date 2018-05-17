package cz.mendelu.xkopri10.bp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Martin on 10.02.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 42;
    private static final String DATABASE_NAME = "MyAplicationBp";

    //názvy tabulek
    private static final String TABLE_CATEGORY = "categories";
    private static final String TABLE_TYPE = "types";
    private static final String TABLE_CATEGORY_TYPE = "category_types";
    private static final String TABLE_GRATITUDE = "gratitudes";
    private static final String TABLE_GREATFULNESS = "greatfulness";
    private static final String TABLE_DAILY_NOT = "daily_not";
    private static final String TABLE_MOTIVATION_NOT = "motivation_not";

    //názvy sloupců (pro oba stejné - ale rozlišil jsem název kategorie a typu);
    private static final String KEY_ID = "id";
    private static final String KEY_NAME_CATEGORY = "name_category";
    private static final String KEY_NAME_TYPE = "name_type";
    private static final String KEY_COLOR_CATEGORY = "color_category";
    private static final String KEY_SELECTED_STATE = "selected_state";

    //názvy sloupců pro společnou tabulku (JOINOVANOU mezi kategorií a typem
    private static final String KEY_TYPE_ID = "type_id";
    private static final String KEY_CATEGORY_ID = "category_id";

    //nazvy sloupcu pro tabulku radosti Gratitude (PLEASURE) - slovo vděčnost = radost z duvodu abych nemusel sve prepisovat
    private static final String KEY_ID_GRATITUDE = "id_gratitude";
    private static final String KEY_DATE_GRATITUDE = "date_gratitude";
    private static final String KEY_NOTE_GRATITUDE = "note_gratitude";
    private static final String KEY_RATE_GRATITUDE = "rate_gratitude";
    private static final String KEY_MY_ID_CATEGORY = "my_id_category";
    private static final String KEY_MY_ID_TYPE = "my_id_type";
    private static final String KEY_PATH_PICTURE = "path_picture";

    //nazvy sloupcu pro tabulku vděčnosti Greatfulness (vděčnost)
    private static final String KEY_ID_GREATFULNESS = "id_greatfulness";
    private static final String KEY_DATE_GREATFULNESS = "date_greatfulness";
    private static final String KEY_NOTE_GREATFULNESS = "note_greatfulness";
    private static final String KEY_COLOR_GREATFULNESS = "color_greatfulness";

    //nazvy sloupcu pro tabulku daily notification
    private static final String KEY_ID_DAILY_NOT = "id_daily_not";
    private static final String KEY_STATE_DAILY_NOT = "state_daily_not";
    private static final String KEY_HOUR_DAILY_NOT = "hour_daily_not";
    private static final String KEY_MINUTE_DAILY_NOT = "minute_daily_not";

    //nazvy sloupcu pro tabulku motivation notification
    private static final String KEY_ID_MOTIVATION_NOT = "id_motivation_not";
    private static final String KEY_STATE_MOTIVATION_NOT = "state_motivation_not";
    private static final String KEY_HOUR_MOTIVATION_NOT = "hour_motivation_not";
    private static final String KEY_MINUTE_MOTIVATION_NOT = "minute_motivation_not";
    private static final String KEY_START_DATE_MOTIVATION_NOT = "start_date_motivation_not";
    private static final String KEY_DURATION_MOTIVATION_NOT = "duration_motivation_not";


    private static final String create_table_category = "CREATE TABLE " + TABLE_CATEGORY
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME_CATEGORY + " TEXT,"
            + KEY_COLOR_CATEGORY + " TEXT,"
            + KEY_SELECTED_STATE + " TINYINT" + ")";

    private static final String create_table_type = "CREATE TABLE " + TABLE_TYPE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME_TYPE + " TEXT" + ")";

    private static final String create_table_category_type = "CREATE TABLE "
            + TABLE_CATEGORY_TYPE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TYPE_ID + " INTEGER,"
            + KEY_CATEGORY_ID + " INTEGER" + ")";

    private static final String create_table_gratitude = "CREATE TABLE "
            + TABLE_GRATITUDE + "(" + KEY_ID_GRATITUDE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DATE_GRATITUDE + " TEXT,"
            + KEY_NOTE_GRATITUDE + " TEXT,"
            + KEY_MY_ID_CATEGORY + " INTEGER,"
            + KEY_MY_ID_TYPE + " INTEGER,"
            + KEY_RATE_GRATITUDE + " INTEGER,"
            + KEY_PATH_PICTURE + " TEXT" + ")";

    private static final String create_table_greatfulness = "CREATE TABLE "
            + TABLE_GREATFULNESS + "(" + KEY_ID_GREATFULNESS + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DATE_GREATFULNESS + " TEXT,"
            + KEY_NOTE_GREATFULNESS + " TEXT,"
            + KEY_COLOR_GREATFULNESS + " TEXT" + ")";

    private static final String create_table_daily_not = "CREATE TABLE "
            + TABLE_DAILY_NOT + "(" + KEY_ID_DAILY_NOT + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_STATE_DAILY_NOT + " TINYINT,"
            + KEY_HOUR_DAILY_NOT + " INTEGER,"
            + KEY_MINUTE_DAILY_NOT + " INTEGER" + ")";

    private static final String create_table_motivation_not = "CREATE TABLE "
            + TABLE_MOTIVATION_NOT + "(" + KEY_ID_MOTIVATION_NOT + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_STATE_MOTIVATION_NOT + " TINYINT,"
            + KEY_HOUR_MOTIVATION_NOT + " INTEGER,"
            + KEY_MINUTE_MOTIVATION_NOT + " INTEGER,"
            + KEY_START_DATE_MOTIVATION_NOT + " TEXT,"
            + KEY_DURATION_MOTIVATION_NOT + " INTEGER" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //vytvoří nové tabulky
        db.execSQL(create_table_category);
        db.execSQL(create_table_type);
        db.execSQL(create_table_category_type);
        db.execSQL(create_table_gratitude);
        db.execSQL(create_table_greatfulness);
        db.execSQL(create_table_daily_not);
        db.execSQL(create_table_motivation_not);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //updatuje star= tabulky
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRATITUDE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GREATFULNESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_NOT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOTIVATION_NOT);
        //vytvoří nové tabulky
        onCreate(db);
    }

    ////////////////////////operace pro vděčnosti

    public long createGreatfulness(Greatfulness greatfulness) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE_GREATFULNESS, greatfulness.getDateGreatfulness());
        values.put(KEY_NOTE_GREATFULNESS, greatfulness.getNoteGreatfulness());
        values.put(KEY_COLOR_GREATFULNESS, greatfulness.getColorGreatfulness());

        long greatfulness_id = db.insert(TABLE_GREATFULNESS, null, values);
        db.close();
        return greatfulness_id;
    }

    public List<Greatfulness> getAllGreatfulness(int cisloHledani, String vybranyDatum) {
        List<Greatfulness> greatfulnesses = new ArrayList<Greatfulness>();
        String selectQuery = "";
        if (cisloHledani == 1) {
            selectQuery = "SELECT * FROM " + TABLE_GREATFULNESS + " ORDER BY " + KEY_DATE_GREATFULNESS + " DESC";
            Log.e(LOG, selectQuery);
        }else if (cisloHledani == 2){
            selectQuery = "SELECT * FROM " + TABLE_GREATFULNESS + " WHERE " + KEY_DATE_GREATFULNESS
                    + " = '" + getTodayDate() + "'";
            Log.e(LOG, selectQuery);
        }  else if (cisloHledani == 3) {
            selectQuery = "SELECT * FROM " + TABLE_GREATFULNESS
                    + " WHERE " + KEY_DATE_GREATFULNESS
                    + " = '" + vybranyDatum + "' ORDER BY " + KEY_DATE_GREATFULNESS + " DESC";
            Log.e(LOG, selectQuery);
        } else if (cisloHledani == 4) {
            selectQuery = "SELECT * FROM " + TABLE_GREATFULNESS
                    + " WHERE (" + KEY_DATE_GREATFULNESS + " > date('now', '-30 days'))"
                    + " ORDER BY RANDOM(), " + KEY_DATE_GREATFULNESS + " ASC LIMIT 4";
            Log.e(LOG, selectQuery);
        }else if (cisloHledani == 5) {
            selectQuery = "SELECT * FROM " + TABLE_GREATFULNESS + " ORDER BY RANDOM() LIMIT 1";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
            //cyklus na vypsání všech záznamů z databáze a přidání do listu
        if (cursor.moveToFirst()) {
             do {
                 Greatfulness great = new Greatfulness();
                 great.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_GREATFULNESS)));
                 great.setDateGreatfulness(cursor.getString(cursor.getColumnIndex(KEY_DATE_GREATFULNESS)));
                 great.setNoteGreatfulness(cursor.getString(cursor.getColumnIndex(KEY_NOTE_GREATFULNESS)));
                 great.setColorGreatfulness(cursor.getString(cursor.getColumnIndex(KEY_COLOR_GREATFULNESS)));

                    //přidání do listu
                    greatfulnesses.add(great);
                } while (cursor.moveToNext());
            }
            db.close();
            return greatfulnesses;
        }

    public long getCountOfGreatfulness(int varianta) {
        String countQuery = "";
        if (varianta == 1){
            countQuery = "SELECT * FROM " + TABLE_GREATFULNESS;
        } else if (varianta == 2){
            countQuery = "SELECT * FROM " + TABLE_GREATFULNESS
                    + " WHERE " + KEY_DATE_GREATFULNESS
                    + " = '" + getTodayDate() + "'";
        }


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        Log.e(LOG, "Počet vděčností: " + count);
        db.close();
        return count;
    }

    //vrátí mi jednu kategorii
    public Greatfulness getGreatfulness(long greatfulness_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_GREATFULNESS + " WHERE " + KEY_ID_GREATFULNESS + " = " + greatfulness_id;
        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        Greatfulness g = new Greatfulness();
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToNext();
            g.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_GREATFULNESS)));
            g.setDateGreatfulness(cursor.getString(cursor.getColumnIndex(KEY_DATE_GREATFULNESS)));
            g.setNoteGreatfulness(cursor.getString(cursor.getColumnIndex(KEY_NOTE_GREATFULNESS)));
            cursor.close();
        }
        db.close();
        return g;
    }

    //////////////////////////operace pro radosti

    public long createGratitude(Gratitude gratitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE_GRATITUDE, gratitude.getDate());
        values.put(KEY_RATE_GRATITUDE, gratitude.getRate());
        values.put(KEY_NOTE_GRATITUDE, gratitude.getNote());
        values.put(KEY_MY_ID_CATEGORY, gratitude.getIdCategory());
        values.put(KEY_MY_ID_TYPE, gratitude.getIdType());
        values.put(KEY_PATH_PICTURE, gratitude.getPath());

        long gratitude_id = db.insert(TABLE_GRATITUDE, null, values);
        db.close();
        return gratitude_id;
    }

    //vytáhnutí všech vděčností z databáze
    // - pokud parametr cisloHledani = 1 pak se hledaji vsechny
    // - pokud parametr cisloHledani = 2 pak se hledaji jen ty ze dnes
    // - pokud parametr cisloHledani = 3 pak se hledaji ty které vyhovují danému datumu
    // - pokud parametr cisloHledani = 4 pak se vyhledá ta která odpovídá danému ID
    // - pokud parametr cisloHledani = 5 se vyhledá RANDOM jedna vděčnost
    // - pokud parametr cisloHledani = 6 se vyhledá 3 za posledni tyden podle kategorie a hodnocení větší jak 8
    // - pokud parametr cisloHledani = 7 se vyhledá 3 za posledni měsíc podle kategorie a hodnocení větší jak 8
    // - pokud parametr cisloHledani = 8 se vyhleda 1 radost ktera ma svoji kategorii zakriskovanou + se vybere jen vzdy random z MAX - 2 ohodnoceni

    public List<Gratitude> getAllGratitude(int cisloHledani, String vybranyDatum, long idKategorie) {
        List<Gratitude> gratitude = new ArrayList<Gratitude>();
        String selectQuery = "";
        if (cisloHledani == 1){
            selectQuery = "SELECT * FROM " + TABLE_GRATITUDE + " ORDER BY " + KEY_DATE_GRATITUDE + " DESC";
            Log.e(LOG, selectQuery);
        }else if (cisloHledani == 2){
            Log.e(LOG, "dnešní datum: " + getTodayDate());
            selectQuery = "SELECT * FROM " + TABLE_GRATITUDE
                    + " WHERE " + KEY_DATE_GRATITUDE
                    + " = '" + getTodayDate() + "'";
            Log.e(LOG, selectQuery);
        } else if (cisloHledani == 3){
            selectQuery = "SELECT * FROM " + TABLE_GRATITUDE
                    + " WHERE " + KEY_DATE_GRATITUDE
                    + " = '" + vybranyDatum + "' ORDER BY " + KEY_DATE_GRATITUDE + " DESC";
            Log.e(LOG, selectQuery);
        } else if (cisloHledani == 4){
            selectQuery = "SELECT * FROM " + TABLE_GRATITUDE
                    + " WHERE " + KEY_MY_ID_CATEGORY
                    + " = " + idKategorie + " ORDER BY " + KEY_DATE_GRATITUDE + " DESC";
        } else if (cisloHledani == 5){
            selectQuery = "SELECT * FROM " + TABLE_GRATITUDE + " ORDER BY RANDOM() LIMIT 1";
        } else if (cisloHledani == 6) {
            selectQuery = "SELECT * FROM " + TABLE_GRATITUDE
                            +" WHERE ("+ KEY_DATE_GRATITUDE +" >= date('now','localtime', '-6 day')) AND  ("+ KEY_MY_ID_CATEGORY + " = " + idKategorie + ") AND (" + KEY_RATE_GRATITUDE + " >= (SELECT MAX("+ KEY_RATE_GRATITUDE +")-2 FROM "+TABLE_GRATITUDE+ " WHERE (("+ KEY_MY_ID_CATEGORY + " = " + idKategorie + ") AND ("+ KEY_DATE_GRATITUDE +" >= date('now','localtime', '-6 day')))))"
                            + " ORDER BY " + KEY_DATE_GRATITUDE + " ASC, RANDOM() LIMIT 3";
            Log.e(LOG, selectQuery);
        } else if (cisloHledani == 7) {
            selectQuery = "SELECT * FROM " + TABLE_GRATITUDE
                    + " WHERE (" + KEY_DATE_GRATITUDE + " > date('now', '-30 days')) AND (" + KEY_MY_ID_CATEGORY + " = " + idKategorie + ") AND (" + KEY_RATE_GRATITUDE + " >= (SELECT MAX("+ KEY_RATE_GRATITUDE +")-2 FROM "+TABLE_GRATITUDE+ " WHERE (("+ KEY_MY_ID_CATEGORY + " = " + idKategorie + ") AND ("+ KEY_DATE_GRATITUDE +" >= date('now', '-30 days')))))"
                    + " ORDER BY RANDOM(), " + KEY_DATE_GRATITUDE + " ASC LIMIT 3";
            Log.e(LOG, selectQuery);

        }else if (cisloHledani == 8) {
                selectQuery = "SELECT * FROM " + TABLE_GRATITUDE + " tabGr"
                        + " JOIN " + TABLE_CATEGORY + " tabCat ON tabGr." + KEY_MY_ID_CATEGORY + " = tabCat." + KEY_ID
                        + " WHERE ((tabCat." + KEY_SELECTED_STATE + " = 1) AND (tabGr." + KEY_RATE_GRATITUDE + " >= (SELECT MAX("+ KEY_RATE_GRATITUDE +")-2 FROM "+TABLE_GRATITUDE+ " WHERE ("+ KEY_DATE_GRATITUDE +" >= date('now', '-30 days')))))"
                        + " ORDER BY RANDOM() LIMIT 1";
            Log.e(LOG, selectQuery);
        }


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //cyklus na vypsání všech záznamů z databáze a přidání do listu
        if (cursor.moveToFirst()) {
            do {
                Gratitude gra = new Gratitude();
                gra.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_GRATITUDE)));
                gra.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE_GRATITUDE)));
                gra.setIdCategory(cursor.getInt(cursor.getColumnIndex(KEY_MY_ID_CATEGORY)));
                gra.setIdType(cursor.getInt(cursor.getColumnIndex(KEY_MY_ID_TYPE)));
                gra.setRate(cursor.getInt(cursor.getColumnIndex(KEY_RATE_GRATITUDE)));
                gra.setNote(cursor.getString(cursor.getColumnIndex(KEY_NOTE_GRATITUDE)));
                gra.setPath(cursor.getString(cursor.getColumnIndex(KEY_PATH_PICTURE)));

                //přidání do listu
                gratitude.add(gra);
            } while (cursor.moveToNext());
        }
        db.close();
        return gratitude;
    }

    //vrátí mi jednu kategorii
    public Gratitude getGratitude(long gratitude_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_GRATITUDE + " WHERE " + KEY_ID_GRATITUDE + " = " + gratitude_id;
        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        Gratitude g = new Gratitude();
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToNext();
            g.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_GRATITUDE)));
            g.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE_GRATITUDE)));
            g.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE_GRATITUDE)));
            g.setNote(cursor.getString(cursor.getColumnIndex(KEY_NOTE_GRATITUDE)));
            g.setRate(cursor.getInt(cursor.getColumnIndex(KEY_RATE_GRATITUDE)));
            g.setPath(cursor.getString(cursor.getColumnIndex(KEY_PATH_PICTURE)));

            g.setIdCategory(cursor.getInt(cursor.getColumnIndex(KEY_MY_ID_CATEGORY)));
            g.setIdType(cursor.getInt(cursor.getColumnIndex(KEY_MY_ID_TYPE)));
            cursor.close();
        }
        db.close();
        return g;
    }

    // varianta 1 - vyhlada celkem count
    // varianta 2 - vyhleda count za dnes
    public long getCountOfGratitudes(int varianta) {
        String countQuery = "";
        if (varianta == 1){
            countQuery = "SELECT * FROM " + TABLE_GRATITUDE;

        }else if (varianta == 2){
            countQuery = "SELECT * FROM " + TABLE_GRATITUDE
            + " WHERE " + KEY_DATE_GRATITUDE
            + " = '" + getTodayDate() + "'";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    //smazání vděčnosti
    public void deleteGratitude(long gra_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GRATITUDE, KEY_ID_GRATITUDE + " = " + gra_id , null);
    }


    public String getTodayDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate ="";
        try {
            todayDate = (format.format(calendar.getTime()));
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        }
        return todayDate;
    }

    /////////////////////////operace pro typy

    public long createType(Type type, long category_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_TYPE, type.getName());

        //vložení nového řádku(typu)
        long type_id = (db.insert(TABLE_TYPE, null, values));

        //přiřazení typu ke kategorii
            createCategoryType(type_id, category_ids);
            Log.e(LOG, "kategorie ID: " + category_ids);
            Log.e(LOG, "typ ID: " + type_id);
        db.close();
        return type_id;
    }

    //vrátí mi jeden typ
    public Type getType(long type_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TYPE + " WHERE " + KEY_ID + " = " + type_id;
        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        Type t = new Type();
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToNext();
            t.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            t.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_TYPE)));
            cursor.close();
        }
        db.close();
        return t;

    }

    //vrátí mi všechny typy které odpovídají určité kategorii
    public List<Type> getAllTypesByCategory(String mycat) {
        List<Type> types = new ArrayList<Type>();

        String selectQuery = "SELECT * FROM " + TABLE_TYPE + " tp, "
                + TABLE_CATEGORY + " ct, " + TABLE_CATEGORY_TYPE + " tt WHERE ct."
                + KEY_NAME_CATEGORY + " = '" + mycat + "'" + " AND ct." + KEY_ID
                + " = " + "tt." + KEY_CATEGORY_ID + " AND tp." + KEY_ID + " = "
                + "tt." + KEY_TYPE_ID;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Type tp = new Type();
                tp.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                tp.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_TYPE)));

                Log.e("idecka typu: ",tp.getId()+"");
                types.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return types;
    }

    //smazání typu
    public void deleteType(long typ_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TYPE, KEY_ID + " = " + typ_id, null);
    }

    //////////////////////////operace pro kategorii

    //vytvoří novou kategorii
    public long createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_CATEGORY, category.getName());
        values.put(KEY_COLOR_CATEGORY, category.getColorCategory());
        values.put(KEY_SELECTED_STATE, category.getSelectedState());

        long category_id = (db.insert(TABLE_CATEGORY, null, values));
        db.close();
        return category_id;
    }

    //vytáhnutí všech kategorií z databáze
    public List<Category> getAllCategory(int varianta) {
        List<Category> categories = new ArrayList<Category>();
        String selectQuery = "";
        if (varianta == 1) {
            selectQuery = "SELECT * FROM " + TABLE_CATEGORY;
        }
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //cyklus na vypsání všech záznamů z databáze a přidání do listu
        if (cursor.moveToFirst()) {
            do {
                Category cat = new Category();
                cat.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                cat.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
                cat.setColorCategory(cursor.getString(cursor.getColumnIndex(KEY_COLOR_CATEGORY)));
                cat.setSelectedState(cursor.getInt(cursor.getColumnIndex(KEY_SELECTED_STATE)));

                //přidání do listu
                categories.add(cat);
            } while (cursor.moveToNext());
        }
        db.close();
        return categories;
    }

    public List<Category> getUsedCategory() {
        List<Category> categories = new ArrayList<Category>();
        String selectQuery = "";
        selectQuery = "SELECT DISTINCT cat."+KEY_ID+", cat."+KEY_NAME_CATEGORY+", cat."+KEY_SELECTED_STATE+" FROM " + TABLE_CATEGORY + " cat JOIN " + TABLE_GRATITUDE + " gra ON gra." + KEY_MY_ID_CATEGORY + " = cat." + KEY_ID;
         Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //cyklus na vypsání všech záznamů z databáze a přidání do listu
        if (cursor.moveToFirst()) {
            do {
                Category cat = new Category();
                cat.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                cat.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
                cat.setSelectedState(cursor.getInt(cursor.getColumnIndex(KEY_SELECTED_STATE)));

                //přidání do listu
                categories.add(cat);
            } while (cursor.moveToNext());
        }
        db.close();
        return categories;
    }

    public long getSumOfRateUseingCategory(long id) {
        String countQuery = "SELECT SUM("+ KEY_RATE_GRATITUDE +") as Total FROM " + TABLE_GRATITUDE
                + " WHERE " + KEY_MY_ID_CATEGORY
                + " = " + id;
        long total=1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }
        cursor.close();
        Log.e(LOG, "Součet hodnocení vděčností v dané kategorii: " + total);
        db.close();
        return total;
    }

    public long getCountOfGratitudeUseingCategory(long id) {
        String countQuery = "SELECT * FROM " + TABLE_GRATITUDE
                + " WHERE " + KEY_MY_ID_CATEGORY
                + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        Log.e(LOG, "Počet vděčností v dané kategorii: " + count);
        db.close();
        return count;
    }

    //aktualizace kategorie
    public void updateCategories1(long idecko, String nazev, String barva) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_CATEGORY, nazev);
        values.put(KEY_COLOR_CATEGORY, barva);

        db.update(TABLE_CATEGORY, values, KEY_ID + " = " + idecko, null);
        db.close();
    }

    //aktualizace kategorie
    public void updateCategories2(long idecko, int selectedState) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SELECTED_STATE, selectedState);

        db.update(TABLE_CATEGORY, values, KEY_ID + " = " + idecko, null);
        db.close();
    }


    public String getNameOfCategioryById(long id){
        String mujnazev = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + KEY_ID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        Category t = new Category();
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToNext();
            t.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            mujnazev = cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY));
            cursor.close();
        }
        db.close();
        return mujnazev;
    }

    //vrátí mi jednu kategorii
    public Category getCategory(long type_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + KEY_ID + " = " + type_id;
        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        Category t = new Category();
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToNext();
            t.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            t.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
            t.setColorCategory(cursor.getString(cursor.getColumnIndex(KEY_COLOR_CATEGORY)));
            t.setSelectedState(cursor.getInt(cursor.getColumnIndex(KEY_SELECTED_STATE)));

            Log.e(LOG, "vybraná kategorie: " + t.getName());
            cursor.close();
        }
        db.close();
        return t;
    }

    ////////////////////////// operace pro daily notification table

    public long createDailyNot(DailyNot dailyNot) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATE_DAILY_NOT, dailyNot.getState());
        //values.put(KEY_TIME_DAILY_NOT, dailyNot.getTime());
        values.put(KEY_HOUR_DAILY_NOT, dailyNot.getHour());
        values.put(KEY_MINUTE_DAILY_NOT, dailyNot.getMinute());

        long dailyNot_id = db.insert(TABLE_DAILY_NOT, null, values);
        Log.e("Daily NOTIFICATION ID", String.valueOf(dailyNot_id));
        db.close();
        return dailyNot_id;
    }

    //aktualizace tabulky daily not
    public void updateDailyNot(long idecko, int selectedState, int selectedHour, int selectedMinute) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATE_DAILY_NOT, selectedState);
        //values.put(KEY_TIME_DAILY_NOT, selectedTime);
        values.put(KEY_HOUR_DAILY_NOT, selectedHour );
        values.put(KEY_MINUTE_DAILY_NOT, selectedMinute );

        db.update(TABLE_DAILY_NOT, values, KEY_ID_DAILY_NOT + " = " + idecko, null);
        db.close();
    }

    //vrátí mi jednu daily not
    public DailyNot getDailyNot(long daily_not_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_DAILY_NOT + " WHERE " + KEY_ID_DAILY_NOT + " = " + daily_not_id;
        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        DailyNot dn = new DailyNot();
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToNext();
            dn.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DAILY_NOT)));
            dn.setState(cursor.getInt(cursor.getColumnIndex(KEY_STATE_DAILY_NOT)));
            //dn.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME_DAILY_NOT)));
            dn.setHour(cursor.getInt(cursor.getColumnIndex(KEY_HOUR_DAILY_NOT)));
            dn.setMinute(cursor.getInt(cursor.getColumnIndex(KEY_MINUTE_DAILY_NOT)));

            cursor.close();
        }
        db.close();
        return dn;
    }

    ////////////////////////// operace pro motivation notification tab

    public long createMotivationNot(MotivationNot motivationNot) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATE_MOTIVATION_NOT, motivationNot.getState());
        values.put(KEY_HOUR_MOTIVATION_NOT, motivationNot.getHour());
        values.put(KEY_MINUTE_MOTIVATION_NOT, motivationNot.getMinute());
        values.put(KEY_DURATION_MOTIVATION_NOT, motivationNot.getDuration());
        values.put(KEY_START_DATE_MOTIVATION_NOT, motivationNot.getStartDate());

        long motivationNot_id = db.insert(TABLE_MOTIVATION_NOT, null, values);
        db.close();
        Log.e("motiv. NOTIFICATION ID", String.valueOf(motivationNot_id));
        return motivationNot_id;
    }

    //aktualizace tabulky daily not
    public void updateMotivationNot(long idecko, int selectedState, int selectedHour, int selectedMinute, int selectedDuration, String selectedDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATE_MOTIVATION_NOT, selectedState);
        values.put(KEY_HOUR_MOTIVATION_NOT, selectedHour);
        values.put(KEY_MINUTE_MOTIVATION_NOT, selectedMinute);
        values.put(KEY_DURATION_MOTIVATION_NOT, selectedDuration);
        values.put(KEY_START_DATE_MOTIVATION_NOT, selectedDate);

        db.update(TABLE_MOTIVATION_NOT, values, KEY_ID_MOTIVATION_NOT + " = " + idecko, null);
        db.close();
    }

    //vrátí mi jednu motivation not
    public MotivationNot getMotivationNot(long motivation_not_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_MOTIVATION_NOT + " WHERE " + KEY_ID_MOTIVATION_NOT + " = " + motivation_not_id;
        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        MotivationNot mn = new MotivationNot();
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToNext();
            mn.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_MOTIVATION_NOT)));
            mn.setState(cursor.getInt(cursor.getColumnIndex(KEY_STATE_MOTIVATION_NOT)));
            mn.setHour(cursor.getInt(cursor.getColumnIndex(KEY_HOUR_MOTIVATION_NOT)));
            mn.setMinute(cursor.getInt(cursor.getColumnIndex(KEY_MINUTE_MOTIVATION_NOT)));
            mn.setDuration(cursor.getInt(cursor.getColumnIndex(KEY_DURATION_MOTIVATION_NOT)));
            mn.setStartDate(cursor.getString(cursor.getColumnIndex(KEY_START_DATE_MOTIVATION_NOT)));

            cursor.close();
        }
        db.close();
        return mn;
    }


    ////////////////////////// SPOLEČNÉ PRO KATEGORII A PRO TYP

    //tato metoda přiřadí konkrétní typ pod kategorii
    public long createCategoryType(long type_id, long category_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE_ID, type_id);
        values.put(KEY_CATEGORY_ID, category_id);

        long id = db.insert(TABLE_CATEGORY_TYPE, null, values);
        Log.e(LOG, String.valueOf(id));
        db.close();
        return id;
    }

    public long getIdCategoryByName(String label){
        SQLiteDatabase db= this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_CATEGORY + " WHERE " + KEY_NAME_CATEGORY + " = '" + label + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            String ID=cursor.getString(0);
        db.close();
        return Long.valueOf(ID);
    }

    public long getIdTypeByName(String label){
        SQLiteDatabase db= this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_TYPE + " WHERE " + KEY_NAME_TYPE + " = '" + label + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            String ID=cursor.getString(0);
        db.close();
        return Long.valueOf(ID);
    }

    /////////////////////////operace pro graf

    public ArrayList<Long> getCountForChart() {
        ArrayList<Long> values = new ArrayList<>();
        String countQuery = "SELECT " + KEY_MY_ID_CATEGORY + ", COUNT(*) pocet FROM " + TABLE_GRATITUDE
                + " GROUP BY " + KEY_MY_ID_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            values.add(cursor.getLong(cursor.getColumnIndex("pocet")));
        }
        cursor.close();
        db.close();
        return values;
    }

    //vytuji si barvy pro graf
    public ArrayList<String> getColorsForChart() {
        ArrayList<String> values = new ArrayList<>();
        String countQuery = "SELECT " + KEY_MY_ID_CATEGORY + ", "+ KEY_COLOR_CATEGORY + ", COUNT(*) pocet FROM " + TABLE_GRATITUDE
                + " JOIN " + TABLE_CATEGORY + " ON " + KEY_MY_ID_CATEGORY + " = " + KEY_ID
                + " GROUP BY " + KEY_MY_ID_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            values.add(cursor.getString(cursor.getColumnIndex(KEY_COLOR_CATEGORY)));
        }
        cursor.close();
        db.close();
        return values;
    }

    //vytuji si barvy pro graf
    public ArrayList<String> getNamesForChart() {
        ArrayList<String> values = new ArrayList<>();
        String countQuery = "SELECT " + KEY_MY_ID_CATEGORY + ", "+ KEY_NAME_CATEGORY + ", COUNT(*) pocet FROM " + TABLE_GRATITUDE
                + " JOIN " + TABLE_CATEGORY + " ON " + KEY_MY_ID_CATEGORY + " = " + KEY_ID
                + " GROUP BY " + KEY_MY_ID_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            values.add(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
        }
        cursor.close();
        db.close();
        return values;
    }

}
