package cz.mendelu.xkopri10.bp.database;

/**
 * Created by Martin on 26.02.2018.
 */

public class Greatfulness {

    private int id;
    private String noteGreatfulness;
    private String colorGreatfulness;
    private String dateGreatfulness;

    public Greatfulness(int id, String noteGreatfulness, String colorGreatfulness, String dateGreatfulness) {
        this.id = id;
        this.noteGreatfulness = noteGreatfulness;
        this.colorGreatfulness = colorGreatfulness;
        this.dateGreatfulness = dateGreatfulness;
    }

    public Greatfulness() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteGreatfulness() {
        return noteGreatfulness;
    }

    public void setNoteGreatfulness(String noteGreatfulness) {
        this.noteGreatfulness = noteGreatfulness;
    }

    public String getColorGreatfulness() {
        return colorGreatfulness;
    }

    public void setColorGreatfulness(String colorGreatfulness) {
        this.colorGreatfulness = colorGreatfulness;
    }

    public String getDateGreatfulness() {
        return dateGreatfulness;
    }

    public void setDateGreatfulness(String dateGreatfulness) {
        this.dateGreatfulness = dateGreatfulness;
    }
}
