package com.project.gacha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SummonListAdapter extends RecyclerView.Adapter<SummonListAdapter.ViewHolder> {
    private ArrayList<Inventory> historyList;
    public SummonListAdapter(ArrayList<Inventory> images){
        historyList = images;
    }


    public interface OnItemClickListener {
        void onItemClick(Inventory inv);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_holder_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
        holder.cardRecord.setText(Integer.toString(historyList.get(position).getRecord()));
        holder.cardImage.setImageResource(historyList.get(position).getImage());
        holder.cardName.setText(historyList.get(position).getName());
        switch (historyList.get(position).getRarity()) {
            case 3:
                holder.cardRarity.setImageResource(Banner.Three_star);
                break;
            case 4:
                holder.cardRarity.setImageResource(Banner.Four_star);
                break;
            case 5:
                holder.cardRarity.setImageResource(Banner.Five_star);
                break;
        }
    }

    @Override
    public int getItemCount () {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage, cardRarity;
        TextView cardName, cardRecord;

        public ViewHolder(View v) {
            super(v);
            cardRecord = v.findViewById(R.id.cardRecord);
            cardName = v.findViewById(R.id.cardName);
            cardRarity = v.findViewById(R.id.cardRarity);
            cardImage = v.findViewById(R.id.cardImage);

        }

    }

    public void setList(ArrayList<Inventory> invList){
        historyList = invList;
        notifyDataSetChanged();
    }


}
