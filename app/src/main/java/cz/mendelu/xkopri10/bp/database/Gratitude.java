package cz.mendelu.xkopri10.bp.database;

/**
 * Created by Martin on 18.02.2018.
 */

public class Gratitude {

    private int id;
    private String date;
    private String note;
    private int rate;
    private long idCategory;
    private long idType;
    private String path;

    DatabaseHelper db;
    String mujNazev = "";

    public Gratitude(int id, String date, String note, int rate, int idCategory, int idType, String path) {
        this.id = id;
        this.date = date;
        this.note = note;
        this.rate = rate;
        this.idCategory = idCategory;
        this.idType = idType;
        this.path = path;
    }

    public Gratitude() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public long getIdCategory() {
        return idCategory;
    }
    public void setIdCategory(long idCategory) {
        this.idCategory = idCategory;
    }

    public long getIdType() {
        return idType;
    }

    public void setIdType(long idType) {
        this.idType = idType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
