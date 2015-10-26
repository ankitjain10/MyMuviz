package com.example.dell.mymuviz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import pojo.Information;

/**
 * Created by dell on 9/24/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    String TAG=RecyclerAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater mLayoutInflater;
    List<Information> list= Collections.emptyList();

    public RecyclerAdapter(Context context,List<Information> list){
        mContext=context;
        mLayoutInflater=LayoutInflater.from(context);
        this.list = list;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mLayoutInflater.inflate(R.layout.recyclerview_row,parent,false);
        RecyclerViewHolder recyclerViewHolder =new RecyclerViewHolder(view,mContext);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
    Information currentInformation=list.get(position);
        holder.tv_recyclerList.setText(currentInformation.getTitle());
        holder.iv_recyclerList.setImageResource(currentInformation.getIconId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
