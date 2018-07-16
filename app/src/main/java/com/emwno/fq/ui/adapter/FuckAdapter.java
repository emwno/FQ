package com.emwno.fq.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emwno.fq.R;
import com.emwno.fq.model.Fuck;

import java.util.List;

/**
 * Created on 25 May 2018.
 */
public class FuckAdapter extends RecyclerView.Adapter<FuckViewHolder> {

    private Context mContext;
    private List<Fuck> mList;

    public FuckAdapter(Context context, List<Fuck> list) {
        mContext = context;
        mList = list;
        setHasStableIds(true);
    }

    public List<Fuck> getList() {
        return mList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public FuckViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fuck_list_item, viewGroup, false);
        return new FuckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FuckViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

}