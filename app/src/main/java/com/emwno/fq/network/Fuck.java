package com.emwno.fq.network;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created on 25 May 2018.
 */
public class Fuck implements Parcelable {

    public static final Creator<Fuck> CREATOR = new Creator<Fuck>() {
        @Override
        public Fuck createFromParcel(Parcel in) {
            return new Fuck(in);
        }

        @Override
        public Fuck[] newArray(int size) {
            return new Fuck[size];
        }
    };

    private String name;
    private String url;
    private List<Field> fields;

    protected Fuck(Parcel in) {
        name = in.readString();
        url = in.readString();
        fields = in.createTypedArrayList(Field.CREATOR);
    }

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

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeTypedList(fields);
    }
}
