package com.example.insubete_ml;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuantityAdapter extends RecyclerView.Adapter<QuantityAdapter.ViewHolder> {
    private List<String> ingredients;
    private LayoutInflater inflater;
    EditText quantityfoodtext;
    public static ArrayList<EditQuantityModel> editModelArrayList;

    QuantityAdapter(Context context, ArrayList<String> ingredients, ArrayList<EditQuantityModel> editModelArrayList){
        Log.d("data", "titles -> "+ingredients);
        this.ingredients = ingredients;
        this.inflater = LayoutInflater.from(context);
        this.editModelArrayList = editModelArrayList;
        //quantityfoodtext = (EditText) itemView.findViewById(R.id.quantityfoodtext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.quantity_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = ingredients.get(position);

        holder.name.setText(name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here You Do Your Click Magic
               // getQuantities
                System.out.println(v);
            }
        });
        holder.quantity.setText(editModelArrayList.get(position).getEditTextValue());
    }

    public HashMap<String, String> getQuantities(@NonNull ViewHolder holder) {
        String name, quantity = "";
        HashMap<String, String> list_quantities = new HashMap<String, String>();
        for(int i = 0; i< getItemCount(); i++){

            quantity = holder.quantity.getText().toString();
            System.out.println("Quantity = " + quantity);
            name = holder.name.getText().toString();
            list_quantities.put(name, quantity);
        }

        return list_quantities;
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        EditText quantity;
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantityfoodtext);
            mView = itemView;

            quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    editModelArrayList.get(getAdapterPosition()).setEditTextValue(quantity.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
    }
}