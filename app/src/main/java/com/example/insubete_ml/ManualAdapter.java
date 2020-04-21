package com.example.insubete_ml;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.ViewHolder> {
    private List<String> names,gis;
    private LayoutInflater inflater;

    private AdapterView.OnItemClickListener listener;

    ManualAdapter(Context context, List<String> names, List<String> gis, AdapterView.OnItemClickListener listener){
        Log.d("data", "titles -> "+names);
        this.names = names;
        this.gis = gis;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.food_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(names.get(position), listener);
        String name = names.get(position);
        String gi = gis.get(position);

        holder.name.setText(name);
        holder.gi.setText(gi);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here You Do Your Click Magic
                System.out.println(v);
            };
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,gi;
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            gi = itemView.findViewById(R.id.glycemic_index);
            mView = itemView;
        }
        public void bind(final String name, final AdapterView.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(AdapterView a, View v);
                }
            });
        }

    }
    public interface OnItemClickListener {
        void onItemClick(String name);
    }

}