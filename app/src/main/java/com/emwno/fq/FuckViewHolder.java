package com.emwno.fq;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.emwno.fq.network.Field;
import com.emwno.fq.network.Fuck;

/**
 * Created on 25 May 2018.
 */
public class FuckViewHolder extends RecyclerView.ViewHolder {

    private TextView mTitle;
    private TextView mFields;

    public FuckViewHolder(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.fqItemTitle);
        mFields = itemView.findViewById(R.id.fqItemFields);
    }

    public void bind(Fuck fuck) {
        mTitle.setText(fuck.getName());
        String s = "";
        for (Field field : fuck.getFields()) {
            s += field.getName().toUpperCase();
            s += "  ";
        }
        mFields.setText(s.trim());
    }
}
