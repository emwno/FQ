package com.emwno.fq.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created on 25 May 2018.
 */
public class Fuck extends RealmObject {

    @PrimaryKey
    private String name;
    private String url;
    private RealmList<Field> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RealmList<Field> getFields() {
        return fields;
    }

    public void setFields(RealmList<Field> fields) {
        this.fields = fields;
    }

}
