package com.emwno.fq.network;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created on 25 May 2018.
 */
public class Field extends RealmObject {

    @PrimaryKey
    private String name;
    private String field;
    private String data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}