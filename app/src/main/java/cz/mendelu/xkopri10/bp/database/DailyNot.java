package cz.mendelu.xkopri10.bp.database;

/**
 * Created by Martin on 11.03.2018.
 */

public class DailyNot {

    private int id;
    private int state;
    private int hour;
    private int minute;



    public DailyNot(int id, int state, int hour, int minute) {
        this.id = id;
        this.state = state;
        this.hour = hour;
        this.minute = minute;
    }

    public DailyNot() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

}
