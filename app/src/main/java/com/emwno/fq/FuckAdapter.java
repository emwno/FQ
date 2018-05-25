package com.emwno.fq;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emwno.fq.network.Fuck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 25 May 2018.
 */
public class FuckAdapter extends RecyclerView.Adapter<FuckViewHolder> {

    private Context mContext;
    private List<Fuck> mList;

    public FuckAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public List<Fuck> getList() {
        return mList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public FuckViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fuck_list_item, viewGroup, false);
        return new FuckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FuckViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    public void setItems(List<Fuck> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

}
