package com.renegades.labs.googlecontacts;

/**
 * Created by Виталик on 08.03.2017.
 */

public class ListItem {
    private String string;
    private int id;

    public ListItem(String string, int id) {
        this.string = string;
        this.id = id;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
