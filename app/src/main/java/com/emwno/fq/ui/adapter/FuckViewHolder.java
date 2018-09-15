package com.emwno.fq.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.emwno.fq.R;
import com.emwno.fq.model.Field;
import com.emwno.fq.model.Fuck;

import androidx.recyclerview.widget.RecyclerView;

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
        StringBuilder s = new StringBuilder();
        for (Field field : fuck.getFields()) {
            s.append(field.getName().toUpperCase());
            s.append("  ");
        }
        mFields.setText(s.toString().trim());
    }
}
