package com.beyzanur.expiration_date_reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.ViewHolder>{
    private Context mContext;

    private ArrayList<Products> productsArrayList;

    public SearchProductAdapter(Context mContext, ArrayList<Products> productsArrayList) {
        this.mContext = mContext;
        this.productsArrayList = productsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_product_recycler, parent, false);
        return new SearchProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(productsArrayList.get(position).getProductName());
        holder.cat.setText(productsArrayList.get(position).getProductCat());

    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView cat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.seacrh_product_name_recyler);
            cat = itemView.findViewById(R.id.search_product_date_recyler);
        }
    }
}
