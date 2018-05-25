package com.emwno.fq;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emwno.fq.network.FQFactory;
import com.emwno.fq.network.FQService;
import com.emwno.fq.network.Fuck;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 22 May 2018.
 */
public class FuckFragment extends Fragment {

    private OnFuckSelectedListener mListener;
    private RecyclerView mRecyclerView;
    private FuckAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_fuck, container, false);

        mRecyclerView = rootView.findViewById(R.id.fuckList);
        mAdapter = new FuckAdapter(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                (view, position) -> mListener.onFuckSelected(mAdapter.getList().get(position))));

        fetchData();

        return rootView;
    }

    @Override
    public void onAttach(Context activity) {
        mListener = (OnFuckSelectedListener) activity;
        super.onAttach(activity);
    }

    private void fetchData() {
        FQService mService = FQFactory.getRetrofitInstance().create(FQService.class);
        Call<List<Fuck>> call = mService.getOperations();

        call.enqueue(new Callback<List<Fuck>>() {
            @Override
            public void onResponse(Call<List<Fuck>> call, Response<List<Fuck>> response) {
                mAdapter.setItems(response.body());
            }

            @Override
            public void onFailure(Call<List<Fuck>> call, Throwable t) {

            }
        });
    }

    public interface OnFuckSelectedListener {
        void onFuckSelected(Fuck fuck);
    }
}
