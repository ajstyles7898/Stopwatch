package com.example.appstopwatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ExampleViewHolder> {

    private  ArrayList<LapItem> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView adtvSerial;
        public TextView adtvMinl;
        public TextView adtvMinr;
        public TextView adtvSecl;
        public TextView adtvSecr;
        public TextView adtvMsecl;
        public TextView adtvMsecr;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            adtvSerial= itemView.findViewById(R.id.item_tv_serial);
            adtvMinl = itemView.findViewById(R.id.item_tv_minl);
            adtvMinr = itemView.findViewById(R.id.item_tv_minr);
            adtvSecl = itemView.findViewById(R.id.item_tv_secl);
            adtvSecr = itemView.findViewById(R.id.item_tv_secr);
            adtvMsecl = itemView.findViewById(R.id.item_tv_msecl);
            adtvMsecr = itemView.findViewById(R.id.item_tv_msecr);
        }
    }

    public Adapter(ArrayList<LapItem> exampleList){
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_lap_item, parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v);

        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        LapItem currentItem = mExampleList.get(position);

        holder.adtvSerial.setText(currentItem.getSerial());
        holder.adtvMinl.setText(currentItem.getMinl());
        holder.adtvMinr.setText(currentItem.getMinr());
        holder.adtvSecl.setText(currentItem.getSecl());
        holder.adtvSecr.setText(currentItem.getSecr());
        holder.adtvMsecl.setText(currentItem.getMsecl());
        holder.adtvMsecr.setText(currentItem.getMsecr());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}
