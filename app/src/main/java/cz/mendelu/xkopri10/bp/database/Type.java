package cz.mendelu.xkopri10.bp.database;

/**
 * Created by Martin on 10.02.2018.
 */

public class Type {

    private int id;
    private String name;

    //konstruktory
    public Type() {
    }

    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //gettery
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //settery
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
