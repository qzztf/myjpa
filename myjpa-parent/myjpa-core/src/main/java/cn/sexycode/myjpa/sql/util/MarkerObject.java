package cn.sexycode.myjpa.sql.util;

import java.io.Serializable;

/**
 */
public class MarkerObject implements Serializable {
    private String name;

    public MarkerObject(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
