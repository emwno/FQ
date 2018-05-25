package com.emwno.fq;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.emwno.fq.network.Fuck;

/**
 * Created on 25 May 2018.
 */
public class FuckViewHolder extends RecyclerView.ViewHolder {

    private TextView mTitle;

    public FuckViewHolder(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.fuckItemTitle);
    }

    public void bind(Fuck fuck) {
        mTitle.setText(fuck.getName());
    }
}
