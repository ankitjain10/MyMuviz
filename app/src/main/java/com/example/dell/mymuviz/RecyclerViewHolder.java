package com.example.dell.mymuviz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dell on 9/24/2015.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    Context mContext;
    ImageView iv_recyclerList;
    TextView tv_recyclerList;

    public RecyclerViewHolder(View itemView, Context mContext) {
        super(itemView);
        this.mContext=mContext;
        iv_recyclerList=(ImageView)itemView.findViewById(R.id.listIcon);
        tv_recyclerList=(TextView)itemView.findViewById(R.id.listText);
    }

}
