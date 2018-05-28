package com.emwno.fq.network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 25 May 2018.
 */
public class Field implements Parcelable {

    public static final Creator<Field> CREATOR = new Creator<Field>() {
        @Override
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        @Override
        public Field[] newArray(int size) {
            return new Field[size];
        }
    };

    private String name;
    private String field;
    private String data;

    protected Field(Parcel in) {
        name = in.readString();
        field = in.readString();
        data = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(field);
        dest.writeString(data);
    }
}