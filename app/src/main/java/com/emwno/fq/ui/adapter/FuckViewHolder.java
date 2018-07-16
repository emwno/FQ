package com.emwno.fq.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.emwno.fq.R;
import com.emwno.fq.model.Field;
import com.emwno.fq.model.Fuck;

/**
 * Created on 25 May 2018.
 */
public class FuckViewHolder extends RecyclerView.ViewHolder {

    private TextView mTitle;
    private TextView mFields;

    FuckViewHolder(View itemView) {
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
